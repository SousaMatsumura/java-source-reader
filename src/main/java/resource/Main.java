package resource;

import model.JavaProject;
import resource.functional.ImplFunction;

public class Main {
   public static void main(String[] args) {
      //JavaProject project = new JavaProject("C:\\Users\\GabrieldeSousaMatsum\\Desktop\\projects\\br.inpe.autotest\\src\\test\\teste.java", ImplFunction.getGetJavaFiles());
      JavaProject project = new JavaProject("C:\\Users\\GabrieldeSousaMatsum\\Documents\\Java Projects\\FreeERPApp\\src\\main", ImplFunction.getGetJavaFiles());
      System.out.println(project.toString());
      /*for(JavaFile jf : project.getJavaFiles()) {
         for(JavaDeclaration jd : jf.getDeclarations()) {
            if(jd instanceof JavaAnnotation) System.out.println(jd.toString());
         }
      }*/
   }
}
