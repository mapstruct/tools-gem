/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author sjaakd
 */
@SupportedAnnotationTypes( {"org.mapstruct.tools.gem.GemDefinitions", "org.mapstruct.tools.gem.GemDefinition",
        "org.mapstruct.tools.gem.RegisterGem"} )
public class GemProcessor extends AbstractProcessor {

    private Util util;
    private final List<GemInfo> gemInfos = new ArrayList<>( 10 );
    private final List<GemEnum> gemEnums = new ArrayList<>();
    private final Map<String, GemEnum> gemEnumMap = new HashMap<>();
    private boolean noErrors = true;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotationTypes, RoundEnvironment roundEnv) {
        try {
            util = new Util( processingEnv.getTypeUtils(), processingEnv.getElementUtils() );
            for ( TypeElement annotationType : annotationTypes ) {
                String annotationName = annotationType.getQualifiedName().toString();
                for ( Element definingElement : roundEnv.getElementsAnnotatedWith( annotationType ) ) {

                    // get an annotation mirror on @GemDefinitions
                    AnnotationMirror gemDefinitionsMirror = definingElement
                        .getAnnotationMirrors()
                        .stream()
                        .filter( t -> util.isSame( t.getAnnotationType(), annotationName ) )
                        .findFirst()
                        .orElseThrow( IllegalStateException::new );
                    if ( annotationName.endsWith( "s" ) ) {
                        // get annotation mirrors on each @GemDefinitions#value
                        List<AnnotationMirror> gemDefinitionMirrors = util.getAnnotationValue(
                                gemDefinitionsMirror,
                                "value",
                                List.class
                        );
                        gemDefinitionMirrors.forEach( m -> addGemInfo( m, definingElement ) );
                    }
                    else if ( "org.mapstruct.tools.gem.RegisterGem".equals( annotationName ) ) {
                        addEnumMapping( gemDefinitionsMirror, definingElement );
                    }
                    else {
                        addGemInfo( gemDefinitionsMirror, definingElement );
                    }
                }
            }
            if ( noErrors ) {
                postProcessGemInfo();
                write();
            }
        }
        catch ( RuntimeException ex ) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter( sw );
            ex.printStackTrace( pw );
            processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR, sw.toString() );
        }
        return true;
    }

    private void addGemInfo(AnnotationMirror gemDefinitionMirror, Element definingElement) {

        // create gem info
        DeclaredType gemDeclaredType = util.getAnnotationValue( gemDefinitionMirror, "value", DeclaredType.class );
        Element element = gemDeclaredType.asElement();
        ElementKind kind = element.getKind();
        String annotationName = util.getSimpleName( gemDeclaredType );
        String gemName = getGemName( gemDefinitionMirror, annotationName );
        PackageElement pkg = processingEnv.getElementUtils().getPackageOf( definingElement );
        String gemFqn = util.getFullyQualifiedName( gemDeclaredType );
        String gemPackage = pkg.getQualifiedName().toString();
        if ( kind == ElementKind.ENUM ) {
           List<String> enumConstants = getEnumConstants( element );
            GemEnum gemEnum = new GemEnum(gemPackage, gemName, gemFqn, enumConstants, definingElement, element);
            gemEnums.add( gemEnum );
            if ( gemEnumMap.put( gemFqn, gemEnum ) != null ) {
                processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR,
                        "Enum gem " + gemFqn + " can only be registered once" );
                noErrors = false;
            }
        }
        else if ( kind == ElementKind.ANNOTATION_TYPE ) {

            // collect value info
            List<ExecutableElement> methods = ElementFilter.methodsIn( element.getEnclosedElements() );
            List<GemValueInfo> gemValueInfos = methods.stream()
                    .map( e -> new GemValueInfo( e.getSimpleName().toString(), e.getReturnType() ) )
                    .collect( Collectors.toList() );
            GemInfo gemInfo = new GemInfo(
                    gemPackage,
                    gemName,
                    annotationName,
                    gemFqn,
                    gemValueInfos,
                    definingElement,
                    element
            );
            gemInfos.add( gemInfo );
        }
        else {
            throw new IllegalArgumentException();
        }

    }

    private List<String> getEnumConstants(Element element) {
        return element.getEnclosedElements().stream()
                .filter( e -> e.getKind() == ElementKind.ENUM_CONSTANT )
                .map( Element::getSimpleName ).map( Name::toString ).collect( Collectors.toList() );
    }

    private String getGemName(AnnotationMirror gemDefinitionMirror, String annotationName) {
        String implementationName = util.getAnnotationValue( gemDefinitionMirror, "implementationName", String.class );
        if ( implementationName != null ) {
            return implementationName.replace( "<CLASS_NAME>", annotationName );
        }
        return annotationName + "Gem";
    }

    private void postProcessGemInfo() {
        for ( GemInfo gemInfo : gemInfos ) {
            for ( GemValueInfo gemValueInfo : gemInfo.getGemValueInfos() ) {
                if ( TypeKind.ARRAY == gemValueInfo.getTypeMirror().getKind() ) {
                    ArrayType arrayType = (ArrayType) gemValueInfo.getTypeMirror();
                    gemValueInfo.setValueType( getGemValueType( arrayType.getComponentType(), true ) );
                }
                else {
                    gemValueInfo.setValueType( getGemValueType( gemValueInfo.getTypeMirror(), false ) );
                }
            }
        }
    }

    private GemValueType getGemValueType(TypeMirror type, boolean isArray) {
        GemValueType valueType;
        switch ( type.getKind() ) {
            case DECLARED:
                // class, other annotation or enum
                DeclaredType declaredType = (DeclaredType) type;
                String fqn = util.getFullyQualifiedName( declaredType );
                if ( util.isEnumeration( declaredType ) ) {
                    GemEnum gemEnum = gemEnumMap.get( fqn );
                    if (gemEnum != null) {
                        valueType = new GemValueType( gemEnum, isArray );
                    }
                    else {
                        valueType = new GemValueType( String.class, true, isArray );
                    }
                }
                else if ( Class.class.getName().equals( fqn ) ) {
                    valueType = new GemValueType( TypeMirror.class, false, isArray );
                }
                else if (String.class.getName().equals( fqn ) ) {
                    valueType = new GemValueType( String.class, false, isArray );
                }
                else {
                    valueType = gemInfos.stream()
                        .filter( g -> fqn.equals( g.getAnnotationFqn() ) )
                        .findFirst()
                        .map( usedGem -> new GemValueType( usedGem, isArray ) )
                        .orElseGet( () -> new GemValueType( TypeMirror.class, false, isArray ) );
                }
                break;
            case BOOLEAN:
                valueType = new GemValueType( Boolean.class, false, isArray );
                break;
            case BYTE:
                valueType = new GemValueType( Byte.class, false, isArray );
                break;
            case CHAR:
                valueType = new GemValueType( Character.class, false, isArray );
                break;
            case SHORT:
                valueType = new GemValueType( Short.class, false, isArray );
                break;
            case INT:
                valueType = new GemValueType( Integer.class, false, isArray );
                break;
            case LONG:
                valueType = new GemValueType( Long.class, false, isArray );
                break;
            case FLOAT:
                valueType = new GemValueType( Float.class, false, isArray );
                break;
            case DOUBLE:
                valueType = new GemValueType( Double.class, false, isArray );
                break;
            default:
                throw new IllegalArgumentException( "unrecognized annotation type" );
        }
        return valueType;
    }

    private void addEnumMapping(AnnotationMirror gemDefinitionsMirror, Element definingElement) {
        if ( definingElement.getKind() != ElementKind.ENUM) {
            throw new IllegalArgumentException();
        }
        String packageName = processingEnv.getElementUtils().getPackageOf( definingElement )
                .getQualifiedName().toString();
        DeclaredType original = util.getAnnotationValue( gemDefinitionsMirror, "value", DeclaredType.class );
        String originalFullName = util.getFullyQualifiedName( original );
        List<String> originalElements = getEnumConstants( original.asElement() );
        Set<String> originalValues = new HashSet<>( originalElements );
        List<String> enumConstants = getEnumConstants( definingElement );
        Set<String> definedValues =  new HashSet<>( enumConstants );
        List<String> missingOriginals = originalValues.stream()
                .filter( value -> !definedValues.remove( value ) ).collect( Collectors.toList() );

        if ( !missingOriginals.isEmpty() ) {
            processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR, "Enum constants " + missingOriginals
                    + " are missing in " + packageName + "." + definingElement.getSimpleName() + ". A enum gem of "
                    + originalFullName + " should exactly contain " + originalElements );
            noErrors = false;
        }
        if ( !definedValues.isEmpty() ) {
            processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR, "Enum constants " + definedValues
                    + " are only present in " + packageName + "." + definingElement.getSimpleName() + ". A enum gem of "
                    + originalFullName + " should exactly contain " + originalElements );
            noErrors = false;
        }
        if ( gemEnumMap.put( originalFullName, new GemEnum(
                packageName,
                definingElement.getSimpleName().toString(),
                originalFullName,
                enumConstants
        ) ) != null ) {
             processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR,
                     "Enum gem " + originalFullName + " can only be registered once" );
             noErrors = false;
         }
    }

    private void write( ) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setClassForTemplateLoading( GemProcessor.class, "/" );
        cfg.setDefaultEncoding( "UTF-8" );
        for (GemEnum gemEnum : gemEnums) {
            try (Writer writer = processingEnv.getFiler()
                    .createSourceFile(
                            gemEnum.getGemPackageName() + "." + gemEnum.getGemName(),
                            gemEnum.getOriginatingElements()
                    )
                    .openWriter() ) {
                Map<String, Object> templateData = new HashMap<>();

                templateData.put( "gemEnum", gemEnum );
                Template template = cfg.getTemplate( "org/mapstruct/tools/gem/processor/Enum.ftl" );
                template.process( templateData, writer );
                writer.flush();
            }
            catch (TemplateException | IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
        for ( GemInfo gemInfo : gemInfos ) {
            try (Writer writer = processingEnv.getFiler()
                .createSourceFile(
                    gemInfo.getGemPackageName() + "." + gemInfo.getGemName(),
                    gemInfo.getOriginatingElements()
                )
                .openWriter() ) {

                Map<String, Object> templateData = new HashMap<>();

                templateData.put( "gemInfo", gemInfo );
                Template template = cfg.getTemplate( "org/mapstruct/tools/gem/processor/Gem.ftl" );
                template.process( templateData, writer );
            }
            catch ( TemplateException | IOException ex ) {
                throw new IllegalStateException( ex );
            }
        }
        // handled all info, clear
        gemInfos.clear();
        gemEnums.clear();
        gemEnumMap.clear();
    }
}
