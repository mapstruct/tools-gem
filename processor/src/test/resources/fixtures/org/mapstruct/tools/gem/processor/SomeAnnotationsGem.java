package org.mapstruct.tools.gem.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import org.mapstruct.tools.gem.Gem;
import org.mapstruct.tools.gem.GemValue;

public class SomeAnnotationsGem implements Gem {
    private final GemValue<List<SomeAnnotationGem>> value;

    private final boolean isValid;

    private final AnnotationMirror mirror;

    private SomeAnnotationsGem(BuilderImpl builder) {
        this.value = builder.value;
        this.mirror = builder.mirror;

        boolean isValid = this.value != null ? this.value.isValid() : false;
        this.isValid = isValid;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationsGem#value}
     */
    public GemValue<List<SomeAnnotationGem>> value() {
        return this.value;
    }

    @Override
    public AnnotationMirror mirror() {
        return this.mirror;
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }

    public static SomeAnnotationsGem instanceOn(Element element) {
        return build( element, new BuilderImpl() );
    }

    public static SomeAnnotationsGem instanceOn(AnnotationMirror mirror) {
        return build( mirror, new BuilderImpl() );
    }

    public static <T> T build(Element element, Builder<T> builder) {
        AnnotationMirror mirror = element.getAnnotationMirrors().stream()
                .filter( a -> "org.mapstruct.tools.gem.test.SomeAnnotations".contentEquals( ( ( TypeElement ) a.getAnnotationType().asElement() ).getQualifiedName() ) )
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
            if ( "value".equals( methodName ) ) {
                builder.setValue( GemValue.createArray( values.get( methodName ), defaultValues.get( methodName ), SomeAnnotationGem::instanceOn ) );
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
         * Sets the {@link GemValue} for {@link SomeAnnotationsGem#value}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationsGem}
         */
        Builder setValue(GemValue<List<SomeAnnotationGem>> value);

        /**
         * Sets the annotation mirror
         *
         * @param mirror the mirror which this gem represents
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationsGem}
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

    private static class BuilderImpl implements Builder<SomeAnnotationsGem> {
        private GemValue<List<SomeAnnotationGem>> value;

        private AnnotationMirror mirror;

        @Override
        public Builder setValue(GemValue<List<SomeAnnotationGem>> value) {
            this.value = value;
            return this;
        }

        @Override
        public Builder setMirror(AnnotationMirror mirror) {
            this.mirror = mirror;
            return this;
        }

        @Override
        public SomeAnnotationsGem build() {
            return new SomeAnnotationsGem( this );
        }
    }
}
