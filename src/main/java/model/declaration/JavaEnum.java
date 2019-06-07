package model.declaration;

import model.declaration.method.variable.JavaAnnotationModifier;
import model.JavaModifier;
import resource.DeclarationType;

import java.util.Iterator;
import java.util.Set;

import static resource.Cons.*;
import static resource.Cons.CLOSE_BRACE;

public class JavaEnum extends NormalJavaDeclaration implements JavaDeclaration {
   private Set<String> labels;
   private Set<String> implementations;

   private JavaEnum(Set<JavaAnnotationModifier> annotationModifiers, Set<Integer> modifiers, String name, Set<JavaDeclaration> innerDeclarations, Set<String> labels, Set<String> implementations) {
      super(annotationModifiers, modifiers, name, innerDeclarations);
      this.labels = labels;
      this.implementations = implementations;
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
      result.append(DeclarationType.ENUM.toString()).append(SPACE).append(getName());
      if(implementations != null && implementations.size() > 0) {
         result.append(SPACE).append(IMPLEMENTS);
         Iterator<String> iterator = implementations.iterator();
         result.append(SPACE).append(iterator.next());
         while (iterator.hasNext()) result.append(COMMA).append(SPACE).append(iterator.next());
      }
      result.append(OPEN_BRACE).append(LF);

      if(labels != null && labels.size()>0){
         for (int count = 0; count < (depth+1); count++) result.append(INDENT);
         Iterator<String> iterator = labels.iterator();
         result.append(iterator.next());
         while (iterator.hasNext()) result.append(COMMA).append(SPACE).append(iterator.next());
      }
      result.append(SEMI_COLON).append(LF);

      if(getInnerDeclarations() != null && getInnerDeclarations().size() > 0){
         result.append(LF);
         for(JavaDeclaration jd : getInnerDeclarations()) result.append(jd.toStringByDepth(depth+1)).append(LF);
      }
      for (int count = 0; count < depth; count++) result.append(INDENT);
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
      private Set<String> labels;
      private Set<String> implementations;

      public Builder(String name, Set<String> labels){
         this.name = name;
         this.labels = labels;
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
      public Builder implementations(Set<String> implementations){
         this.implementations = implementations;
         return this;
      }
      public JavaEnum build(){
         return new JavaEnum(annotationModifiers, modifiers, name, innerDeclarations, labels, implementations){};
      }
   }

   public Set<String> getLabels() {
      return labels;
   }

   public Set<String> getImplementations() {
      return implementations;
   }
}
