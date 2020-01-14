package org.annotationhelper.impl;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class Util {

    private final Types types;
    private final Elements elements;

    Util(Types types, Elements elements) {
        this.types = types;
        this.elements = elements;
    }

    <T> T getAnnotationValue(AnnotationMirror mirror, String methodName, Class<T> clazz ) {
        AnnotationValue annotationValue = getAnnotationValue( mirror, methodName );
        return annotationValue == null ? null : clazz.cast( annotationValue.getValue() );
    }

    AnnotationValue getAnnotationValue(AnnotationMirror mirror, String methodName  ) {
        ExecutableElement method = mirror.getAnnotationType()
            .asElement()
            .getEnclosedElements()
            .stream()
            .filter( e -> isMethodWithName( e, methodName ) )
            .map( ExecutableElement.class::cast )
            .findAny()
            .orElse( null );

        return mirror.getElementValues().get( method );
    }

    private boolean isMethodWithName(Element e, String methodName ) {
        return ElementKind.METHOD == e.getKind() && methodName.equals(e.getSimpleName().toString() );
    }

    String getFullyQualifiedName(DeclaredType type) {
        return ( (TypeElement) type.asElement() ).getQualifiedName().toString();
    }

    boolean isEnumeration( TypeMirror type ) {
        if (TypeKind.DECLARED == type.getKind() ) {
            DeclaredType declaredType = ( DeclaredType ) type;
            return ElementKind.ENUM == declaredType.asElement().getKind();
        }
        return false;
    }

    boolean isSame(TypeMirror type, Class<?> clazz) {
        TypeElement typeOfClazzElement = elements.getTypeElement( clazz.getName() );
        if ( type.getKind() == TypeKind.DECLARED ) {
            DeclaredType declaredType = (DeclaredType) type;
            if ( declaredType.getTypeArguments().size() != typeOfClazzElement.getTypeParameters().size() ) {
                return false;
            }
            TypeMirror[] typeArgs = declaredType.getTypeArguments().toArray( new TypeMirror[0] );
            DeclaredType typeOfClazz = types.getDeclaredType( typeOfClazzElement, typeArgs );
            return types.isSameType( type, typeOfClazz );
        }
        return false;
    }

    String getSimpleName(DeclaredType type) {
        return type.asElement().getSimpleName().toString();
    }

    static String capitalize(String name) {
        if ( name == null ) {
            return null;
        }
        else if ( name.length() == 1 ) {
            return name.substring( 0, 1 ).toUpperCase();
        }
        else {
            return name.substring( 0, 1 ).toUpperCase() + name.substring( 1 );
        }
    }

}
