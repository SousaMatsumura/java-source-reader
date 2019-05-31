package model.context.method;

import model.context.JavaDeclaration;

import java.util.Set;

public class JavaAnnotationMethod implements JavaDeclaration {
   private Set<String> annotationModifiers;
   private String name;
   private Object defaultValue;
   private String returns;

   private JavaAnnotationMethod(Set<String> annotationModifiers, String name, Object defaultValue, String returns) {
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
      return null;
   }

   public static class Builder{
      private Set<String> annotationModifiers;
      private String name;
      private Object defaultValue;
      private String returns;

      public Builder(String name){
         this.name = name;
      }
      public Builder annotationModifiers(Set<String> annotationModifiers){
         this.annotationModifiers = annotationModifiers;
         return this;
      }
      public Builder defaultValue(Object defaultValue){
         this.defaultValue = defaultValue;
         return this;
      }
      public Builder returns(String returns){
         this.returns = returns;
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
