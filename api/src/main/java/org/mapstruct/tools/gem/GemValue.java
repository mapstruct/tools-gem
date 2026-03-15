/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;

/**
 * Class representing a annotation value
 *
 * @param <T> the type represented by this annotation value
 */
public class GemValue<T> {

    public static <V> GemValue<V> create(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue, Class<V> valueClass) {
        ValueAnnotationValueVisitor<V, V> visitor = new ValueAnnotationValueVisitor<>( Function.identity() );
        V value = visit( annotationValue, visitor, valueClass );
        V defaultValue = visit( annotationDefaultValue, visitor, valueClass );
        return new GemValue<>( value, defaultValue, annotationValue );
    }

    public static <V> GemValue<List<V>> createArray(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue, Class<V> valueClass) {
        ValueAnnotationValueListVisitor<V, V> visitor = new ValueAnnotationValueListVisitor<>( Function.identity() );
        List<V> value = visitList( annotationValue, visitor, valueClass );
        List<V> defaultValue = visitList( annotationDefaultValue, visitor, valueClass );

        return new GemValue<>( value, defaultValue, annotationValue );
    }

    public static GemValue<String> createEnum(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue) {
        ValueAnnotationValueVisitor<VariableElement, String> visitor = new ValueAnnotationValueVisitor<>(
            variableElement -> variableElement.getSimpleName().toString() );
        String value = visit( annotationValue, visitor, VariableElement.class );
        String defaultValue = visit( annotationDefaultValue, visitor, VariableElement.class );
        return new GemValue<>( value, defaultValue, annotationValue );
    }

    public static GemValue<List<String>> createEnumArray(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue) {
        ValueAnnotationValueListVisitor<VariableElement, String> visitor = new ValueAnnotationValueListVisitor<>(
            variableElement -> variableElement.getSimpleName().toString() );
        List<String> value = visitList( annotationValue, visitor, VariableElement.class );
        List<String> defaultValue = visitList( annotationDefaultValue, visitor, VariableElement.class );

        return new GemValue<>( value, defaultValue, annotationValue );
    }

    public static <V> GemValue<V> create(AnnotationValue annotationValue, AnnotationValue annotationDefaultValue,
        Function<AnnotationMirror, V> creator) {
        ValueAnnotationValueVisitor<AnnotationMirror, V> visitor = new ValueAnnotationValueVisitor<>( creator );
        V value = visit( annotationValue, visitor, AnnotationMirror.class );
        V defaultValue = visit( annotationDefaultValue, visitor, AnnotationMirror.class );
        return new GemValue<>( value, defaultValue, annotationValue );
    }

    public static <V> GemValue<List<V>> createArray(AnnotationValue annotationValue,
                                                    AnnotationValue annotationDefaultValue,
                                                    Function<AnnotationMirror, V> creator) {
        ValueAnnotationValueListVisitor<AnnotationMirror, V> visitor = new ValueAnnotationValueListVisitor<>( creator );
        List<V> value = visitList( annotationValue, visitor, AnnotationMirror.class );
        List<V> defaultValue = visitList( annotationDefaultValue, visitor, AnnotationMirror.class );
        return new GemValue<>( value, defaultValue, annotationValue );
    }

    private static <V, R> R visit(AnnotationValue annotationValue,
                                  AnnotationValueVisitor<R, Class<V>> visitor, Class<V> vClass) {
        return annotationValue == null ? null : annotationValue.accept( visitor, vClass );
    }

    private static <V, R> List<R> visitList(AnnotationValue annotationValue,
                                            AnnotationValueVisitor<List<R>, Class<V>> visitor,
                                            Class<V> vClass) {
        return annotationValue == null ? null : annotationValue.accept( visitor, vClass );
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

    private static class ValueAnnotationValueVisitor<V, R> extends SimpleAnnotationValueVisitor8<R, Class<V>> {

        private final Function<V, R> valueMapper;

        private ValueAnnotationValueVisitor(Function<V, R> valueMapper) {
            this.valueMapper = valueMapper;
        }

        @Override
        protected R defaultAction(Object o, Class<V> vClass) {
            if ( o == null ) {
                return null;
            }
            else if ( vClass.isInstance( o ) ) {
                return valueMapper.apply( vClass.cast( o ) );
            }
            else {
                return null;
            }
        }
    }

    private static class ValueAnnotationValueListVisitor<V, R>
        extends SimpleAnnotationValueVisitor8<List<R>, Class<V>> {

        private final ValueAnnotationValueVisitor<V, R> arrayVisitor;

        private ValueAnnotationValueListVisitor(Function<V, R> valueMapper) {
            arrayVisitor = new ValueAnnotationValueVisitor<>( valueMapper );
        }

        @Override
        public List<R> visitArray(List<? extends AnnotationValue> vals, Class<V> vClass) {
            boolean valid = true;
            List<R> values = new ArrayList<>( vals.size() );
            for ( AnnotationValue val : vals ) {
                R value = val.accept( arrayVisitor, vClass );
                values.add( value );
            }

            return values;
        }
    }

}
