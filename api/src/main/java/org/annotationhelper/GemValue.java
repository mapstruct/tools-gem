package org.annotationhelper;

import javax.lang.model.element.AnnotationValue;
import javax.tools.Diagnostic;

/**
 * Class representing a annotation value
 *
 * @param <T> the type represented by this annotation value
 */
public interface GemValue<T> {

    /**
     * @return the value, null when not set
     */
    T getValue();

    /**
     * @return the default value for the annotation value
     */
    T getDefaultValue();

    /**
     * The annotation value, e.g. for printing messages {@link javax.annotation.processing.Messager#printMessage}
     *
     * @return the annotation value (null when not set)
     */
    AnnotationValue getAnnotationValue();

    /**
     * @return true a value is set
     */
    boolean hasValue();

    /**
     * An annotation is valid when set or when not set and default value is present.
     *
     * @return true when valid
     */
    boolean isValid();
}
