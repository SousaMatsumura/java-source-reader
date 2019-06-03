package parser.visit;

import model.JavaAnnotationModifier;
import model.JavaVariable;
import model.JavaModifier;
import model.context.JavaDeclaration;
import model.context.declaration.JavaAnnotation;
import model.context.declaration.JavaClass;
import model.context.declaration.JavaEnum;
import model.context.declaration.JavaInterface;
import model.context.method.JavaAnnotationMethod;
import model.context.method.JavaConstructor;
import model.context.method.JavaMethod;
import org.apache.commons.lang3.StringUtils;
import parser.Java8Parser;
import resource.DeclarationType;

import java.util.HashSet;
import java.util.Set;

import static resource.Cons.*;

public class JavaDeclarationVisitor extends Java8BaseVisitor<Set<JavaDeclaration>> {

   @Override
   public Set<JavaDeclaration> visitNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      final Set<JavaDeclaration> declarations = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<String> implementations = new HashSet<>();

      String name = "", extend = "";
      if(ctx != null){
         int i = 0, s = ctx.getChildCount();
         while(ctx.getChild(i) instanceof Java8Parser.ClassModifierContext){
            if(ctx.getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
               annotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
            else modifiers.add(JavaModifier.getIndex(ctx.getChild(i).getText()));
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
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();

      String name = "";
      if(ctx != null){
         int i = 0, s = ctx.getChildCount();
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
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<String> implementations = new HashSet<>();
      final Set<String> labels = new HashSet<>();

      String name = "";
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
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<String> extents = new HashSet<>();

      String name = "";
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
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<JavaVariable> parameters = new HashSet<>();
      final Set<String> exceptions = new HashSet<>();

      String name = "";

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
               String paramName = null, dataKind = null;
               Set<Integer> paramModifiers = new HashSet<>();
               Set<JavaAnnotationModifier> javaAnnotationModifiers = new HashSet<>();
               for(i = 0; i<ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChildCount(); i++) {
                  if (ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind = ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext){
                     if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                        javaAnnotationModifiers.add(new JavaAnnotationModifier(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                     else paramModifiers.add(JavaModifier.getIndex(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.constructorDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText().equals(ELLIPSIS)){
                     dataKind += ELLIPSIS;
                  }
               }
               parameters.add(new JavaVariable.Builder(dataKind, paramName).annotationModifiers(javaAnnotationModifiers)
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

         JavaConstructor javaConstructor = new JavaConstructor.Builder(name).innerDeclarations(innerDeclarations)
            .annotationModifiers(annotationModifiers).modifiers(modifiers).exceptions(exceptions).parameters(parameters).build();
         result.add(javaConstructor);
      }
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      final Set<JavaDeclaration> innerDeclarations = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<JavaVariable> parameters = new HashSet<>();
      final Set<String> exceptions = new HashSet<>();

      String name = "", returns = "";

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
               String paramName = null, dataKind = null;
               Set<Integer> paramModifiers = new HashSet<>();
               Set<JavaAnnotationModifier> paramAnnotationModifiers = new HashSet<>();
               for(i = 0; i<ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChildCount(); i++) {
                  if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind = ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext){
                     if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                        paramAnnotationModifiers.add(new JavaAnnotationModifier(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                     else
                        paramModifiers.add(JavaModifier.getIndex(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText().equals(ELLIPSIS)){
                     dataKind += ELLIPSIS;
                  }
               }
               parameters.add(new JavaVariable.Builder(dataKind, paramName).annotationModifiers(paramAnnotationModifiers)
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

         JavaMethod javaMethod = new JavaMethod.Builder(name, returns).innerDeclarations(innerDeclarations)
            .annotationModifiers(annotationModifiers).modifiers(modifiers).exceptions(exceptions).parameters(parameters).build();
         result.add(javaMethod);
      }
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitAnnotationTypeElementDeclaration(Java8Parser.AnnotationTypeElementDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();

      String name = "", returns = "", defaultValue = "";

      if(ctx != null){
         int i = 0, s;
         while(ctx.getChild(i) instanceof Java8Parser.AnnotationTypeElementModifierContext){
            annotationModifiers.add(new JavaAnnotationModifier(ctx.getChild(i).getText()));
            i++;
         }
         returns = ctx.getChild(i).getText();
         name = ctx.getChild(i+1).getText();

         if(ctx.defaultValue() != null) defaultValue = ctx.defaultValue().getChild(1).getText();

         JavaAnnotationMethod javaAnnotationMethod = new JavaAnnotationMethod.Builder(name, returns)
            .annotationModifiers(annotationModifiers).defaultValue(defaultValue).build();
         result.add(javaAnnotationMethod);
      }
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitInterfaceMethodDeclaration(Java8Parser.InterfaceMethodDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      final Set<JavaDeclaration> innerDeclarations = new HashSet<>();
      final Set<JavaAnnotationModifier> annotationModifiers = new HashSet<>();
      final Set<Integer> modifiers = new HashSet<>();
      final Set<JavaVariable> parameters = new HashSet<>();
      final Set<String> exceptions = new HashSet<>();

      String name = "", returns = "";

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
               String paramName = null, dataKind = null;
               Set<Integer> paramModifiers = new HashSet<>();
               Set<JavaAnnotationModifier> javaAnnotationModifiers = new HashSet<>();
               for(i = 0; i<ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChildCount(); i++) {
                  if (ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.UnannTypeContext) {
                     dataKind = ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableModifierContext){
                     if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getChild(0) instanceof Java8Parser.AnnotationContext)
                        javaAnnotationModifiers.add(new JavaAnnotationModifier(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                     else paramModifiers.add(JavaModifier.getIndex(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText()));
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i) instanceof Java8Parser.VariableDeclaratorIdContext){
                     paramName = ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText();
                  }else if(ctx.methodHeader().methodDeclarator().formalParameterList().lastFormalParameter().getChild(i).getText().equals(ELLIPSIS)){
                     dataKind += ELLIPSIS;
                  }
               }
               parameters.add(new JavaVariable.Builder(dataKind, paramName).annotationModifiers(javaAnnotationModifiers)
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

         JavaMethod javaMethod = new JavaMethod.Builder(name, returns).innerDeclarations(innerDeclarations)
            .annotationModifiers(annotationModifiers).modifiers(modifiers).exceptions(exceptions).parameters(parameters).build();
         result.add(javaMethod);
      }
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
