package org.annotationhelper.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GemInfo {

    private final String gemPackageName;
    private final String annotationName;
    private final String annotationFqn;
    private final String gemName;

    private final List<GemValueInfo> gemValueInfos;

    public GemInfo(String gemPackageName, String annotationName, String annotationFqn, List<GemValueInfo> gemValueInfos ) {
        this.gemPackageName = gemPackageName;
        this.gemName = annotationName + "Gem";
        this.annotationName = annotationName;
        this.annotationFqn = annotationFqn;
        this.gemValueInfos = gemValueInfos;
    }

    public String getGemName() {
        return gemName;
    }

    public String getAnnotationName() {
        return annotationName;
    }

    public String getAnnotationFqn() {
        return annotationFqn;
    }

    public String getGemPackageName() {
        return gemPackageName;
    }

    public List<GemValueInfo> getGemValueInfos() {
        return gemValueInfos;
    }

    public Set<String> getImports() {
        return gemValueInfos.stream()
            .map( GemValueInfo::getValueType )
            .filter( this::isNotJavaLang )
            .filter( this::isNotSamePackage )
            .map( GemValueType::getFqn )
            .collect( Collectors.toSet() );
    }

    public boolean isContainingArrays() {
        return gemValueInfos.stream()
            .map( GemValueInfo::getValueType )
            .filter( GemValueType::isArray )
            .findAny()
            .map( Objects::nonNull )
            .orElse( false );
    }

    public Set<GemValueType> getUsedGemValueTypes() {
        return gemValueInfos.stream().map( GemValueInfo::getValueType ).collect( Collectors.toSet() );
    }

    private boolean isNotSamePackage( GemValueType valueType ) {
        return !valueType.getPacakage().equals( gemPackageName );
    }

    private boolean isNotJavaLang( GemValueType valueType ) {
        return !"java.lang".equals( valueType.getPacakage() );
    }
}
