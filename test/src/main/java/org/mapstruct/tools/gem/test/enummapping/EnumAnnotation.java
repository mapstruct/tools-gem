/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.test.enummapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE )
public @interface EnumAnnotation {
    SimpleEnum mySimpleEnumWithDefault() default SimpleEnum.C;
    SimpleEnum[] mySimpleEnumArrayWithDefault() default {SimpleEnum.A, SimpleEnum.B};
    SimpleEnum mySimpleEnum();
    SimpleEnum[] mySimpleEnumArray();
}
