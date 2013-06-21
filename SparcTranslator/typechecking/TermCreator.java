package typechecking;

import parser.ASTadditiveArithmeticTerm;
import parser.ASTarithmeticTerm;
import parser.ASTatomicArithmeticTerm;
import parser.ASTmultiplicativeArithmeticTerm;
import parser.ASTsymbolicConstant;
import parser.ASTsymbolicTerm;
import parser.ASTterm;
import parser.SparcTranslatorTreeConstants;

/**
 * Create symbolic or arithmetic term from provided string of image
 */
public class TermCreator {
	String image;

	/**
	 * Constructor
	 * 
	 * @param image
	 *            of term to be created
	 */
	public TermCreator(String image) {
		this.image = image;
	}

	/**
	 * Create symbolic constant from provided image
	 * 
	 * @return root of AST for created term
	 */
	public ASTterm createSimpleSymbolicTerm() {
		ASTsymbolicConstant sconstant = new ASTsymbolicConstant(
				SparcTranslatorTreeConstants.JJTSYMBOLICCONSTANT);
		sconstant.image = image;
		ASTsymbolicTerm sterm = new ASTsymbolicTerm(
				SparcTranslatorTreeConstants.JJTSYMBOLICTERM);
		sterm.jjtAddChild(sconstant, 0);
		ASTterm term = new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
		term.jjtAddChild(sterm, 0);
		return term;
	}

	/**
	 * Create arithmetic constant from provided image
	 * 
	 * @return root of AST for created term
	 */
	public ASTterm createSimpleArithmeticTerm() {
		/*
		 * subtree with the following structure is created: term arithmeticTerm
		 * additiveArithmeticTerm multiplicativeArithmeticTerm
		 * atomicArithmeticTerm image
		 */
		ASTatomicArithmeticTerm aaterm = new ASTatomicArithmeticTerm(
				SparcTranslatorTreeConstants.JJTATOMICARITHMETICTERM);
		ASTmultiplicativeArithmeticTerm materm = new ASTmultiplicativeArithmeticTerm(
				SparcTranslatorTreeConstants.JJTMULTIPLICATIVEARITHMETICTERM);
		ASTadditiveArithmeticTerm adaterm = new ASTadditiveArithmeticTerm(
				SparcTranslatorTreeConstants.JJTADDITIVEARITHMETICTERM);
		ASTarithmeticTerm aterm = new ASTarithmeticTerm(
				SparcTranslatorTreeConstants.JJTARITHMETICTERM);
		// root
		ASTterm term = new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
		aaterm.image = adaterm.image=image;
		// attach subtrees to the root
		materm.jjtAddChild(aaterm, 0);
		adaterm.jjtAddChild(materm, 0);
		aterm.jjtAddChild(adaterm, 0);
		term.jjtAddChild(aterm, 0);
		return term;
	}
}
