package hyperheuristics;

import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum FiniteValues {
   OBJECT(1), BOOLEAN(2), BYTE(3), SHORT(4), INT(5), LONG(6), FLOAT(7), DOUBLE(8), CHAR(9), STRING(10),
   BOOLEAN_WRAPPER(11), BYTE_WRAPPER(12), SHORT_WRAPPER(13), INTEGER(14), LONG_WRAPPER(15), FLOAT_WRAPPER(16),
   DOUBLE_WRAPPER(17), CHARACTER(18);

   private int id;

   FiniteValues(int id){
      this.id = id;
   }

   public Map<Integer, Object> Parameters(){
      switch (id){
         case 1: return Parameters.OBJECT_MAP;
         case 2: return Parameters.BOOLEAN_MAP;
         case 3: return Parameters.BYTE_MAP;
         case 4: return Parameters.SHORT_MAP;
         case 5: return Parameters.INT_MAP;
         case 6: return Parameters.LONG_MAP;
         case 7: return Parameters.FLOAT_MAP;
         case 8: return Parameters.DOUBLE_MAP;
         case 9: return Parameters.CHAR_MAP;
         case 10: return Parameters.STRING_MAP;
         case 11: return Parameters.BOOLEAN_WRAPPER_MAP;
         case 12: return Parameters.BYTE_WRAPPER_MAP;
         case 13: return Parameters.SHORT_WRAPPER_MAP;
         case 14: return Parameters.INTEGER_MAP;
         case 15: return Parameters.LONG_WRAPPER_MAP;
         case 16: return Parameters.FLOAT_WRAPPER_MAP;
         case 17: return Parameters.DOUBLE_WRAPPER_MAP;
         case 18: return Parameters.CHARACTER_MAP;
         default: throw new RuntimeException("Finite values for type id="+id+" not found.");
      }
   }

   @Override
   public String toString() {
      switch (id) {
         case 2: return "boolean";
         case 3: return "byte";
         case 4: return "short";
         case 5: return "int";
         case 6: return "long";
         case 7: return "float";
         case 8: return "double";
         case 9: return "char";
         case 10: return "String";
         case 11: return "Boolean";
         case 12: return "Byte";
         case 13: return "Short";
         case 14: return "Integer";
         case 15: return "Long";
         case 16: return "Float";
         case 17: return "Double";
         case 18: return "Character";
         default: return "Object";
      }
   }

   public static Map<Integer, Object> getParameter(String value) {
      switch (value) {
         case "boolean": return Parameters.BOOLEAN_MAP;
         case "byte": return Parameters.BYTE_MAP;
         case "short": return Parameters.SHORT_MAP;
         case "int": return Parameters.INT_MAP;
         case "long": return Parameters.LONG_MAP;
         case "float": return Parameters.FLOAT_MAP;
         case "double": return Parameters.DOUBLE_MAP;
         case "char": return Parameters.CHAR_MAP;
         case "String": return Parameters.STRING_MAP;
         case "Boolean": return Parameters.BOOLEAN_WRAPPER_MAP;
         case "Byte": return Parameters.BYTE_WRAPPER_MAP;
         case "Short": return Parameters.SHORT_WRAPPER_MAP;
         case "Integer": return Parameters.INTEGER_MAP;
         case "Long": return Parameters.LONG_WRAPPER_MAP;
         case "Float": return Parameters.FLOAT_WRAPPER_MAP;
         case "Double": return Parameters.DOUBLE_WRAPPER_MAP;
         case "Character": return Parameters.CHARACTER_MAP;
         default: return Parameters.OBJECT_MAP;
      }
   }

   private static class Parameters{
      private static final Map<Integer, Object> BOOLEAN_MAP = createMap(new Object[]{true, false});
      private static final Map<Integer, Object> BOOLEAN_WRAPPER_MAP = new HashMap<Integer, Object>(){{
         putAll(BOOLEAN_MAP);
         put(3, null);
      }};
      private static final Map<Integer, Object> BYTE_MAP = new HashMap<Integer, Object>(){{
         put(1, Byte.MIN_VALUE);
         put(2, Byte.parseByte("0"));
         put(3, Byte.MAX_VALUE);
      }};
      private static final Map<Integer, Object> BYTE_WRAPPER_MAP = new HashMap<Integer, Object>(){{
         putAll(BYTE_MAP);
         put(4, null);
      }};
      private static final Map<Integer, Object> SHORT_MAP = new HashMap<Integer, Object>(){{
         put(1, Short.MIN_VALUE);
         put(2, Short.parseShort("0"));
         put(3, Short.MAX_VALUE);
      }};
      private static final Map<Integer, Object> SHORT_WRAPPER_MAP = new HashMap<Integer, Object>(){{
         putAll(SHORT_MAP);
         put(4, null);
      }};
      private static final Map<Integer, Object> INT_MAP = new HashMap<Integer, Object>(){{
         put(1, Integer.MIN_VALUE);
         put(2, Integer.parseInt("0"));
         put(3, Integer.MAX_VALUE);
      }};
      private static final Map<Integer, Object> INTEGER_MAP = new HashMap<Integer, Object>(){{
         putAll(INT_MAP);
         put(4, null);
      }};
      private static final Map<Integer, Object> LONG_MAP = new HashMap<Integer, Object>(){{
         put(1, Integer.MIN_VALUE);
         put(2, Long.parseLong("0"));
         put(3, Integer.MAX_VALUE);
      }};
      private static final Map<Integer, Object> LONG_WRAPPER_MAP = new HashMap<Integer, Object>(){{
         putAll(LONG_MAP);
         put(4, null);
      }};
      private static final Map<Integer, Object> FLOAT_MAP = new HashMap<Integer, Object>(){{
         put(1, Float.MIN_VALUE);
         put(2, Float.parseFloat("0"));
         put(3, Float.MAX_VALUE);
      }};
      private static final Map<Integer, Object> FLOAT_WRAPPER_MAP = new HashMap<Integer, Object>(){{
         putAll(FLOAT_MAP);
         put(4, null);
      }};
      private static final Map<Integer, Object> DOUBLE_MAP = new HashMap<Integer, Object>(){{
         put(1, Double.MIN_VALUE);
         put(2, Double.parseDouble("0"));
         put(3, Double.MAX_VALUE);
      }};
      private static final Map<Integer, Object> DOUBLE_WRAPPER_MAP = new HashMap<Integer, Object>(){{
         putAll(DOUBLE_MAP);
         put(4,null);
      }};
      private static final Map<Integer, Object> CHAR_MAP = new HashMap<Integer, Object>(){{
         put(1, Character.MIN_VALUE);
         put(2, null);
         put(3, Character.MAX_VALUE);
      }};
      private static final Map<Integer, Object> CHARACTER_MAP = new HashMap<Integer, Object>(){{
         putAll(CHAR_MAP);
         put(4, null);
      }};
      private static final Map<Integer, Object> STRING_MAP = new HashMap<Integer, Object>(){{
         put(1, "");
         put(2, null);
      }};
      private static final Map<Integer, Object> OBJECT_MAP = new HashMap<Integer, Object>(){{
         put(1, null);
      }};
      private static Map<Integer, Object> createMap(Object[] values) {
         Map<Integer, Object> result = new HashMap<Integer, Object>();
         int i = 1;
         for(Object obj : values) {
            result.put(i, obj);
            i++;
         }
         return Collections.unmodifiableMap(result);
      }
   }
}
