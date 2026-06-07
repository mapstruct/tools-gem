/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

    static class GenericArrayAnnotationValue implements AnnotationValue {

        private final List<AnnotationValue> value;

        GenericArrayAnnotationValue(AnnotationValue... values) {
            this.value = java.util.Arrays.asList( values );
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

    @Nested
    class CreateEnumAsStringValueTest {

        @Test
        void createSimpleValue() {
            EnumAnnotationValue annotationValue = new EnumAnnotationValue( TestEnum.A );
            GemValue<String> gemValue = GemValue.createEnum( annotationValue, new EnumAnnotationValue( TestEnum.B ) );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( "A" );
            assertThat( gemValue.getDefaultValue() ).isEqualTo( "B" );
            assertThat( gemValue.get() ).as( "get should return value" ).isEqualTo( "A" );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( annotationValue );
            assertThat( gemValue.getValueOrElseGet( () -> "C" ) )
                    .as( "getValueOrElseGet should return value" ).isEqualTo( "A" );
        }

        @Test
        void createSimpleValueWithoutAnnotationValue() {
            GemValue<String> gemValue = GemValue.createEnum( null, new EnumAnnotationValue( TestEnum.B ) );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isEqualTo( "B" );
            assertThat( gemValue.get() ).as( "get should return defaultValue" ).isEqualTo( "B" );
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( () -> "C" ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( "C" );
        }

        @Test
        void createSimpleValueWithoutAnnotationDefaultValue() {
            EnumAnnotationValue annotationValue = new EnumAnnotationValue( TestEnum.A );
            GemValue<String> gemValue = GemValue.createEnum( annotationValue, null );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( "A" );
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return value" ).isEqualTo( "A" );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( annotationValue );
            assertThat( gemValue.getValueOrElseGet( () -> "C" ) )
                    .as( "getValueOrElseGet should return value" ).isEqualTo( "A" );
        }

        @Test
        void createSimpleValueInvalid() {
            GemValue<String> gemValue = GemValue.createEnum( null, null );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isFalse();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return null" ).isNull();
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( () -> "C" ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( "C" );
        }
    }

    @Nested
    class CreateEnumArrayTest {

        @Test
        void createEnumArrayValue() {
            EnumAnnotationValue annotationValue = new EnumAnnotationValue( TestEnum.A );
            EnumAnnotationValue defaultAnnotationValue = new EnumAnnotationValue( TestEnum.B );

            GenericArrayAnnotationValue arrayValue = new GenericArrayAnnotationValue( annotationValue );
            GenericArrayAnnotationValue defaultArrayValue = new GenericArrayAnnotationValue( defaultAnnotationValue );

            GemValue<List<String>> gemValue = GemValue.createEnumArray( arrayValue, defaultArrayValue );

            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( Collections.singletonList( "A" ) );
            assertThat( gemValue.getDefaultValue() ).isEqualTo( Collections.singletonList( "B" ) );
            assertThat( gemValue.get() ).as( "get should return value" ).isEqualTo( Collections.singletonList( "A" ) );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( arrayValue );
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return value" ).isEqualTo( Collections.singletonList( "A" ) );
        }

        @Test
        void createEnumArrayValueWithoutAnnotationValue() {
            EnumAnnotationValue defaultAnnotationValue = new EnumAnnotationValue( TestEnum.B );
            GenericArrayAnnotationValue defaultArrayValue = new GenericArrayAnnotationValue( defaultAnnotationValue );

            GemValue<List<String>> gemValue = GemValue.createEnumArray( null, defaultArrayValue );

            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isEqualTo( Collections.singletonList( "B" ) );
            assertThat( gemValue.get() ).as( "get should return defaultValue" )
                    .isEqualTo( Collections.singletonList( "B" ) );
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( Collections.emptyList() );
        }

        @Test
        void createEnumArrayValueWithoutAnnotationDefaultValue() {
            EnumAnnotationValue annotationValue = new EnumAnnotationValue( TestEnum.A );
            GenericArrayAnnotationValue arrayValue = new GenericArrayAnnotationValue( annotationValue );

            GemValue<List<String>> gemValue = GemValue.createEnumArray( arrayValue, null );

            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( Collections.singletonList( "A" ) );
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return value" ).isEqualTo( Collections.singletonList( "A" ) );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( arrayValue );
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return value" ).isEqualTo( Collections.singletonList( "A" ) );
        }

        @Test
        void createEnumArrayValueInvalid() {
            GemValue<List<String>> gemValue = GemValue.createEnumArray( null, null );

            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isFalse();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return null" ).isNull();
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( Collections.emptyList() );
        }
    }

    @Nested
    class CreateEnumAsEnumTest {

        @Test
        void createSimpleValue() {
            EnumAnnotationValue annotationValue = new EnumAnnotationValue( TestEnum.A );
            GemValue<TestEnum> gemValue = GemValue.createEnum( annotationValue, new EnumAnnotationValue( TestEnum.B ),
                    TestEnum.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( TestEnum.A );
            assertThat( gemValue.getDefaultValue() ).isEqualTo( TestEnum.B );
            assertThat( gemValue.get() ).as( "get should return value" ).isEqualTo( TestEnum.A );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( annotationValue );
            assertThat( gemValue.getValueOrElseGet( () -> TestEnum.C ) )
                    .as( "getValueOrElseGet should return value" ).isEqualTo( TestEnum.A );
        }

        @Test
        void createSimpleValueWithoutAnnotationValue() {
            GemValue<TestEnum> gemValue = GemValue.createEnum( null, new EnumAnnotationValue( TestEnum.B ),
                    TestEnum.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isEqualTo( TestEnum.B );
            assertThat( gemValue.get() ).as( "get should return defaultValue" ).isEqualTo( TestEnum.B );
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( () -> TestEnum.C ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( TestEnum.C );
        }

        @Test
        void createSimpleValueWithoutAnnotationDefaultValue() {
            EnumAnnotationValue annotationValue = new EnumAnnotationValue( TestEnum.A );
            GemValue<TestEnum> gemValue = GemValue.createEnum( annotationValue, null, TestEnum.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo(  TestEnum.A  );
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return value" ).isEqualTo(  TestEnum.A  );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( annotationValue );
            assertThat( gemValue.getValueOrElseGet( () ->  TestEnum.C ) )
                    .as( "getValueOrElseGet should return value" ).isEqualTo(  TestEnum.A );
        }

        @Test
        void createSimpleValueInvalid() {
            GemValue<TestEnum> gemValue = GemValue.createEnum( null, null, TestEnum.class );
            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isFalse();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return null" ).isNull();
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( () -> TestEnum.C ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( TestEnum.C );
        }
    }

    @Nested
    class CreateEnumArrayAsEnumTest {

        @Test
        void createEnumArrayValue() {
            EnumAnnotationValue annotationValue = new EnumAnnotationValue( TestEnum.A );
            EnumAnnotationValue defaultAnnotationValue = new EnumAnnotationValue( TestEnum.B );

            GenericArrayAnnotationValue arrayValue = new GenericArrayAnnotationValue( annotationValue );
            GenericArrayAnnotationValue defaultArrayValue = new GenericArrayAnnotationValue( defaultAnnotationValue );

            GemValue<List<TestEnum>> gemValue = GemValue.createEnumArray( arrayValue, defaultArrayValue,
                    TestEnum.class );

            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( Collections.singletonList( TestEnum.A ) );
            assertThat( gemValue.getDefaultValue() ).isEqualTo( Collections.singletonList( TestEnum.B ) );
            assertThat( gemValue.get() ).as( "get should return value" )
                    .isEqualTo( Collections.singletonList( TestEnum.A ) );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( arrayValue );
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return value" ).isEqualTo( Collections.singletonList( TestEnum.A ) );
        }

        @Test
        void createEnumArrayValueWithoutAnnotationValue() {
            EnumAnnotationValue defaultAnnotationValue = new EnumAnnotationValue( TestEnum.B );
            GenericArrayAnnotationValue defaultArrayValue = new GenericArrayAnnotationValue( defaultAnnotationValue );

            GemValue<List<TestEnum>> gemValue = GemValue.createEnumArray( null, defaultArrayValue, TestEnum.class );

            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isEqualTo( Collections.singletonList( TestEnum.B ) );
            assertThat( gemValue.get() ).as( "get should return defaultValue" )
                    .isEqualTo( Collections.singletonList( TestEnum.B ) );
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( Collections.emptyList() );
        }

        @Test
        void createEnumArrayValueWithoutAnnotationDefaultValue() {
            EnumAnnotationValue annotationValue = new EnumAnnotationValue( TestEnum.A );
            GenericArrayAnnotationValue arrayValue = new GenericArrayAnnotationValue( annotationValue );

            GemValue<List<TestEnum>> gemValue = GemValue.createEnumArray( arrayValue, null, TestEnum.class );

            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isTrue();
            assertThat( gemValue.hasValue() ).isTrue();
            assertThat( gemValue.getValue() ).isEqualTo( Collections.singletonList( TestEnum.A ) );
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return value" )
                    .isEqualTo( Collections.singletonList( TestEnum.A ) );
            assertThat( gemValue.getAnnotationValue() ).isEqualTo( arrayValue );
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return value" ).isEqualTo( Collections.singletonList( TestEnum.A ) );
        }

        @Test
        void createEnumArrayValueInvalid() {
            GemValue<List<TestEnum>> gemValue = GemValue.createEnumArray( null, null, TestEnum.class );

            assertThat( gemValue ).isNotNull();
            assertThat( gemValue.isValid() ).isFalse();
            assertThat( gemValue.hasValue() ).isFalse();
            assertThat( gemValue.getValue() ).isNull();
            assertThat( gemValue.getDefaultValue() ).isNull();
            assertThat( gemValue.get() ).as( "get should return null" ).isNull();
            assertThat( gemValue.getAnnotationValue() ).isNull();
            assertThat( gemValue.getValueOrElseGet( Collections::emptyList ) )
                    .as( "getValueOrElseGet should return other" ).isEqualTo( Collections.emptyList() );
        }
    }


    enum TestEnum {
        A, B, C
    }

    static class EnumAnnotationValue implements AnnotationValue {

        private final String value;

        EnumAnnotationValue(TestEnum value) {
            this.value = value.toString();
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public <R, P> R accept(AnnotationValueVisitor<R, P> v, P p) {
            VariableElement variableElement = new VariableElement() {
                @Override
                public TypeMirror asType() {
                    return null;
                }

                @Override
                public Object getConstantValue() {
                    return null;
                }

                @Override
                public Name getSimpleName() {
                    return new Name() {
                        @Override
                        public boolean contentEquals(CharSequence cs) {
                            return cs.toString().equals( value );
                        }

                        @Override
                        public int length() {
                            return  value.length();
                        }

                        @Override
                        public char charAt(int index) {
                            return value.charAt( index );
                        }

                        @Override
                        public CharSequence subSequence(int start, int end) {
                            return value.subSequence( start, end );
                        }

                        @Override
                        public String toString() {
                            return value;
                        }
                    };
                }

                @Override
                public Element getEnclosingElement() {
                    return null;
                }

                @Override
                public ElementKind getKind() {
                    return null;
                }

                @Override
                public Set<Modifier> getModifiers() {
                    return new HashSet<>();
                }

                @Override
                public List<? extends Element> getEnclosedElements() {
                    return Collections.emptyList();
                }

                @Override
                public List<? extends AnnotationMirror> getAnnotationMirrors() {
                    return Collections.emptyList();
                }

                @Override
                public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
                    return null;
                }

                @Override
                public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
                    return null;
                }

                @Override
                public <S, T> S accept(ElementVisitor<S, T> v, T p) {
                    return null;
                }
            };
            return v.visitEnumConstant( variableElement, p );
        }
    }
}
