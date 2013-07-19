package warnings;

import java.util.ArrayList;
import java.util.HashSet;

public class StringListUtils {

	public static String getSeparatedList(ArrayList<String> list,String separator) {
		StringBuilder commaSepList = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i != 0)
				commaSepList.append(separator);
			commaSepList.append(list.get(i).toString());
		}
		return commaSepList.toString();
	}
	

	public static String getSeparatedList(HashSet<String> list,String separator) {
         return getSeparatedList(new ArrayList<String>(list),separator);
	}
	
	/**
	 * Split term into record name and its arguments
	 * @param term string representing the term
	 * @return split result or null of the term is not a record
	 */
	public static Pair<String,ArrayList<String>> splitTerm(String term) {
		if(term.indexOf('(')==-1)
			return null;
		String recordName=term.substring(0,term.indexOf('('));
		ArrayList<String> arguments=new ArrayList<String>();
		String argumentString=term.substring(term.indexOf('('),term.length()-1);
		int parCount=0;
		int lastBeginIndex=0;
		for(int i=0;i<argumentString.length();i++) {
			if(argumentString.charAt(i)=='(')
				parCount++;
			if(argumentString.charAt(i)==')')
				parCount--;
			if(argumentString.charAt(i)==',' && parCount==0) {
				arguments.add(argumentString.substring(lastBeginIndex, i));
				lastBeginIndex=i+1;
			}
		}
		return new Pair<String,ArrayList<String>>(recordName,arguments);
	}
}
