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

public class SomeArrayAnnotationGem implements Gem {
    private final GemValue<List<TypeMirror>> myClassWithDefault;

    private final GemValue<List<Boolean>> myBooleanWithDefault;

    private final GemValue<List<String>> myEnumWithDefault;

    private final GemValue<SomeAnnotationGem> myAnnotation;

    private final boolean isValid;

    private final AnnotationMirror mirror;

    private SomeArrayAnnotationGem(BuilderImpl builder) {
        this.myClassWithDefault = builder.myClassWithDefault;
        this.myBooleanWithDefault = builder.myBooleanWithDefault;
        this.myEnumWithDefault = builder.myEnumWithDefault;
        this.myAnnotation = builder.myAnnotation;
        this.mirror = builder.mirror;

        boolean isValid = this.myClassWithDefault != null ? this.myClassWithDefault.isValid() : false;
        isValid = isValid && this.myBooleanWithDefault != null ? this.myBooleanWithDefault.isValid() : false;
        isValid = isValid && this.myEnumWithDefault != null ? this.myEnumWithDefault.isValid() : false;
        isValid = isValid && this.myAnnotation != null ? this.myAnnotation.isValid() : false;
        this.isValid = isValid;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeArrayAnnotationGem#myClassWithDefault}
     */
    public GemValue<List<TypeMirror>> myClassWithDefault() {
        return this.myClassWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeArrayAnnotationGem#myBooleanWithDefault}
     */
    public GemValue<List<Boolean>> myBooleanWithDefault() {
        return this.myBooleanWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeArrayAnnotationGem#myEnumWithDefault}
     */
    public GemValue<List<String>> myEnumWithDefault() {
        return this.myEnumWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeArrayAnnotationGem#myAnnotation}
     */
    public GemValue<SomeAnnotationGem> myAnnotation() {
        return this.myAnnotation;
    }

    @Override
    public AnnotationMirror mirror() {
        return this.mirror;
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }

    public static SomeArrayAnnotationGem instanceOn(Element element) {
        return build( element, new BuilderImpl() );
    }

    public static SomeArrayAnnotationGem instanceOn(AnnotationMirror mirror) {
        return build( mirror, new BuilderImpl() );
    }

    public static <T> T build(Element element, Builder<T> builder) {
        AnnotationMirror mirror = element.getAnnotationMirrors().stream()
                .filter( a -> "org.mapstruct.tools.gem.test.gem.SomeArrayAnnotation".contentEquals( ( ( TypeElement ) a.getAnnotationType().asElement() ).getQualifiedName() ) )
                .findAny()
                .orElse( null );
        return build( mirror, builder );
    }

    public static <T> T build(AnnotationMirror mirror, Builder<T> builder) {
        // return fast
        if ( mirror == null || builder == null ) {
            return null;
        }

        // fetch defaults from all defined values in the annotation type
        List<ExecutableElement> enclosed = ElementFilter.methodsIn( mirror.getAnnotationType().asElement().getEnclosedElements() );
        Map<String, AnnotationValue> defaultValues = new HashMap<>( enclosed.size() );
        enclosed.forEach( e -> defaultValues.put( e.getSimpleName().toString(), e.getDefaultValue() ) );

        // fetch all explicitly set annotation values in the annotation instance
        Map<String, AnnotationValue> values = new HashMap<>( enclosed.size() );
        mirror.getElementValues().entrySet().forEach( e -> values.put( e.getKey().getSimpleName().toString(), e.getValue() ) );

        // iterate and populate builder
        for ( String methodName : defaultValues.keySet() ) {
            if ( "myClassWithDefault".equals( methodName ) ) {
                builder.setMyClassWithDefault( GemValue.createArray( values.get( methodName ), defaultValues.get( methodName ), TypeMirror.class ) );
            }
            else if ( "myBooleanWithDefault".equals( methodName ) ) {
                builder.setMyBooleanWithDefault( GemValue.createArray( values.get( methodName ), defaultValues.get( methodName ), Boolean.class ) );
            }
            else if ( "myEnumWithDefault".equals( methodName ) ) {
                builder.setMyEnumWithDefault( GemValue.createEnumArray( values.get( methodName ), defaultValues.get( methodName ) ) );
            }
            else if ( "myAnnotation".equals( methodName ) ) {
                builder.setMyAnnotation( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), SomeAnnotationGem::instanceOn ) );
            }
        }
        builder.setMirror( mirror );
        return builder.build();
    }

    /**
     * A builder that can be implemented by the user to define custom logic
     * e.g. in the build method, prior to creating the annotation gem.
     */
    public interface Builder<T> {
        /**
         * Sets the {@link GemValue} for {@link SomeArrayAnnotationGem#myClassWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeArrayAnnotationGem}
         */
        Builder setMyClassWithDefault(GemValue<List<TypeMirror>> myClassWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeArrayAnnotationGem#myBooleanWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeArrayAnnotationGem}
         */
        Builder setMyBooleanWithDefault(GemValue<List<Boolean>> myBooleanWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeArrayAnnotationGem#myEnumWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeArrayAnnotationGem}
         */
        Builder setMyEnumWithDefault(GemValue<List<String>> myEnumWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeArrayAnnotationGem#myAnnotation}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeArrayAnnotationGem}
         */
        Builder setMyAnnotation(GemValue<SomeAnnotationGem> myAnnotation);

        /**
         * Sets the annotation mirror
         *
         * @param mirror the mirror which this gem represents
         *
         * @return the {@link Builder} for this gem, representing {@link SomeArrayAnnotationGem}
         */
        Builder setMirror(AnnotationMirror mirror);

        /**
         * The build method can be overridden in a custom implementation,
         * which allows the user to define their own custom validation on the annotation.
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

        @Override
        public Builder setMyClassWithDefault(GemValue<List<TypeMirror>> myClassWithDefault) {
            this.myClassWithDefault = myClassWithDefault;
            return this;
        }

        @Override
        public Builder setMyBooleanWithDefault(GemValue<List<Boolean>> myBooleanWithDefault) {
            this.myBooleanWithDefault = myBooleanWithDefault;
            return this;
        }

        @Override
        public Builder setMyEnumWithDefault(GemValue<List<String>> myEnumWithDefault) {
            this.myEnumWithDefault = myEnumWithDefault;
            return this;
        }

        @Override
        public Builder setMyAnnotation(GemValue<SomeAnnotationGem> myAnnotation) {
            this.myAnnotation = myAnnotation;
            return this;
        }

        @Override
        public Builder setMirror(AnnotationMirror mirror) {
            this.mirror = mirror;
            return this;
        }

        @Override
        public SomeArrayAnnotationGem build() {
            return new SomeArrayAnnotationGem( this );
        }
    }
}
