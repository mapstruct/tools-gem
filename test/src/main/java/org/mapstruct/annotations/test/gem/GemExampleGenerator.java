package org.mapstruct.annotations.test.gem;

import org.mapstruct.annotations.GemDefinition;
import org.mapstruct.annotations.test.SomeAnnotation;
import org.mapstruct.annotations.test.SomeAnnotations;
import org.mapstruct.annotations.test.Builder;

@GemDefinition(value = SomeAnnotation.class)
@GemDefinition(value = SomeAnnotations.class)
@GemDefinition(value = SomeArrayAnnotation.class)
@GemDefinition(value = Builder.class)
public class GemExampleGenerator {
}
