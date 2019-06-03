package parser.visit;

import model.*;
import model.context.JavaDeclaration;
import org.apache.commons.lang3.StringUtils;
import parser.Java8Parser;


import java.util.HashSet;
import java.util.Set;

public class JavaFileVisitor extends Java8BaseVisitor<JavaFile> {
   private String path;
   private final Set<String> imports = new HashSet<>();
   private final Set<JavaDeclaration> declarations = new HashSet<>();

   public JavaFileVisitor(String path){
      this.path = path;
   }

   @Override
   public JavaFile visitPackageDeclaration(Java8Parser.PackageDeclarationContext ctx) {
      return new JavaFile.Builder(path).pack(ctx.packageName().getText()).build();
   }

   @Override
   public JavaFile visitImportDeclaration(Java8Parser.ImportDeclarationContext ctx) {
      if(ctx != null) {
         if (ctx.getChild(0) instanceof Java8Parser.SingleStaticImportDeclarationContext) {
            StringBuilder sb = new StringBuilder("static ");
            for(int i = 2, l=ctx.getChild(0).getChildCount(); i<l; i++) sb.append(ctx.getChild(0).getChild(i).getText());
            imports.add(sb.toString());
         } else imports.add(StringUtils.remove(ctx.getText(), "import"));
      }
      return new JavaFile.Builder(path).imports(imports).build();
   }

   @Override
   public JavaFile visitTypeDeclaration(Java8Parser.TypeDeclarationContext ctx) {
      if(ctx != null) declarations.addAll(new JavaDeclarationVisitor().visit(ctx));
      return new JavaFile.Builder(path).declarations(declarations).build();
   }

   /*@Override
   public Set<JavaFile> visitNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
      final Set<Class> result = new HashSet<>();
      final Set<String> implement = new HashSet<>();
      Integer kind = null;
      String name = "", extend = "";
      if(ctx != null){
         int i = 0, s = ctx.getChildCount();
         while(ctx.getChild(i) instanceof Java8Parser.ClassModifierContext){
            modifiers.add(Modifiers.getIndex(ctx.getChild(i).getText()));
            i++;
         }
         if(ClassKind.isClassKind(ctx.getChild(i).getText())){
            kind = ClassKind.getIndex(ctx.getChild(i).getText());
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
               if(!interfaceList.getChild(j).getText().equals(COMMA)){
                  implement.add(interfaceList.getChild(j).getText());
               }
            }
         }
         if(ctx.classBody() != null){
            Set<Method> temp = new MethodSignatureVisitor().visit(ctx.classBody());
            if(temp != null && temp.size()>0) methods.addAll(temp);
            temp = new ConstructorSignatureVisitor().visit(ctx.classBody());
            if(temp != null && temp.size()>0) constructors.addAll(temp);
         }
         Class cla = new Class.Builder(path, name, kind).imports(imports).pack(pack).extend(extend).implement(implement)
                           .methods(methods).constructors(constructors).build();
         if(ctx.classBody() != null){
            Set<Class> temp = new InnerClassVisitor(path).visit(ctx.classBody());
            if(temp != null && temp.size()>0) innerClasses.addAll(temp);
            cla = new Class.Builder(cla).innerClasses(innerClasses).build();
         }
         result.add(cla);
      }
      return result;
   }*/

   @Override
   protected JavaFile aggregateResult(JavaFile aggregate, JavaFile nextResult) {
      if(aggregate == null){
         return nextResult;
      }
      if(nextResult == null){
         return aggregate;
      }
      JavaFile.Builder result = new JavaFile.Builder(aggregate);
      if(nextResult.getDeclarations() != null) result.declarations(nextResult.getDeclarations());
      if(nextResult.getImports() != null) result.imports(nextResult.getImports());
      if(nextResult.getPack() != null) result.pack(nextResult.getPack());
      return result.build();
   }


}
