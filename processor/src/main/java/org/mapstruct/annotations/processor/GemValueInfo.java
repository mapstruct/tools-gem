package org.mapstruct.annotations.processor;

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
