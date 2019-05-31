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
import static resource.Cons.LF;

public class JavaClass extends NormalJavaDeclaration implements JavaDeclaration {
   private String extend;
   private Set<String> implementations;
   private JavaClass(Set<String> annotationModifiers, Set<Integer> modifiers, String name, Set<JavaDeclaration> innerDeclarations, String extend, Set<String> implementations) {
      super(annotationModifiers, modifiers, name, innerDeclarations);
      this.extend = extend;
      this.implementations = implementations;
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
      result.append(DeclarationType.CLASS.toString()).append(SPACE).append(getName());
      if(!extend.equals("")) result.append(EXTEND).append(SPACE).append(extend);
      if(implementations != null && implementations.size() > 0) {
         result.append(SPACE).append(IMPLEMENTS);
         Iterator<String> iterator = implementations.iterator();
         result.append(SPACE).append(iterator.next());
         while (iterator.hasNext()) result.append(COMMA).append(SPACE).append(iterator.next());
      }
      result.append(OPEN_BRACE).append(LF);

      if(getInnerDeclarations() != null && getInnerDeclarations().size() > 0){
         for(JavaDeclaration jd : getInnerDeclarations()){
            for (int count = 0; count < depth; count++) result.append(INDENT);
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
      private String extend;
      private Set<String> implementations;

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
      public Builder extend(String extend){
         this.extend = extend;
         return this;
      }
      public Builder implementations(Set<String> implementations){
         this.implementations = implementations;
         return this;
      }
      public JavaClass build(){
         return new JavaClass(annotationModifiers, modifiers, name, innerDeclarations, extend, implementations){};
      }
   }

   public String getExtend() {
      return extend;
   }

   public Set<String> getImplementations() {
      return implementations;
   }
}
