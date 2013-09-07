package translating;

import java.util.ArrayList;
import java.util.HashSet;
import parser.ASTadditiveArithmeticTerm;
import parser.ASTarithmeticTerm;
import parser.ASTatom;
import parser.ASTmultiplicativeArithmeticTerm;
import parser.ASTterm;
import parser.ASTvar;
import parser.SparcTranslator;
import parser.SparcTranslatorTreeConstants;

/**
 * This class splits an expression containing arithmetic operations into a set
 * of atoms of the form A = B, A is a variable and B is of the form t_1 op t_2,
 * where t_1 and t_2 are arithmetic terms not containing operations and op is
 * one of *,-,+.
 */
public class ExpressionSplitter {
	// set of variable names which were already used in the rule
	HashSet<String> usedVariableNames;

	// prefix to be added to introduced variables
	final String prefix = "VAR_";

	// variable id to be used in generating new names
	int varid = 0;

	public ExpressionSplitter(HashSet<String> usedVariableNames) {
		this.usedVariableNames = usedVariableNames;
	}

	/**
	 * Split arithmetic term
	 * 
	 * @param term
	 * @param newAtoms
	 *            array where newly introduced atoms will be added
	 * @return equivalent list of atoms of the form A=t1 op t2
	 */
	public ASTterm split(ASTarithmeticTerm term, ArrayList<ASTatom> newAtoms) {
		return split((ASTadditiveArithmeticTerm) term.jjtGetChild(0), newAtoms);
	}

	/**
	 * Split additive arithmetic term possibly containing one or more + and -
	 * operations
	 * 
	 * @param term
	 * @param newAtoms
	 *            array where newly introduced atoms will be added
	 * @return equivalent list of atoms of the form A=t1 op t2
	 */

	private ASTterm split(ASTadditiveArithmeticTerm additiveTerm,
			ArrayList<ASTatom> newAtoms) {
		return split(additiveTerm, 0,false, newAtoms);
	}

	/**
	 * Split additive arithmetic term possibly containing one or more operands
	 * connected by +, - operators
	 * 
	 * @param term
	 * @param newAtoms
	 *            array where newly introduced atoms will be added
	 * @param fIndex
	 *            the first index in additiveTerm children to consider
	 * @param invertSign
	 *            indicates whether it is needed to replace '+' by '-' and '-'
	 *            by '+'
	 * @return equivalent list of atoms of the form A=t1 op t2
	 */

	private ASTterm split(ASTadditiveArithmeticTerm additiveTerm, int fIndex,
			boolean invertSign, ArrayList<ASTatom> newAtoms) {
		ASTterm firstMultiplicative = split(
				(ASTmultiplicativeArithmeticTerm) additiveTerm
						.jjtGetChild(fIndex),
				newAtoms);
		// if this is the last term:
		if (fIndex == additiveTerm.jjtGetNumChildren() - 1) {
			return firstMultiplicative;
		}
		else {
			ASTvar newVar = new ASTvar(SparcTranslatorTreeConstants.JJTVAR);
			newVar.image = createUniqueVarName();
			ASTterm newVarTerm = new ASTterm(newVar);
			newAtoms.add(createEqAtom(newVarTerm, firstMultiplicative));
			return null;
		}

	}

	/**
	 * Split multiplicative arithmetic term possibly containing one or more
	 * operands connected by * operator
	 * 
	 * @param term
	 * @param newAtoms
	 *            array where newly introduced atoms will be added
	 * @param fIndex
	 *            the first index in additiveTerm children to consider
	 * @return equivalent list of atoms of the form A=t1 op t2
	 */

	private ASTterm split(ASTmultiplicativeArithmeticTerm multTerm,
			ArrayList<ASTatom> newAtoms) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Split multiplicative arithmetic term possibly containing one or more
	 * operands connected by * operator
	 * 
	 * @param term
	 * @param newAtoms
	 *            array where newly introduced atoms will be added
	 * @param fIndex
	 *            the first index in additiveTerm children to consider
	 * @return equivalent list of atoms of the form A=t1 op t2
	 */

	private ASTterm split(ASTmultiplicativeArithmeticTerm mTerm, int fIndex,
			ArrayList<ASTatom> newAtoms) {
		// TODO Auto-generated method stub
		return null;
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

	/**
	 * Create atom of the form (left term)=(right term)
	 * 
	 * @param left
	 *            left term
	 * @param right
	 *            right term
	 * @return atom
	 */
	private ASTatom createEqAtom(ASTterm left, ASTterm right) {
		ASTatom newAtom = new ASTatom(SparcTranslatorTreeConstants.JJTATOM);
		newAtom.jjtAddChild(left, 0);
		newAtom.jjtAddChild(right, 1);
		newAtom.image = "=";
		return newAtom;
	}

}
