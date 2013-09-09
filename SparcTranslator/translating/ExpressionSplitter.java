package translating;

import java.util.ArrayList;
import java.util.HashSet;
import parser.ASTadditiveArithmeticTerm;
import parser.ASTarithmeticTerm;
import parser.ASTatom;
import parser.ASTatomicArithmeticTerm;
import parser.ASTmultiplicativeArithmeticTerm;
import parser.ASTterm;
import parser.ASTvar;
import parser.SimpleNode;
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
		return split(additiveTerm, additiveTerm.jjtGetNumChildren()-1, newAtoms);
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
	 * @return equivalent list of atoms of the form A=t1 op t2
	 */

	private ASTterm split(ASTadditiveArithmeticTerm additiveTerm, int fIndex, ArrayList<ASTatom> newAtoms) {
		ASTterm lastMultiplicative = split(
				(ASTmultiplicativeArithmeticTerm) additiveTerm
						.jjtGetChild(fIndex),
				newAtoms);
		// if this is the last term:
		if (fIndex == 0) {
			return lastMultiplicative;
		}
		else {
			// create new variable
			ASTvar newVar = new ASTvar(SparcTranslatorTreeConstants.JJTVAR);
			newVar.image = createUniqueVarName();
			ASTterm newVarTerm = ASTterm.createArithmeticVarTerm(newVar);
			//take the last two multiplicative Terms
			ASTterm allButLast=split(additiveTerm,fIndex-1,newAtoms);
			//create new additive Term
			ASTterm newTerm=new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
			ASTarithmeticTerm newATerm=new ASTarithmeticTerm(SparcTranslatorTreeConstants.JJTARITHMETICTERM);
			ASTadditiveArithmeticTerm newAdTerm=new ASTadditiveArithmeticTerm(SparcTranslatorTreeConstants.JJTADDITIVEARITHMETICTERM);
			newAdTerm.image="+"+Character.toString(additiveTerm.image.charAt(fIndex));
			newAdTerm.jjtAddChild(allButLast.getLeftMostMultiplicativeTerm(), 0);
			newAdTerm.jjtAddChild(lastMultiplicative.getLeftMostMultiplicativeTerm(), 1);
			newATerm.jjtAddChild(newAdTerm, 0);
			newTerm.jjtAddChild(newATerm, 0);
			newAtoms.add(createEqAtom(newVarTerm,newTerm));
			return newVarTerm;
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
		return split(multTerm, multTerm.jjtGetNumChildren()-1, newAtoms);
		
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
		ASTterm lastAtomic = split(
				(ASTatomicArithmeticTerm) mTerm.jjtGetChild(fIndex),
				newAtoms);
		// if this is the last term:
		if (fIndex == 0) {
			return lastAtomic;
		}
		else {
			// create new variable
			ASTvar newVar = new ASTvar(SparcTranslatorTreeConstants.JJTVAR);
			newVar.image = createUniqueVarName();
			ASTterm newVarTerm = ASTterm.createArithmeticVarTerm(newVar);
			//take the last two multiplicative Terms
			ASTterm allButLast=split(mTerm,fIndex-1,newAtoms);
			//create new additive Term
			ASTterm newTerm=new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
			ASTarithmeticTerm newATerm=new ASTarithmeticTerm(SparcTranslatorTreeConstants.JJTARITHMETICTERM);
			ASTadditiveArithmeticTerm newAdTerm=new ASTadditiveArithmeticTerm(SparcTranslatorTreeConstants.JJTADDITIVEARITHMETICTERM);
			ASTmultiplicativeArithmeticTerm newMTerm=new ASTmultiplicativeArithmeticTerm(SparcTranslatorTreeConstants.JJTMULTIPLICATIVEARITHMETICTERM);
			newMTerm.image=Character.toString(mTerm.image.charAt(fIndex-1));;
			newAdTerm.image="+";
			newMTerm.jjtAddChild(allButLast.getLeftMostAtomicTerm(), 0);
			newMTerm.jjtAddChild(lastAtomic.getLeftMostAtomicTerm(), 1);
			newTerm.jjtAddChild(newATerm, 0);
			newATerm.jjtAddChild(newAdTerm, 0);
			newAdTerm.jjtAddChild(newMTerm, 0);
			newAtoms.add(createEqAtom(newVarTerm,newTerm));
			newMTerm.toString();
			return newVarTerm;
		}

	}
	
	/**
	 * Split atomic arithmetic term possibly containing one or more
	 * operands 
	 * 
	 * @param term
	 * @param newAtoms
	 *            array where newly introduced atoms will be added
	 * @return equivalent list of atoms of the form A=t1 op t2
	 */

	private ASTterm split(ASTatomicArithmeticTerm atomTerm, 
			ArrayList<ASTatom> newAtoms) {
          if(atomTerm.jjtGetNumChildren()>0) {
        	  SimpleNode child=(SimpleNode)atomTerm.jjtGetChild(0);
        	  if(child.getId()==SparcTranslatorTreeConstants.JJTVAR) {
        		  return ASTterm.createArithmeticVarTerm((ASTvar)child);
        	  } else { // the child is arithmetic term
        		  return split((ASTarithmeticTerm)child,newAtoms);
        	  }
          } else { // there are no children, which means there is a number
        	  return  new ASTterm(Long.parseLong(atomTerm.image));
          }
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
