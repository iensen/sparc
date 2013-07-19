package warnings;

import java.util.HashSet;

public class MaterializedGroundSet extends GroundSet{
	  HashSet<String> elements;
	   public MaterializedGroundSet(HashSet<String> elements) {
		   this.type=GroundSetType.Materialized;
		   this.elements=elements;
	   }
	   
	   public boolean containsElement(String element) {
		   return elements.contains(element);
	   }

	   
	   public boolean isNumeric() {
		   for(String element:elements) {
			   if(!isNumber(element))
	              return false;
		   }
		   return true;
	   }
	   
	   public boolean hasNumber() {
		   for(String element:elements) {
			   if(isNumber(element))
	              return true;
		   }
		   return false;   
	   }

	@Override
	public HashSet<String> getElements() {
       return elements;
	}

	@Override
	public String toString() {
	   return "{"+StringListUtils.getSeparatedList(elements, ",")+"}";
	}
}
