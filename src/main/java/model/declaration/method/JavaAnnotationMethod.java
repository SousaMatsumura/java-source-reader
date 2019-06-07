package model.declaration.method;

import model.declaration.method.variable.JavaAnnotationModifier;
import model.declaration.JavaDeclaration;

import java.util.Set;

import static resource.Cons.*;

public class JavaAnnotationMethod implements JavaDeclaration {
   private Set<JavaAnnotationModifier> annotationModifiers;
   private String name;
   private String defaultValue;
   private String returns;

   private JavaAnnotationMethod(Set<JavaAnnotationModifier> annotationModifiers, String name, String defaultValue, String returns) {
      this.annotationModifiers = annotationModifiers;
      this.name = name;
      this.defaultValue = defaultValue;
      this.returns = returns;
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == null) return false;
      if(obj == this) return true;
      if(obj.getClass() != this.getClass()) return false;
      JavaAnnotationMethod njm = (JavaAnnotationMethod) obj;
      return getID(name, returns) != null && getID(name, returns).equals(getID(njm.getName(), njm.getReturns()));
   }

   @Override
   public int hashCode() {
      return getID(name, returns)!=null ? getID(name, returns).hashCode() : 1;
   }

   private static String getID(String name, String returns){
      if(name == null || returns == null) return null;
      return name+returns;
   }

   @Override
   public String toStringByDepth(int depth) {
      StringBuilder result = new StringBuilder();
      if(annotationModifiers!=null && annotationModifiers.size()>0) {
         for (JavaAnnotationModifier amd : annotationModifiers) {
            for (int count = 0; count < depth; count++) result.append(INDENT);
            result.append(amd.toString()).append(LF);
         }
      }
      for (int count = 0; count < depth; count++) result.append(INDENT);

      result.append(returns).append(SPACE).append(getName()).append(OPEN_PARENTHESES).append(CLOSE_PARENTHESES);
      if(defaultValue != null && !defaultValue.equals("")) result.append(SPACE).append(DEFAULT).append(SPACE).append(defaultValue);
      result.append(SEMI_COLON).append(LF);
      return result.toString();
   }

   @Override
   public String toString() {
      return toStringByDepth(0);
   }

   public static class Builder{
      private Set<JavaAnnotationModifier> annotationModifiers;
      private String name;
      private String defaultValue;
      private String returns;

      public Builder(String name, String returns){
         this.name = name;
         this.returns = returns;
      }
      public Builder annotationModifiers(Set<JavaAnnotationModifier> annotationModifiers){
         this.annotationModifiers = annotationModifiers;
         return this;
      }
      public Builder defaultValue(String defaultValue){
         this.defaultValue = defaultValue;
         return this;
      }
      public JavaAnnotationMethod build(){
         return new JavaAnnotationMethod(annotationModifiers, name, defaultValue, returns){};
      }
   }

   public String getName() {
      return name;
   }

   public Object getDefaultValue() {
      return defaultValue;
   }

   public String getReturns() {
      return returns;
   }
}
