package org.mapstruct.tools.gem.processor;

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

import javax.lang.model.type.TypeMirror;

public class SomeArrayAnnotationGem implements Gem {

    private final GemValue<List<TypeMirror>> myClassWithDefault;
    private final GemValue<List<Boolean>> myBooleanWithDefault;
    private final GemValue<List<String>> myEnumWithDefault;
    private final GemValue<SomeAnnotationGem> myAnnotation;
    private final boolean isValid;
    private final AnnotationMirror mirror;

    private SomeArrayAnnotationGem( BuilderImpl builder ) {
        this.myClassWithDefault = builder.myClassWithDefault;
        this.myBooleanWithDefault = builder.myBooleanWithDefault;
        this.myEnumWithDefault = builder.myEnumWithDefault;
        this.myAnnotation = builder.myAnnotation;
        isValid = ( this.myClassWithDefault != null && this.myClassWithDefault.isValid() )
               && ( this.myBooleanWithDefault != null && this.myBooleanWithDefault.isValid() )
               && ( this.myEnumWithDefault != null && this.myEnumWithDefault.isValid() )
               && ( this.myAnnotation != null && this.myAnnotation.isValid() );
        mirror = builder.mirror;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeArrayAnnotationGem#myClassWithDefault}
    */
    public GemValue<List<TypeMirror>> myClassWithDefault( ) {
        return myClassWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeArrayAnnotationGem#myBooleanWithDefault}
    */
    public GemValue<List<Boolean>> myBooleanWithDefault( ) {
        return myBooleanWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeArrayAnnotationGem#myEnumWithDefault}
    */
    public GemValue<List<String>> myEnumWithDefault( ) {
        return myEnumWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeArrayAnnotationGem#myAnnotation}
    */
    public GemValue<SomeAnnotationGem> myAnnotation( ) {
        return myAnnotation;
    }

    @Override
    public AnnotationMirror mirror( ) {
        return mirror;
    }

    @Override
    public boolean isValid( ) {
        return isValid;
    }

    public static SomeArrayAnnotationGem  instanceOn(Element element) {
        return build( element, new BuilderImpl() );
    }

    public static SomeArrayAnnotationGem instanceOn(AnnotationMirror mirror ) {
        return build( mirror, new BuilderImpl() );
    }

    public static  <T> T  build(Element element, Builder<T> builder) {
        AnnotationMirror mirror = element.getAnnotationMirrors().stream()
            .filter( a ->  "org.mapstruct.tools.gem.test.gem.SomeArrayAnnotation".contentEquals( ( ( TypeElement )a.getAnnotationType().asElement() ).getQualifiedName() ) )
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
        for ( String methodName : defaultValues.keySet() ) {

            if ( "myClassWithDefault".equals( methodName ) ) {
                builder.setMyclasswithdefault( GemValue.createArray( values.get( methodName ), defaultValues.get( methodName ), TypeMirror.class ) );
            }
            else if ( "myBooleanWithDefault".equals( methodName ) ) {
                builder.setMybooleanwithdefault( GemValue.createArray( values.get( methodName ), defaultValues.get( methodName ), Boolean.class ) );
            }
            else if ( "myEnumWithDefault".equals( methodName ) ) {
                builder.setMyenumwithdefault( GemValue.createEnumArray( values.get( methodName ), defaultValues.get( methodName ) ) );
            }
            else if ( "myAnnotation".equals( methodName ) ) {
                builder.setMyannotation( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), SomeAnnotationGem::instanceOn ) );
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
        * Sets the {@link GemValue} for {@link SomeArrayAnnotationGem#myClassWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeArrayAnnotationGem}
        */
        Builder<T> setMyclasswithdefault(GemValue<List<TypeMirror>> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeArrayAnnotationGem#myBooleanWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeArrayAnnotationGem}
        */
        Builder<T> setMybooleanwithdefault(GemValue<List<Boolean>> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeArrayAnnotationGem#myEnumWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeArrayAnnotationGem}
        */
        Builder<T> setMyenumwithdefault(GemValue<List<String>> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeArrayAnnotationGem#myAnnotation}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeArrayAnnotationGem}
        */
        Builder<T> setMyannotation(GemValue<SomeAnnotationGem> methodName );

        /**
         * Sets the annotation mirror
         *
         * @param mirror the mirror which this gem represents
         *
         * @return the {@link Builder} for this gem, representing {@link SomeArrayAnnotationGem}
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

    private static class BuilderImpl implements Builder<SomeArrayAnnotationGem> {

        private GemValue<List<TypeMirror>> myClassWithDefault;
        private GemValue<List<Boolean>> myBooleanWithDefault;
        private GemValue<List<String>> myEnumWithDefault;
        private GemValue<SomeAnnotationGem> myAnnotation;
        private AnnotationMirror mirror;

        public Builder<SomeArrayAnnotationGem> setMyclasswithdefault(GemValue<List<TypeMirror>> myClassWithDefault ) {
            this.myClassWithDefault = myClassWithDefault;
            return this;
        }

        public Builder<SomeArrayAnnotationGem> setMybooleanwithdefault(GemValue<List<Boolean>> myBooleanWithDefault ) {
            this.myBooleanWithDefault = myBooleanWithDefault;
            return this;
        }

        public Builder<SomeArrayAnnotationGem> setMyenumwithdefault(GemValue<List<String>> myEnumWithDefault ) {
            this.myEnumWithDefault = myEnumWithDefault;
            return this;
        }

        public Builder<SomeArrayAnnotationGem> setMyannotation(GemValue<SomeAnnotationGem> myAnnotation ) {
            this.myAnnotation = myAnnotation;
            return this;
        }

        public Builder<SomeArrayAnnotationGem> setMirror( AnnotationMirror mirror ) {
            this.mirror = mirror;
            return this;
        }

        public SomeArrayAnnotationGem build() {
            return new SomeArrayAnnotationGem( this );
        }
    }

}
