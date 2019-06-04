package parser.visit;

import model.JavaAnnotationModifier;
import model.JavaModifier;
import model.declaration.JavaDeclaration;
import model.declaration.JavaAnnotation;
import org.apache.commons.lang3.StringUtils;
import parser.Java8Parser;
import resource.DeclarationType;

import java.util.HashSet;
import java.util.Set;

import static resource.Cons.EMPTY;
import static resource.Cons.OPEN_BRACE;

public class JavaAnnotationVisitor extends Java8BaseVisitor<JavaAnnotation> {

   @Override
   public JavaAnnotation visitAnnotationTypeDeclaration(Java8Parser.AnnotationTypeDeclarationContext ctx) {
      JavaAnnotation result = null;
      final Set<JavaDeclaration> declarations = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();

      String name;
      if(ctx != null){
         int i = 0;
         while(ctx.getChild(i) instanceof Java8Parser.InterfaceModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(new JavaAnnotationModifier( ctx.getChild(i).getText()));
            else modifiers.add(JavaModifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         name = StringUtils.split((StringUtils.splitByWholeSeparator(ctx.getText(),
               DeclarationType.INTERFACE.toString())[1]), OPEN_BRACE)[0];
         if(ctx.annotationTypeBody() != null){
            Set<JavaDeclaration> temp = new JavaDeclarationVisitor().visit(ctx.annotationTypeBody());
            if(temp != null && temp.size()>0) declarations.addAll(temp);
         }
         result = new JavaAnnotation.Builder(name).innerDeclarations(declarations)
            .annotationModifiers(annotationModifiers).modifiers(modifiers).build();
      }
      return result;
   }
}
