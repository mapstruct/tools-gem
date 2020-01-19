/*
 *
 */
package ${gemInfo.gemPackageName};

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
<#if gemInfo.containingArrays>
import java.util.stream.Stream;
import java.util.stream.Collectors;
</#if>

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.AbstractAnnotationValueVisitor8;
import javax.lang.model.util.ElementFilter;
import org.annotationhelper.GemValue;

<#list gemInfo.imports as importItem>
import ${importItem};
</#list>

public class ${gemInfo.gemName}  {

    public static ${gemInfo.annotationName}  instanceOn(Element element) {
        return build( element, new ${gemInfo.builderImplName}() );
    }

    public static ${gemInfo.annotationName} instanceOn(AnnotationMirror mirror ) {
        return build( mirror, new ${gemInfo.builderImplName}() );
    }

    public static  <T> T  build(Element element, ${gemInfo.builderName}<T> builder) {
        AnnotationMirror mirror = element.getAnnotationMirrors().stream()
            .filter( a ->  "${gemInfo.annotationFqn}".contentEquals( ( ( TypeElement )a.getAnnotationType().asElement() ).getQualifiedName() ) )
            .findAny()
            .orElse( null );
        return build( mirror, builder );
    }

    public static <T> T build(AnnotationMirror mirror, ${gemInfo.builderName}<T> builder ) {

        // return fast
        if ( mirror == null || builder == null ) {
            return null;
        }
        <#if gemInfo.gemValueInfos?size != 0>

        // fetch defaults from all defined values in the annotation type
        List<ExecutableElement> enclosed = ElementFilter.methodsIn( mirror.getAnnotationType().asElement().getEnclosedElements() );
        Map<String, AnnotationValue> defaultValues = new HashMap<>( enclosed.size() );
        enclosed.forEach( e -> defaultValues.put( e.getSimpleName().toString(), e.getDefaultValue() ) );

        // fetch all explicitely set annotation values in the annotation instance
        Map<String, AnnotationValue> values = new HashMap<>( enclosed.size() );
        mirror.getElementValues().entrySet().forEach( e -> values.put( e.getKey().getSimpleName().toString(), e.getValue() ) );

        // iterate and populate builder
        for ( String methodName : defaultValues.keySet() ) {

        <#list gemInfo.gemValueInfos as gemValueInfo>
            <#if gemValueInfo_index != 0>else </#if>if ( "${gemValueInfo.name}".equals( methodName ) ) {
                builder.set${gemValueInfo.name?capitalize}( new ${gemValueInfo.valueType.gemValueClass}( values.get( methodName ), defaultValues.get( methodName ) ) );
            }
        </#list>
        }
        </#if>

        return builder.build();
    }

    /**
     * A builder that can be implemented by the user to define custom logic e.g. in the
     * build method, prior to creating the annotation gem.
     */
    public interface ${gemInfo.builderName}<T> {

    <#list gemInfo.gemValueInfos as gemValueInfo>
       /**
        * Sets the {@link GemValue} for {@link ${gemInfo.annotationName}#${gemValueInfo.name}}
        *
        * @return the {@link ${gemInfo.builderName}} for this gem, representing {@link ${gemInfo.annotationName}}
        */
        ${gemInfo.builderName} set${gemValueInfo.name?capitalize}(GemValue<${gemValueInfo.valueType.name}> methodName );

    </#list>
        /**
         * Sets the annotation mirror
         *
         * @parameter mirror the mirror which this gem represents
         *
         * @return the {@link ${gemInfo.builderName}} for this gem, representing {@link ${gemInfo.annotationName}}
         */
          ${gemInfo.builderName} setMirror( AnnotationMirror mirror );

        /**
         * The build method can be overriden in a custom custom implementation, which allows
         * the user to define his own custom validation on the annotation.
         *
         * @return the representation of the annotation
         */
        T build();
    }

    private static class ${gemInfo.builderImplName} implements ${gemInfo.builderName}<${gemInfo.annotationName}> {

    <#list gemInfo.gemValueInfos as gemValueInfo>
        private GemValue<${gemValueInfo.valueType.name}> ${gemValueInfo.name};
    </#list>
        private AnnotationMirror mirror;

    <#list gemInfo.gemValueInfos as gemValueInfo>
        public ${gemInfo.builderName} set${gemValueInfo.name?capitalize}(GemValue<${gemValueInfo.valueType.name}> ${gemValueInfo.name} ) {
            this.${gemValueInfo.name} = ${gemValueInfo.name};
            return this;
        }

    </#list>
        public ${gemInfo.builderName}  setMirror( AnnotationMirror mirror ) {
            this.mirror = mirror;
            return this;
        }

        public ${gemInfo.annotationName} build() {
            return new ${gemInfo.annotationName}( this );
        }
    }

    public static class ${gemInfo.annotationName} {

    <#list gemInfo.gemValueInfos as gemValueInfo>
        private final GemValue<${gemValueInfo.valueType.name}> ${gemValueInfo.name};
    </#list>
        private final boolean isValid;
        private final AnnotationMirror mirror;

        private ${gemInfo.annotationName}( ${gemInfo.builderImplName} builder ) {
    <#list gemInfo.gemValueInfos as gemValueInfo>
            ${gemValueInfo.name} = builder.${gemValueInfo.name};
    </#list>
     <#list gemInfo.gemValueInfos as gemValueInfo>
            <#if gemValueInfo_index == 0>isValid = <#else>       && </#if>( ${gemValueInfo.name} != null ? ${gemValueInfo.name}.isValid() : false )<#if !(gemValueInfo_has_next)>;</#if>
    </#list>
            <#if gemInfo.gemValueInfos?size==0>isValid = true;</#if>
            mirror = builder.mirror;
        }

    <#list gemInfo.gemValueInfos as gemValueInfo>
       /**
        * accessor
        *
        * @return the {@link GemValue} for {@link ${gemInfo.annotationName}#${gemValueInfo.name}}
        */
        public GemValue<${gemValueInfo.valueType.name}> ${gemValueInfo.name}( ) {
            return ${gemValueInfo.name};
        }

    </#list>
        public AnnotationMirror mirror( ) {
            return mirror;
        }

        public boolean isValid( ) {
            return isValid;
        }
    }

    <#list gemInfo.usedGemValueTypes as valueType>
    private static class ${valueType.gemValueClass} implements GemValue<${valueType.name}> {

        private final ${valueType.name} value;
        private final ${valueType.name} defaultValue;
        private final AnnotationValue annotationValue;

        public ${valueType.gemValueClass}(AnnotationValue annotationValue, AnnotationValue annotationDefaultValue ) {
        <#if valueType.array>
          <#if valueType.enum>
            if ( annotationValue != null && annotationValue instanceof List ) {
                value = toStream( (List) annotationValue ).map( VariableElement.class::cast ).map( v -> v.getSimpleName().toString() ).collect( Collectors.toList() );
            }
            else {
                value = null;
            }
            if ( annotationDefaultValue != null && annotationDefaultValue instanceof List ) {
                defaultValue = toStream( (List) annotationDefaultValue ).map( VariableElement.class::cast ).map( v -> v.getSimpleName().toString() ).collect( Collectors.toList() );
            }
            else {
                defaultValue = null;
            }
          <#elseif valueType.gem>
            if ( annotationValue != null && annotationValue instanceof List ) {
                value = toStream( (List) annotationValue ).map( ${valueType.gemName}::instanceOn ).collect( Collectors.toList() );
            }
            else {
                value = null;
            }
            if ( annotationDefaultValue != null && annotationDefaultValue instanceof List ) {
                defaultValue = toStream( (List) annotationDefaultValue ).map( ${valueType.gemName}::instanceOn ).collect( Collectors.toList() );
            }
            else {
                defaultValue = null;
            }
          <#else>
            if ( annotationValue != null && annotationValue instanceof List ) {
                value = toStream( (List) annotationValue ).map( ${valueType.elementName}.class::cast ).collect( Collectors.toList() );
            }
            else {
                value = null;
            }
            if ( annotationDefaultValue != null && annotationDefaultValue instanceof List ) {
                defaultValue = toStream( (List) annotationDefaultValue ).map( ${valueType.elementName}.class::cast ).collect( Collectors.toList() );
            }
            else {
                defaultValue = null;
            }
          </#if>
            this.annotationValue = annotationValue;
        <#else>
          <#if valueType.enum>
            this.value = annotationValue == null ? null : ( (VariableElement) annotationValue.getValue() ).getSimpleName().toString();
            this.defaultValue = annotationDefaultValue == null ? null : ( (VariableElement) annotationDefaultValue.getValue() ).getSimpleName().toString();
          <#elseif valueType.gem>
            this.value = annotationValue == null ? null : ${valueType.gemName}.instanceOn( (AnnotationMirror) annotationValue.getValue() );
            this.defaultValue = annotationDefaultValue == null ? null : ${valueType.gemName}.instanceOn( (AnnotationMirror) annotationDefaultValue.getValue() );
          <#else>
            this.value = annotationValue == null ? null : (${valueType.name}) annotationValue.getValue();
            this.defaultValue = annotationDefaultValue == null ? null : (${valueType.name}) annotationDefaultValue.getValue();
          </#if>
            this.annotationValue = annotationValue;
        </#if>
        }

        @Override
        public ${valueType.name} get() {
            return value != null ? value : defaultValue;
        }

        @Override
        public ${valueType.name} getValue() {
            return value;
        }

        @Override
        public ${valueType.name} getDefaultValue() {
            return defaultValue;
        }

        @Override
        public AnnotationValue getAnnotationValue() {
            return annotationValue;
        }

        @Override
        public boolean hasValue() {
            return value != null;
        }

        @Override
        public boolean isValid() {
            return value != null || defaultValue != null;
        }
    }

    </#list>
<#if gemInfo.containingArrays>
    private static Stream<AnnotationMirror> toStream( List<?> annotationValues ) {
        return annotationValues.stream()
        .filter( AnnotationValue.class::isInstance )
        .map( AnnotationValue.class::cast )
        .map( AnnotationValue::getValue )
        .filter( AnnotationMirror.class::isInstance )
        .map( AnnotationMirror.class::cast );
    }
</#if>
}
