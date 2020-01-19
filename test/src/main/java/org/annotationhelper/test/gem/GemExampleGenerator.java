package org.annotationhelper.test.gem;

import org.annotationhelper.GemDefinition;
import org.annotationhelper.test.Builder;
import org.annotationhelper.test.SomeAnnotation;
import org.annotationhelper.test.SomeAnnotations;

@GemDefinition(value = SomeAnnotation.class)
@GemDefinition(value = SomeAnnotations.class)
@GemDefinition(value = SomeArrayAnnotation.class)
@GemDefinition(value = Builder.class)
public class GemExampleGenerator {
}
