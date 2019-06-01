package model.context.method;

import model.Modifier;
import model.Variable;
import model.context.JavaDeclaration;
import model.context.declaration.JavaAnnotation;
import model.context.declaration.JavaClass;
import model.context.declaration.JavaEnum;
import model.context.declaration.JavaInterface;

import java.util.Iterator;
import java.util.Set;

import static resource.Cons.*;
import static resource.Cons.CLOSE_BRACE;

public class JavaConstructor extends NormalJavaMethod implements JavaDeclaration {

   private JavaConstructor(Set<Integer> modifiers, String name, Set<JavaDeclaration> innerDeclarations, Set<Variable> parameters, Set<String> exceptions, Set<String> annotationModifiers) {
      super(modifiers, name, innerDeclarations, parameters, exceptions, annotationModifiers);
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
      result.append(getName()).append(OPEN_PARENTHESES);
      if(getParameters() != null && getParameters().size() > 0) {
         Iterator<Variable> iterator = getParameters().iterator();
         Variable param = iterator.next();
         if(param.getModifiers() != null && param.getModifiers().size() > 0)
            for (Integer mod : param.getModifiers()) result.append(Modifier.getValue(mod)).append(SPACE);
         result.append(param.getID());
         while (iterator.hasNext()) {
            result.append(COMMA).append(SPACE);
            param = iterator.next();
            if (param.getModifiers() != null && param.getModifiers().size() > 0)
               for (Integer mod : param.getModifiers()) result.append(Modifier.getValue(mod)).append(SPACE);
            result.append(param.getID());
         }
      }
      result.append(CLOSE_PARENTHESES);
      if(getExceptions() != null && getExceptions().size()>0){
         Iterator<String> iterator = getExceptions().iterator();
         result.append(SPACE).append(THROWS).append(SPACE).append(iterator.next());
         while (iterator.hasNext()){
            result.append(COMMA).append(SPACE).append(iterator.next());
         }
      }
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
      private Set<Variable> parameters;
      private Set<String> exceptions;

      public Builder(String name){
         this.name = name;
      }
      public Builder modifiers(Set<Integer> modifiers){
         this.modifiers = modifiers;
         return this;
      }
      public Builder annotationModifiers(Set<String> annotationModifiers){
         this.annotationModifiers = annotationModifiers;
         return this;
      }
      public Builder innerDeclarations(Set<JavaDeclaration> innerDeclarations){
         this.innerDeclarations = innerDeclarations;
         return this;
      }
      public Builder parameters(Set<Variable> paraters){
         this.parameters = paraters;
         return this;
      }
      public Builder exceptions(Set<String> exceptions){
         this.exceptions = exceptions;
         return this;
      }
      public JavaConstructor build(){
         return new JavaConstructor(modifiers, name, innerDeclarations, parameters, exceptions, annotationModifiers){};
      }
   }
}
