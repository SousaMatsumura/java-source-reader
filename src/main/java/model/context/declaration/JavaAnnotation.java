package model.context.declaration;

import model.Modifier;
import model.context.JavaDeclaration;
import model.context.method.JavaAnnotationMethod;
import model.context.method.JavaConstructor;
import model.context.method.JavaMethod;
import resource.DeclarationType;

import java.util.Iterator;
import java.util.Set;

import static resource.Cons.*;

public class JavaAnnotation extends NormalJavaDeclaration implements JavaDeclaration {
   private JavaAnnotation(Set<String> annotationModifiers, Set<Integer> modifiers, String name, Set<JavaDeclaration> innerDeclarations) {
      super(annotationModifiers, modifiers, name, innerDeclarations);
   }

   @Override
   public String toStringByDepth(int depth) {
      StringBuilder result = new StringBuilder();
      if(getAnnotationModifiers()!=null && getAnnotationModifiers().size()>0) {
         for (String amd : getAnnotationModifiers()) {
            for (int count = 0; count < depth; count++) result.append(INDENT);
            result.append(amd).append(LF);
         }
      }
      if(getModifiers() != null && getModifiers().size()>0){
         for (int count = 0; count < depth; count++) result.append(INDENT);
         for (Integer mod : getModifiers()) result.append(Modifier.getValue(mod)).append(SPACE);
      }
      result.append(DeclarationType.ANNOTATION.toString()).append(SPACE).append(getName());
      result.append(OPEN_BRACE).append(LF);

      if(getInnerDeclarations() != null && getInnerDeclarations().size() > 0){
         for(JavaDeclaration jd : getInnerDeclarations()){
            if(jd instanceof JavaClass){
               JavaClass jc = (JavaClass) jd;
               result.append(jd.toStringByDepth(depth+1));
            }
            else if(jd instanceof JavaEnum){
               JavaEnum je = (JavaEnum) jd;
               result.append(je.toStringByDepth(depth+1));
            }
            else if(jd instanceof JavaInterface){
               JavaInterface ji = (JavaInterface) jd;
               result.append(ji.toStringByDepth(depth+1));
            }
            else if(jd instanceof JavaAnnotation){
               JavaAnnotation ja = (JavaAnnotation) jd;
               result.append(ja.toStringByDepth(depth+1));
            }
            else if(jd instanceof JavaConstructor){
               JavaConstructor jc = (JavaConstructor) jd;
               result.append(jc.toStringByDepth(depth+1));
            }
            else if(jd instanceof JavaMethod){
               JavaMethod jm = (JavaMethod) jd;
               result.append(jm.toStringByDepth(depth+1));
            }
            else if(jd instanceof JavaAnnotationMethod){
               JavaAnnotationMethod jam = (JavaAnnotationMethod) jd;
               result.append(jam.toStringByDepth(depth+1));
            }
            result.append(LF);
         }
      }
      for (int count = 0; count < depth; count++) result.append(INDENT);
      result.append(CLOSE_BRACE);
      return result.toString();
   }

   public static class Builder{
      private Set<String> annotationModifiers;
      private Set<Integer> modifiers;
      private String name;
      private Set<JavaDeclaration> innerDeclarations;

      public Builder(String name){
         this.name = name;
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
      public JavaAnnotation build(){
         return new JavaAnnotation(annotationModifiers, modifiers, name, innerDeclarations){};
      }
   }
}
