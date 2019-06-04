package model.declaration;

import model.JavaAnnotationModifier;
import model.JavaModifier;
import resource.DeclarationType;

import java.util.Set;

import static resource.Cons.*;

public class JavaAnnotation extends NormalJavaDeclaration implements JavaDeclaration {
   private JavaAnnotation(Set<JavaAnnotationModifier> annotationModifiers, Set<Integer> modifiers, String name, Set<JavaDeclaration> innerDeclarations) {
      super(annotationModifiers, modifiers, name, innerDeclarations);
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
      result.append(DeclarationType.ANNOTATION.toString()).append(SPACE).append(getName());
      result.append(OPEN_BRACE);

      if(getInnerDeclarations() != null && getInnerDeclarations().size() > 0){
         result.append(LF);
         for(JavaDeclaration jd : getInnerDeclarations()) result.append(jd.toStringByDepth(depth+1)).append(LF);
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

      public Builder(String name){
         this.name = name;
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
      public JavaAnnotation build(){
         return new JavaAnnotation(annotationModifiers, modifiers, name, innerDeclarations){};
      }
   }
}
