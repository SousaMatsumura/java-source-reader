package parser.visit;

import model.JavaAnnotationModifier;
import model.JavaModifier;
import model.declaration.JavaClass;
import model.declaration.JavaDeclaration;
import model.declaration.method.JavaAnnotationMethod;
import parser.Java8Parser;
import resource.DeclarationType;

import java.util.HashSet;
import java.util.Set;

import static resource.Cons.COMMA;
import static resource.Cons.EMPTY;

public class JavaAnnotatioMethodVisitor extends Java8BaseVisitor<JavaAnnotationMethod> {

   @Override
   public JavaAnnotationMethod visitAnnotationTypeElementDeclaration(Java8Parser.AnnotationTypeElementDeclarationContext ctx) {
      JavaAnnotationMethod result = null;
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();

      String name, returns, defaultValue = EMPTY;

      if(ctx != null){
         int i = 0;
         while(ctx.getChild(i) instanceof Java8Parser.AnnotationTypeElementModifierContext){
            annotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
            i++;
         }
         returns = ctx.getChild(i).getText();
         name = ctx.getChild(i+1).getText();

         if(ctx.defaultValue() != null) defaultValue = ctx.defaultValue().getChild(1).getText();

         result = new JavaAnnotationMethod.Builder(name, returns)
            .annotationModifiers(annotationModifiers).defaultValue(defaultValue).build();
      }
      return result;
   }
}
