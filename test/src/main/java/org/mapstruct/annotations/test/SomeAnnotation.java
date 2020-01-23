/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.annotations.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sjaak Derksen
 */
@Repeatable(SomeAnnotations.class)
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE )
public @interface SomeAnnotation {

    public enum TEST { A, B };

    Class<?> myClassWithDefault() default SomeAnnotation.class;

    boolean myBooleanWithDefault() default false;

    char myCharWithDefault() default 'c';

    byte myByteWithDefault() default 0x0F;

    short mShortWithDefault() default 5;

    int myIntWithDefault() default 7;

    int myLongWithDefault() default 44;

    float myFloatWithDefault() default 3.3f;

    double myDoubleWithDefault() default 9.1d;

    String myStringWithDefault() default "test";

    TEST myEnumWithDefault() default TEST.A;

    Class<?> myClass();

    boolean myBoolean();

    char myChar();

    byte myByte();

    short myShort();

    int myInt();

    int myLong();

    float myFloat();

    double myDouble();

    String myString();

    TEST myEnum();

}
