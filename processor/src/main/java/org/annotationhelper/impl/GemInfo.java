package org.annotationhelper.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GemInfo {

    private final String BUILDER_NAME = "Builder";
    private final String BUILDER_IMPL_NAME = "BuilderImpl";

    private final String gemPackageName;
    private final String annotationName;
    private final String annotationFqn;
    private final String gemName;
    private final String builderName;
    private final String builderImplName;

    private final List<GemValueInfo> gemValueInfos;

    public GemInfo(String gemPackageName, String annotationName, String annotationFqn, List<GemValueInfo> gemValueInfos ) {
        this.gemPackageName = gemPackageName;
        this.gemName = annotationName + "Gem";
        this.annotationName = annotationName;
        this.annotationFqn = annotationFqn;
        this.gemValueInfos = gemValueInfos;
        this.builderName = BUILDER_NAME + ( BUILDER_NAME.equals( annotationName ) ? "_" : "" );
        this.builderImplName = BUILDER_IMPL_NAME + ( BUILDER_IMPL_NAME.equals( annotationName ) ? "_" : "" );
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

    public String getBuilderName() {
        return builderName;
    }

    public String getBuilderImplName() {
        return builderImplName;
    }

    private boolean isNotSamePackage(GemValueType valueType ) {
        return !valueType.getPacakage().equals( gemPackageName );
    }

    private boolean isNotJavaLang( GemValueType valueType ) {
        return !"java.lang".equals( valueType.getPacakage() );
    }
}
