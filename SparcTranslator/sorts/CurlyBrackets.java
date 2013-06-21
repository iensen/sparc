package sorts;

import java.util.HashSet;

import parser.ASTconstantTerm;
import parser.ASTconstantTermList;
import parser.ASTcurlyBrackets;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;

public class CurlyBrackets {

	/**
	 * Retrieve all ground terms and their subterms occurring in the list of
	 * constant(ground) terms
	 * 
	 * @param termList
	 *            AST node representing the term list
	 * @param terms
	 *            the set where all found terms will be written
	 */
	public static void retrieveAllTerms(ASTconstantTermList termList,
			HashSet<String> terms) {
		for (int i = 0; i < termList.jjtGetNumChildren(); i++) {
			retrieveAllTerms((ASTconstantTerm) termList.jjtGetChild(0), terms);
		}

	}

	/**
	 * Retrieve all ground terms and their subterms occurring in the constant
	 * term
	 * 
	 * @param termList
	 *            AST node representing the term list
	 * @param terms
	 *            the set where all found terms will be written
	 */
	private static void retrieveAllTerms(ASTconstantTerm constantTerm,
			HashSet<String> terms) {
		terms.add(constantTerm.toString());
		if (constantTerm.jjtGetNumChildren() != 0
				&& ((SimpleNode) constantTerm.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTCONSTANTTERMLIST) {
			for (int i = 0; i < constantTerm.jjtGetNumChildren(); i++) {
				retrieveAllTerms(
						(ASTconstantTermList) (constantTerm.jjtGetChild(i)), terms);
			}
		}
	}


	/**
	 * Retrieve all functional symbols in the curly Brackets
	 * @param curlyBrackets
	 * @param functionalSymbols the set where all found functional symbols will be stored 
	 */
	public static void retrieveAllFunctionalSymbols(
			ASTcurlyBrackets curlyBrackets, HashSet<String> functionalSymbols) {
         retrieveAllFunctionalSymbolsR(curlyBrackets, functionalSymbols);
	}
	
	/**
	 * Retrieve all functional symbols in the abstract syntax tree node
	 * @param curlyBrackets
	 * @param functionalSymbols the set where all found functional symbols will be stored 
	 */
	private static void retrieveAllFunctionalSymbolsR(SimpleNode n, HashSet<String> functionalSymbols) {
		if(n.getId()==SparcTranslatorTreeConstants.JJTCONSTANTTERM) {
			if(n.image.indexOf('(')!=-1) {
				functionalSymbols.add(n.image.substring(0,n.image.indexOf('(')));
			}
		}
		for(int i=0;i<n.jjtGetNumChildren();i++) {
			retrieveAllFunctionalSymbolsR((SimpleNode)n.jjtGetChild(i), functionalSymbols);
		}
	}
}
