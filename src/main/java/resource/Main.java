package resource;

import model.JavaProject;
import resource.functional.ImplFunction;

public class Main {
   public static void main(String[] args) {
      JavaProject project = new JavaProject("C:\\Users\\GabrieldeSousaMatsum\\Desktop\\projects\\br.inpe.autotest\\src\\test\\teste.java", ImplFunction.getGetJavaFiles());
      System.out.println(project.toString());
   }
}
