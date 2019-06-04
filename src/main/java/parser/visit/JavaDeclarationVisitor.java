package parser.visit;

import model.declaration.JavaDeclaration;
import parser.Java8Parser;

import java.util.HashSet;
import java.util.Set;

public class JavaDeclarationVisitor extends Java8BaseVisitor<Set<JavaDeclaration>> {

   @Override
   public Set<JavaDeclaration> visitNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      result.add(new JavaClassVisitor().visit(ctx));
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitAnnotationTypeDeclaration(Java8Parser.AnnotationTypeDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      result.add(new JavaAnnotationVisitor().visit(ctx));
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitEnumDeclaration(Java8Parser.EnumDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      result.add(new JavaEnumVisitor().visit(ctx));
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitNormalInterfaceDeclaration(Java8Parser.NormalInterfaceDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      result.add(new JavaInterfaceVisitor().visit(ctx));
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitConstructorDeclaration(Java8Parser.ConstructorDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      result.add(new JavaConstructorVisitor().visit(ctx));
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      result.add(new JavaMethodVisitor().visit(ctx));
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitAnnotationTypeElementDeclaration(Java8Parser.AnnotationTypeElementDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      result.add(new JavaAnnotatioMethodVisitor().visit(ctx));
      return result;
   }

   @Override
   public Set<JavaDeclaration> visitInterfaceMethodDeclaration(Java8Parser.InterfaceMethodDeclarationContext ctx) {
      final Set<JavaDeclaration> result = new HashSet<>();
      result.add(new JavaMethodVisitor().visit(ctx));
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
