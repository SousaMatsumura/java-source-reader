package model.context.method;

import model.JavaAnnotationModifier;
import model.JavaModifier;
import model.JavaVariable;
import model.context.JavaDeclaration;

import java.util.Iterator;
import java.util.Set;

import static resource.Cons.*;
import static resource.Cons.CLOSE_BRACE;

public class JavaMethod extends NormalJavaMethod implements JavaDeclaration {
   private String returns;

   private JavaMethod(Set<Integer> modifiers, String name, Set<JavaDeclaration> innerDeclarations, Set<JavaVariable> parameters, Set<String> exceptions, String returns, Set<JavaAnnotationModifier> annotationModifiers) {
      super(modifiers, name, innerDeclarations, parameters, exceptions, annotationModifiers);
      this.returns = returns;
   }

   @Override
   public String toStringByDepth(int depth) {
      StringBuilder result = new StringBuilder();
      if(getAnnotationModifiers()!=null && getAnnotationModifiers().size()>0) {
         for (JavaAnnotationModifier amd : getAnnotationModifiers()) {
            for (int count = 0; count < depth; count++) result.append(INDENT);
            result.append(amd.toString()).append(LF);
         }
      }
      for (int count = 0; count < depth; count++) result.append(INDENT);
      if(getModifiers() != null && getModifiers().size()>0){
         for (Integer mod : getModifiers()) result.append(JavaModifier.getValue(mod)).append(SPACE);
      }
      result.append(returns).append(SPACE).append(getName()).append(OPEN_PARENTHESES);
      if(getParameters() != null && getParameters().size() > 0) {
         Iterator<JavaVariable> iterator = getParameters().iterator();
         result.append(iterator.next());
         while(iterator.hasNext()) result.append(COMMA).append(SPACE).append(iterator.next());
      }
      result.append(CLOSE_PARENTHESES);
      if(getExceptions() != null && getExceptions().size()>0){
         Iterator<String> iterator = getExceptions().iterator();
         result.append(SPACE).append(THROWS).append(SPACE).append(iterator.next());
         while (iterator.hasNext()){
            result.append(COMMA).append(SPACE).append(iterator.next());
         }
      }
      result.append(OPEN_BRACE);

      if(getInnerDeclarations() != null && getInnerDeclarations().size() > 0){
         result.append(LF);
         for(JavaDeclaration jd : getInnerDeclarations()){
            result.append(jd.toStringByDepth(depth+1)).append(LF);
         }
         for (int count = 0; count < depth; count++) result.append(INDENT);
      }
      result.append(CLOSE_BRACE).append(LF);
      return result.toString();
   }

   @Override
   public String toString() {
      return toStringByDepth(0);
   }

   public static class Builder{
      private Set<JavaAnnotationModifier> annotationModifiers;
      private Set<Integer> modifiers;
      private String name;
      private Set<JavaDeclaration> innerDeclarations;
      private Set<JavaVariable> parameters;
      private Set<String> exceptions;
      private String returns;


      public Builder(String name, String returns){
         this.name = name;
         this.returns = returns;
      }

      public Builder annotationModifiers(Set<JavaAnnotationModifier> annotationModifiers){
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
      public Builder parameters(Set<JavaVariable> paraters){
         this.parameters = paraters;
         return this;
      }
      public JavaMethod build(){
         return new JavaMethod(modifiers, name, innerDeclarations, parameters, exceptions, returns, annotationModifiers){};
      }
   }

}
