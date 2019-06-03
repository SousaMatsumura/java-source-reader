package resource.functional;

import model.JavaFile;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import parser.Java8Lexer;
import parser.Java8Parser;
import parser.visit.JavaFileVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImplFunction {
   private ImplFunction(){}
   private static final Function<String, Set<JavaFile>> GET_JAVA_FILES = new Function<String, Set<JavaFile>>() {
      @Override
      public Set<JavaFile> apply(String projectPath) {
         Set<JavaFile> javaFiles = new HashSet<>();
         ParseTree parseTree;
         List<String> javaFilesPaths = GET_JAVA_FILES_PATHS.apply(projectPath);
         for(String file : javaFilesPaths){
            CharStream charStream = null;
            try {
               charStream = CharStreams.fromFileName(file);
            } catch (IOException e) {
               e.printStackTrace();
            }
            Java8Lexer java8Lexer = new Java8Lexer(charStream);
            CommonTokenStream commonTokenStream = new CommonTokenStream(java8Lexer);
            Java8Parser java8Parser = new Java8Parser(commonTokenStream);
            parseTree = java8Parser.compilationUnit();
            javaFiles.add(new JavaFileVisitor(file).visit(parseTree));
         }
         return javaFiles;
      }
   };

   private static final Function<String, List<String>> GET_JAVA_FILES_PATHS = new Function<String, List<String>>() {
      @Override
      public List<String> apply(String address) {
         Path p = Paths.get(address);
         List<String> result;
         try(Stream<Path> subPaths = Files.walk(p)){
            result = subPaths.filter(Files::isRegularFile).filter(path -> {
               String s = path.toAbsolutePath().toString();
               return s.contains(".java");
            }).map(Objects::toString).collect(Collectors.toList());
            return result;
         }catch (Exception e){
            e.printStackTrace();
         }
         return null;
      }
   };

   public static Function<String, Set<JavaFile>> getGetJavaFiles() {
      return GET_JAVA_FILES;
   }
}
