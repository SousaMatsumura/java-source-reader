package model.context.method;

import model.Variable;
import model.context.JavaDeclaration;

import java.util.Set;

public abstract class NormalJavaMethod{
   private Set<Integer> modifiers;
   private Set<String> annotationModifiers;
   private String name;
   private Set<JavaDeclaration> innerDeclarations;
   private Set<Variable> parameters;
   private Set<String> exceptions;

   NormalJavaMethod(Set<Integer> modifiers, String name, Set<JavaDeclaration> innerDeclarations, Set<Variable> parameters, Set<String> exceptions, Set<String> annotationModifiers) {
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

   private String getMethodSignature(){
      if(name==null) return null;
      final StringBuilder result = new StringBuilder(name);
      result.append('(');
      if(parameters != null && parameters.size()>0)
         for(Variable p : parameters) result.append(p.getDataKind()).append(',');
      return result.deleteCharAt(result.length()-1).append(')').toString();
   }

   public Set<Integer> getModifiers() {
      return modifiers;
   }

   public String getName() {
      return name;
   }

   public Set<Variable> getParameters() {
      return parameters;
   }

   public Set<JavaDeclaration> getInnerDeclarations() {
      return innerDeclarations;
   }

   public Set<String> getExceptions() {
      return exceptions;
   }
}
