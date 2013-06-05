package typechecking;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import parser.ASTarithmeticTerm;
import parser.ASTsymbolicTerm;
import parser.ASTterm;
import parser.ASTtermList;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslator;
import parser.SparcTranslatorConstants;
import parser.SparcTranslatorTreeConstants;

/**
 * Class for unification(matching) between terms with variables and ground
 * terms. Used for typechecking purposes
 * 
 */
public class Unifier {
	TypeChecker typechecker;

	public Unifier(TypeChecker tc) {
		this.typechecker = tc;
	}

	/**
	 * Unify(match) term with each of string constants from the list (without
	 * variables)
	 * 
	 * @param term
	 *            term with variables
	 * @param constants
	 *            list of constants
	 * @return true if at least one of the unifications succeeds
	 */
	public boolean unify(ASTterm term, ArrayList<String> constants) {
		boolean result = false;
		// try to unify every constant from the list
		for (String s : constants) {
			if (unify(term, s)) {
				result = true; // result is true, if unification succeeds
				break;
			}
		}
		return result;
	}

	/**
	 * Unify(match) term with variables with given string s, representing ground
	 * term
	 * 
	 * @param termWithVariables
	 * @param s
	 *            , string, representing constant ground term
	 * @return true if there is a substitution for variables from
	 *         termWithVariables which makes the term equal to s.
	 */
	private boolean unify(ASTterm termWithVariables, String s) {
		ASTterm groundTerm = createTerm(s);
		groundTerm.toString();
		return unify(termWithVariables, groundTerm);
	}

	/**
	 * Unify(match) term with variables with given term without variables,
	 * 
	 * @param termWithVariables
	 * @param groundterm
	 * @return true if there exists a substitution for term with variables which
	 *         makes it equivalent to ground term.
	 */
	private boolean unify(ASTterm termWithVariables, ASTterm groundTerm) {
		// TODO Auto-generated method stub
		if (((SimpleNode) termWithVariables.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTVAR) {
			return true;
		} else if (((SimpleNode) termWithVariables.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTSYMBOLICTERM) {
			if (((SimpleNode) groundTerm.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTSYMBOLICTERM) {
				return unify(
						(ASTsymbolicTerm) termWithVariables.jjtGetChild(0),
						(ASTsymbolicTerm) groundTerm.jjtGetChild(0));

			} else { // arithmetic term
				return unify(
						(ASTsymbolicTerm) termWithVariables.jjtGetChild(0),
						(ASTarithmeticTerm) groundTerm.jjtGetChild(0));

			}
		} else { // term with variables is arithmetic
			if (((SimpleNode) groundTerm.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTSYMBOLICTERM) {
				return unify(
						(ASTarithmeticTerm) termWithVariables.jjtGetChild(0),
						(ASTsymbolicTerm) groundTerm.jjtGetChild(0));

			} else { // ground term is arithmetic
				return unify(
						(ASTarithmeticTerm) termWithVariables.jjtGetChild(0),
						(ASTarithmeticTerm) groundTerm.jjtGetChild(0));
			}

		}
	}

	/**
	 * Unify(match) arithmetic term with variables with arithmetic term without
	 * variables,
	 * 
	 * @param termWithVariables
	 * @param groundterm
	 * @return true if there exists a substitution for term with variables which
	 *         makes it equivalent to ground term.
	 */
	private boolean unify(ASTarithmeticTerm termWithVariables,
			ASTarithmeticTerm groundTerm) {
		// we assume arithmetic terms are always unifiable
		return true;
	}

	/**
	 * Unify(match) arithmetic term with variables with ground symbolic term
	 * 
	 * @param termWithVariables
	 * @param groundTerm
	 *            symbolic ground term
	 * @return false (terms are always not unifiable)
	 */
	private boolean unify(ASTarithmeticTerm termWithVariables,
			ASTsymbolicTerm groundTerm) {
		// we can never get a symbolic constant out of arithmetic term
		return false;
	}

	/**
	 * Unify(match) symbolic term with variables with arithmetic term without
	 * variables,
	 * 
	 * @param termWithVariables
	 * @param groundterm
	 * @return true if there exists a substitution for term with variables which
	 *         makes it equivalent to ground term.
	 */

	private boolean unify(ASTsymbolicTerm termWithVariables,
			ASTarithmeticTerm groundTerm) {
		SimpleNode child1 = (SimpleNode) termWithVariables.jjtGetChild(0);
		TermEvaluator tEval = new TermEvaluator(groundTerm);
		String value = null;
		try {
			value = Long.toString(tEval.evaluate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (child1.getId() == SparcTranslatorTreeConstants.JJTSYMBOLICFUNCTION) {
			return false;
		} else {
			if (typechecker.constantsMapping.containsKey(termWithVariables
					.toString())) {
				return typechecker.constantsMapping.get(
						termWithVariables.toString()).equals(value);
			} else {
				return false;
			}
		}
	}

	/**
	 * Unify(match) symbolic term with variables with symbolic term without
	 * variables,
	 * 
	 * @param termWithVariables
	 * @param groundterm
	 * @return true if there exists a substitution for term with variables which
	 *         makes it equivalent to ground term.
	 */

	private boolean unify(ASTsymbolicTerm termWithVariables,
			ASTsymbolicTerm groundTerm) {
		SimpleNode child1 = (SimpleNode) termWithVariables.jjtGetChild(0);
		SimpleNode child2 = (SimpleNode) groundTerm.jjtGetChild(0);
		// either both of the terms start from functional symbol or both of them
		// are
		// constants
		if (child1.getId() != child2.getId()) {
			return false;
		} else if (child1.getId() == SparcTranslatorTreeConstants.JJTSYMBOLICCONSTANT) {
			return child1.image.equals(child2.image);
		} else { // both are symbolic functions:
			if (!child1.image.equals(child2.image)) {
				return false;
			}
			SimpleNode termList1 = (ASTtermList) termWithVariables
					.jjtGetChild(1);
			SimpleNode termList2 = (ASTtermList) groundTerm.jjtGetChild(1);
			return unifyTermLists(termList1, termList2);
		}
	}

	/**
	 * Unify(match) two list of terms
	 * 
	 * @param termList1
	 * @param termList2
	 * @return true if all corresponding elements of two lists are unifiable)
	 */
	private boolean unifyTermLists(SimpleNode termList1, SimpleNode termList2) {

		if (termList1.jjtGetNumChildren() != termList2.jjtGetNumChildren()) {
			return false;
		} else {
			boolean result = true;
			for (int index = 0; index < termList1.jjtGetNumChildren(); index++) {
				ASTterm t1 = (ASTterm) termList1.jjtGetChild(index);
				ASTterm t2 = (ASTterm) termList2.jjtGetChild(index);
				if (!unify(t1, t2)) {
					result = false;
					break;
				}
			}
			return result;
		}
	}

	/**
	 * Create a term from given string
	 * 
	 * @param content
	 *            given string
	 * @return Abstract Syntax Tree Node representing the term
	 */
	private ASTterm createTerm(String content) {
		Reader sr = new StringReader(content);
		SparcTranslator p = new SparcTranslator(sr);
		p.token_source.SwitchTo(SparcTranslatorConstants.IN_PROGRAM_RULES);
		ASTterm result = null;
		try {
			result = (ASTterm) p.term();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
}
