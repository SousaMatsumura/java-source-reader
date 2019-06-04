package parser.visit;

import model.JavaAnnotationModifier;
import model.JavaModifier;
import model.declaration.JavaDeclaration;
import model.declaration.JavaClass;
import parser.Java8Parser;
import resource.DeclarationType;

import java.util.HashSet;
import java.util.Set;

import static resource.Cons.*;

public class JavaClassVisitor extends Java8BaseVisitor<JavaClass> {

   @Override
   public JavaClass visitNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
      JavaClass result = null;
      final Set<JavaDeclaration> declarations = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<String> implementations = new HashSet<>();

      String name = EMPTY, extend = EMPTY;
      if (ctx != null) {
         int i = 0;
         while (ctx.getChild(i) instanceof Java8Parser.ClassModifierContext) {
            if (ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
            else modifiers.add(JavaModifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         if (ctx.getChild(i).getText().equals(DeclarationType.CLASS.toString())) {
            name = ctx.getChild(i + 1).getText();
            i += 2;
         }
         if (ctx.getChild(i) instanceof Java8Parser.SuperclassContext) {
            extend = ctx.getChild(i).getChild(1).getText();
            i++;
         }
         if (ctx.getChild(i) instanceof Java8Parser.SuperinterfacesContext) {
            Java8Parser.InterfaceTypeListContext interfaceList = (Java8Parser.InterfaceTypeListContext) ctx.getChild(i).getChild(1);
            for (int j = 0, l = interfaceList.getChildCount(); j < l; j++) {
               if (interfaceList.getChild(j).getText().charAt(0) != COMMA) {
                  implementations.add(interfaceList.getChild(j).getText());
               }
            }
         }
         if (ctx.classBody() != null) {
            Set<JavaDeclaration> temp = new JavaDeclarationVisitor().visit(ctx.classBody());
            if (temp != null && temp.size() > 0) declarations.addAll(temp);
         }
         result = new JavaClass.Builder(name).extend(extend).implementations(implementations)
            .innerDeclarations(declarations).annotationModifiers(annotationModifiers).modifiers(modifiers).build();
      }
      return result;
   }
}
