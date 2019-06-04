package parser.visit;

import model.JavaAnnotationModifier;
import model.JavaModifier;
import model.JavaVariable;
import model.declaration.JavaClass;
import model.declaration.JavaDeclaration;
import model.declaration.method.JavaMethod;
import parser.Java8Parser;
import resource.Cons;
import resource.DeclarationType;

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

      String name = EMPTY, returns = EMPTY;

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
               for (Java8Parser.FormalParameterContext param : ctx.methodHeader().methodDeclarator().formalParameterList().formalParameters().formalParameter()) {
                  String paramName = null, dataKind = null;
                  Set<Integer> paramModifiers = new HashSet<>();
                  Set<JavaAnnotationModifier> paramAnnotationModifiers = new HashSet<>();
                  for(i = 0, s = param.getChildCount(); i<s; i++) {
                     if (param.getChild(i) instanceof Java8Parser.UnannTypeContext) {
                        dataKind = param.getChild(i).getText();
                     }else if(param.getChild(i) instanceof Java8Parser.VariableModifierContext){
                        if(param.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                           paramAnnotationModifiers.add(new JavaAnnotationModifier(param.getChild(i).getText()));
                        else paramModifiers.add(JavaModifier.getIndex(param.getChild(i).getText()));
                     }else if(param.getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                        paramName = param.getChild(i).getText();
                     }
                  }
                  parameters.add(new JavaVariable.Builder(dataKind, paramName).modifiers(paramModifiers)
                                       .annotationModifiers(paramAnnotationModifiers).build());
               }
            }

            if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter() != null) {
               String paramName = null, dataKind = null;
               Set<Integer> paramModifiers = new HashSet<>();
               Set<JavaAnnotationModifier> paramAnnotationModifiers = new HashSet<>();
               for(i = 0; i<ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChildCount(); i++) {
                  if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind = ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText();
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext){
                     if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                        paramAnnotationModifiers.add(new JavaAnnotationModifier(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText()));
                     else
                        paramModifiers.add(JavaModifier.getIndex(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText()));
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText();
                  }
               }
               parameters.add(new JavaVariable.Builder(dataKind, paramName).annotationModifiers(paramAnnotationModifiers)
                  .modifiers(paramModifiers).build());
            }else{
               String paramName = null;
               StringBuilder dataKind = new StringBuilder(Cons.EMPTY);
               Set<Integer> paramModifiers = new HashSet<>();
               Set<JavaAnnotationModifier> paramAnnotationModifiers = new HashSet<>();
               for(i = 0; i<ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChildCount(); i++) {
                  if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind.append(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText());
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext){
                     if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                        paramAnnotationModifiers.add(new JavaAnnotationModifier(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                     else
                        paramModifiers.add(JavaModifier.getIndex(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText().equals(ELLIPSIS)){
                     dataKind.append(ELLIPSIS);
                  }
               }
               parameters.add(new JavaVariable.Builder(dataKind.toString(), paramName)
                  .annotationModifiers(paramAnnotationModifiers).modifiers(paramModifiers).build());
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
               for (Java8Parser.FormalParameterContext param : ctx.methodHeader().methodDeclarator().formalParameterList().formalParameters().formalParameter()) {
                  String paramName = null, dataKind = null;
                  Set<Integer> paramModifiers = new HashSet<>();
                  Set<JavaAnnotationModifier> javaAnnotationModifiers = new HashSet<>();
                  for(i = 0, s = param.getChildCount(); i<s; i++)
                     if (param.getChild(i) instanceof Java8Parser.UnannTypeContext) {
                        dataKind = param.getChild(i).getText();
                     } else if (param.getChild(i) instanceof Java8Parser.VariableModifierContext) {
                        if (param.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext) {
                           javaAnnotationModifiers.add(new JavaAnnotationModifier(param.getChild(i).getText()));
                        } else
                           paramModifiers.add(JavaModifier.getIndex(param.getChild(i).getText()));
                     } else if (param.getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext) {
                        paramName = param.getChild(i).getText();
                     }
                  parameters.add(new JavaVariable.Builder(dataKind, paramName).annotationModifiers(javaAnnotationModifiers)
                                       .modifiers(paramModifiers).build());
               }
            }

            if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter() != null) {
               String paramName = null, dataKind = null;
               Set<Integer> paramModifiers = new HashSet<>();
               Set<JavaAnnotationModifier> javaAnnotationModifiers = new HashSet<>();
               for(i = 0; i<ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChildCount(); i++) {
                  if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind = ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText();
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext) {
                     if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext){
                        javaAnnotationModifiers.add(new JavaAnnotationModifier(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText()));
                     }else paramModifiers.add(JavaModifier.getIndex(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText()));
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText();
                  }
               }
               parameters.add(new JavaVariable.Builder(dataKind, paramName).annotationModifiers(javaAnnotationModifiers)
                                    .modifiers(paramModifiers).build());
            }else{
               String paramName = null;
               StringBuilder dataKind = new StringBuilder(EMPTY);
               Set<Integer> paramModifiers = new HashSet<>();
               Set<JavaAnnotationModifier> javaAnnotationModifiers = new HashSet<>();
               for(i = 0; i<ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChildCount(); i++) {
                  if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind.append(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText());
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext){
                     if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                        javaAnnotationModifiers.add(new JavaAnnotationModifier(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                     else paramModifiers.add(JavaModifier.getIndex(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText().equals(ELLIPSIS)){
                     dataKind.append(ELLIPSIS);
                  }
               }
               parameters.add(new JavaVariable.Builder(dataKind.toString(), paramName).annotationModifiers(javaAnnotationModifiers)
                                    .modifiers(paramModifiers).build());
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
