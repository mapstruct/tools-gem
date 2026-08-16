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


public class EnumAnnotationRegisterGem implements Gem {

    private final GemValue<MySimpleEnumGem> mySimpleEnumWithDefault;
    private final GemValue<List<MySimpleEnumGem>> mySimpleEnumArrayWithDefault;
    private final GemValue<MySimpleEnumGem> mySimpleEnum;
    private final GemValue<List<MySimpleEnumGem>> mySimpleEnumArray;
    private final boolean isValid;
    private final AnnotationMirror mirror;

    private EnumAnnotationRegisterGem( BuilderImpl builder ) {
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
    * @return the {@link GemValue} for {@link EnumAnnotationRegisterGem#mySimpleEnumWithDefault}
    */
    public GemValue<MySimpleEnumGem> mySimpleEnumWithDefault( ) {
        return mySimpleEnumWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link EnumAnnotationRegisterGem#mySimpleEnumArrayWithDefault}
    */
    public GemValue<List<MySimpleEnumGem>> mySimpleEnumArrayWithDefault( ) {
        return mySimpleEnumArrayWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link EnumAnnotationRegisterGem#mySimpleEnum}
    */
    public GemValue<MySimpleEnumGem> mySimpleEnum( ) {
        return mySimpleEnum;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link EnumAnnotationRegisterGem#mySimpleEnumArray}
    */
    public GemValue<List<MySimpleEnumGem>> mySimpleEnumArray( ) {
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

    public static EnumAnnotationRegisterGem  instanceOn(Element element) {
        return build( element, new BuilderImpl() );
    }

    public static EnumAnnotationRegisterGem instanceOn(AnnotationMirror mirror ) {
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
                    builder.setMysimpleenumwithdefault( GemValue.createEnum( value, defaultValue, MySimpleEnumGem.class ) );
                    break;
                case "mySimpleEnumArrayWithDefault":
                    builder.setMysimpleenumarraywithdefault( GemValue.createEnumArray( value, defaultValue, MySimpleEnumGem.class ) );
                    break;
                case "mySimpleEnum":
                    builder.setMysimpleenum( GemValue.createEnum( value, defaultValue, MySimpleEnumGem.class ) );
                    break;
                case "mySimpleEnumArray":
                    builder.setMysimpleenumarray( GemValue.createEnumArray( value, defaultValue, MySimpleEnumGem.class ) );
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
        * Sets the {@link GemValue} for {@link EnumAnnotationRegisterGem#mySimpleEnumWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link EnumAnnotationRegisterGem}
        */
        Builder<T> setMysimpleenumwithdefault(GemValue<MySimpleEnumGem> methodName );

       /**
        * Sets the {@link GemValue} for {@link EnumAnnotationRegisterGem#mySimpleEnumArrayWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link EnumAnnotationRegisterGem}
        */
        Builder<T> setMysimpleenumarraywithdefault(GemValue<List<MySimpleEnumGem>> methodName );

       /**
        * Sets the {@link GemValue} for {@link EnumAnnotationRegisterGem#mySimpleEnum}
        *
        * @return the {@link Builder} for this gem, representing {@link EnumAnnotationRegisterGem}
        */
        Builder<T> setMysimpleenum(GemValue<MySimpleEnumGem> methodName );

       /**
        * Sets the {@link GemValue} for {@link EnumAnnotationRegisterGem#mySimpleEnumArray}
        *
        * @return the {@link Builder} for this gem, representing {@link EnumAnnotationRegisterGem}
        */
        Builder<T> setMysimpleenumarray(GemValue<List<MySimpleEnumGem>> methodName );

        /**
         * Sets the annotation mirror
         *
         * @param mirror the mirror which this gem represents
         *
         * @return the {@link Builder} for this gem, representing {@link EnumAnnotationRegisterGem}
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

    private static class BuilderImpl implements Builder<EnumAnnotationRegisterGem> {

        private GemValue<MySimpleEnumGem> mySimpleEnumWithDefault;
        private GemValue<List<MySimpleEnumGem>> mySimpleEnumArrayWithDefault;
        private GemValue<MySimpleEnumGem> mySimpleEnum;
        private GemValue<List<MySimpleEnumGem>> mySimpleEnumArray;
        private AnnotationMirror mirror;

        public Builder<EnumAnnotationRegisterGem> setMysimpleenumwithdefault(GemValue<MySimpleEnumGem> mySimpleEnumWithDefault ) {
            this.mySimpleEnumWithDefault = mySimpleEnumWithDefault;
            return this;
        }

        public Builder<EnumAnnotationRegisterGem> setMysimpleenumarraywithdefault(GemValue<List<MySimpleEnumGem>> mySimpleEnumArrayWithDefault ) {
            this.mySimpleEnumArrayWithDefault = mySimpleEnumArrayWithDefault;
            return this;
        }

        public Builder<EnumAnnotationRegisterGem> setMysimpleenum(GemValue<MySimpleEnumGem> mySimpleEnum ) {
            this.mySimpleEnum = mySimpleEnum;
            return this;
        }

        public Builder<EnumAnnotationRegisterGem> setMysimpleenumarray(GemValue<List<MySimpleEnumGem>> mySimpleEnumArray ) {
            this.mySimpleEnumArray = mySimpleEnumArray;
            return this;
        }

        public Builder<EnumAnnotationRegisterGem> setMirror( AnnotationMirror mirror ) {
            this.mirror = mirror;
            return this;
        }

        public EnumAnnotationRegisterGem build() {
            return new EnumAnnotationRegisterGem( this );
        }
    }

}
