package model.declaration.method.variable;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static resource.Cons.*;

public class JavaAnnotationModifier {
   private class JavaAnnotationModifierVariable{
      String name;
      String value;
      JavaAnnotationModifierVariable(String modifierVariable){
         //System.out.println(modifierVariable);
         String[] temp = modifierVariable.split(Character.toString(EQUAL));
         this.name = temp[0];
         this.value = temp[1];
      }
      String getName(){ return name;}
      String getValue(){ return value;}
   }
   private String name;
   private Set<JavaAnnotationModifierVariable> variables;

   public JavaAnnotationModifier(String annotationModifier){
      if(StringUtils.contains(annotationModifier, OPEN_PARENTHESES)) {
         String[] tokens = StringUtils.split(annotationModifier, OPEN_PARENTHESES);
         this.name = tokens[0];
         if (tokens.length > 1) {
            Set<JavaAnnotationModifierVariable> var = new HashSet<>();
            String[] varTokens = (tokens[1].replace(CLOSE_PARENTHESES, ' ').trim()).split(Character.toString(COMMA));
            for (String s : varTokens) {
               var.add(new JavaAnnotationModifierVariable(s));
            }

            this.variables = var;
         }
      }else{
         this.name = annotationModifier;
      }
   }

   @Override
   public String toString() {
      StringBuilder result = new StringBuilder();
      result.append(name);
      if(variables != null && variables.size()>0){
         Iterator<JavaAnnotationModifierVariable> iterator = variables.iterator();
         JavaAnnotationModifierVariable amv = iterator.next();
         result.append(OPEN_PARENTHESES).append(amv.name).append(EQUAL).append(amv.value);
         while (iterator.hasNext()){
            amv = iterator.next();
            result.append(COMMA).append(SPACE).append(amv.name).append(EQUAL).append(amv.value);
         }
         result.append(CLOSE_PARENTHESES);
      }
      return result.toString();
   }
}
