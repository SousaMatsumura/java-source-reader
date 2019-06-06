package resource;

import model.JavaFile;
import model.JavaProject;
import model.declaration.JavaDeclaration;
import model.declaration.method.JavaMethod;
import resource.functional.ImplFunction;

import java.util.Iterator;

public class Main {
   public static void main(String[] args) {
      //JavaProject project = new JavaProject("C:\\Users\\GabrieldeSousaMatsum\\Desktop\\projects\\br.inpe.autotest\\src\\test\\teste.java", ImplFunction.getGetJavaFiles());
      JavaProject project = new JavaProject("C:\\Users\\GabrieldeSousaMatsum\\Documents\\Java Projects\\FreeERPApp\\src\\main", ImplFunction.getGetJavaFiles());
      Iterator<JavaFile> itrJavaFille = project.getJavaFiles().iterator();
      Iterator<JavaDeclaration> itrHighDeclaration = itrJavaFille.next().getDeclarations().iterator();
      Iterator<JavaDeclaration> itrLowDeclaration;
      System.out.println(itrHighDeclaration.next().toString());
      /*do jd = itrHighDeclaration.next();
      while(!(jd instanceof JavaMethod) && itrHighDeclaration.hasNext());
      System.out.println(jd.toString());*/

      /*JavaMethod jm = (JavaMethod) jd;
      System.out.println(jm.toString());
      jm.getParameters().iterator();*/
      /*for(JavaFile jf : project.getJavaFiles()) {
         for(JavaDeclaration jd : jf.getDeclarations()) {
            if(jd instanceof JavaAnnotation) System.out.println(jd.toString());
         }
      }*/
   }
}
