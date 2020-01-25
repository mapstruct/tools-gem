<#-- @ftlvariable name="gemInfo" type="org.mapstruct.annotations.processor.GemInfo" -->
package ${gemInfo.gemPackageName};

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;

import org.mapstruct.annotations.GemValue;

public class GemValueImpl<T> implements GemValue<T> {

    private final T value;
    private final T defaultValue;
    private final AnnotationValue annotationValue;

    public GemValueImpl(T value, T defaultValue, AnnotationValue annotationValue) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.annotationValue = annotationValue;
    }

    @Override
    public T get() {
        return value != null ? value : defaultValue;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public AnnotationValue getAnnotationValue() {
        return annotationValue;
    }

    @Override
    public boolean hasValue() {
        return value != null;
    }

    @Override
    public boolean isValid() {
        return value != null || defaultValue != null;
    }

    public static <V> GemValue<V> create(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue, Class<V> valueClass) {
        V value = annotationValue == null ? null : valueClass.cast( annotationValue.getValue() );
        V defaultValue = annotationDefaultValue == null ? null : valueClass.cast( annotationDefaultValue.getValue() );
        return new GemValueImpl<>(value, defaultValue, annotationValue);
    }

    public static <V> GemValue<List<V>> createArray(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue, Class<V> valueClass) {
        List<V> value = extractListValues( annotationValue, valueClass, Function.identity() );
        List<V> defaultValue = extractListValues( annotationDefaultValue, valueClass, Function.identity() );

        return new GemValueImpl<>(value, defaultValue, annotationValue);
    }

    public static GemValue<String> createEnum(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue) {
        String value = annotationValue == null ? null : ( (VariableElement) annotationValue.getValue() ).getSimpleName().toString();
        String defaultValue = annotationDefaultValue == null ? null : ( (VariableElement) annotationDefaultValue.getValue() ).getSimpleName().toString();
        return new GemValueImpl<>(value, defaultValue, annotationValue);
    }

    public static GemValue<List<String>> createEnumArray(AnnotationValue annotationValue,
        AnnotationValue annotationDefaultValue) {
        List<String> value = extractListValues( annotationValue, VariableElement.class, variableElement -> variableElement.getSimpleName().toString() );
        List<String> defaultValue = extractListValues( annotationDefaultValue, VariableElement.class, variableElement -> variableElement.getSimpleName().toString() );

        return new GemValueImpl<>(value, defaultValue, annotationValue);
    }

    public static <V> GemValue<V> create(AnnotationValue annotationValue, AnnotationValue annotationDefaultValue,
        Function<AnnotationMirror, V> creator) {
        V value = annotationValue == null ? null : creator.apply( (AnnotationMirror) annotationValue.getValue() );
        V defaultValue = annotationDefaultValue == null ? null : creator.apply( (AnnotationMirror) annotationDefaultValue.getValue() );
        return new GemValueImpl<>( value, defaultValue, annotationValue );
    }

    public static <V> GemValue<List<V>> createArray(AnnotationValue annotationValue, AnnotationValue annotationDefaultValue,
        Function<AnnotationMirror, V> creator) {
        List<V> value = extractListValues( annotationValue, AnnotationMirror.class, creator );
        List<V> defaultValue = extractListValues( annotationDefaultValue, AnnotationMirror.class, creator );
        return new GemValueImpl<>( value, defaultValue, annotationValue );
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

    private static <T> Stream<T> toStream( List<?> annotationValues, Class<T> clz ) {
        return annotationValues.stream()
            .filter( AnnotationValue.class::isInstance )
            .map( AnnotationValue.class::cast )
            .map( AnnotationValue::getValue )
            .filter( clz::isInstance )
            .map( clz::cast );
    }
}
