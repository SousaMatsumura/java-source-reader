package parser.visit;

import model.declaration.method.variable.JavaAnnotationModifier;
import model.JavaModifier;
import model.declaration.method.variable.JavaVariable;
import model.declaration.JavaDeclaration;
import model.declaration.method.JavaMethod;
import parser.Java8Parser;

import java.util.HashSet;
import java.util.Set;

import static resource.Cons.*;

public class JavaMethodVisitor extends Java8BaseVisitor<JavaMethod> {

   @Override
   public JavaMethod visitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
      JavaMethod result = null;
      final Set<JavaDeclaration> innerDeclarations = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<JavaVariable> parameters = new HashSet<>();
      final Set<String> exceptions = new HashSet<>();

      String name, returns;

      if(ctx != null){
         int i = 0, s;
         while(ctx.getChild(i) instanceof Java8Parser.MethodModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
            else modifiers.add(JavaModifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         returns = ctx.methodHeader().getChild(0).getText();
         name = ctx.methodHeader().methodDeclarator().getChild(0).getText();

         if(ctx.methodHeader().methodDeclarator().formalParameterList() != null) {
            if (ctx.methodHeader().methodDeclarator().formalParameterList().formalParameters() != null) {
               for (Java8Parser.FormalParameterContext param :
                     ctx.methodHeader().methodDeclarator().formalParameterList().formalParameters().formalParameter()) {
                  parameters.add(new JavaParameterVisitor().visit(param));
               }
            }

            if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter() !=
                  null) {
               parameters.add(new JavaParameterVisitor().visit(ctx.methodHeader().methodDeclarator()
                  .formalParameterList().lastFormalParameter().formalParameter()));
            }else{
               parameters.add(new JavaParameterVisitor().visit(ctx.methodHeader().methodDeclarator()
                  .formalParameterList().lastFormalParameter()));
            }
         }

         if(ctx.methodHeader().throws_() != null && ctx.methodHeader().throws_().exceptionTypeList() != null) {
            for (i = 0, s = ctx.methodHeader().throws_().exceptionTypeList().getChildCount(); i < s; i++) {
               if (ctx.methodHeader().throws_().exceptionTypeList().getChild(i).getText().charAt(0) != COMMA) {
                  exceptions.add(ctx.methodHeader().throws_().exceptionTypeList().getChild(i).getText());
               }
            }
         }

         if(ctx.methodBody() != null){
            Set<JavaDeclaration> temp = new JavaDeclarationVisitor().visit(ctx.methodBody());
            if(temp != null && temp.size()>0) innerDeclarations.addAll(temp);
         }

         result = new JavaMethod.Builder(name, returns).innerDeclarations(innerDeclarations)
            .annotationModifiers(annotationModifiers).modifiers(modifiers).exceptions(exceptions).parameters(parameters)
            .build();
      }

      return result;
   }

   @Override
   public JavaMethod visitInterfaceMethodDeclaration(Java8Parser.InterfaceMethodDeclarationContext ctx) {
      JavaMethod result = null;
      final Set<JavaDeclaration> innerDeclarations = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<JavaVariable> parameters = new HashSet<>();
      final Set<String> exceptions = new HashSet<>();

      String name, returns;

      if(ctx != null){
         int i = 0, s;
         while(ctx.getChild(i) instanceof Java8Parser.MethodModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext) {
               annotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
            }else modifiers.add(JavaModifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         returns = ctx.methodHeader().getChild(0).getText();
         name = ctx.methodHeader().methodDeclarator().getChild(0).getText();

         if(ctx.methodHeader().methodDeclarator().formalParameterList() != null) {
            if (ctx.methodHeader().methodDeclarator().formalParameterList().formalParameters() != null) {
               for (Java8Parser.FormalParameterContext param :
                     ctx.methodHeader().methodDeclarator().formalParameterList().formalParameters().formalParameter()) {
                  parameters.add(new JavaParameterVisitor().visit(param));
               }
            }

            if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter() !=
                  null) {
               parameters.add(new JavaParameterVisitor().visit(ctx.methodHeader().methodDeclarator()
                  .formalParameterList().lastFormalParameter().formalParameter()));
            }else{
               parameters.add(new JavaParameterVisitor().visit(ctx.methodHeader().methodDeclarator()
                  .formalParameterList().lastFormalParameter()));
            }
         }

         if(ctx.methodHeader().throws_() != null && ctx.methodHeader().throws_().exceptionTypeList() != null) {
            for (i = 0, s = ctx.methodHeader().throws_().exceptionTypeList().getChildCount(); i < s; i++) {
               if (ctx.methodHeader().throws_().exceptionTypeList().getChild(i).getText().charAt(0) != COMMA) {
                  exceptions.add(ctx.methodHeader().throws_().exceptionTypeList().getChild(i).getText());
               }
            }
         }

         if(ctx.methodBody() != null){
            Set<JavaDeclaration> temp = new JavaDeclarationVisitor().visit(ctx.methodBody());
            if(temp != null && temp.size()>0) innerDeclarations.addAll(temp);
         }

         result = new JavaMethod.Builder(name, returns).innerDeclarations(innerDeclarations)
            .annotationModifiers(annotationModifiers).modifiers(modifiers).exceptions(exceptions).parameters(parameters)
            .build();
      }
      return result;
   }


}
