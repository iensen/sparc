package sorts;

import java.util.HashSet;

public class SortUtils {
	 public static String generateNewSortName(HashSet<String> usedSortNames) {
	    	final String prefix = "s";
	    	Integer suffix = 0;
	    	while(usedSortNames.contains(prefix+suffix)) {
	    		++ suffix;
	    	}
	    	usedSortNames.add(prefix+suffix);
	    	return prefix+suffix;        	
	    }
}
