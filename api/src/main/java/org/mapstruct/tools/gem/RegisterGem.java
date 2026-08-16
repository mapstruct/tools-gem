/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Registers the annotated {@code enum} as the gem enum for {@code value()}.
 * The annotated enum must contain exactly all constants of the {@code enum} specified in {@code value()}.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
public @interface RegisterGem {

    /**
     * @return the {@code enum} which should be represented by the annotated class.
     */
    Class<? extends Enum<?>> value();
}
