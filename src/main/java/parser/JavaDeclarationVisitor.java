package parser;

import model.Modifier;
import model.Variable;
import model.context.JavaDeclaration;
import model.context.declaration.JavaAnnotation;
import model.context.declaration.JavaClass;
import model.context.declaration.JavaEnum;
import model.context.declaration.JavaInterface;
import model.context.method.JavaConstructor;
import model.context.method.JavaMethod;
import org.apache.commons.lang3.StringUtils;
import resource.Cons;
import resource.DeclarationType;

import java.util.HashSet;
import java.util.Set;

import static resource.Cons.*;

public class JavaDeclarationVisitor extends Java8BaseVisitor<Set<JavaDeclaration>>{

   @Override
   public Set<JavaDeclaration> visitNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      final Set<JavaDeclaration> declarations = new HashSet<>();
      final Set<String> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<String> implementations = new HashSet<>();

      String name = "", extend = "";
      if(ctx != null){
         int i = 0, s = ctx.getChildCount();
         while(ctx.getChild(i) instanceof Java8Parser.ClassModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(ctx.getChild(i).getText());
            else modifiers.add(Modifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         if(ctx.getChild(i).getText().equals(DeclarationType.CLASS.toString())){
            name = ctx.getChild(i+1).getText();
            i+=2;
         }
         if(ctx.getChild(i) instanceof Java8Parser.SuperclassContext){
            extend = ctx.getChild(i).getChild(1).getText();
            i++;
         }
         if (ctx.getChild(i) instanceof Java8Parser.SuperinterfacesContext){
            Java8Parser.InterfaceTypeListContext interfaceList = (Java8Parser.InterfaceTypeListContext) ctx.getChild(i).getChild(1);
            for(int j = 0, l = interfaceList.getChildCount(); j<l; j++){
               if(interfaceList.getChild(j).getText().charAt(0) != COMMA){
                  implementations.add(interfaceList.getChild(j).getText());
               }
            }
         }
         if(ctx.classBody() != null){
            Set<JavaDeclaration> temp = new JavaDeclarationVisitor().visit(ctx.classBody());
            if(temp != null && temp.size()>0) declarations.addAll(temp);
         }
         JavaClass javaClass = new JavaClass.Builder(name).extend(extend).implementations(implementations)
            .innerDeclarations(declarations).annotationModifiers(annotationModifiers).modifiers(modifiers).build();
         result.add(javaClass);
      }
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitAnnotationTypeDeclaration(Java8Parser.AnnotationTypeDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      final Set<JavaDeclaration> declarations = new HashSet<>();
      final Set<String> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();

      String name = "";
      if(ctx != null){
         int i = 0, s = ctx.getChildCount();
         while(ctx.getChild(i) instanceof Java8Parser.InterfaceModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(ctx.getChild(i).getText());
            else modifiers.add(Modifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         name = StringUtils.split((StringUtils.splitByWholeSeparator(ctx.getText(),
            DeclarationType.INTERFACE.toString())[1]), OPEN_BRACE)[0];
         if(ctx.annotationTypeBody() != null){
            Set<JavaDeclaration> temp = new JavaDeclarationVisitor().visit(ctx.annotationTypeBody());
            if(temp != null && temp.size()>0) declarations.addAll(temp);
         }
         JavaAnnotation javaAnnotation = new JavaAnnotation.Builder(name).innerDeclarations(declarations)
            .annotationModifiers(annotationModifiers).modifiers(modifiers).build();
         result.add(javaAnnotation);
      }
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitEnumDeclaration(Java8Parser.EnumDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      final Set<JavaDeclaration> declarations = new HashSet<>();
      final Set<String> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<String> implementations = new HashSet<>();
      final Set<String> labels = new HashSet<>();

      String name = "";
      if(ctx != null){
         int i = 0;
         while(ctx.getChild(i) instanceof Java8Parser.ClassModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(ctx.getChild(i).getText());
            else modifiers.add(Modifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         if(ctx.getChild(i).getText().equals(DeclarationType.ENUM.toString())){
            name = ctx.getChild(i+1).getText();
            i+=2;
         }
         if (ctx.getChild(i) instanceof Java8Parser.SuperinterfacesContext){
            Java8Parser.InterfaceTypeListContext interfaceList = (Java8Parser.InterfaceTypeListContext) ctx.getChild(i).getChild(1);
            for(int j = 0, l = interfaceList.getChildCount(); j<l; j++){
               if(!interfaceList.getChild(j).getText().equals(COMMA)){
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
         JavaEnum javaEnum = new JavaEnum.Builder(name, labels).implementations(implementations)
            .innerDeclarations(declarations).annotationModifiers(annotationModifiers).modifiers(modifiers).build();
         result.add(javaEnum);
      }
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitNormalInterfaceDeclaration(Java8Parser.NormalInterfaceDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      final Set<JavaDeclaration> declarations = new HashSet<>();
      final Set<String> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<String> extents = new HashSet<>();


      String name = "";
      if(ctx != null){
         int i = 0, s = ctx.getChildCount();
         while(ctx.getChild(i) instanceof Java8Parser.InterfaceModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(ctx.getChild(i).getText());
            else modifiers.add(Modifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         if(ctx.getChild(i).getText().equals(DeclarationType.INTERFACE.toString())){
            name = ctx.getChild(i+1).getText();
            i+=2;
         }
         if (ctx.getChild(i) instanceof Java8Parser.ExtendsInterfacesContext){
            Java8Parser.InterfaceTypeListContext interfaceList = (Java8Parser.InterfaceTypeListContext) ctx.getChild(i).getChild(1);
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
         JavaInterface javaInterface = new JavaInterface.Builder(name).innerDeclarations(declarations)
            .extents(extents).annotationModifiers(annotationModifiers).modifiers(modifiers).build();
         result.add(javaInterface);
      }
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitConstructorDeclaration(Java8Parser.ConstructorDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      final Set<JavaDeclaration> innerDeclarations = new HashSet<>();
      final Set<String> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<Variable> parameters = new HashSet<>();
      final Set<String> exceptions = new HashSet<>();

      String name = "";

      if(ctx != null){
         int i = 0, s = ctx.getChildCount();
         while(ctx.getChild(i) instanceof Java8Parser.ConstructorModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(ctx.getChild(i).getText());
            else modifiers.add(Modifier.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         name = ctx.getChild(i).getChild(0).getChild(0).getText();
         if(ctx.constructorDeclarator().formalParameterList() != null) {
            if (ctx.constructorDeclarator().formalParameterList().formalParameters() != null) {
               for (Java8Parser.FormalParameterContext param : ctx.constructorDeclarator().formalParameterList().formalParameters().formalParameter()) {
                  String paramName = null, dataKind = null;
                  Set<Integer> paramModifiers = new HashSet<>();
                  for(i = 0, s = param.getChildCount(); i<s; i++) {
                     if (param.getChild(i) instanceof Java8Parser.UnannTypeContext) {
                        dataKind = param.getChild(i).getText();
                     }else if(param.getChild(i) instanceof Java8Parser.VariableModifierContext){
                        paramModifiers.add(Modifier.getIndex(param.getChild(i).getText()));
                     }else if(param.getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                        paramName = param.getChild(i).getText();
                     }
                  }
                  parameters.add(new Variable.Builder(dataKind, paramName).modifiers(paramModifiers).build());
               }
            }

            if (ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter() != null) {
               String paramName = null, dataKind = null;
               Set<Integer> paramModifiers = new HashSet<>();
               for(i = 0; i<ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChildCount(); i++) {
                  if (ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind = ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText();
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext){
                     paramModifiers.add(Modifier.getIndex(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText()));
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.constructorDeclarator().formalParameterList().lastFormalParameter().formalParameter().getChild(i).getText();
                  }
               }
               parameters.add(new Variable.Builder(dataKind, paramName).modifiers(paramModifiers).build());
            }else{
               String paramName = null, dataKind = null;
               Set<Integer> paramModifiers = new HashSet<>();
               for(i = 0; i<ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChildCount(); i++) {
                  if (ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind = ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext){
                     paramModifiers.add(Modifier.getIndex(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText().equals(ELLIPSIS)){
                     dataKind += ELLIPSIS;
                  }
               }
               parameters.add(new Variable.Builder(dataKind, paramName).modifiers(paramModifiers).build());
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

         JavaConstructor javaConstructor = new JavaConstructor.Builder(name).innerDeclarations(innerDeclarations)
            .annotationModifiers(annotationModifiers).modifiers(modifiers).exceptions(exceptions).parameters(parameters).build();
         result.add(javaConstructor);
      }
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      if(ctx != null) result.add(new JavaMethod.Builder("Method!", "returns!").build());
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitAnnotationTypeElementDeclaration(Java8Parser.AnnotationTypeElementDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      if(ctx != null) result.add(new JavaMethod.Builder("Annotation Method!", "returns!").build());
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitInterfaceMethodDeclaration(Java8Parser.InterfaceMethodDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      if(ctx != null) result.add(new JavaMethod.Builder("Interface Method is a Method!", "returns!").build());
      return result;
   }

   @Override
   protected Set<JavaDeclaration> aggregateResult(Set<JavaDeclaration> aggregate, Set<JavaDeclaration> nextResult) {
      if(aggregate == null){
         return nextResult;
      }
      if(nextResult == null){
         return aggregate;
      }
      final Set<JavaDeclaration> result = new HashSet<>();
      result.addAll(aggregate);
      result.addAll(nextResult);
      return result;
   }


}
