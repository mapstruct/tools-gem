<#-- @ftlvariable name="gemInfo" type="org.mapstruct.tools.gem.processor.GemInfo" -->
/*
 *
 */
package ${gemInfo.gemPackageName};

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.mapstruct.tools.gem.Gem;
import org.mapstruct.tools.gem.GemValue;

<#list gemInfo.imports as importItem>
import ${importItem};
</#list>

public class ${gemInfo.gemName} implements Gem {

<#list gemInfo.gemValueInfos as gemValueInfo>
    private final GemValue<${gemValueInfo.valueType.name}> ${gemValueInfo.name};
</#list>
    private final boolean isValid;
    private final AnnotationMirror mirror;

    private ${gemInfo.gemName}( ${gemInfo.builderImplName} builder ) {
    <#list gemInfo.gemValueInfos as gemValueInfo>
        this.${gemValueInfo.name} = builder.${gemValueInfo.name};
    </#list>
    <#list gemInfo.gemValueInfos as gemValueInfo>
        <#if gemValueInfo_index == 0>isValid = <#else>       && </#if>( this.${gemValueInfo.name} != null ? this.${gemValueInfo.name}.isValid() : false )<#if !(gemValueInfo_has_next)>;</#if>
    </#list>
    <#if gemInfo.gemValueInfos?size==0>
        isValid = true;
    </#if>
        mirror = builder.mirror;
    }

    <#list gemInfo.gemValueInfos as gemValueInfo>
    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link ${gemInfo.gemName}#${gemValueInfo.name}}
    */
    public GemValue<${gemValueInfo.valueType.name}> ${gemValueInfo.name}( ) {
        return ${gemValueInfo.name};
    }

    </#list>
    @Override
    public AnnotationMirror mirror( ) {
        return mirror;
    }

    @Override
    public boolean isValid( ) {
        return isValid;
    }

    public static ${gemInfo.gemName}  instanceOn(Element element) {
        return build( element, new ${gemInfo.builderImplName}() );
    }

    public static ${gemInfo.gemName} instanceOn(AnnotationMirror mirror ) {
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
                <@compress single_line=true>builder.set${gemValueInfo.name?capitalize}(
                        <#if gemValueInfo.valueType.gem>
                            <#if gemValueInfo.valueType.array>
                                GemValueImpl.createArray( values.get( methodName ), defaultValues.get( methodName ), ${gemValueInfo.valueType.gemName}::instanceOn )
                            <#else>
                                GemValueImpl.create( values.get( methodName ), defaultValues.get( methodName ), ${gemValueInfo.valueType.gemName}::instanceOn )
                            </#if>
                        <#elseif gemValueInfo.valueType.enum>
                            <#if gemValueInfo.valueType.array>
                                GemValueImpl.createEnumArray( values.get( methodName ), defaultValues.get( methodName ) )
                            <#else>
                                GemValueImpl.createEnum( values.get( methodName ), defaultValues.get( methodName ) )
                            </#if>
                        <#else>
                            <#if gemValueInfo.valueType.array>
                                GemValueImpl.createArray( values.get( methodName ), defaultValues.get( methodName ), ${gemValueInfo.valueType.elementName}.class )
                            <#else>
                                GemValueImpl.create( values.get( methodName ), defaultValues.get( methodName ), ${gemValueInfo.valueType.elementName}.class )
                            </#if>
                        </#if>
                    );</@compress>
            }
        </#list>
        }
        </#if>
        builder.setMirror( mirror );
        return builder.build();
    }

    /**
     * A builder that can be implemented by the user to define custom logic e.g. in the
     * build method, prior to creating the annotation gem.
     */
    public interface ${gemInfo.builderName}<T> {

    <#list gemInfo.gemValueInfos as gemValueInfo>
       /**
        * Sets the {@link GemValue} for {@link ${gemInfo.gemName}#${gemValueInfo.name}}
        *
        * @return the {@link ${gemInfo.builderName}} for this gem, representing {@link ${gemInfo.gemName}}
        */
        ${gemInfo.builderName} set${gemValueInfo.name?capitalize}(GemValue<${gemValueInfo.valueType.name}> methodName );

    </#list>
        /**
         * Sets the annotation mirror
         *
         * @parameter mirror the mirror which this gem represents
         *
         * @return the {@link ${gemInfo.builderName}} for this gem, representing {@link ${gemInfo.gemName}}
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

    private static class ${gemInfo.builderImplName} implements ${gemInfo.builderName}<${gemInfo.gemName}> {

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

        public ${gemInfo.gemName} build() {
            return new ${gemInfo.gemName}( this );
        }
    }

}
