package model;

import model.context.JavaDeclaration;
import model.context.declaration.*;
import resource.Cons;

import java.util.Set;

import static resource.Cons.*;
import static resource.Cons.JF;

public class JavaFile {
   private String path;
   private String pack;
   private Set<String> imports;
   private Set<JavaDeclaration> declarations;

   private JavaFile(String path, String pack, Set<String> imports, Set<JavaDeclaration> declarations) {
      this.path = path;
      this.pack = pack;
      this.imports = imports;
      this.declarations = declarations;
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == null) return false;
      if(obj == this) return true;
      if(obj.getClass() != this.getClass()) return false;
      JavaFile jf = (JavaFile) obj;
      return path != null && path.equals(jf.path);
   }

   @Override
   public int hashCode() {
      return path!=null ? path.hashCode() : 1;
   }


   public String getPath() {
      return path;
   }

   public String getPack() {
      return pack;
   }

   public Set<String> getImports() {
      return imports;
   }

   public Set<JavaDeclaration> getDeclarations() {
      return declarations;
   }

   public static class Builder{
      private String path;
      private String pack;
      private Set<String> imports;
      private Set<JavaDeclaration> declarations;

      public Builder(String path){
         this.path = path;
      }
      public Builder(JavaFile javaFile){
         this.path = javaFile.getPath();
         this.declarations = javaFile.getDeclarations();
         this.imports = javaFile.getImports();
         this.pack = javaFile.getPack();
      }
      public Builder declarations(Set<JavaDeclaration> declarations){
         this.declarations = declarations;
         return this;
      }
      public Builder pack(String pack){
         this.pack = pack;
         return this;
      }
      public Builder imports(Set<String> imports){
         this.imports = imports;
         return this;
      }
      public JavaFile build(){
         return new JavaFile(path, pack, imports, declarations);
      }
   }

   @Override
   public String toString() {
      return toStringByDepth(1);
   }

   private String toStringByDepth(int depth){
      StringBuilder result = new StringBuilder();
      for(int count = 0; count < depth; count++) result.append(INDENT);
      result.append(JF).append(COLON).append(SPACE).append(path).append(SEMI_COLON).append(LF).append(LF);
      if(path != null) {
         for(int count = 0; count < depth; count++) result.append(INDENT);
         result.append(PACK).append(SPACE).append(pack).append(SEMI_COLON).append(LF).append(LF);
      }
      if(imports != null && imports.size()>0) {
         for (String imp : imports) {
            for (int count = 0; count < depth; count++) result.append(INDENT);
            result.append(IMPORT).append(SPACE).append(imp).append(LF);
         }
         result.append(LF);
      }
      if(declarations != null && declarations.size() > 0){
         for(JavaDeclaration jd : declarations){
            if(jd instanceof JavaClass){
               JavaClass jc = (JavaClass) jd;
               result.append(jd.toStringByDepth(depth+1));
            }
            else if(jd instanceof JavaEnum){
               JavaEnum je = (JavaEnum) jd;
               result.append(je.toStringByDepth(depth+1));
            }
            else if(jd instanceof JavaInterface){
               JavaInterface ji = (JavaInterface) jd;
               result.append(ji.toStringByDepth(depth+1));
            }
            else if(jd instanceof JavaAnnotation){
               JavaAnnotation ja = (JavaAnnotation) jd;
               result.append(ja.toStringByDepth(depth+1));
            }
            result.append(LF).append(LF);
         }
      }
      result.append("-------------------------------------------------");
      return result.toString();
   }
}