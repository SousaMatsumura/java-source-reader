package model.visit;

import model.declaration.JavaDeclaration;

@FunctionalInterface
public interface JavaSearchDeclaration<T> {
   T Search(JavaDeclaration declaration, String name, Class<?extends JavaDeclaration> clazz);
}
