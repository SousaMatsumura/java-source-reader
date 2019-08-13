package resource.functional;

import hyperheuristics.FiniteValues;
import model.JavaFile;
import model.JavaProject;
import model.declaration.JavaDeclaration;
import model.declaration.NormalJavaDeclaration;
import model.declaration.method.NormalJavaMethod;
import model.declaration.method.variable.JavaVariable;
import model.visit.JavaVisitorUtils;
import org.omg.CORBA.portable.IDLEntity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Consumer;
import static resource.Cons.*;

public class ImplConsumer {
   private ImplConsumer(){}
   private static final Consumer<JavaProject> SHOW_ENCODE = new Consumer<JavaProject>(){
      @Override
      public void accept(JavaProject project) {
         Iterator<JavaFile> javaFileIterator = project.getJavaFiles().iterator();
         StringBuilder result = new StringBuilder();
         result.append(JP).append(COLON).append(SPACE).append(project.getPath()).append(LF).append(LF);
         result.append(SECTION_SEPARATOR).append(LF);

         while(javaFileIterator.hasNext()){
            JavaFile fileTemp = javaFileIterator.next();
            result.append(JF).append(COLON).append(SPACE).append(fileTemp.getPath());
            Iterator<JavaDeclaration> javaDeclarationIterator = fileTemp.getDeclarations().iterator();
            while (javaDeclarationIterator.hasNext()){
               JavaDeclaration javaDeclarationTemp = javaDeclarationIterator.next();
               result.append(LF).append(INDENT).append(javaDeclarationTemp).append(LF);



            }
         }

         System.out.println(result);
      }
   };

   private String handleDeclarationDeep(JavaDeclaration javaDeclaration, int deep){
      StringBuilder result = new StringBuilder("");

      if(javaDeclaration instanceof NormalJavaDeclaration){
         NormalJavaDeclaration normalJavaDeclaration = (NormalJavaDeclaration) javaDeclaration;
         for(int i = 0; i<deep; i++) result.append(INDENT);
            result.append(normalJavaDeclaration.getName());
         if(normalJavaDeclaration.getInnerDeclarations() != null){
            Iterator normalJavaDeclarationIterator = normalJavaDeclaration
                  .getInnerDeclarations().iterator();
            while (normalJavaDeclarationIterator.hasNext()) ;
         }

      }

   }


   /*HashSet<NormalJavaMethod> methods = (HashSet<NormalJavaMethod>) JavaVisitorUtils
                     .ALL_NORMAL_METHODS.visit().visit(javaDeclarationTemp);
   for(NormalJavaMethod njm : methods){

      int i = 0;
      System.out.println(njm.getMethodSignature());
      for (JavaVariable jv : njm.getParameters()){
         System.out.println(i);
         System.out.println(INDENT+jv.toString()+": "+ FiniteValues.getParameter(jv.getDataKind()));
         i++;
      }
   }*/

   /*while(itrJavaFille.hasNext()) {
      Iterator<JavaDeclaration> itrHighDeclaration = itrJavaFille.next().getDeclarations().iterator();

      JavaDeclaration temp = itrHighDeclaration.next();
      if (itrHighDeclaration.hasNext()) temp = itrHighDeclaration.next();

      //System.out.println(temp.toString());

      System.out.println("--------------------------------");

      HashSet<NormalJavaMethod> methods =
            (HashSet<NormalJavaMethod>) JavaVisitorUtils.ALL_NORMAL_METHODS.visit().visit(temp);
      for(NormalJavaMethod njm : methods){
         int i = 0;
         System.out.println(njm.getMethodSignature());
         for (JavaVariable jv : njm.getParameters()){
            System.out.println(i);
            System.out.println("   "+jv.toString()+": "+ FiniteValues.getParameter(jv.getDataKind()));
            i++;
         }
      }

   }*/

}