/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;

/**
 * Class representing a annotation value
 *
 * @param <T> the type represented by this annotation value
 */
public class GemValue<T> {

    public static <V> GemValue<V> create(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue, Class<V> valueClass) {
        V value = annotationValue == null ? null : valueClass.cast( annotationValue.getValue() );
        V defaultValue = annotationDefaultValue == null ? null : valueClass.cast( annotationDefaultValue.getValue() );
        return new GemValue<>( value, defaultValue, annotationValue );
    }

    public static <V> GemValue<List<V>> createArray(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue, Class<V> valueClass) {
        List<V> value = extractListValues( annotationValue, valueClass, Function.identity() );
        List<V> defaultValue = extractListValues( annotationDefaultValue, valueClass, Function.identity() );

        return new GemValue<>( value, defaultValue, annotationValue );
    }

    public static GemValue<String> createEnum(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue) {
        String value = annotationValue == null ? null :
            ( (VariableElement) annotationValue.getValue() ).getSimpleName().toString();
        String defaultValue = annotationDefaultValue == null ? null :
            ( (VariableElement) annotationDefaultValue.getValue() ).getSimpleName().toString();
        return new GemValue<>( value, defaultValue, annotationValue );
    }

    public static GemValue<List<String>> createEnumArray(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue) {
        List<String> value = extractListValues(
            annotationValue,
            VariableElement.class,
            variableElement -> variableElement.getSimpleName().toString()
        );
        List<String> defaultValue = extractListValues(
            annotationDefaultValue,
            VariableElement.class,
            variableElement -> variableElement.getSimpleName().toString()
        );

        return new GemValue<>( value, defaultValue, annotationValue );
    }

    public static <V> GemValue<V> create(AnnotationValue annotationValue, AnnotationValue annotationDefaultValue,
        Function<AnnotationMirror, V> creator) {
        V value = annotationValue == null ? null : creator.apply( (AnnotationMirror) annotationValue.getValue() );
        V defaultValue = annotationDefaultValue == null ? null :
            creator.apply( (AnnotationMirror) annotationDefaultValue.getValue() );
        return new GemValue<>( value, defaultValue, annotationValue );
    }

    public static <V> GemValue<List<V>> createArray(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue,
        Function<AnnotationMirror, V> creator) {
        List<V> value = extractListValues( annotationValue, AnnotationMirror.class, creator );
        List<V> defaultValue = extractListValues( annotationDefaultValue, AnnotationMirror.class, creator );
        return new GemValue<>( value, defaultValue, annotationValue );
    }

    private static <V, R> List<R> extractListValues(AnnotationValue annotationValue, Class<V> valueClass,
        Function<V, R> mapper) {
        List<R> value;
        if ( annotationValue != null ) {
            Object definedValue = annotationValue.getValue();
            if ( definedValue instanceof List ) {
                value = toStream( (List) definedValue, valueClass ).map( mapper ).collect( Collectors.toList() );
            }
            else {
                value = null;
            }
        }
        else {
            value = null;
        }
        return value;
    }

    private static <T> Stream<T> toStream(List<?> annotationValues, Class<T> clz) {
        return annotationValues.stream()
            .filter( AnnotationValue.class::isInstance )
            .map( AnnotationValue.class::cast )
            .map( AnnotationValue::getValue )
            .filter( clz::isInstance )
            .map( clz::cast );
    }

    private final T value;
    private final T defaultValue;
    private final AnnotationValue annotationValue;

    private GemValue(T value, T defaultValue, AnnotationValue annotationValue) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.annotationValue = annotationValue;
    }

    /**
     * The implied valued, the value set by the user, default value when not defined
     *
     * @return the implied value
     */
    public T get() {
        return value != null ? value : defaultValue;
    }

    /**
     * The value set by the user
     *
     * @return the value, null when not set
     */
    public T getValue() {
        return value;
    }

    /**
     * The default value, as declared in the annotation
     *
     * @return the default value
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * The annotation value, e.g. for printing messages {@link javax.annotation.processing.Messager#printMessage}
     *
     * @return the annotation value (null when not set)
     */
    public AnnotationValue getAnnotationValue() {
        return annotationValue;
    }

    /**
     * @return true a value is set by user
     */
    public boolean hasValue() {
        return value != null;
    }

    /**
     * An annotation set to be valid when set by user or a default value is present.
     *
     * @return true when valid
     */
    public boolean isValid() {
        return value != null || defaultValue != null;
    }

}
