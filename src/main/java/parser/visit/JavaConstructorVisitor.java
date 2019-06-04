package parser.visit;

import model.JavaAnnotationModifier;
import model.JavaModifier;
import model.JavaVariable;
import model.declaration.JavaClass;
import model.declaration.JavaDeclaration;
import model.declaration.method.JavaConstructor;
import parser.Java8Parser;
import resource.DeclarationType;

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
                  String paramName = null, dataKind = null;
                  Set<Integer> paramModifiers = new HashSet<>();
                  Set<JavaAnnotationModifier> javaAnnotationModifiers = new HashSet<>();
                  for(i = 0, s = param.getChildCount(); i<s; i++) {
                     if (param.getChild(i) instanceof Java8Parser.UnannTypeContext) {
                        dataKind = param.getChild(i).getText();
                     }else if(param.getChild(i) instanceof Java8Parser.VariableModifierContext){
                        if(param.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                           javaAnnotationModifiers.add(new JavaAnnotationModifier(param.getChild(i).getText()));
                        else paramModifiers.add(JavaModifier.getIndex(param.getChild(i).getText()));
                     }else if(param.getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                        paramName = param.getChild(i).getText();
                     }
                  }
                  parameters.add(new JavaVariable.Builder(dataKind, paramName)
                     .annotationModifiers(javaAnnotationModifiers).modifiers(paramModifiers).build());
               }
            }

            if (ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter() != null) {
               String paramName = null, dataKind = null;
               Set<Integer> paramModifiers = new HashSet<>();
               Set<JavaAnnotationModifier> javaAnnotationModifiers = new HashSet<>();
               for(i = 0; i<ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChildCount(); i++) {
                  if (ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind = ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText();
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext){
                     if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                        javaAnnotationModifiers.add(new JavaAnnotationModifier(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText()));
                     else paramModifiers.add(JavaModifier.getIndex(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText()));
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText();
                  }
               }
               parameters.add(new JavaVariable.Builder(dataKind, paramName).annotationModifiers(javaAnnotationModifiers)
                                    .modifiers(paramModifiers).build());
            }else{
               String paramName = null;
               StringBuilder dataKind = new StringBuilder(EMPTY);
               Set<Integer> paramModifiers = new HashSet<>();
               Set<JavaAnnotationModifier> javaAnnotationModifiers = new HashSet<>();
               for(i = 0; i<ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChildCount(); i++) {
                  if (ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind.append(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText());
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext){
                     if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                        javaAnnotationModifiers.add(new JavaAnnotationModifier(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                     else paramModifiers.add(JavaModifier.getIndex(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText().equals(ELLIPSIS)){
                     dataKind.append(ELLIPSIS);
                  }
               }
               parameters.add(new JavaVariable.Builder(dataKind.toString(), paramName).annotationModifiers(javaAnnotationModifiers)
                                    .modifiers(paramModifiers).build());
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
