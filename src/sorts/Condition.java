package sorts;

/**
 * Condition parser
 * 
 */
public class Condition {
	/**
	 * Constructor, parses condition into class fields
	 * 
	 * @param conditionString
	 *            string to parse
	 */
	public Condition(String conditionString) {
		// example conditionString: {&([0])&!=&([1])&}
		// remove parenthesis
		conditionString = conditionString.replace("(", "");
		conditionString = conditionString.replace(")", "");
		int index = 0;
		// find first argument index
		while (conditionString.charAt(index) != '[')
			++index;
		++index;
		StringBuilder firstArgumentStr = new StringBuilder();
		while (conditionString.charAt(index) != ']') {
			firstArgumentStr.append(conditionString.charAt(index));
			++index;
		}
		firstArgument = Integer.parseInt(firstArgumentStr.toString());
		index++;// switch to start of relation
		StringBuilder relationString = new StringBuilder();
		while (conditionString.charAt(index) != '[') {
			relationString.append(conditionString.charAt(index));
			++index;
		}
		// parse relation
		if (relationString.toString().equals("<")) {
			relation = Relation.SMALLER;
		} else if (relationString.toString().equals(">")) {
			relation = Relation.GREATER;
		} else if (relationString.toString().equals("!=")) {
			relation = Relation.NOTEQUAL;
		} else if (relationString.toString().equals(">=")) {
			relation = Relation.GREATEROREQUAL;
		} else if (relationString.toString().equals("<=")) {
			relation = Relation.SMALLEROREQUAL;
		} else if (relationString.toString().equals("=")) {
			relation = Relation.EQUAL;
		}

		index++;
		// find second argument
		StringBuilder secondArgumentStr = new StringBuilder();
		while (conditionString.charAt(index) != ']') {
			secondArgumentStr.append(conditionString.charAt(index));
			++index;
		}
		secondArgument = Integer.parseInt(secondArgumentStr.toString());
	}

	private int firstArgument;

	/**
	 * @return first condition argument
	 */
	public int getFirstArgument() {
		return firstArgument;
	}

	private int secondArgument;

	/**
	 * @return second argument
	 */
	public int getSecondArgument() {
		return secondArgument;
	}

	private Relation relation;

	/**
	 * @return relation
	 */
	public Relation getRelation() {
		return relation;
	}
	public static boolean checkRelation(Relation relation,int o1,int o2) {
		switch(relation) {
		  case SMALLER: return o1<o2;
		  case GREATER: return o1>o2;
		  case NOTEQUAL: return o1!=o2;
		  case GREATEROREQUAL: return o1>=o2;
		  case SMALLEROREQUAL: return o1<=o2;
		  case EQUAL: return o1==o2;
		  default: return false;
		}
	}
	
	public static boolean checkRelation(Relation relation,String s1,String s2) {
		if((s1.indexOf('(')!=-1 || s1.indexOf('(')!=-1) && 
				(relation!=Relation.EQUAL && relation !=Relation.NOTEQUAL)) {
					return false;
				}
		switch(relation) {
		  case SMALLER: return s1.compareTo(s2)<0;
		  case GREATER: return s1.compareTo(s2)>0;
		  case NOTEQUAL: return s1.compareTo(s2)!=0;
		  case GREATEROREQUAL: return s1.compareTo(s2)>=0;
		  case SMALLEROREQUAL: return s1.compareTo(s2)<=0;
		  case EQUAL: return s1.compareTo(s2)==0;
		  default: return false;
		}
	}
	
	public boolean checkInts(int o1,int o2) {
	    return checkRelation(relation,o1,o2);
	}
	
	public boolean checkStrings(String s1,String s2) {
		return checkRelation(relation,s1,s2);
	}
}
