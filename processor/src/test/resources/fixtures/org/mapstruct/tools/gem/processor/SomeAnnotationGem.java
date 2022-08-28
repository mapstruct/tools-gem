package org.mapstruct.tools.gem.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.mapstruct.tools.gem.Gem;
import org.mapstruct.tools.gem.GemValue;

public class SomeAnnotationGem implements Gem {
    private final GemValue<TypeMirror> myClassWithDefault;

    private final GemValue<Boolean> myBooleanWithDefault;

    private final GemValue<Character> myCharWithDefault;

    private final GemValue<Byte> myByteWithDefault;

    private final GemValue<Short> mShortWithDefault;

    private final GemValue<Integer> myIntWithDefault;

    private final GemValue<Integer> myLongWithDefault;

    private final GemValue<Float> myFloatWithDefault;

    private final GemValue<Double> myDoubleWithDefault;

    private final GemValue<String> myStringWithDefault;

    private final GemValue<String> myEnumWithDefault;

    private final GemValue<TypeMirror> myClass;

    private final GemValue<Boolean> myBoolean;

    private final GemValue<Character> myChar;

    private final GemValue<Byte> myByte;

    private final GemValue<Short> myShort;

    private final GemValue<Integer> myInt;

    private final GemValue<Integer> myLong;

    private final GemValue<Float> myFloat;

    private final GemValue<Double> myDouble;

    private final GemValue<String> myString;

    private final GemValue<String> myEnum;

    private final boolean isValid;

    private final AnnotationMirror mirror;

    private SomeAnnotationGem(BuilderImpl builder) {
        this.myClassWithDefault = builder.myClassWithDefault;
        this.myBooleanWithDefault = builder.myBooleanWithDefault;
        this.myCharWithDefault = builder.myCharWithDefault;
        this.myByteWithDefault = builder.myByteWithDefault;
        this.mShortWithDefault = builder.mShortWithDefault;
        this.myIntWithDefault = builder.myIntWithDefault;
        this.myLongWithDefault = builder.myLongWithDefault;
        this.myFloatWithDefault = builder.myFloatWithDefault;
        this.myDoubleWithDefault = builder.myDoubleWithDefault;
        this.myStringWithDefault = builder.myStringWithDefault;
        this.myEnumWithDefault = builder.myEnumWithDefault;
        this.myClass = builder.myClass;
        this.myBoolean = builder.myBoolean;
        this.myChar = builder.myChar;
        this.myByte = builder.myByte;
        this.myShort = builder.myShort;
        this.myInt = builder.myInt;
        this.myLong = builder.myLong;
        this.myFloat = builder.myFloat;
        this.myDouble = builder.myDouble;
        this.myString = builder.myString;
        this.myEnum = builder.myEnum;
        this.mirror = builder.mirror;

        boolean isValid = this.myClassWithDefault != null ? this.myClassWithDefault.isValid() : false;
        isValid = isValid && this.myBooleanWithDefault != null ? this.myBooleanWithDefault.isValid() : false;
        isValid = isValid && this.myCharWithDefault != null ? this.myCharWithDefault.isValid() : false;
        isValid = isValid && this.myByteWithDefault != null ? this.myByteWithDefault.isValid() : false;
        isValid = isValid && this.mShortWithDefault != null ? this.mShortWithDefault.isValid() : false;
        isValid = isValid && this.myIntWithDefault != null ? this.myIntWithDefault.isValid() : false;
        isValid = isValid && this.myLongWithDefault != null ? this.myLongWithDefault.isValid() : false;
        isValid = isValid && this.myFloatWithDefault != null ? this.myFloatWithDefault.isValid() : false;
        isValid = isValid && this.myDoubleWithDefault != null ? this.myDoubleWithDefault.isValid() : false;
        isValid = isValid && this.myStringWithDefault != null ? this.myStringWithDefault.isValid() : false;
        isValid = isValid && this.myEnumWithDefault != null ? this.myEnumWithDefault.isValid() : false;
        isValid = isValid && this.myClass != null ? this.myClass.isValid() : false;
        isValid = isValid && this.myBoolean != null ? this.myBoolean.isValid() : false;
        isValid = isValid && this.myChar != null ? this.myChar.isValid() : false;
        isValid = isValid && this.myByte != null ? this.myByte.isValid() : false;
        isValid = isValid && this.myShort != null ? this.myShort.isValid() : false;
        isValid = isValid && this.myInt != null ? this.myInt.isValid() : false;
        isValid = isValid && this.myLong != null ? this.myLong.isValid() : false;
        isValid = isValid && this.myFloat != null ? this.myFloat.isValid() : false;
        isValid = isValid && this.myDouble != null ? this.myDouble.isValid() : false;
        isValid = isValid && this.myString != null ? this.myString.isValid() : false;
        isValid = isValid && this.myEnum != null ? this.myEnum.isValid() : false;
        this.isValid = isValid;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myClassWithDefault}
     */
    public GemValue<TypeMirror> myClassWithDefault() {
        return this.myClassWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myBooleanWithDefault}
     */
    public GemValue<Boolean> myBooleanWithDefault() {
        return this.myBooleanWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myCharWithDefault}
     */
    public GemValue<Character> myCharWithDefault() {
        return this.myCharWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myByteWithDefault}
     */
    public GemValue<Byte> myByteWithDefault() {
        return this.myByteWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#mShortWithDefault}
     */
    public GemValue<Short> mShortWithDefault() {
        return this.mShortWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myIntWithDefault}
     */
    public GemValue<Integer> myIntWithDefault() {
        return this.myIntWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myLongWithDefault}
     */
    public GemValue<Integer> myLongWithDefault() {
        return this.myLongWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myFloatWithDefault}
     */
    public GemValue<Float> myFloatWithDefault() {
        return this.myFloatWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myDoubleWithDefault}
     */
    public GemValue<Double> myDoubleWithDefault() {
        return this.myDoubleWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myStringWithDefault}
     */
    public GemValue<String> myStringWithDefault() {
        return this.myStringWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myEnumWithDefault}
     */
    public GemValue<String> myEnumWithDefault() {
        return this.myEnumWithDefault;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myClass}
     */
    public GemValue<TypeMirror> myClass() {
        return this.myClass;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myBoolean}
     */
    public GemValue<Boolean> myBoolean() {
        return this.myBoolean;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myChar}
     */
    public GemValue<Character> myChar() {
        return this.myChar;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myByte}
     */
    public GemValue<Byte> myByte() {
        return this.myByte;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myShort}
     */
    public GemValue<Short> myShort() {
        return this.myShort;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myInt}
     */
    public GemValue<Integer> myInt() {
        return this.myInt;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myLong}
     */
    public GemValue<Integer> myLong() {
        return this.myLong;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myFloat}
     */
    public GemValue<Float> myFloat() {
        return this.myFloat;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myDouble}
     */
    public GemValue<Double> myDouble() {
        return this.myDouble;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myString}
     */
    public GemValue<String> myString() {
        return this.myString;
    }

    /**
     * accessor
     *
     * @return the {@link GemValue} for {@link SomeAnnotationGem#myEnum}
     */
    public GemValue<String> myEnum() {
        return this.myEnum;
    }

    @Override
    public AnnotationMirror mirror() {
        return this.mirror;
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }

    public static SomeAnnotationGem instanceOn(Element element) {
        return build( element, new BuilderImpl() );
    }

    public static SomeAnnotationGem instanceOn(AnnotationMirror mirror) {
        return build( mirror, new BuilderImpl() );
    }

    public static <T> T build(Element element, Builder<T> builder) {
        AnnotationMirror mirror = element.getAnnotationMirrors().stream()
                .filter( a -> "org.mapstruct.tools.gem.test.SomeAnnotation".contentEquals( ( ( TypeElement ) a.getAnnotationType().asElement() ).getQualifiedName() ) )
                .findAny()
                .orElse( null );
        return build( mirror, builder );
    }

    public static <T> T build(AnnotationMirror mirror, Builder<T> builder) {
        // return fast
        if ( mirror == null || builder == null ) {
            return null;
        }

        // fetch defaults from all defined values in the annotation type
        List<ExecutableElement> enclosed = ElementFilter.methodsIn( mirror.getAnnotationType().asElement().getEnclosedElements() );
        Map<String, AnnotationValue> defaultValues = new HashMap<>( enclosed.size() );
        enclosed.forEach( e -> defaultValues.put( e.getSimpleName().toString(), e.getDefaultValue() ) );

        // fetch all explicitly set annotation values in the annotation instance
        Map<String, AnnotationValue> values = new HashMap<>( enclosed.size() );
        mirror.getElementValues().entrySet().forEach( e -> values.put( e.getKey().getSimpleName().toString(), e.getValue() ) );

        // iterate and populate builder
        for ( String methodName : defaultValues.keySet() ) {
            if ( "myClassWithDefault".equals( methodName ) ) {
                builder.setMyClassWithDefault( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), TypeMirror.class ) );
            }
            else if ( "myBooleanWithDefault".equals( methodName ) ) {
                builder.setMyBooleanWithDefault( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Boolean.class ) );
            }
            else if ( "myCharWithDefault".equals( methodName ) ) {
                builder.setMyCharWithDefault( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Character.class ) );
            }
            else if ( "myByteWithDefault".equals( methodName ) ) {
                builder.setMyByteWithDefault( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Byte.class ) );
            }
            else if ( "mShortWithDefault".equals( methodName ) ) {
                builder.setMShortWithDefault( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Short.class ) );
            }
            else if ( "myIntWithDefault".equals( methodName ) ) {
                builder.setMyIntWithDefault( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Integer.class ) );
            }
            else if ( "myLongWithDefault".equals( methodName ) ) {
                builder.setMyLongWithDefault( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Integer.class ) );
            }
            else if ( "myFloatWithDefault".equals( methodName ) ) {
                builder.setMyFloatWithDefault( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Float.class ) );
            }
            else if ( "myDoubleWithDefault".equals( methodName ) ) {
                builder.setMyDoubleWithDefault( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Double.class ) );
            }
            else if ( "myStringWithDefault".equals( methodName ) ) {
                builder.setMyStringWithDefault( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), String.class ) );
            }
            else if ( "myEnumWithDefault".equals( methodName ) ) {
                builder.setMyEnumWithDefault( GemValue.createEnum( values.get( methodName ), defaultValues.get( methodName ) ) );
            }
            else if ( "myClass".equals( methodName ) ) {
                builder.setMyClass( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), TypeMirror.class ) );
            }
            else if ( "myBoolean".equals( methodName ) ) {
                builder.setMyBoolean( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Boolean.class ) );
            }
            else if ( "myChar".equals( methodName ) ) {
                builder.setMyChar( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Character.class ) );
            }
            else if ( "myByte".equals( methodName ) ) {
                builder.setMyByte( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Byte.class ) );
            }
            else if ( "myShort".equals( methodName ) ) {
                builder.setMyShort( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Short.class ) );
            }
            else if ( "myInt".equals( methodName ) ) {
                builder.setMyInt( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Integer.class ) );
            }
            else if ( "myLong".equals( methodName ) ) {
                builder.setMyLong( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Integer.class ) );
            }
            else if ( "myFloat".equals( methodName ) ) {
                builder.setMyFloat( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Float.class ) );
            }
            else if ( "myDouble".equals( methodName ) ) {
                builder.setMyDouble( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), Double.class ) );
            }
            else if ( "myString".equals( methodName ) ) {
                builder.setMyString( GemValue.create( values.get( methodName ), defaultValues.get( methodName ), String.class ) );
            }
            else if ( "myEnum".equals( methodName ) ) {
                builder.setMyEnum( GemValue.createEnum( values.get( methodName ), defaultValues.get( methodName ) ) );
            }
        }
        builder.setMirror( mirror );
        return builder.build();
    }

    /**
     * A builder that can be implemented by the user to define custom logic
     * e.g. in the build method, prior to creating the annotation gem.
     */
    public interface Builder<T> {
        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myClassWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyClassWithDefault(GemValue<TypeMirror> myClassWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myBooleanWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyBooleanWithDefault(GemValue<Boolean> myBooleanWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myCharWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyCharWithDefault(GemValue<Character> myCharWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myByteWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyByteWithDefault(GemValue<Byte> myByteWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#mShortWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMShortWithDefault(GemValue<Short> mShortWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myIntWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyIntWithDefault(GemValue<Integer> myIntWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myLongWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyLongWithDefault(GemValue<Integer> myLongWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myFloatWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyFloatWithDefault(GemValue<Float> myFloatWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myDoubleWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyDoubleWithDefault(GemValue<Double> myDoubleWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myStringWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyStringWithDefault(GemValue<String> myStringWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myEnumWithDefault}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyEnumWithDefault(GemValue<String> myEnumWithDefault);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myClass}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyClass(GemValue<TypeMirror> myClass);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myBoolean}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyBoolean(GemValue<Boolean> myBoolean);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myChar}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyChar(GemValue<Character> myChar);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myByte}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyByte(GemValue<Byte> myByte);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myShort}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyShort(GemValue<Short> myShort);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myInt}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyInt(GemValue<Integer> myInt);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myLong}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyLong(GemValue<Integer> myLong);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myFloat}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyFloat(GemValue<Float> myFloat);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myDouble}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyDouble(GemValue<Double> myDouble);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myString}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyString(GemValue<String> myString);

        /**
         * Sets the {@link GemValue} for {@link SomeAnnotationGem#myEnum}
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMyEnum(GemValue<String> myEnum);

        /**
         * Sets the annotation mirror
         *
         * @param mirror the mirror which this gem represents
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
        Builder setMirror(AnnotationMirror mirror);

        /**
         * The build method can be overridden in a custom implementation,
         * which allows the user to define their own custom validation on the annotation.
         *
         * @return the representation of the annotation
         */
        T build();
    }

    private static class BuilderImpl implements Builder<SomeAnnotationGem> {
        private GemValue<TypeMirror> myClassWithDefault;

        private GemValue<Boolean> myBooleanWithDefault;

        private GemValue<Character> myCharWithDefault;

        private GemValue<Byte> myByteWithDefault;

        private GemValue<Short> mShortWithDefault;

        private GemValue<Integer> myIntWithDefault;

        private GemValue<Integer> myLongWithDefault;

        private GemValue<Float> myFloatWithDefault;

        private GemValue<Double> myDoubleWithDefault;

        private GemValue<String> myStringWithDefault;

        private GemValue<String> myEnumWithDefault;

        private GemValue<TypeMirror> myClass;

        private GemValue<Boolean> myBoolean;

        private GemValue<Character> myChar;

        private GemValue<Byte> myByte;

        private GemValue<Short> myShort;

        private GemValue<Integer> myInt;

        private GemValue<Integer> myLong;

        private GemValue<Float> myFloat;

        private GemValue<Double> myDouble;

        private GemValue<String> myString;

        private GemValue<String> myEnum;

        private AnnotationMirror mirror;

        @Override
        public Builder setMyClassWithDefault(GemValue<TypeMirror> myClassWithDefault) {
            this.myClassWithDefault = myClassWithDefault;
            return this;
        }

        @Override
        public Builder setMyBooleanWithDefault(GemValue<Boolean> myBooleanWithDefault) {
            this.myBooleanWithDefault = myBooleanWithDefault;
            return this;
        }

        @Override
        public Builder setMyCharWithDefault(GemValue<Character> myCharWithDefault) {
            this.myCharWithDefault = myCharWithDefault;
            return this;
        }

        @Override
        public Builder setMyByteWithDefault(GemValue<Byte> myByteWithDefault) {
            this.myByteWithDefault = myByteWithDefault;
            return this;
        }

        @Override
        public Builder setMShortWithDefault(GemValue<Short> mShortWithDefault) {
            this.mShortWithDefault = mShortWithDefault;
            return this;
        }

        @Override
        public Builder setMyIntWithDefault(GemValue<Integer> myIntWithDefault) {
            this.myIntWithDefault = myIntWithDefault;
            return this;
        }

        @Override
        public Builder setMyLongWithDefault(GemValue<Integer> myLongWithDefault) {
            this.myLongWithDefault = myLongWithDefault;
            return this;
        }

        @Override
        public Builder setMyFloatWithDefault(GemValue<Float> myFloatWithDefault) {
            this.myFloatWithDefault = myFloatWithDefault;
            return this;
        }

        @Override
        public Builder setMyDoubleWithDefault(GemValue<Double> myDoubleWithDefault) {
            this.myDoubleWithDefault = myDoubleWithDefault;
            return this;
        }

        @Override
        public Builder setMyStringWithDefault(GemValue<String> myStringWithDefault) {
            this.myStringWithDefault = myStringWithDefault;
            return this;
        }

        @Override
        public Builder setMyEnumWithDefault(GemValue<String> myEnumWithDefault) {
            this.myEnumWithDefault = myEnumWithDefault;
            return this;
        }

        @Override
        public Builder setMyClass(GemValue<TypeMirror> myClass) {
            this.myClass = myClass;
            return this;
        }

        @Override
        public Builder setMyBoolean(GemValue<Boolean> myBoolean) {
            this.myBoolean = myBoolean;
            return this;
        }

        @Override
        public Builder setMyChar(GemValue<Character> myChar) {
            this.myChar = myChar;
            return this;
        }

        @Override
        public Builder setMyByte(GemValue<Byte> myByte) {
            this.myByte = myByte;
            return this;
        }

        @Override
        public Builder setMyShort(GemValue<Short> myShort) {
            this.myShort = myShort;
            return this;
        }

        @Override
        public Builder setMyInt(GemValue<Integer> myInt) {
            this.myInt = myInt;
            return this;
        }

        @Override
        public Builder setMyLong(GemValue<Integer> myLong) {
            this.myLong = myLong;
            return this;
        }

        @Override
        public Builder setMyFloat(GemValue<Float> myFloat) {
            this.myFloat = myFloat;
            return this;
        }

        @Override
        public Builder setMyDouble(GemValue<Double> myDouble) {
            this.myDouble = myDouble;
            return this;
        }

        @Override
        public Builder setMyString(GemValue<String> myString) {
            this.myString = myString;
            return this;
        }

        @Override
        public Builder setMyEnum(GemValue<String> myEnum) {
            this.myEnum = myEnum;
            return this;
        }

        @Override
        public Builder setMirror(AnnotationMirror mirror) {
            this.mirror = mirror;
            return this;
        }

        @Override
        public SomeAnnotationGem build() {
            return new SomeAnnotationGem( this );
        }
    }
}
