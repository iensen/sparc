package warnings;

import java.util.HashSet;

public class NumericRangeGroundSet extends GroundSet {

	long lowest,highest;
	@Override
	public boolean containsElement(String element) {
	   if(!isNumber(element))
		   return false;
	   long longElement=Long.parseLong(element);
	   return longElement>=lowest && longElement<=highest;
	}

	@Override
	public boolean isNumeric() {
		return true;
	}

	@Override
	public HashSet<String> getElements() {
		// we should never ever call this
		HashSet<String> elements=new HashSet<String>();
		for(long n=lowest;n<=highest;n++) {
			elements.add(Long.toString(n));
		}
		return elements;
	}
	
	public NumericRangeGroundSet(long lowest,long highest) {
        this.type=GroundSetType.NumericRange;
		this.lowest=lowest;
		this.highest=highest;
	}

	@Override
	public String toString() {
	   return lowest+".."+highest;
	}

	@Override
	public boolean hasNumber() {
		return true;
	}
}
