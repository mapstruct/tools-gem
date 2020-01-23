/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.annotations.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sjaak Derksen
 */
@Retention(RetentionPolicy.CLASS)
@Target( ElementType.TYPE )
public @interface SomeAnnotations {

    /**
     * The configuration of the bean attributes.
     *
     * @return The configuration of the bean attributes.
     */
    SomeAnnotation[] value();
}
