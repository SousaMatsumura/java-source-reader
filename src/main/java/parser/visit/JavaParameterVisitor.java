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

public class JavaParameterVisitor extends Java8BaseVisitor<JavaVariable> {

   @Override
   public JavaVariable visitFormalParameter(Java8Parser.FormalParameterContext ctx) {
      JavaVariable result = null;
      if (ctx != null) {
         String paramName = null, dataKind = null;
         Set<Integer> paramModifiers = new HashSet<>();
         Set<JavaAnnotationModifier> javaAnnotationModifiers = new HashSet<>();
         for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof Java8Parser.UnannTypeContext) {
               dataKind = ctx.getChild(i).getText();
            } else if (ctx.getChild(i) instanceof Java8Parser.VariableModifierContext) {
               if (ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                  javaAnnotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
               else paramModifiers.add(JavaModifier.getIndex(ctx.getChild(i).getText()));
            } else if (ctx.getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext) {
               paramName = ctx.getChild(i).getText();
            }
         }
         result = new JavaVariable.Builder(dataKind, paramName).annotationModifiers(javaAnnotationModifiers)
                        .modifiers(paramModifiers).build();
      }
      return result;
   }

   @Override
   public JavaVariable visitLastFormalParameter(Java8Parser.LastFormalParameterContext ctx) {
      JavaVariable result = null;
      if(ctx != null) {
         String paramName = null;
         StringBuilder dataKind = new StringBuilder(EMPTY);
         Set<Integer> paramModifiers = new HashSet<>();
         Set<JavaAnnotationModifier> javaAnnotationModifiers = new HashSet<>();
         for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof Java8Parser.UnannTypeContext) {
               dataKind.append(ctx.getChild(i).getText());
            } else if (ctx.getChild(i) instanceof Java8Parser.VariableModifierContext) {
               if (ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                  javaAnnotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
               else paramModifiers.add(JavaModifier.getIndex(ctx.getChild(i).getText()));
            } else if (ctx.getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext) {
               paramName = ctx.getChild(i).getText();
            } else if (ctx.getChild(i).getText().equals(ELLIPSIS)) {
               dataKind.append(ELLIPSIS);
            }
         }
         result =  new JavaVariable.Builder(dataKind.toString(), paramName).annotationModifiers(javaAnnotationModifiers)
            .modifiers(paramModifiers).build();
      }
      return result;
   }
}
