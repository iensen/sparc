package translating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import parser.ASTaggregateElement;
import parser.ASTarithmeticTerm;
import parser.ASTatom;
import parser.ASTchoice_element;
import parser.ASTprogramRule;
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
	HashMap<String, ASTarithmeticTerm> createdVariables;
	// mapping from creating variables to strings representing arithmetic expressions found in atoms
    HashMap<String, String> createdVariablesStrings;
	//mapping from arithmetic expressions found in atoms to variables
	HashMap<String, String> createdVariablesInverse;
	// prefix to be added to introduced variables
	final String prefix = "VAR_";
	// id for found variables
	int varid = 0;

	/**
	 * @param a
	 *            set variables used in rule they will not be used for new
	 *            variable names
	 */
	public ExpressionFetcher(Set<String> set) {
		this.usedVariableNames = new HashSet<String>();
		for (String s : set) {
			usedVariableNames.add(s);
		}
		this.createdVariablesInverse=new HashMap<String,String>();
		this.createdVariablesStrings=new HashMap<String,String>();
	}

	/**
	 * @return a map of new variables assigned to fetched arithmetic expressions
	 */

	public HashMap<String, ASTarithmeticTerm> getDetectedArithmeticVariables() {
		return createdVariables;
	}

	/**
	 * @param program
	 *            rule where atoms will be searched
	 * @return atoms of the form var_name = [arithmetic expression], where
	 *         [arithmetic_expression] goes over all possible regular expression
	 *         from the rule
	 */
	public ArrayList<ASTatom> fetchGlobalExpressions(ASTprogramRule rule) {
		createdVariables = new HashMap<String, ASTarithmeticTerm>();
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
		createdVariables = new HashMap<String, ASTarithmeticTerm>();
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
		createdVariables = new HashMap<String, ASTarithmeticTerm>();
		return fetchExpressions(elem, false);
	}

	/**
	 * Create atom of the form (variable_name)=(arithmetic term)
	 * 
	 * @param varName
	 *            name of a variable on left hand side of the atom
	 * @param aterm
	 *            term on the right hand side of atom
	 * @return atom
	 */
	private ASTatom createAtom(String varName, ASTarithmeticTerm aterm) {
		ASTatom newAtom = new ASTatom(SparcTranslatorTreeConstants.JJTATOM);
		ASTvar var = new ASTvar(SparcTranslatorTreeConstants.JJTVAR);
		var.image = varName;
		newAtom.jjtAddChild(var, 0);
		newAtom.jjtAddChild(aterm, 1);
		newAtom.image = "=";
		return newAtom;
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

		// we are only interested in expressions in atoms
		if (n.getId() == SparcTranslatorTreeConstants.JJTATOM
				|| n.getId() == SparcTranslatorTreeConstants.JJTSIMPLEATOM) {
			if (n.jjtGetNumChildren() == 0)
				return new ArrayList<ASTatom>();
			SimpleNode child = (SimpleNode) (n.jjtGetChild(0));
			if (child.getId() != SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM
					&& child.getId() != SparcTranslatorTreeConstants.JJTAGGREGATE) {
				return new ArrayList<ASTatom>();
			}
		}

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
				String newName = createUniqueVarName();
				if(createdVariablesInverse.containsKey(n.toString())) {
					newName=createdVariablesInverse.get(n.toString());
				}
				createdVariables.put(newName, (ASTarithmeticTerm) n);
				createdVariablesStrings.put(newName, n.toString());
				createdVariablesInverse.put(n.toString(),newName);
				ASTarithmeticTerm newTerm = new ASTarithmeticTerm(
						SparcTranslatorTreeConstants.JJTTERM);
				newTerm.image = n.image;
				for (int i = 0; i < n.jjtGetNumChildren(); i++) {
					newTerm.jjtAddChild(n.jjtGetChild(i), i);
				}
				result.add(createAtom(newName, newTerm));
				n.removeChildren();
				ASTvar newVar=new ASTvar(SparcTranslatorTreeConstants.JJTVAR);
				newVar.image = newName;
				n.jjtAddChild(newVar, 0);

			}
		}

		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			result.addAll(fetchExpressions((SimpleNode) (n.jjtGetChild(i)),
					ignoreLocals));
		}
		return result;
	}

	/**
	 * Create unique variable names. Considers all variable names which were
	 * already used
	 * 
	 * @return name string, representing unique variable name
	 */
	private String createUniqueVarName() {
		while (usedVariableNames.contains(prefix + varid)) {
			++varid;
		}
		usedVariableNames.add(prefix + varid);
		return prefix + varid;
	}

}
