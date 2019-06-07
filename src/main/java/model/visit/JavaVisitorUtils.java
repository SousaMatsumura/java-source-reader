package model.visit;

import model.declaration.JavaDeclaration;
import model.declaration.NormalJavaDeclaration;
import model.declaration.method.JavaConstructor;
import model.declaration.method.JavaMethod;
import model.declaration.method.NormalJavaMethod;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public enum JavaVisitorUtils {
   ALL_NORMAL_METHODS(1), ALL_METHODS(2), ALL_CONSTRUCTORS(3);
   private int id;
   JavaVisitorUtils(int id){ this.id = id;}

   public JavaVisitor visit(){
      switch (id){
         case 1: return VISIT_ALL_NORMAL_METHODS;
         case 2: return VISIT_ALL_METHODS;
         case 3: return VISIT_ALL_CONSTRUCTORS;
         default: throw new RuntimeException("Visitor with id="+id+" not found.");
      }
   }

   private static final JavaVisitor<Set<NormalJavaMethod>> VISIT_ALL_NORMAL_METHODS = new JavaVisitor<Set<NormalJavaMethod>>(){
      @Override
      public Set<NormalJavaMethod> visit(JavaDeclaration declaration) {
         HashSet<NormalJavaMethod> result = new HashSet<>();
         if(declaration instanceof NormalJavaMethod){
            result.add((NormalJavaMethod) declaration);
         }
         if(declaration instanceof NormalJavaDeclaration || declaration instanceof NormalJavaMethod){
            Iterator<JavaDeclaration> itr;
            if(declaration instanceof NormalJavaDeclaration)
               itr = ((NormalJavaDeclaration) declaration).getInnerDeclarations().iterator();
            else itr = ((NormalJavaMethod) declaration).getInnerDeclarations().iterator();
            while(itr.hasNext()){
               JavaDeclaration jd = itr.next();
               if (jd instanceof NormalJavaDeclaration || jd instanceof NormalJavaMethod) {
                  result.addAll(visit(jd));
               }
            }
         }
         return result;
      }
   };

   private static final JavaVisitor<Set<JavaMethod>> VISIT_ALL_METHODS= new JavaVisitor<Set<JavaMethod>>(){
      @Override
      public Set<JavaMethod> visit(JavaDeclaration declaration) {
         HashSet<JavaMethod> result = new HashSet<>();
         if(declaration instanceof JavaMethod){
            result.add((JavaMethod) declaration);
         }
         if(declaration instanceof NormalJavaDeclaration || declaration instanceof NormalJavaMethod){
            Iterator<JavaDeclaration> itr;
            if(declaration instanceof NormalJavaDeclaration)
               itr = ((NormalJavaDeclaration) declaration).getInnerDeclarations().iterator();
            else itr = ((NormalJavaMethod) declaration).getInnerDeclarations().iterator();
            while(itr.hasNext()){
               JavaDeclaration jd = itr.next();
               if (jd instanceof NormalJavaDeclaration || jd instanceof NormalJavaMethod) {
                  result.addAll(visit(jd));
               }
            }
         }
         return result;
      }
   };

   private static final JavaVisitor<Set<JavaConstructor>> VISIT_ALL_CONSTRUCTORS= new JavaVisitor<Set<JavaConstructor>>(){
      @Override
      public Set<JavaConstructor> visit(JavaDeclaration declaration) {
         HashSet<JavaConstructor> result = new HashSet<>();
         if(declaration instanceof JavaConstructor){
            result.add((JavaConstructor) declaration);
         }
         if(declaration instanceof NormalJavaDeclaration || declaration instanceof NormalJavaMethod){
            Iterator<JavaDeclaration> itr;
            if(declaration instanceof NormalJavaDeclaration)
               itr = ((NormalJavaDeclaration) declaration).getInnerDeclarations().iterator();
            else itr = ((NormalJavaMethod) declaration).getInnerDeclarations().iterator();
            while(itr.hasNext()){
               JavaDeclaration jd = itr.next();
               if (jd instanceof NormalJavaDeclaration || jd instanceof NormalJavaMethod) {
                  result.addAll(visit(jd));
               }
            }
         }
         return result;
      }
   };

}
