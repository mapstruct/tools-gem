package org.mapstruct.tools.gem.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.AbstractAnnotationValueVisitor8;
import javax.lang.model.util.ElementFilter;
import org.mapstruct.tools.gem.Gem;
import org.mapstruct.tools.gem.GemValue;

import javax.lang.model.type.TypeMirror;

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

    private SomeAnnotationGem( BuilderImpl builder ) {
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
        isValid = ( this.myClassWithDefault != null ? this.myClassWithDefault.isValid() : false )
               && ( this.myBooleanWithDefault != null ? this.myBooleanWithDefault.isValid() : false )
               && ( this.myCharWithDefault != null ? this.myCharWithDefault.isValid() : false )
               && ( this.myByteWithDefault != null ? this.myByteWithDefault.isValid() : false )
               && ( this.mShortWithDefault != null ? this.mShortWithDefault.isValid() : false )
               && ( this.myIntWithDefault != null ? this.myIntWithDefault.isValid() : false )
               && ( this.myLongWithDefault != null ? this.myLongWithDefault.isValid() : false )
               && ( this.myFloatWithDefault != null ? this.myFloatWithDefault.isValid() : false )
               && ( this.myDoubleWithDefault != null ? this.myDoubleWithDefault.isValid() : false )
               && ( this.myStringWithDefault != null ? this.myStringWithDefault.isValid() : false )
               && ( this.myEnumWithDefault != null ? this.myEnumWithDefault.isValid() : false )
               && ( this.myClass != null ? this.myClass.isValid() : false )
               && ( this.myBoolean != null ? this.myBoolean.isValid() : false )
               && ( this.myChar != null ? this.myChar.isValid() : false )
               && ( this.myByte != null ? this.myByte.isValid() : false )
               && ( this.myShort != null ? this.myShort.isValid() : false )
               && ( this.myInt != null ? this.myInt.isValid() : false )
               && ( this.myLong != null ? this.myLong.isValid() : false )
               && ( this.myFloat != null ? this.myFloat.isValid() : false )
               && ( this.myDouble != null ? this.myDouble.isValid() : false )
               && ( this.myString != null ? this.myString.isValid() : false )
               && ( this.myEnum != null ? this.myEnum.isValid() : false );
        mirror = builder.mirror;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myClassWithDefault}
    */
    public GemValue<TypeMirror> myClassWithDefault( ) {
        return myClassWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myBooleanWithDefault}
    */
    public GemValue<Boolean> myBooleanWithDefault( ) {
        return myBooleanWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myCharWithDefault}
    */
    public GemValue<Character> myCharWithDefault( ) {
        return myCharWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myByteWithDefault}
    */
    public GemValue<Byte> myByteWithDefault( ) {
        return myByteWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#mShortWithDefault}
    */
    public GemValue<Short> mShortWithDefault( ) {
        return mShortWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myIntWithDefault}
    */
    public GemValue<Integer> myIntWithDefault( ) {
        return myIntWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myLongWithDefault}
    */
    public GemValue<Integer> myLongWithDefault( ) {
        return myLongWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myFloatWithDefault}
    */
    public GemValue<Float> myFloatWithDefault( ) {
        return myFloatWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myDoubleWithDefault}
    */
    public GemValue<Double> myDoubleWithDefault( ) {
        return myDoubleWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myStringWithDefault}
    */
    public GemValue<String> myStringWithDefault( ) {
        return myStringWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myEnumWithDefault}
    */
    public GemValue<String> myEnumWithDefault( ) {
        return myEnumWithDefault;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myClass}
    */
    public GemValue<TypeMirror> myClass( ) {
        return myClass;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myBoolean}
    */
    public GemValue<Boolean> myBoolean( ) {
        return myBoolean;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myChar}
    */
    public GemValue<Character> myChar( ) {
        return myChar;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myByte}
    */
    public GemValue<Byte> myByte( ) {
        return myByte;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myShort}
    */
    public GemValue<Short> myShort( ) {
        return myShort;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myInt}
    */
    public GemValue<Integer> myInt( ) {
        return myInt;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myLong}
    */
    public GemValue<Integer> myLong( ) {
        return myLong;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myFloat}
    */
    public GemValue<Float> myFloat( ) {
        return myFloat;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myDouble}
    */
    public GemValue<Double> myDouble( ) {
        return myDouble;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myString}
    */
    public GemValue<String> myString( ) {
        return myString;
    }

    /**
    * accessor
    *
    * @return the {@link GemValue} for {@link SomeAnnotationGem#myEnum}
    */
    public GemValue<String> myEnum( ) {
        return myEnum;
    }

    @Override
    public AnnotationMirror mirror( ) {
        return mirror;
    }

    @Override
    public boolean isValid( ) {
        return isValid;
    }

    public static SomeAnnotationGem  instanceOn(Element element) {
        return build( element, new BuilderImpl() );
    }

    public static SomeAnnotationGem instanceOn(AnnotationMirror mirror ) {
        return build( mirror, new BuilderImpl() );
    }

    public static  <T> T  build(Element element, Builder<T> builder) {
        AnnotationMirror mirror = element.getAnnotationMirrors().stream()
            .filter( a ->  "org.mapstruct.tools.gem.test.SomeAnnotation".contentEquals( ( ( TypeElement )a.getAnnotationType().asElement() ).getQualifiedName() ) )
            .findAny()
            .orElse( null );
        return build( mirror, builder );
    }

    public static <T> T build(AnnotationMirror mirror, Builder<T> builder ) {

        // return fast
        if ( mirror == null || builder == null ) {
            return null;
        }

        // fetch defaults from all defined values in the annotation type
        List<ExecutableElement> enclosed = ElementFilter.methodsIn( mirror.getAnnotationType().asElement().getEnclosedElements() );
        Map<String, AnnotationValue> defaultValues = new HashMap<>( enclosed.size() );
        enclosed.forEach( e -> defaultValues.put( e.getSimpleName().toString(), e.getDefaultValue() ) );

        // fetch all explicitely set annotation values in the annotation instance
        Map<String, AnnotationValue> values = new HashMap<>( enclosed.size() );
        mirror.getElementValues().entrySet().forEach( e -> values.put( e.getKey().getSimpleName().toString(), e.getValue() ) );

        // iterate and populate builder
        for ( Map.Entry<String, AnnotationValue> defaultMethod : defaultValues.entrySet() ) {
            String methodName = defaultMethod.getKey();
            AnnotationValue defaultValue = defaultMethod.getValue();
            AnnotationValue value = values.get( methodName );
            switch ( methodName ) {
                case "myClassWithDefault":
                    builder.setMyclasswithdefault( GemValue.create( value, defaultValue, TypeMirror.class ) );
                    break;
                case "myBooleanWithDefault":
                    builder.setMybooleanwithdefault( GemValue.create( value, defaultValue, Boolean.class ) );
                    break;
                case "myCharWithDefault":
                    builder.setMycharwithdefault( GemValue.create( value, defaultValue, Character.class ) );
                    break;
                case "myByteWithDefault":
                    builder.setMybytewithdefault( GemValue.create( value, defaultValue, Byte.class ) );
                    break;
                case "mShortWithDefault":
                    builder.setMshortwithdefault( GemValue.create( value, defaultValue, Short.class ) );
                    break;
                case "myIntWithDefault":
                    builder.setMyintwithdefault( GemValue.create( value, defaultValue, Integer.class ) );
                    break;
                case "myLongWithDefault":
                    builder.setMylongwithdefault( GemValue.create( value, defaultValue, Integer.class ) );
                    break;
                case "myFloatWithDefault":
                    builder.setMyfloatwithdefault( GemValue.create( value, defaultValue, Float.class ) );
                    break;
                case "myDoubleWithDefault":
                    builder.setMydoublewithdefault( GemValue.create( value, defaultValue, Double.class ) );
                    break;
                case "myStringWithDefault":
                    builder.setMystringwithdefault( GemValue.create( value, defaultValue, String.class ) );
                    break;
                case "myEnumWithDefault":
                    builder.setMyenumwithdefault( GemValue.createEnum( value, defaultValue ) );
                    break;
                case "myClass":
                    builder.setMyclass( GemValue.create( value, defaultValue, TypeMirror.class ) );
                    break;
                case "myBoolean":
                    builder.setMyboolean( GemValue.create( value, defaultValue, Boolean.class ) );
                    break;
                case "myChar":
                    builder.setMychar( GemValue.create( value, defaultValue, Character.class ) );
                    break;
                case "myByte":
                    builder.setMybyte( GemValue.create( value, defaultValue, Byte.class ) );
                    break;
                case "myShort":
                    builder.setMyshort( GemValue.create( value, defaultValue, Short.class ) );
                    break;
                case "myInt":
                    builder.setMyint( GemValue.create( value, defaultValue, Integer.class ) );
                    break;
                case "myLong":
                    builder.setMylong( GemValue.create( value, defaultValue, Integer.class ) );
                    break;
                case "myFloat":
                    builder.setMyfloat( GemValue.create( value, defaultValue, Float.class ) );
                    break;
                case "myDouble":
                    builder.setMydouble( GemValue.create( value, defaultValue, Double.class ) );
                    break;
                case "myString":
                    builder.setMystring( GemValue.create( value, defaultValue, String.class ) );
                    break;
                case "myEnum":
                    builder.setMyenum( GemValue.createEnum( value, defaultValue ) );
                    break;
            }
        }
        builder.setMirror( mirror );
        return builder.build();
    }

    /**
     * A builder that can be implemented by the user to define custom logic e.g. in the
     * build method, prior to creating the annotation gem.
     */
    public interface Builder<T> {

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myClassWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMyclasswithdefault(GemValue<TypeMirror> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myBooleanWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMybooleanwithdefault(GemValue<Boolean> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myCharWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMycharwithdefault(GemValue<Character> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myByteWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMybytewithdefault(GemValue<Byte> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#mShortWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMshortwithdefault(GemValue<Short> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myIntWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMyintwithdefault(GemValue<Integer> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myLongWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMylongwithdefault(GemValue<Integer> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myFloatWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMyfloatwithdefault(GemValue<Float> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myDoubleWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMydoublewithdefault(GemValue<Double> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myStringWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMystringwithdefault(GemValue<String> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myEnumWithDefault}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMyenumwithdefault(GemValue<String> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myClass}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMyclass(GemValue<TypeMirror> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myBoolean}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMyboolean(GemValue<Boolean> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myChar}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMychar(GemValue<Character> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myByte}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMybyte(GemValue<Byte> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myShort}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMyshort(GemValue<Short> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myInt}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMyint(GemValue<Integer> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myLong}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMylong(GemValue<Integer> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myFloat}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMyfloat(GemValue<Float> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myDouble}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMydouble(GemValue<Double> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myString}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMystring(GemValue<String> methodName );

       /**
        * Sets the {@link GemValue} for {@link SomeAnnotationGem#myEnum}
        *
        * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
        */
        Builder setMyenum(GemValue<String> methodName );

        /**
         * Sets the annotation mirror
         *
         * @param mirror the mirror which this gem represents
         *
         * @return the {@link Builder} for this gem, representing {@link SomeAnnotationGem}
         */
          Builder setMirror( AnnotationMirror mirror );

        /**
         * The build method can be overriden in a custom custom implementation, which allows
         * the user to define his own custom validation on the annotation.
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

        public Builder setMyclasswithdefault(GemValue<TypeMirror> myClassWithDefault ) {
            this.myClassWithDefault = myClassWithDefault;
            return this;
        }

        public Builder setMybooleanwithdefault(GemValue<Boolean> myBooleanWithDefault ) {
            this.myBooleanWithDefault = myBooleanWithDefault;
            return this;
        }

        public Builder setMycharwithdefault(GemValue<Character> myCharWithDefault ) {
            this.myCharWithDefault = myCharWithDefault;
            return this;
        }

        public Builder setMybytewithdefault(GemValue<Byte> myByteWithDefault ) {
            this.myByteWithDefault = myByteWithDefault;
            return this;
        }

        public Builder setMshortwithdefault(GemValue<Short> mShortWithDefault ) {
            this.mShortWithDefault = mShortWithDefault;
            return this;
        }

        public Builder setMyintwithdefault(GemValue<Integer> myIntWithDefault ) {
            this.myIntWithDefault = myIntWithDefault;
            return this;
        }

        public Builder setMylongwithdefault(GemValue<Integer> myLongWithDefault ) {
            this.myLongWithDefault = myLongWithDefault;
            return this;
        }

        public Builder setMyfloatwithdefault(GemValue<Float> myFloatWithDefault ) {
            this.myFloatWithDefault = myFloatWithDefault;
            return this;
        }

        public Builder setMydoublewithdefault(GemValue<Double> myDoubleWithDefault ) {
            this.myDoubleWithDefault = myDoubleWithDefault;
            return this;
        }

        public Builder setMystringwithdefault(GemValue<String> myStringWithDefault ) {
            this.myStringWithDefault = myStringWithDefault;
            return this;
        }

        public Builder setMyenumwithdefault(GemValue<String> myEnumWithDefault ) {
            this.myEnumWithDefault = myEnumWithDefault;
            return this;
        }

        public Builder setMyclass(GemValue<TypeMirror> myClass ) {
            this.myClass = myClass;
            return this;
        }

        public Builder setMyboolean(GemValue<Boolean> myBoolean ) {
            this.myBoolean = myBoolean;
            return this;
        }

        public Builder setMychar(GemValue<Character> myChar ) {
            this.myChar = myChar;
            return this;
        }

        public Builder setMybyte(GemValue<Byte> myByte ) {
            this.myByte = myByte;
            return this;
        }

        public Builder setMyshort(GemValue<Short> myShort ) {
            this.myShort = myShort;
            return this;
        }

        public Builder setMyint(GemValue<Integer> myInt ) {
            this.myInt = myInt;
            return this;
        }

        public Builder setMylong(GemValue<Integer> myLong ) {
            this.myLong = myLong;
            return this;
        }

        public Builder setMyfloat(GemValue<Float> myFloat ) {
            this.myFloat = myFloat;
            return this;
        }

        public Builder setMydouble(GemValue<Double> myDouble ) {
            this.myDouble = myDouble;
            return this;
        }

        public Builder setMystring(GemValue<String> myString ) {
            this.myString = myString;
            return this;
        }

        public Builder setMyenum(GemValue<String> myEnum ) {
            this.myEnum = myEnum;
            return this;
        }

        public Builder  setMirror( AnnotationMirror mirror ) {
            this.mirror = mirror;
            return this;
        }

        public SomeAnnotationGem build() {
            return new SomeAnnotationGem( this );
        }
    }

}
