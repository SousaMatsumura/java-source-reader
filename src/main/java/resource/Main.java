package resource;

import hyperheuristics.FiniteValues;
import model.JavaFile;
import model.JavaProject;
import model.declaration.JavaDeclaration;
import model.declaration.method.JavaConstructor;
import model.declaration.method.JavaMethod;
import model.declaration.method.NormalJavaMethod;
import model.declaration.method.variable.JavaVariable;
import model.visit.JavaVisitorUtils;
import resource.functional.ImplFunction;

import java.util.HashSet;
import java.util.Iterator;

public class Main {
   public static void main(String[] args) {
      //JavaProject project = new JavaProject("C:\\Users\\GabrieldeSousaMatsum\\Desktop\\projects\\br.inpe.autotest\\src\\test\\teste.java", ImplFunction.getGetJavaFiles());
      JavaProject project = new JavaProject("C:\\Users\\Gabriel\\Documents\\case-studies\\src\\main\\java\\SWPDC_original\\src\\main\\java\\swpdc\\MainGerenciarDados.java", ImplFunction.getGetJavaFiles());
      Iterator<JavaFile> itrJavaFille = project.getJavaFiles().iterator();
      while(itrJavaFille.hasNext()) {
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
               System.out.println("   "+jv.toString()+": "+FiniteValues.getParameter(jv.getDataKind()));
               i++;
            }
         }

      }
      System.out.println(FiniteValues.BOOLEAN.Parameters());

   }
}
