package model.declaration.method;

import model.declaration.method.variable.JavaAnnotationModifier;
import model.declaration.method.variable.JavaVariable;
import model.declaration.JavaDeclaration;

import java.util.Set;

import static resource.Cons.*;

public abstract class NormalJavaMethod implements JavaDeclaration{
   private Set<Integer> modifiers;
   private Set<JavaAnnotationModifier> annotationModifiers;
   private String name;
   private Set<JavaDeclaration> innerDeclarations;
   private Set<JavaVariable> parameters;
   private Set<String> exceptions;

   NormalJavaMethod(Set<Integer> modifiers, String name, Set<JavaDeclaration> innerDeclarations, Set<JavaVariable> parameters, Set<String> exceptions, Set<JavaAnnotationModifier> annotationModifiers) {
      this.modifiers = modifiers;
      this.name = name;
      this.innerDeclarations = innerDeclarations;
      this.parameters = parameters;
      this.exceptions = exceptions;
      this.annotationModifiers = annotationModifiers;
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == null) return false;
      if(obj == this) return true;
      if(obj.getClass() != this.getClass()) return false;
      NormalJavaMethod njm = (NormalJavaMethod) obj;
      return getMethodSignature() != null && getMethodSignature().equals(njm.getMethodSignature());
   }

   @Override
   public int hashCode() {
      return getMethodSignature()!=null ? getMethodSignature().hashCode() : 1;
   }

   abstract public String getMethodSignature();

   public Set<Integer> getModifiers() {
      return modifiers;
   }

   public String getName() {
      return name;
   }

   public Set<JavaVariable> getParameters() {
      return parameters;
   }

   public Set<JavaDeclaration> getInnerDeclarations() {
      return innerDeclarations;
   }

   public Set<String> getExceptions() {
      return exceptions;
   }

   public Set<JavaAnnotationModifier> getAnnotationModifiers() {
      return annotationModifiers;
   }
}
