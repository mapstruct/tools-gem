package org.annotationhelper;

import javax.lang.model.element.AnnotationValue;

/**
 * Class representing a annotation value
 *
 * @param <T> the type represented by this annotation value
 */
public interface GemValue<T> {

    /**
     * The implied valued, the value set by the user, default value when not defined
     *
     * @return the implied value
     */
    T get();

    /**
     * The value set by the user
     *
     * @return the value, null when not set
     */
    T getValue();

    /**
     * The default value, as declared in the annotation
     *
     * @return the default value
     */
    T getDefaultValue();

    /**
     * The annotation value, e.g. for printing messages {@link javax.annotation.processing.Messager#printMessage}
     *
     * @return the annotation value (null when not set)
     */
    AnnotationValue getAnnotationValue();

    /**
     *
     * @return true a value is set by user
     */
     boolean hasValue();

    /**
     * An annotation set to be valid when set by user or a default value is present.
     *
     * @return true when valid
     */
    boolean isValid();
}
