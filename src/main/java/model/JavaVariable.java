package model;

import resource.Cons;

import java.util.Set;

import static resource.Cons.SPACE;

public class JavaVariable {
   private String name;
   private String dataKind;
   private String value;
   private Set<Integer> modifiers;
   private Set<JavaAnnotationModifier> annotationModifiers;

   private JavaVariable(String name, String dataKind, String value, Set<Integer> modifiers, Set<JavaAnnotationModifier> annotationModifiers){
      this.name = name;
      this.dataKind = dataKind;
      this.value = value;
      this.modifiers = modifiers;
      this.annotationModifiers = annotationModifiers;
   }

   public static class Builder{
      private String name;
      private String dataKind;
      private String value;
      private Set<Integer> modifiers;
      private Set<JavaAnnotationModifier> annotationModifiers;

      public Builder(String dataKind, String name){
         this.name = name;
         this.dataKind = dataKind;
      }
      public Builder value(String value){
         this.value = value;
         return this;
      }
      public Builder modifiers(Set<Integer> modifiers){
         this.modifiers = modifiers;
         return this;
      }
      public Builder annotationModifiers(Set<JavaAnnotationModifier> annotationModifiers){
         this.annotationModifiers = annotationModifiers;
         return this;
      }
      public JavaVariable build(){
         return new JavaVariable(name, dataKind, value, modifiers, annotationModifiers);
      }
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == null) return false;
      if(obj == this) return true;
      if(obj.getClass() != this.getClass()) return false;
      JavaVariable var = (JavaVariable) obj;
      return !getID().isEmpty() && getID().equals(var.getID());
   }

   @Override
   public int hashCode() {
      return getID().equals("") ? getID().hashCode() : 1;
   }

   public String getID(){
      final StringBuilder result = new StringBuilder(dataKind);
      result.append(SPACE).append(name);
      if(value != null) result.append(Cons.EQUAL).append(value);
      return result.toString();
   }

   public String getName() {
      return name;
   }

   public String getDataKind() {
      return dataKind;
   }

   public String getValue() {
      return value;
   }

   public Set<Integer> getModifiers() {
      return modifiers;
   }

   @Override
   public String toString() {
      StringBuilder result = new StringBuilder();

      if (annotationModifiers != null && annotationModifiers.size()>0){
         for (JavaAnnotationModifier jam : annotationModifiers)
            result.append(jam.toString()).append(SPACE);
      }
      if (modifiers != null && modifiers.size()>0){
         for (Integer i : modifiers) result.append(JavaModifier.getValue(i)).append(SPACE);
      }
      result.append(dataKind).append(SPACE).append(name);
      return result.toString();
   }

}
