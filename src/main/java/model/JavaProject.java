package model;

import java.util.Set;
import java.util.function.Function;

import static resource.Cons.*;


public class JavaProject {
   private String path;
   private Set<JavaFile> javaFiles;

   public JavaProject(String path, Function<String, Set<JavaFile>> generateJavaFiles) {
      this.path = path;
      this.javaFiles = generateJavaFiles.apply(path);
   }

   public String getPath() {
      return path;
   }

   public Set<JavaFile> getJavaFiles() {
      return javaFiles;
   }

   @Override
   public String toString() {
      StringBuilder result = new StringBuilder();
      result.append(JP).append(COLON).append(SPACE).append(path).append(SEMI_COLON).append(LF).append(LF);
      for(JavaFile jf : javaFiles) {
         result.append("-------------------------------------------------").append(LF);
         result.append(jf.toString());
      }
      return result.toString();
   }
}
