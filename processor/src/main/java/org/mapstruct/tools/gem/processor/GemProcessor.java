/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.lang.model.element.ExecutableElement;
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
import freemarker.template.Version;

/**
 * @author sjaakd
 */
@SupportedAnnotationTypes( "org.mapstruct.tools.gem.GemDefinitions" )
public class GemProcessor extends AbstractProcessor {

    private Util util;
    private List<GemInfo> gemInfos = new ArrayList<>( 10 );

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotationTypes, RoundEnvironment roundEnv) {
        try {
            util = new Util( processingEnv.getTypeUtils(), processingEnv.getElementUtils() );
            for ( TypeElement annotationType : annotationTypes ) {

                for ( Element definingElement : roundEnv.getElementsAnnotatedWith( annotationType ) ) {

                    // get an annotation mirror on @GemDefinitions
                    AnnotationMirror gemDefinitionsMirror = definingElement
                        .getAnnotationMirrors()
                        .stream()
                        .filter( t -> util.isSame( t.getAnnotationType(), "org.mapstruct.tools.gem.GemDefinitions" ) )
                        .findFirst()
                        .orElseThrow( IllegalStateException::new );

                    // get annotation mirrors on each @GemDefinitions#value
                    List<AnnotationMirror> gemDefinitionMirrors = util.getAnnotationValue(
                        gemDefinitionsMirror,
                        "value",
                        List.class
                    );
                    gemDefinitionMirrors.forEach( m -> addGemInfo( m, definingElement ) );
                }
            }
            postProcessGemInfo();
            write();
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
        PackageElement pkg = processingEnv.getElementUtils().getPackageOf( definingElement );
        String gemName = util.getSimpleName( gemDeclaredType );
        String gemFqn = util.getFullyQualifiedName( gemDeclaredType );
        String gemPackage = pkg.getQualifiedName().toString();

        // collect value info
        List<ExecutableElement> methods = ElementFilter.methodsIn( gemDeclaredType.asElement().getEnclosedElements() );
        List<GemValueInfo> gemValueInfos = methods.stream()
            .map( e -> new GemValueInfo( e.getSimpleName().toString(), e.getReturnType() ) )
            .collect( Collectors.toList() );
        GemInfo gemInfo = new GemInfo(
            gemPackage,
            gemName,
            gemFqn,
            gemValueInfos,
            definingElement,
            gemDeclaredType.asElement()
        );
        gemInfos.add( gemInfo );
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
                    valueType = new GemValueType( String.class, true, isArray );
                }
                else if ( Class.class.getName().equals( fqn ) ) {
                    valueType = new GemValueType( TypeMirror.class, false, isArray );
                }
                else if (String.class.getName().equals( fqn ) ) {
                    valueType = new GemValueType( String.class, false, isArray );
                }
                else {
                    GemInfo usedGem = gemInfos.stream()
                        .filter( g -> fqn.equals( g.getAnnotationFqn() ) )
                        .findFirst()
                        .orElse( null );
                    if ( usedGem != null ) {
                        valueType = new GemValueType( usedGem, isArray );
                    }
                    else {
                        valueType = new GemValueType( TypeMirror.class, false, isArray );
                    }
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

    private void write( ) {
        for ( GemInfo gemInfo : gemInfos ) {
            try (Writer writer = processingEnv.getFiler()
                .createSourceFile(
                    gemInfo.getGemPackageName() + "." + gemInfo.getGemName(),
                    gemInfo.getOriginatingElements()
                )
                .openWriter()) {
                Configuration cfg = new Configuration( new Version( "2.3.21" ) );
                cfg.setClassForTemplateLoading( GemProcessor.class, "/" );
                cfg.setDefaultEncoding( "UTF-8" );

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
    }
}
