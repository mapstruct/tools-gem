package org.mapstruct.tools.gem.processor;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.mapstruct.tools.gem.Gem;

public class BuilderGem implements Gem {
    private final boolean isValid;

    private final AnnotationMirror mirror;

    private BuilderGem(BuilderImpl builder) {
        this.mirror = builder.mirror;

        this.isValid = true;
    }

    @Override
    public AnnotationMirror mirror() {
        return this.mirror;
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }

    public static BuilderGem instanceOn(Element element) {
        return build( element, new BuilderImpl() );
    }

    public static BuilderGem instanceOn(AnnotationMirror mirror) {
        return build( mirror, new BuilderImpl() );
    }

    public static <T> T build(Element element, Builder_<T> builder) {
        AnnotationMirror mirror = element.getAnnotationMirrors().stream()
                .filter( a -> "org.mapstruct.tools.gem.test.Builder".contentEquals( ( ( TypeElement ) a.getAnnotationType().asElement() ).getQualifiedName() ) )
                .findAny()
                .orElse( null );
        return build( mirror, builder );
    }

    public static <T> T build(AnnotationMirror mirror, Builder_<T> builder) {
        // return fast
        if ( mirror == null || builder == null ) {
            return null;
        }
        builder.setMirror( mirror );
        return builder.build();
    }

    /**
     * A builder that can be implemented by the user to define custom logic
     * e.g. in the build method, prior to creating the annotation gem.
     */
    public interface Builder_<T> {
        /**
         * Sets the annotation mirror
         *
         * @param mirror the mirror which this gem represents
         *
         * @return the {@link Builder_} for this gem, representing {@link BuilderGem}
         */
        Builder_ setMirror(AnnotationMirror mirror);

        /**
         * The build method can be overridden in a custom implementation,
         * which allows the user to define their own custom validation on the annotation.
         *
         * @return the representation of the annotation
         */
        T build();
    }

    private static class BuilderImpl implements Builder_<BuilderGem> {
        private AnnotationMirror mirror;

        @Override
        public Builder_ setMirror(AnnotationMirror mirror) {
            this.mirror = mirror;
            return this;
        }

        @Override
        public BuilderGem build() {
            return new BuilderGem( this );
        }
    }
}
