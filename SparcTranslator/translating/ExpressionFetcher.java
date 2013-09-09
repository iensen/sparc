package translating;

import java.util.ArrayList;
import java.util.HashSet;
import parser.ASTaggregateElement;
import parser.ASTarithmeticTerm;
import parser.ASTatom;
import parser.ASTchoice_element;
import parser.ASTprogramRule;
import parser.ASTterm;
import parser.ASTvar;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;

/**
 * This class fetches arithmetic expressions from atoms' arguments to fix
 * well-known DLV issue with arithmetics expressions For each arithmetic
 * expression new variable is created, and arithmetic term is assigned to the
 * variable. Atom representing this new assignment is moved to the body of
 * corresponding rule Example: p(X+1). becomes p(Y):-Y=X+1. The list of such
 * atoms is returned by functions fetch(Global|Local) variables
 */
public class ExpressionFetcher {
	HashSet<String> usedVariableNames;
	// mapping from creating variables to arithmetic expressions found in atoms
	
	ExpressionSplitter exprSplitter;

	/**
	 * @param a
	 *            set variables used in rule they will not be used for new
	 *            variable names
	 */
	public ExpressionFetcher(HashSet<String> set) {
		this.usedVariableNames = set;
		exprSplitter=new ExpressionSplitter(set);
	}



	/**
	 * @param program
	 *            rule where atoms will be searched
	 * @return atoms of the form var_name = [arithmetic expression], where
	 *         [arithmetic_expression] goes over all possible regular expression
	 *         from the rule
	 */
	public ArrayList<ASTatom> fetchGlobalExpressions(ASTprogramRule rule) {
		return fetchExpressions(rule, true);
	}

	/**
	 * @param aggregate
	 *            element to be explored
	 * @return list of atoms of the form var_name = [arithmetic expression],
	 *         where [arithmetic_expression] goes over all possible regular
	 *         expression from the aggregate element
	 */
	public ArrayList<ASTatom> fetchLocalExpressions(ASTaggregateElement elem) {
		return fetchExpressions(elem, false);
	}

	/**
	 * @param choice
	 *            rule element where arithmetic atoms will be searched.
	 * @return list of atoms of the form var_name = [arithmetic expression],
	 *         where [arithmetic_expression] goes over all possible regular
	 *         expression from the choice rule element
	 */
	public ArrayList<ASTatom> fetchLocalExpressions(ASTchoice_element elem) {
		return fetchExpressions(elem, false);
	}



	/**
	 * Fetch expressions from given AST node and return a list of found
	 * expression.
	 * 
	 * @param n
	 *            node to be explored
	 * @param ignoreLocals
	 *            if the parameter is true, choice rules and aggregates are
	 * @return list of atoms which were built
	 */
	private ArrayList<ASTatom> fetchExpressions(SimpleNode n,
			boolean ignoreLocals) {


		// ignore choice rules and aggregates in case ignore locals was set to
		// true
		ArrayList<ASTatom> result = new ArrayList<ASTatom>();
		if (ignoreLocals
				&& (n.getId() == SparcTranslatorTreeConstants.JJTAGGREGATE || n
						.getId() == SparcTranslatorTreeConstants.JJTCHOICE_RULE)) {
			return result;
		}

		if (n.getId() == SparcTranslatorTreeConstants.JJTARITHMETICTERM) {
			// we are only interested in arithmetic terms which are not
			// supported by DLV ,i.e, it those with operation signs
			if (n.toString().indexOf('-') != -1
					|| n.toString().indexOf('+') != -1
					|| n.toString().indexOf('*') != -1
					|| n.toString().indexOf('/') != -1) {
				ASTterm newTerm=exprSplitter.split((ASTarithmeticTerm)n,result);
				ASTvar newVar=new ASTvar(SparcTranslatorTreeConstants.JJTVAR);
				newVar.image=newTerm.toString();
				n.jjtAddChild(newVar, 0);

			}
		}

		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			result.addAll(fetchExpressions((SimpleNode) (n.jjtGetChild(i)),
					ignoreLocals));
		}
		return result;
	}

	
}
