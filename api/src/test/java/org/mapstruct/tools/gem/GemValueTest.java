/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GemValueTest {

    static class SimpleAnnotationValue implements AnnotationValue {

        private final int value;

        SimpleAnnotationValue(int value) {
            this.value = value;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public <R, P> R accept(AnnotationValueVisitor<R, P> v, P p) {
            return v.visitInt( value, p );
        }
    }

    @Nested
    class CreateSimpleValueTest {

        @Test
        void createSimpleValue() {
            SimpleAnnotationValue annotationValue = new SimpleAnnotationValue(1);
            GemValue<Integer> gemValue = GemValue.create( annotationValue, new SimpleAnnotationValue(2),
                    Integer.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( 1 );
            assertThat( gemValue.getDefaultValue() ).isEqualTo( 2 );
            assertThat( gemValue.get() ).as( "get should return value" ).isEqualTo( 1 );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( annotationValue );
            assertThat( gemValue.getValueOrElseGet( () -> 3 ) )
                    .as( "getValueOrElseGet should return value" ).isEqualTo( 1 );
        }

        @Test
        void createSimpleValueWithoutAnnotationValue() {
            GemValue<Integer> gemValue = GemValue.create( null, new SimpleAnnotationValue(2),
                    Integer.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isEqualTo( 2 );
            assertThat( gemValue.get() ).as( "get should return defaultValue" ).isEqualTo( 2 );
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( () -> 3 ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( 3 );
        }

        @Test
        void createSimpleValueWithoutAnnotationDefaultValue() {
            SimpleAnnotationValue annotationValue = new SimpleAnnotationValue(1);
            GemValue<Integer> gemValue = GemValue.create( annotationValue, null, Integer.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( 1 );
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return value" ).isEqualTo( 1 );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( annotationValue );
            assertThat( gemValue.getValueOrElseGet( () -> 3 ) )
                    .as( "getValueOrElseGet should return value" ).isEqualTo( 1 );
        }

        @Test
        void createSimpleValueInvalid() {
            GemValue<Integer> gemValue = GemValue.create( null, null, Integer.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isFalse();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return null" ).isNull();
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( () -> 3 ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( 3 );
        }
    }

    static class SimpleArrayAnnotationValue implements AnnotationValue {

        private final List<SimpleAnnotationValue> value;

        SimpleArrayAnnotationValue(int... intValues) {
            this.value = new ArrayList<>( intValues.length );
            for ( int v : intValues ) {
                value.add( new SimpleAnnotationValue( v ) );
            }
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public <R, P> R accept(AnnotationValueVisitor<R, P> v, P p) {
            return v.visitArray( value, p );
        }
    }

    @Nested
    class CreateArrayTest {

        @Test
        void createArrayValue() {
            SimpleArrayAnnotationValue annotationValue = new SimpleArrayAnnotationValue(1);
            GemValue<List<Integer>> gemValue = GemValue.createArray( annotationValue,
                    new SimpleArrayAnnotationValue(2), Integer.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( Collections.singletonList( 1 ) );
            assertThat( gemValue.getDefaultValue() ).isEqualTo( Collections.singletonList( 2 ) );
            assertThat( gemValue.get() ).as( "get should return value" ).isEqualTo(
                    Collections.singletonList( 1 ) );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( annotationValue );
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return value" )
                    .isEqualTo( Collections.singletonList( 1 ) );
        }

        @Test
        void createArrayValueWithoutAnnotationValue() {
            GemValue<List<Integer>> gemValue = GemValue.createArray( null,
                    new SimpleArrayAnnotationValue(2), Integer.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isEqualTo( Collections.singletonList( 2 ) );
            assertThat( gemValue.get() ).as( "get should return defaultValue" )
                    .isEqualTo( Collections.singletonList( 2 ) );
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( Collections.emptyList() );
        }

        @Test
        void createArrayValueWithoutAnnotationDefaultValue() {
            SimpleArrayAnnotationValue annotationValue = new SimpleArrayAnnotationValue(1);
            GemValue<List<Integer>> gemValue = GemValue.createArray( annotationValue, null, Integer.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( Collections.singletonList( 1 ) );
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return value" )
                    .isEqualTo( Collections.singletonList( 1 ) );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( annotationValue );
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return value" )
                    .isEqualTo( Collections.singletonList( 1 ) );
        }

        @Test
        void createArrayValueInvalid() {
            GemValue<List<Integer>> gemValue = GemValue.createArray( null, null, Integer.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isFalse();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return null" ).isNull();
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( () -> Collections.singletonList( 3 ) ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( Collections.singletonList( 3 ) );
        }
    }
}
