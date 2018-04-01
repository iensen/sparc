package warnings;


import java.util.HashSet;

import translating.InstanceGenerator;

public class IdentifierRangeGroundSet extends GroundSet{

	String lowest,highest;
	
	@Override
	public boolean containsElement(String element) {
		return element.length()>=lowest.length() && element.length()<=highest.length()
			&&  element.compareTo(lowest)>=0 && element.compareTo(highest)<=0;
	}

	@Override
	public boolean isNumeric() {
		return false;
	}

	@Override
	public HashSet<String> getElements() {
	  InstanceGenerator gen=new InstanceGenerator(null);
	  return gen.generateStrings(lowest, highest);
	}

	@Override
	public String toString() {
	   return lowest+".."+highest;
	}

	@Override
	public boolean hasNumber() {
		// TODO Auto-generated method stub
		return false;
	}

}
