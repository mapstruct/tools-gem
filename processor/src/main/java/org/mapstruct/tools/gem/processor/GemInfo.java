/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.processor;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;

public class GemInfo {

    private static final String BUILDER_NAME = "Builder";
    private static final String BUILDER_IMPL_NAME = "BuilderImpl";

    private final String gemPackageName;
    private final String annotationName;
    private final String annotationFqn;
    private final String gemName;
    private final String builderName;
    private final String builderImplName;

    private final List<GemValueInfo> gemValueInfos;
    private final Collection<Element> originatingElements;

    public GemInfo(String gemPackageName, String annotationName, String annotationFqn,
        List<GemValueInfo> gemValueInfos, Collection<Element> originatingElements) {
        this.gemPackageName = gemPackageName;
        this.gemName = annotationName + "Gem";
        this.annotationName = annotationName;
        this.annotationFqn = annotationFqn;
        this.gemValueInfos = gemValueInfos;
        this.builderName = BUILDER_NAME + ( BUILDER_NAME.equals( annotationName ) ? "_" : "" );
        this.builderImplName = BUILDER_IMPL_NAME + ( BUILDER_IMPL_NAME.equals( annotationName ) ? "_" : "" );
        this.originatingElements = originatingElements;
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

    public String getBuilderName() {
        return builderName;
    }

    public String getBuilderImplName() {
        return builderImplName;
    }

    public Collection<Element> getOriginatingElements() {
        return originatingElements;
    }

    private boolean isNotSamePackage(GemValueType valueType ) {
        return !valueType.getPacakage().equals( gemPackageName );
    }

    private boolean isNotJavaLang( GemValueType valueType ) {
        return !"java.lang".equals( valueType.getPacakage() );
    }
}
