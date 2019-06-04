package model.declaration;

import model.JavaAnnotationModifier;

import java.util.Set;

public abstract class NormalJavaDeclaration{
   private Set<JavaAnnotationModifier> annotationModifiers;
   private Set<Integer> modifiers;
   private String name;
   private Set<JavaDeclaration> innerDeclarations;

   public NormalJavaDeclaration(Set<JavaAnnotationModifier> annotationModifiers, Set<Integer> modifiers, String name, Set<JavaDeclaration> innerDeclarations) {
      this.annotationModifiers = annotationModifiers;
      this.modifiers = modifiers;
      this.name = name;
      this.innerDeclarations = innerDeclarations;
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == null) return false;
      if(obj == this) return true;
      if(obj.getClass() != this.getClass()) return false;
      NormalJavaDeclaration njd = (NormalJavaDeclaration) obj;
      return name != null && name.equals(njd.getName());
   }

   @Override
   public int hashCode() {
      return name!=null ? name.hashCode() : 1;
   }

   public Set<Integer> getModifiers() {
      return modifiers;
   }

   public String getName() {
      return name;
   }

   public Set<JavaDeclaration> getInnerDeclarations() {
      return innerDeclarations;
   }

   public Set<JavaAnnotationModifier> getAnnotationModifiers() {
      return annotationModifiers;
   }
}
