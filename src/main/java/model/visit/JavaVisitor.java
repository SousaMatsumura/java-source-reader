package model.visit;

import model.declaration.JavaDeclaration;

@FunctionalInterface
public interface JavaVisitor<T> {
   T visit(JavaDeclaration declaration);
   default T aggregateResult(T aggregate, T nextResult) {
      return nextResult;
   }
}
