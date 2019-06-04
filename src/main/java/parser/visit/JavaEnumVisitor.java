package parser.visit;

import model.JavaAnnotationModifier;
import model.JavaModifier;
import model.declaration.JavaDeclaration;
import model.declaration.JavaEnum;
import parser.Java8Parser;
import resource.DeclarationType;

import java.util.HashSet;
import java.util.Set;

import static resource.Cons.COMMA;
import static resource.Cons.EMPTY;

public class JavaEnumVisitor extends Java8BaseVisitor<JavaEnum> {

   @Override
   public JavaEnum visitEnumDeclaration(Java8Parser.EnumDeclarationContext ctx) {
      JavaEnum result = null;
      final Set<JavaDeclaration> declarations = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<String> implementations = new HashSet<>();
      final Set<String> labels = new HashSet<>();

      String name = EMPTY;
      if(ctx != null){
         int i = 0;
         while(ctx.getChild(i) instanceof Java8Parser.ClassModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
            else modifiers.add(JavaModifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         if(ctx.getChild(i).getText().equals(DeclarationType.ENUM.toString())){
            name = ctx.getChild(i+1).getText();
            i+=2;
         }
         if (ctx.getChild(i) instanceof Java8Parser.SuperinterfacesContext){
            Java8Parser.InterfaceTypeListContext interfaceList = (Java8Parser.InterfaceTypeListContext) ctx.getChild(i).getChild(1);
            for(int j = 0, l = interfaceList.getChildCount(); j<l; j++){
               if(!interfaceList.getChild(j).getText().equals(Character.toString(COMMA))){
                  implementations.add(interfaceList.getChild(j).getText());
               }
            }
         }
         if(ctx.enumBody() != null){
            int l = ctx.enumBody().enumConstantList().getChildCount();
            if(ctx.enumBody().enumConstantList() != null && l>0){
               for(int j = 0; j < l; j++){
                  if(ctx.enumBody().enumConstantList().getChild(j).getText().charAt(0) != COMMA){
                     labels.add(ctx.enumBody().enumConstantList().getChild(j).getText());
                  }
               }
            }
            Set<JavaDeclaration> temp = new JavaDeclarationVisitor().visit(ctx.enumBody());
            if(temp != null && temp.size()>0) declarations.addAll(temp);
         }
         result = new JavaEnum.Builder(name, labels).implementations(implementations)
            .innerDeclarations(declarations).annotationModifiers(annotationModifiers).modifiers(modifiers).build();
      }
      return result;
   }
}
