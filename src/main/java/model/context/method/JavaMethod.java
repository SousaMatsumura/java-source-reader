package model.context.method;

import model.Modifier;
import model.Variable;
import model.context.JavaDeclaration;

import java.util.Set;

public class JavaMethod extends NormalJavaMethod implements JavaDeclaration {
   private String returns;

   private JavaMethod(Set<Integer> modifiers, String name, Set<JavaDeclaration> innerDeclarations, Set<Variable> parameters, Set<String> exceptions, String returns, Set<String> annotationModifiers) {
      super(modifiers, name, innerDeclarations, parameters, exceptions, annotationModifiers);
      this.returns = returns;
   }

   @Override
   public String toStringByDepth(int depth) {
      return null;
   }

   public static class Builder{
      private Set<String> annotationModifiers;
      private Set<Integer> modifiers;
      private String name;
      private Set<JavaDeclaration> innerDeclarations;
      private Set<Variable> parameters;
      private Set<String> exceptions;
      private String returns;


      public Builder(String name, String returns){
         this.name = name;
         this.returns = returns;
      }

      public Builder annotationModifiers(Set<String> annotationModifiers){
         this.annotationModifiers = annotationModifiers;
         return this;
      }
      public Builder modifiers(Set<Integer> modifiers){
         this.modifiers = modifiers;
         return this;
      }
      public Builder innerDeclarations(Set<JavaDeclaration> innerDeclarations){
         this.innerDeclarations = innerDeclarations;
         return this;
      }
      public Builder exceptions(Set<String> exceptions){
         this.exceptions = exceptions;
         return this;
      }
      public Builder parameters(Set<Variable> paraters){
         this.parameters = paraters;
         return this;
      }
      public JavaMethod build(){
         return new JavaMethod(modifiers, name, innerDeclarations, parameters, exceptions, returns, annotationModifiers){};
      }
   }

}
