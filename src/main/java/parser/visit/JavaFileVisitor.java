package parser.visit;

import model.*;
import model.declaration.JavaDeclaration;
import org.apache.commons.lang3.StringUtils;
import parser.Java8Parser;
import resource.Cons;


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
            for(int i = 2, l=ctx.getChild(0).getChildCount(); i<l; i++)
               sb.append(ctx.getChild(0).getChild(i).getText());
            imports.add(sb.toString());
         } else imports.add(StringUtils.remove(ctx.getText(), Cons.IMPORT));
      }
      return new JavaFile.Builder(path).imports(imports).build();
   }

   @Override
   public JavaFile visitTypeDeclaration(Java8Parser.TypeDeclarationContext ctx) {
      if(ctx != null) declarations.addAll(new JavaDeclarationVisitor().visit(ctx));
      return new JavaFile.Builder(path).declarations(declarations).build();
   }

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
