package parser.visit;

import model.JavaAnnotationModifier;
import model.JavaModifier;
import model.declaration.JavaDeclaration;
import model.declaration.JavaInterface;
import parser.Java8Parser;
import resource.DeclarationType;

import java.util.HashSet;
import java.util.Set;

import static resource.Cons.COMMA;
import static resource.Cons.EMPTY;

public class JavaInterfaceVisitor extends Java8BaseVisitor<JavaInterface> {

   @Override
   public JavaInterface visitNormalInterfaceDeclaration(Java8Parser.NormalInterfaceDeclarationContext ctx) {
      JavaInterface result = null;
      final Set<JavaDeclaration> declarations = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<String> extents = new HashSet<>();

      String name = EMPTY;
      if(ctx != null){
         int i = 0;
         while(ctx.getChild(i) instanceof Java8Parser.InterfaceModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
            else modifiers.add(JavaModifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         if(ctx.getChild(i).getText().equals(DeclarationType.INTERFACE.toString())){
            name = ctx.getChild(i+1).getText();
            i+=2;
         }
         if (ctx.getChild(i) instanceof Java8Parser.ExtendsInterfacesContext){
            Java8Parser.InterfaceTypeListContext interfaceList =
                  (Java8Parser.InterfaceTypeListContext) ctx.getChild(i).getChild(1);
            for(int j = 0, l = interfaceList.getChildCount(); j<l; j++){
               if(interfaceList.getChild(j).getText().charAt(0) != COMMA){
                  extents.add(interfaceList.getChild(j).getText());
               }
            }
         }
         if(ctx.interfaceBody() != null){
            Set<JavaDeclaration> temp = new JavaDeclarationVisitor().visit(ctx.interfaceBody());
            if(temp != null && temp.size()>0) declarations.addAll(temp);
         }
         result = new JavaInterface.Builder(name).innerDeclarations(declarations).extents(extents)
                        .annotationModifiers(annotationModifiers).modifiers(modifiers).build();
      }
      return result;
   }
}
