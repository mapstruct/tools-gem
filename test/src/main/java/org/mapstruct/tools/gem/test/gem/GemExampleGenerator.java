package org.mapstruct.tools.gem.test.gem;

import org.mapstruct.tools.gem.GemDefinition;
import org.mapstruct.tools.gem.test.SomeAnnotation;
import org.mapstruct.tools.gem.test.SomeAnnotations;
import org.mapstruct.tools.gem.test.Builder;

@GemDefinition(value = SomeAnnotation.class)
@GemDefinition(value = SomeAnnotations.class)
@GemDefinition(value = SomeArrayAnnotation.class)
@GemDefinition(value = Builder.class)
public class GemExampleGenerator {
}
