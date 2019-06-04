package parser.visit;

import model.JavaAnnotationModifier;
import model.JavaModifier;
import model.JavaVariable;
import model.declaration.JavaDeclaration;
import model.declaration.method.JavaConstructor;
import parser.Java8Parser;

import java.util.HashSet;
import java.util.Set;

import static resource.Cons.*;

public class JavaConstructorVisitor extends Java8BaseVisitor<JavaConstructor> {

   @Override
   public JavaConstructor visitConstructorDeclaration(Java8Parser.ConstructorDeclarationContext ctx) {
      JavaConstructor result = null;
      final Set<JavaDeclaration> innerDeclarations = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<JavaVariable> parameters = new HashSet<>();
      final Set<String> exceptions = new HashSet<>();

      String name;

      if(ctx != null){
         int i = 0, s;
         while(ctx.getChild(i) instanceof Java8Parser.ConstructorModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
            else modifiers.add(JavaModifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         name = ctx.getChild(i).getChild(0).getChild(0).getText();
         if(ctx.constructorDeclarator().formalParameterList() != null) {
            if (ctx.constructorDeclarator().formalParameterList().formalParameters() != null) {
               for (Java8Parser.FormalParameterContext param : ctx.constructorDeclarator().formalParameterList().formalParameters().formalParameter()) {
                  parameters.add(new JavaParameterVisitor().visit(param));
               }
            }

            if (ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter() != null) {
               parameters.add(new JavaParameterVisitor().visit(ctx.constructorDeclarator().formalParameterList()
                  .lastFormalParameter().formalParameter()));
            }else{
               parameters.add(new JavaParameterVisitor().visit(ctx.constructorDeclarator().formalParameterList()
                  .lastFormalParameter()));
            }
         }

         if(ctx.throws_() != null && ctx.throws_().exceptionTypeList() != null) {
            for (i = 0, s = ctx.throws_().exceptionTypeList().getChildCount(); i < s; i++) {
               if (ctx.throws_().exceptionTypeList().getChild(i).getText().charAt(0) != COMMA) {
                  exceptions.add(ctx.throws_().exceptionTypeList().getChild(i).getText());
               }
            }
         }

         if(ctx.constructorBody() != null){
            Set<JavaDeclaration> temp = new JavaDeclarationVisitor().visit(ctx.constructorBody());
            if(temp != null && temp.size()>0) innerDeclarations.addAll(temp);
         }

         result = new JavaConstructor.Builder(name).innerDeclarations(innerDeclarations).annotationModifiers(
            annotationModifiers).modifiers(modifiers).exceptions(exceptions).parameters(parameters).build();
      }
      return result;
   }
}
