/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

/**
 * @author Filip Hrisafov
 */
public class JavaPoetWriter {

    private static final ClassName GEM = ClassName.get( "org.mapstruct.tools.gem", "Gem" );
    private static final ClassName GEM_VALUE = ClassName.get( "org.mapstruct.tools.gem", "GemValue" );

    private static final ClassName LIST = ClassName.get( List.class );

    private JavaPoetWriter() {
        // hide utility class contstructor
    }

    public static TypeSpec createGemSpec(GemInfo gemInfo) {
        ClassName generatedGemClassName = ClassName.get( gemInfo.getGemPackageName(), gemInfo.getGemName() );
        ClassName gemBuilder = generatedGemClassName.nestedClass( gemInfo.getBuilderName() );
        ClassName gemBuilderImpl = generatedGemClassName.nestedClass( gemInfo.getBuilderImplName() );

        TypeSpec builder = createBuilderInterfaceSpec( gemBuilder, gemInfo );

        TypeSpec builderImpl = createBuilderImplClassSpec(
            gemBuilderImpl,
            gemBuilder,
            generatedGemClassName,
            gemInfo
        );

        Collection<MethodSpec> getters = new ArrayList<>( gemInfo.getGemValueInfos().size() );
        Collection<FieldSpec> fields = new ArrayList<>( gemInfo.getGemValueInfos().size() );
        for ( GemValueInfo gemValueInfo : gemInfo.getGemValueInfos() ) {

            getters.add( MethodSpec.methodBuilder( gemValueInfo.getName() )
                .addModifiers( Modifier.PUBLIC )
                .returns( gemValueParameterTypeName( gemValueInfo.getValueType() ) )
                .addStatement( "return this.$N", gemValueInfo.getName() )
                .addJavadoc( "accessor\n" )
                .addJavadoc(
                    "\n@return the {@link $L} for {@link $L#$L}",
                    GEM_VALUE.simpleName(),
                    gemInfo.getGemName(),
                    gemValueInfo.getName()
                )
                .build() );

            fields.add( FieldSpec.builder(
                gemValueParameterTypeName( gemValueInfo.getValueType() ),
                gemValueInfo.getName(),
                Modifier.PRIVATE, Modifier.FINAL
            ).build() );
        }

        TypeSpec.Builder gemTypeSpecBuilder = TypeSpec.classBuilder( generatedGemClassName );

        for ( Element originatingElement : gemInfo.getOriginatingElements() ) {
            gemTypeSpecBuilder.addOriginatingElement( originatingElement );
        }


        return gemTypeSpecBuilder
            .addSuperinterface( GEM )
            .addModifiers( Modifier.PUBLIC )
            .addFields( fields )
            .addField( FieldSpec.builder( TypeName.BOOLEAN, "isValid", Modifier.PRIVATE, Modifier.FINAL ).build() )
            .addField( FieldSpec.builder( AnnotationMirror.class, "mirror", Modifier.PRIVATE, Modifier.FINAL ).build() )
            .addMethod( MethodSpec.constructorBuilder()
                .addModifiers( Modifier.PRIVATE )
                .addParameter( gemBuilderImpl, "builder" )
                .addCode( constructorSetValueFields( gemInfo ) )
                .addStatement( "this.$1N = builder.$1N", "mirror" )
                .addCode( "\n" )
                .addCode( constructorValidDefinition( gemInfo ) )
                .build() )
            // getters
            .addMethods( getters )
            .addMethod( MethodSpec.methodBuilder( "mirror" )
                .addAnnotation( Override.class )
                .addModifiers( Modifier.PUBLIC )
                .returns( AnnotationMirror.class )
                .addStatement( "return this.mirror" )
                .build() )
            .addMethod( MethodSpec.methodBuilder( "isValid" )
                .addAnnotation( Override.class )
                .addModifiers( Modifier.PUBLIC )
                .returns( TypeName.BOOLEAN )
                .addStatement( "return this.isValid" )
                .build() )
            // static methods
            .addMethod( MethodSpec.methodBuilder( "instanceOn" )
                .addModifiers( Modifier.PUBLIC, Modifier.STATIC )
                .addParameter( Element.class, "element" )
                .returns( generatedGemClassName )
                .addStatement( "return build( element, new $T() )", gemBuilderImpl )
                .build() )
            .addMethod( MethodSpec.methodBuilder( "instanceOn" )
                .addModifiers( Modifier.PUBLIC, Modifier.STATIC )
                .addParameter( AnnotationMirror.class, "mirror" )
                .returns( generatedGemClassName )
                .addStatement( "return build( mirror, new $T() )", gemBuilderImpl )
                .build() )
            .addMethod( MethodSpec.methodBuilder( "build" )
                .addModifiers( Modifier.PUBLIC, Modifier.STATIC )
                .addTypeVariable( TypeVariableName.get( "T" ) )
                .addParameter( Element.class, "element" )
                .addParameter( ParameterizedTypeName.get( gemBuilder, TypeVariableName.get( "T" ) ), "builder" )
                .returns( TypeVariableName.get( "T" ) )
                .addStatement( "$T mirror = element.getAnnotationMirrors().stream()" +
                    "\n.filter( a -> $S.contentEquals( ( ( $T ) a.getAnnotationType().asElement() )" +
                    ".getQualifiedName() ) )" +
                    "\n.findAny()" +
                    "\n.orElse( null )", AnnotationMirror.class, gemInfo.getAnnotationFqn(), TypeElement.class )
                .addStatement( "return build( mirror, builder )" )
                .build() )
            .addMethod( gemBuildMethod( gemBuilder, gemInfo ) )
            .addType( builder )
            .addType( builderImpl )
            .build();
    }

    protected static TypeSpec createBuilderInterfaceSpec(ClassName gemBuilder, GemInfo gemInfo) {
        Collection<MethodSpec> gemSetterMethods = new ArrayList<>( gemInfo.getGemValueInfos().size() );
        for ( GemValueInfo gemValueInfo : gemInfo.getGemValueInfos() ) {
            gemSetterMethods.add( MethodSpec.methodBuilder( "set" + Util.capitalize( gemValueInfo.getName() ) )
                .addModifiers( Modifier.PUBLIC, Modifier.ABSTRACT )
                .addParameter( createParameterSpec( gemValueInfo ) )
                .addJavadoc(
                    "Sets the {@link $T} for {@link $L#$L}",
                    GEM_VALUE,
                    gemInfo.getGemName(),
                    gemValueInfo.getName()
                )
                .addJavadoc(
                    "\n\n@return the {@link $T} for this gem, representing {@link $L}",
                    gemBuilder,
                    gemInfo.getGemName()
                )
                .returns( gemBuilder )
                .build() );

        }

        return TypeSpec.interfaceBuilder( gemBuilder )
            .addTypeVariable( TypeVariableName.get( "T" ) )
            .addModifiers( Modifier.PUBLIC, Modifier.STATIC )
            .addJavadoc( "A builder that can be implemented by the user to define custom logic" )
            .addJavadoc( "\ne.g. in the build method, prior to creating the annotation gem." )
            .addMethods( gemSetterMethods )
            .addMethod( MethodSpec.methodBuilder( "setMirror" )
                .addModifiers( Modifier.PUBLIC, Modifier.ABSTRACT )
                .addParameter( AnnotationMirror.class, "mirror" )
                .returns( gemBuilder )
                .addJavadoc( "Sets the annotation mirror\n" )
                .addJavadoc( "\n@param mirror the mirror which this gem represents" )
                .addJavadoc(
                    "\n\n@return the {@link $L} for this gem, representing {@link $L}",
                    gemBuilder.simpleName(),
                    gemInfo.getGemName()
                )
                .build() )
            .addMethod( MethodSpec.methodBuilder( "build" )
                .addModifiers( Modifier.PUBLIC, Modifier.ABSTRACT )
                .returns( TypeVariableName.get( "T" ) )
                .addJavadoc( "The build method can be overridden in a custom implementation," )
                .addJavadoc( "\nwhich allows the user to define their own custom validation on the annotation." )
                .addJavadoc( "\n\n@return the representation of the annotation" )
                .build() )
            .build();
    }

    protected static TypeSpec createBuilderImplClassSpec(ClassName gemBuilderImpl, ClassName gemBuilder,
        ClassName generatedGem, GemInfo gemInfo) {
        Collection<MethodSpec> gemSetterMethods = new ArrayList<>( gemInfo.getGemValueInfos().size() );
        Collection<FieldSpec> fields = new ArrayList<>( gemInfo.getGemValueInfos().size() );
        for ( GemValueInfo gemValueInfo : gemInfo.getGemValueInfos() ) {
            gemSetterMethods.add( MethodSpec.methodBuilder( "set" + Util.capitalize( gemValueInfo.getName() ) )
                .addAnnotation( Override.class )
                .addModifiers( Modifier.PUBLIC )
                .addParameter( createParameterSpec( gemValueInfo ) )
                .addStatement( "this.$1N = $1N", gemValueInfo.getName() )
                .addStatement( "return this" )
                .returns( gemBuilder )
                .build() );

            fields.add( FieldSpec.builder(
                gemValueParameterTypeName( gemValueInfo.getValueType() ),
                gemValueInfo.getName(),
                Modifier.PRIVATE
            ).build() );

        }

        return TypeSpec.classBuilder( gemBuilderImpl )
            .addSuperinterface( ParameterizedTypeName.get( gemBuilder, generatedGem ) )
            .addModifiers( Modifier.PRIVATE, Modifier.STATIC )
            .addFields( fields )
            .addField( AnnotationMirror.class, "mirror", Modifier.PRIVATE )
            .addMethods( gemSetterMethods )
            .addMethod( MethodSpec.methodBuilder( "setMirror" )
                .addModifiers( Modifier.PUBLIC )
                .addAnnotation( Override.class )
                .addParameter( AnnotationMirror.class, "mirror" )
                .addStatement( "this.$1N = $1N", "mirror" )
                .addStatement( "return this" )
                .returns( gemBuilder )
                .build() )
            .addMethod( MethodSpec.methodBuilder( "build" )
                .addModifiers( Modifier.PUBLIC )
                .addAnnotation( Override.class )
                .addStatement( "return new $T( this )", generatedGem )
                .returns( generatedGem )
                .build() )
            .build();
    }

    protected static CodeBlock constructorSetValueFields(GemInfo gemInfo) {
        CodeBlock.Builder builder = CodeBlock.builder();
        for ( GemValueInfo gemValueInfo : gemInfo.getGemValueInfos() ) {
            builder.addStatement( "this.$1N = builder.$1N", gemValueInfo.getName() );
        }

        return builder.build();
    }

    protected static CodeBlock constructorValidDefinition(GemInfo gemInfo) {
        boolean first = true;
        CodeBlock.Builder codeBuilder = CodeBlock.builder();
        for ( GemValueInfo gemValueInfo : gemInfo.getGemValueInfos() ) {
            if ( first ) {
                codeBuilder.addStatement(
                    "boolean isValid = this.$1N != null ? this.$1N.isValid() : false",
                    gemValueInfo.getName()
                );
                first = false;
            }
            else {
                codeBuilder.addStatement(
                    "isValid = isValid && this.$1N != null ? this.$1N.isValid() : false",
                    gemValueInfo.getName()
                );
            }
        }

        if ( first ) {
            // If this is true then there is no intermediate variable created
            codeBuilder.addStatement( "this.isValid = true" );
        }
        else {
            codeBuilder.addStatement( "this.isValid = isValid" );
        }


        return codeBuilder.build();

    }

    protected static MethodSpec gemBuildMethod(ClassName gemBuilder, GemInfo gemInfo) {
        return MethodSpec.methodBuilder( "build" )
            .addModifiers( Modifier.PUBLIC, Modifier.STATIC )
            .addTypeVariable( TypeVariableName.get( "T" ) )
            .addParameter( AnnotationMirror.class, "mirror" )
            .addParameter( ParameterizedTypeName.get( gemBuilder, TypeVariableName.get( "T" ) ), "builder" )
            .returns( TypeVariableName.get( "T" ) )
            .addCode( CodeBlock.builder()
                .add( "// return fast\n" )
                .beginControlFlow( "if ( mirror == null || builder == null )" )
                .addStatement( "return null" )
                .endControlFlow()
                .add( getBuildMethodSetValueFromAnnotation( gemInfo ) )
                .addStatement( "builder.setMirror( mirror )" )
                .addStatement( "return builder.build()" )
                .build() )
            .build();
    }

    public static CodeBlock getBuildMethodSetValueFromAnnotation(GemInfo gemInfo) {
        if ( gemInfo.getGemValueInfos().isEmpty() ) {
            return CodeBlock.builder().build();
        }
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        codeBlockBuilder.add( "\n// fetch defaults from all defined values in the annotation type\n" )
            .addStatement(
                "$T enclosed = $T.methodsIn( mirror.getAnnotationType().asElement().getEnclosedElements() )",
                ParameterizedTypeName.get( List.class, ExecutableElement.class ),
                ElementFilter.class
            )
            .addStatement(
                "$T defaultValues = new $T<>( enclosed.size() )",
                ParameterizedTypeName.get( Map.class, String.class, AnnotationValue.class ),
                HashMap.class
            )
            .addStatement(
                "enclosed.forEach( e -> defaultValues.put( e.getSimpleName().toString(), e.getDefaultValue() ) )" )
            .add( "\n// fetch all explicitly set annotation values in the annotation instance\n" )
            .addStatement(
                "$T values = new $T<>( enclosed.size() )",
                ParameterizedTypeName.get( Map.class, String.class, AnnotationValue.class ),
                HashMap.class
            )
            .addStatement(
                "mirror.getElementValues().entrySet().forEach( e -> values.put( e.getKey().getSimpleName()" +
                    ".toString(), e.getValue() ) )" )
            .add( "\n// iterate and populate builder\n" );


        codeBlockBuilder.beginControlFlow( "for ( $T methodName : defaultValues.keySet() )", String.class );
        boolean first = true;
        for ( GemValueInfo gemValueInfo : gemInfo.getGemValueInfos() ) {
            if ( first ) {
                first = false;
                codeBlockBuilder.beginControlFlow( "if ( $S.equals( methodName ) )", gemValueInfo.getName() );
            }
            else {
                codeBlockBuilder.beginControlFlow( "else if ( $S.equals( methodName ) )", gemValueInfo.getName() );
            }

            codeBlockBuilder.add( createGemValueAndSetOnBuilderCodeBlock( gemValueInfo ) )
                .endControlFlow();
        }

        return codeBlockBuilder.endControlFlow()
            .build();
    }

    protected static CodeBlock createGemValueAndSetOnBuilderCodeBlock(GemValueInfo gemValueInfo) {
        GemValueType valueType = gemValueInfo.getValueType();
        String name = Util.capitalize( gemValueInfo.getName() );
        if ( valueType.isGem() ) {
            ClassName gemType = ClassName.get( valueType.getPacakage(), valueType.getGemName() );
            if ( valueType.isArray() ) {
                return CodeBlock.builder()
                    .addStatement(
                        "builder.set$N( $T.createArray( values.get( methodName ), defaultValues.get( methodName ), " +
                            "$T::instanceOn ) )",
                        name,
                        GEM_VALUE,
                        gemType
                    )
                    .build();
            }
            else {
                return CodeBlock.builder()
                    .addStatement(
                        "builder.set$N( $T.create( values.get( methodName ), defaultValues.get( methodName ), " +
                            "$T::instanceOn ) )",
                        name,
                        GEM_VALUE,
                        gemType
                    )
                    .build();
            }
        }
        else if ( valueType.isEnum() ) {
            if ( valueType.isArray() ) {
                return CodeBlock.builder()
                    .addStatement(
                        "builder.set$N( $T.createEnumArray( values.get( methodName ), " +
                            "defaultValues.get( methodName ) ) )",
                        name,
                        GEM_VALUE
                    )
                    .build();
            }
            else {
                return CodeBlock.builder()
                    .addStatement(
                        "builder.set$N( $T.createEnum( values.get( methodName ), defaultValues.get( methodName ) ) )",
                        name,
                        GEM_VALUE
                    )
                    .build();
            }
        }
        else {
            ClassName type = ClassName.get( valueType.getPacakage(), valueType.getElementName() );
            if ( valueType.isArray() ) {
                return CodeBlock.builder()
                    .addStatement(
                        "builder.set$N( $T.createArray( values.get( methodName ), defaultValues.get( methodName ), " +
                            "$T.class ) )",
                        name,
                        GEM_VALUE,
                        type
                    )
                    .build();
            }
            else {
                return CodeBlock.builder()
                    .addStatement(
                        "builder.set$N( $T.create( values.get( methodName ), defaultValues.get( methodName ), " +
                            "$T.class ) )",
                        name,
                        GEM_VALUE,
                        type
                    )
                    .build();
            }
        }
    }

    protected static ParameterSpec createParameterSpec(GemValueInfo gemValueInfo) {
        return ParameterSpec.builder(
            gemValueParameterTypeName( gemValueInfo.getValueType() ),
            gemValueInfo.getName()
        ).build();
    }

    protected static TypeName gemValueParameterTypeName(GemValueType type) {
        TypeName base = ClassName.get( type.getPacakage(), type.getElementName() );
        TypeName finalType;
        if ( type.isArray() ) {
            finalType = ParameterizedTypeName.get( LIST, base );
        }
        else {
            finalType = base;
        }

        return ParameterizedTypeName.get( GEM_VALUE, finalType );
    }
}
