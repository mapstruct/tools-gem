/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem;

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
