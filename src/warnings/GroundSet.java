package warnings;

import java.util.HashSet;




public abstract class GroundSet extends Operand{
   GroundSetType type;
   public abstract boolean containsElement(String element);
   public abstract boolean isNumeric();
   public abstract boolean hasNumber();
   protected boolean isNumber(String s) {
	   for(char c:s.toCharArray()) {
		   if(!Character.isDigit(c))
			   return false;
	   }
	   return true;
    }
   
   public abstract HashSet<String> getElements();
}
