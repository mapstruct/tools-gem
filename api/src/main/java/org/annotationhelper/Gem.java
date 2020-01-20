package org.annotationhelper;

import javax.lang.model.element.AnnotationMirror;

public interface Gem {

    /**
     * The annotation mirror on which this Gem is based
     *
     * @return the annotation mirror
     */
    AnnotationMirror mirror( );

    /**
     *
     * @return the Gem is valid
     */
    boolean isValid( );
}
