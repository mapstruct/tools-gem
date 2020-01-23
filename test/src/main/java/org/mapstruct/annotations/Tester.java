package org.mapstruct.annotations;

import org.mapstruct.annotations.test.SomeAnnotation;

@SomeAnnotation(
    myClass = Object.class,
    myBoolean = true,
    myChar = 'a',
    myByte = 0x22,
    myShort = 1,
    myInt = 2,
    myLong = 3,
    myFloat = 99.3f,
    myDouble = 5033.19d,
    myString = "some",
    myEnum = SomeAnnotation.TEST.B
)
public class Tester {
}
