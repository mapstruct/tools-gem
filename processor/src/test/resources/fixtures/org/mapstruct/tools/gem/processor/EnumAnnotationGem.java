package org.mapstruct.tools.gem.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.mapstruct.tools.gem.Gem;
import org.mapstruct.tools.gem.GemValue;


public class EnumAnnotationGem implements Gem {

    private final GemValue<SimpleEnumGem> mySimpleEnumWithDefault;
    private final GemValue<List<SimpleEnumGem>> mySimpleEnumArrayWithDefault;
    private final GemValue<SimpleEnumGem> mySimpleEnum;
    private final GemValue<List<SimpleEnumGem>> mySimpleEnumArray;
    private final boolean isValid;
    private final AnnotationMirror mirror;

    private EnumAnnotationGem( BuilderImpl builder ) {
        this.mySimpleEnumWithDefault = builder.mySimpleEnumWithDefault;
        this.mySimpleEnumArrayWithDefault = builder.mySimpleEnumArrayWithDefault;
        this.mySimpleEnum = builder.mySimpleEnum;
        this.mySimpleEnumArray = builder.mySimpleEnumArray;
        isValid = ( this.mySimpleEnumWithDefault != null && this.mySimpleEnumWithDefault.isValid() )
               && ( this.mySimpleEnumArrayWithDefault != null && this.mySimpleEnumArrayWithDefault.isValid() )
               && ( this.mySimpleEnum != null && this.mySimpleEnum.isValid() )
               && ( this.mySimpleEnumArray != null && this.mySimpleEnumArray.isValid() );
        mirror = builder.mirror;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link EnumAnnotationGem#mySimpleEnumWithDefault}
    */
    public GemValue<SimpleEnumGem> mySimpleEnumWithDefault( ) {
        return mySimpleEnumWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link EnumAnnotationGem#mySimpleEnumArrayWithDefault}
    */
    public GemValue<List<SimpleEnumGem>> mySimpleEnumArrayWithDefault( ) {
        return mySimpleEnumArrayWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link EnumAnnotationGem#mySimpleEnum}
    */
    public GemValue<SimpleEnumGem> mySimpleEnum( ) {
        return mySimpleEnum;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link EnumAnnotationGem#mySimpleEnumArray}
    */
    public GemValue<List<SimpleEnumGem>> mySimpleEnumArray( ) {
        return mySimpleEnumArray;
    }

    @Override
    public AnnotationMirror mirror( ) {
        return mirror;
    }

    @Override
    public boolean isValid( ) {
        return isValid;
    }

    public static EnumAnnotationGem  instanceOn(Element element) {
        return build( element, new BuilderImpl() );
    }

    public static EnumAnnotationGem instanceOn(AnnotationMirror mirror ) {
        return build( mirror, new BuilderImpl() );
    }

    public static  <T> T  build(Element element, Builder<T> builder) {
        AnnotationMirror mirror = element.getAnnotationMirrors().stream()
            .filter( a ->  "org.mapstruct.tools.gem.test.enummapping.EnumAnnotation".contentEquals( ( ( TypeElement )a.getAnnotationType().asElement() ).getQualifiedName() ) )
            .findAny()
            .orElse( null );
        return build( mirror, builder );
    }

    public static <T> T build(AnnotationMirror mirror, Builder<T> builder ) {

        // return fast
        if ( mirror == null || builder == null ) {
            return null;
        }

        // fetch defaults from all defined values in the annotation type
        List<ExecutableElement> enclosed = ElementFilter.methodsIn( mirror.getAnnotationType().asElement().getEnclosedElements() );
        Map<String, AnnotationValue> defaultValues = new HashMap<>( enclosed.size() );
        enclosed.forEach( e -> defaultValues.put( e.getSimpleName().toString(), e.getDefaultValue() ) );

        // fetch all explicitely set annotation values in the annotation instance
        Map<String, AnnotationValue> values = new HashMap<>( enclosed.size() );
        mirror.getElementValues().forEach( (key, value) -> values.put( key.getSimpleName().toString(), value ) );

        // iterate and populate builder
        for ( Map.Entry<String, AnnotationValue> defaultMethod : defaultValues.entrySet() ) {
            String methodName = defaultMethod.getKey();
            AnnotationValue defaultValue = defaultMethod.getValue();
            AnnotationValue value = values.get( methodName );
            switch ( methodName ) {
                case "mySimpleEnumWithDefault":
                    builder.setMysimpleenumwithdefault( GemValue.createEnum( value, defaultValue, SimpleEnumGem.class ) );
                    break;
                case "mySimpleEnumArrayWithDefault":
                    builder.setMysimpleenumarraywithdefault( GemValue.createEnumArray( value, defaultValue, SimpleEnumGem.class ) );
                    break;
                case "mySimpleEnum":
                    builder.setMysimpleenum( GemValue.createEnum( value, defaultValue, SimpleEnumGem.class ) );
                    break;
                case "mySimpleEnumArray":
                    builder.setMysimpleenumarray( GemValue.createEnumArray( value, defaultValue, SimpleEnumGem.class ) );
                    break;
            }
        }
        builder.setMirror( mirror );
        return builder.build();
    }

    /**
     * A builder that can be implemented by the user to define custom logic e.g. in the
     * build method, prior to creating the annotation gem.
     */
    public interface Builder<T> {

       /**
        * Sets the {@link GemValue} for {@link EnumAnnotationGem#mySimpleEnumWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link EnumAnnotationGem}
        */
        Builder<T> setMysimpleenumwithdefault(GemValue<SimpleEnumGem> methodName );

       /**
        * Sets the {@link GemValue} for {@link EnumAnnotationGem#mySimpleEnumArrayWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link EnumAnnotationGem}
        */
        Builder<T> setMysimpleenumarraywithdefault(GemValue<List<SimpleEnumGem>> methodName );

       /**
        * Sets the {@link GemValue} for {@link EnumAnnotationGem#mySimpleEnum}
        *
        * @return the {@link Builder} for this gem, representing {@link EnumAnnotationGem}
        */
        Builder<T> setMysimpleenum(GemValue<SimpleEnumGem> methodName );

       /**
        * Sets the {@link GemValue} for {@link EnumAnnotationGem#mySimpleEnumArray}
        *
        * @return the {@link Builder} for this gem, representing {@link EnumAnnotationGem}
        */
        Builder<T> setMysimpleenumarray(GemValue<List<SimpleEnumGem>> methodName );

        /**
         * Sets the annotation mirror
         *
         * @param mirror the mirror which this gem represents
         *
         * @return the {@link Builder} for this gem, representing {@link EnumAnnotationGem}
         */
          Builder<T> setMirror( AnnotationMirror mirror );

        /**
         * The build method can be overriden in a custom custom implementation, which allows
         * the user to define his own custom validation on the annotation.
         *
         * @return the representation of the annotation
         */
        T build();
    }

    private static class BuilderImpl implements Builder<EnumAnnotationGem> {

        private GemValue<SimpleEnumGem> mySimpleEnumWithDefault;
        private GemValue<List<SimpleEnumGem>> mySimpleEnumArrayWithDefault;
        private GemValue<SimpleEnumGem> mySimpleEnum;
        private GemValue<List<SimpleEnumGem>> mySimpleEnumArray;
        private AnnotationMirror mirror;

        public Builder<EnumAnnotationGem> setMysimpleenumwithdefault(GemValue<SimpleEnumGem> mySimpleEnumWithDefault ) {
            this.mySimpleEnumWithDefault = mySimpleEnumWithDefault;
            return this;
        }

        public Builder<EnumAnnotationGem> setMysimpleenumarraywithdefault(GemValue<List<SimpleEnumGem>> mySimpleEnumArrayWithDefault ) {
            this.mySimpleEnumArrayWithDefault = mySimpleEnumArrayWithDefault;
            return this;
        }

        public Builder<EnumAnnotationGem> setMysimpleenum(GemValue<SimpleEnumGem> mySimpleEnum ) {
            this.mySimpleEnum = mySimpleEnum;
            return this;
        }

        public Builder<EnumAnnotationGem> setMysimpleenumarray(GemValue<List<SimpleEnumGem>> mySimpleEnumArray ) {
            this.mySimpleEnumArray = mySimpleEnumArray;
            return this;
        }

        public Builder<EnumAnnotationGem> setMirror( AnnotationMirror mirror ) {
            this.mirror = mirror;
            return this;
        }

        public EnumAnnotationGem build() {
            return new EnumAnnotationGem( this );
        }
    }

}
