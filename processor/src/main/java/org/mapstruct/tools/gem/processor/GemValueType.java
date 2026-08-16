/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.processor;

public class GemValueType {

    private final String name;
    private final String fqn;
    private final String packageName;
    private final boolean isEnum;
    private final boolean isArray;
    private final boolean isGem;

    private String gemName;
    private String elementName;

    public GemValueType(GemEnum gemEnum, boolean isArray) {
        this.elementName = gemEnum.getGemName();
        this.name = isArray ? "List<" + elementName + ">" : elementName;
        this.fqn = gemEnum.getGemPackageName() + "." + gemEnum.getGemName();
        this.gemName = gemEnum.getGemName();
        this.packageName = gemEnum.getGemPackageName();
        this.isEnum = true;
        this.isArray = isArray;
        this.isGem = false;
    }

    public GemValueType(GemInfo gemInfo, boolean isArray) {
        this.elementName = gemInfo.getGemName();
        this.name = isArray ? "List<" + elementName + ">" : elementName;
        this.fqn = gemInfo.getGemPackageName() + "." + gemInfo.getGemName();
        this.gemName = gemInfo.getGemName();
        this.packageName = gemInfo.getGemPackageName();
        this.isEnum = false;
        this.isArray = isArray;
        this.isGem = true;
    }

    public GemValueType(Class clazz, boolean isEnum, boolean isArray) {
        this.elementName = clazz.getSimpleName();
        this.name = isArray ? "List<" + clazz.getSimpleName() + ">" : clazz.getSimpleName();
        this.fqn = clazz.getName();
        this.packageName = clazz.getPackage().getName();
        this.isEnum = isEnum;
        this.isArray = isArray;
        this.isGem = false;
    }

    public String getFqn() {
        return fqn;
    }

    public String getPackageName() {
        return packageName;
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

    public String getGemName() {
        return gemName;
    }

}
