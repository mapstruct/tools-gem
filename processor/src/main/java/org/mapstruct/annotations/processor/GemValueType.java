package org.mapstruct.annotations.processor;

import java.util.Objects;

public class GemValueType {

    private final String gemValueClass;
    private final String name;
    private final String fqn;
    private final String pacakage;
    private final boolean isEnum;
    private final boolean isArray;
    private final boolean isGem;

    private String gemName;
    private String elementName;

    public GemValueType(GemInfo gemInfo, boolean isArray) {
        this.elementName = gemInfo.getGemName() + "." + gemInfo.getAnnotationName();
        this.name = isArray? "List<" + elementName + ">" : elementName;
        this.fqn = gemInfo.getGemPackageName() + "." + gemInfo.getGemName();
        this.gemName = gemInfo.getGemName();
        this.pacakage = gemInfo.getGemPackageName();
        this.isEnum = false;
        this.isArray = isArray;
        this.isGem = true;
        this.gemValueClass = "Gem"+ gemInfo.getAnnotationName() + ( isArray ? "Array" : "" ) + "Value" ;
    }

    public GemValueType(Class clazz, boolean isEnum, boolean isArray) {
        this.elementName = clazz.getSimpleName();
        this.name = isArray? "List<" + clazz.getSimpleName() + ">" : clazz.getSimpleName();
        this.fqn = clazz.getName();
        this.pacakage = clazz.getPackage().getName();
        this.isEnum = isEnum;
        this.isArray = isArray;
        this.isGem = false;
        this.gemValueClass = isEnum? "GemEnumValue" : "Gem"+ clazz.getSimpleName() + ( isArray ? "Array" : "" ) + "Value" ;
    }

    public String getFqn() {
        return fqn;
    }

    public String getPacakage() {
        return pacakage;
    }

    public String getName() {
        return name;
    }

    public String getElementName() {
        return elementName;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public boolean isArray() {
        return isArray;
    }

    public boolean isGem() {
        return isGem;
    }

    public String getGemValueClass() {
        return gemValueClass;
    }

    public String getGemName() {
        return gemName;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        GemValueType that = (GemValueType) o;
        return Objects.equals( gemValueClass, that.gemValueClass );
    }

    @Override
    public int hashCode() {
        return Objects.hash( gemValueClass );
    }
}
