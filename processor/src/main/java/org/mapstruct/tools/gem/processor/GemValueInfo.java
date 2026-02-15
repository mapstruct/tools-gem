/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.processor;

import javax.lang.model.type.TypeMirror;

public class GemValueInfo {

    private final String name;
    private final TypeMirror typeMirror;
    private GemValueType valueType;

    public GemValueInfo(String name, TypeMirror typeMirror) {
        this.name = name;
        this.typeMirror = typeMirror;
    }

    public TypeMirror getTypeMirror() {
        return typeMirror;
    }

    public String getName() {
        return name;
    }

    public GemValueType getValueType() {
        return valueType;
    }

    public void setValueType(GemValueType valueType) {
        this.valueType = valueType;
    }

}
