package sorts;

import java.util.ArrayList;

import parser.ASTandCondition;
import parser.ASTcondition;
import parser.ASTorCondition;
import parser.ASTunaryCondition;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;

/**
 * Condition parser
 * 
 */
enum TermType {
	integer, identifier, functionalSymbol;
}

 enum Relation {
	EQUAL("="), GREATEROREQUAL(">="), GREATER(">"), SMALLER("<"), SMALLEROREQUAL(
			"<="), NOTEQUAL("!=");
	private String image;

	Relation(String image) {
		this.image = image;
	}
    
	public String toString() {
		return image;
	}
}


public class Condition {
	/**
	 * Constructor, parses condition into class fields
	 * 
	 * @param conditionString
	 *            string to parse
	 */

	public Condition() {
	
		

	}

	/**
	 * Check condition on arguments
	 * 
	 * @param cond
	 *            AST node specifying the condition
	 * @param arguments
	 * @return true if the condition is satisfied and false otherwise
	 */
	public boolean check(ASTcondition cond, ArrayList<String> arguments) {
		return checkCondition((ASTorCondition) cond.jjtGetChild(0), arguments);
	}

	/**
	 * Check condition (disjunction) on arguments
	 * 
	 * @param cond
	 *            AST node specifying the condition
	 * @param arguments
	 * @return true if the condition is satisfied and false otherwise
	 */
	private boolean checkCondition(ASTorCondition orCondition,
			ArrayList<String> arguments) {
		for (int i = 0; i < orCondition.jjtGetNumChildren(); i++) {
			if (checkCondition((ASTandCondition) orCondition.jjtGetChild(i),
					arguments)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check condition (conjunction) on arguments
	 * 
	 * @param cond
	 *            AST node specifying the condition
	 * @param arguments
	 * @return true if the condition is satisfied and false otherwise
	 */
	private boolean checkCondition(ASTandCondition andCondition,
			ArrayList<String> arguments) {
		for (int i = 0; i < andCondition.jjtGetNumChildren(); i++) {
			if (!checkCondition(
					(ASTunaryCondition) andCondition.jjtGetChild(i), arguments)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Retrieve type of the term
	 * 
	 * @param s
	 *            string containint the term
	 */
	private TermType getTermType(String s) {
		if (s.indexOf(')') != -1) {
			return TermType.functionalSymbol;
		} else {
			for (int i = 0; i < s.length(); i++) {
				if (!Character.isDigit(s.charAt(i))) {
					return TermType.identifier;
				}
			}
			return TermType.integer;
		}
	}

	/**
	 * Check condition (relation or general condition) on arguments
	 * 
	 * @param cond
	 *            AST node specifying the condition
	 * @param arguments
	 * @return true if the condition is satisfied and false otherwise
	 */
	public boolean checkCondition(ASTunaryCondition unaryCond,
			ArrayList<String> arguments) {
		if (unaryCond.jjtGetNumChildren() == 1
				&& ((SimpleNode) unaryCond.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTCONDITION) {
			if(unaryCond.image != null && unaryCond.image.trim().equals("not("))
			    return !check((ASTcondition) unaryCond.jjtGetChild(0), arguments);
			else 
				return check((ASTcondition) unaryCond.jjtGetChild(0), arguments);
		}
		String[] relationArray = unaryCond.image.split(" ");

		int arg1 = Integer.parseInt(relationArray[0]);
		int arg2 = Integer.parseInt(relationArray[2]);
		String relationString = relationArray[1];
		Relation relation = null;
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

		String argument1 = arguments.get(arg1);
		String argument2 = arguments.get(arg2);
		TermType type1 = getTermType(argument1);
		TermType type2 = getTermType(argument2);
		if (type1 != type2) {
			if (relation != Relation.NOTEQUAL) {
				return false;
			} else {
				return true;
			}
		}

		else
			switch (type1) {
			case integer:
				return checkIntegerRelation(relation,
						Integer.parseInt(argument1),
						Integer.parseInt(argument2));
			case identifier:
				return checkStringRelation(relation, argument1, argument2);
			case functionalSymbol:
				return checkFunctionalRelation(relation, argument1, argument2);
			default:
				return false;
			}
	}

	private boolean checkFunctionalRelation(Relation relation, String f1,
			String f2) {
		switch (relation) {
		case NOTEQUAL:
			return f1.compareTo(f2) != 0;
		case EQUAL:
			return f1.compareTo(f2) == 0;
		default:
			return false;
		}
	}

	private boolean checkIntegerRelation(Relation relation, int o1, int o2) {
		switch (relation) {
		case SMALLER:
			return o1 < o2;
		case GREATER:
			return o1 > o2;
		case NOTEQUAL:
			return o1 != o2;
		case GREATEROREQUAL:
			return o1 >= o2;
		case SMALLEROREQUAL:
			return o1 <= o2;
		case EQUAL:
			return o1 == o2;
		default:
			return false;
		}
	}

	private boolean checkStringRelation(Relation relation, String s1, String s2) {

		switch (relation) {
		case SMALLER:
			return s1.compareTo(s2) < 0;
		case GREATER:
			return s1.compareTo(s2) > 0;
		case NOTEQUAL:
			return s1.compareTo(s2) != 0;
		case GREATEROREQUAL:
			return s1.compareTo(s2) >= 0;
		case SMALLEROREQUAL:
			return s1.compareTo(s2) <= 0;
		case EQUAL:
			return s1.compareTo(s2) == 0;
		default:
			return false;
		}
	}
}