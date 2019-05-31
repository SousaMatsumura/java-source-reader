package resource;

import org.apache.commons.lang3.ArrayUtils;

public enum DeclarationType {
   CLASS(1), INTERFACE(2), ENUM(3), ANNOTATION(4);
   private int value;

   DeclarationType(int value){this.value = value;}

   public static int getIndex(String value){
      switch(value){
         case "class": return 1;
         case "interface": return 2;
         case "enum": return 3;
         case "@interface": return 4;
         default: return 0;
      }
   }

   public static String getValue(int index){
      switch(index){
         case 1: return "class";
         case 2: return "interface";
         case 3: return "enum";
         case 4: return "@interface";
         default: return null;
      }
   }

   @Override
   public String toString(){
      switch(value){
         case 1: return "class";
         case 2: return "interface";
         case 3: return "enum";
         case 4: return "@interface";
         default: return null;
      }
   }
}
