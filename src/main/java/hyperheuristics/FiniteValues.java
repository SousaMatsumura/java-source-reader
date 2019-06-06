package hyperheuristics;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public enum FiniteValues {
   BOOLEAN(1), BYTE(2), SHORT(3), INTEGER(4), LONG(5), FLOAT(6), DOUBLE(7), CHAR(8), STRING(9), FILE(10), OBJECT(11);
   private int id;

   FiniteValues(int id){
      this.id = id;
   }

   public Map<Integer, Object> Parameters(){
      switch (id){
         case 1: return Parameters.BOOLEAN_MAP;
         case 2: return Parameters.BYTE_MAP;
         case 3: return Parameters.SHORT_MAP;
         case 4: return Parameters.INTEGER_MAP;
         case 5: return Parameters.LONG_MAP;
         case 6: return Parameters.FLOAT_MAP;
         case 7: return Parameters.DOUBLE_MAP;
         case 8: return Parameters.CHAR_MAP;
         case 9: return Parameters.STRING_MAP;
         case 10: return Parameters.FILE_MAP;
         case 11: return Parameters.OBJECT_MAP;
         default: throw new RuntimeException("Finite values for type id="+id+" not found.");
      }
   }

   private static class Parameters{
      private static final Map<Integer, Object> BOOLEAN_MAP = new HashMap<Integer, Object>(){{
         put(1, false);
         put(2, true);
      }};
      private static final Map<Integer, Object> BYTE_MAP = new HashMap<Integer, Object>(){{
         put(1, Byte.MIN_VALUE);
         put(2, Byte.parseByte("0"));
         put(3, Byte.MAX_VALUE);
      }};
      private static final Map<Integer, Object> SHORT_MAP = new HashMap<Integer, Object>(){{
         put(1, Short.MIN_VALUE);
         put(2, Short.parseShort("0"));
         put(3, Short.MAX_VALUE);
      }};
      private static final Map<Integer, Object> INTEGER_MAP = new HashMap<Integer, Object>(){{
         put(1, Integer.MIN_VALUE);
         put(2, Integer.parseInt("0"));
         put(3, Integer.MAX_VALUE);
      }};
      private static final Map<Integer, Object> LONG_MAP = new HashMap<Integer, Object>(){{
         put(1, Integer.MIN_VALUE);
         put(2, Long.parseLong("0"));
         put(3, Integer.MAX_VALUE);
      }};
      private static final Map<Integer, Object> FLOAT_MAP = new HashMap<Integer, Object>(){{
         put(1, Float.MIN_VALUE);
         put(2, Float.parseFloat("0"));
         put(3, Float.MAX_VALUE);
      }};
      private static final Map<Integer, Object> DOUBLE_MAP = new HashMap<Integer, Object>(){{
         put(1, Double.MIN_VALUE);
         put(2, Double.parseDouble("0"));
         put(3, Double.MAX_VALUE);
      }};
      private static final Map<Integer, Object> CHAR_MAP = new HashMap<Integer, Object>(){{
         put(1, Character.MIN_VALUE);
         put(2, null);
         put(3, Character.MAX_VALUE);
      }};
      private static final Map<Integer, Object> STRING_MAP = new HashMap<Integer, Object>(){{
         put(1, "");
         put(2, null);
      }};
      private static final Map<Integer, Object> FILE_MAP = new HashMap<Integer, Object>(){{
         put(1, new File(""));
         put(2, null);
      }};
      private static final Map<Integer, Object> OBJECT_MAP = new HashMap<Integer, Object>(){{
         put(1, null);
      }};
   }
}
