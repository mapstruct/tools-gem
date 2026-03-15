/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sjaak Derksen
 *
 * Defining a GemDefinion will generate a Gem representing the annotation in the {@link GemDefinition#value()}
 */
@Repeatable(GemDefinitions.class)
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.PACKAGE, ElementType.TYPE })
public @interface GemDefinition {

    /**
     * @return the annotation for which a Gem should be generated
     */
    Class<?> value();
}
