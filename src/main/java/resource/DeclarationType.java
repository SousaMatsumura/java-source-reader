package resource;

public enum DeclarationType {
   CLASS(1), INTERFACE(2), ENUM(3), ANNOTATION(4);
   private int value;

   DeclarationType(int value){this.value = value;}

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
