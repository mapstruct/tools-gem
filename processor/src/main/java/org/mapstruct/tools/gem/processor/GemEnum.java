/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.processor;

import java.util.Arrays;
import java.util.List;
import javax.lang.model.element.Element;

public class GemEnum {
    private final String gemPackageName;
    private final String gemName;
    private final String originalEnumFullName;
    private final List<String> enumConstants;
    private final Element[] originatingElements;

    public GemEnum(String gemPackageName, String gemName, String originalEnumFullName,
                   List<String> values, Element... originatingElements) {
        this.gemPackageName = gemPackageName;
        this.gemName = gemName;
        this.originalEnumFullName = originalEnumFullName;
        this.enumConstants = values;
        this.originatingElements = Arrays.copyOf( originatingElements, originatingElements.length );
    }

    public String getGemPackageName() {
        return gemPackageName;
    }

    public String getGemName() {
        return gemName;
    }

    public String getOriginalEnumFullName() {
        return originalEnumFullName;
    }

    public List<String> getEnumConstants() {
        return enumConstants;
    }

    public Element[] getOriginatingElements() {
        return originatingElements;
    }
}
