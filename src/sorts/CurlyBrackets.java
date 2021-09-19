package sorts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import parser.ASTconstantTerm;
import parser.ASTconstantTermList;
import parser.ASTcurlyBrackets;
import parser.ParseException;
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
			retrieveAllTerms((ASTconstantTerm) termList.jjtGetChild(i), terms);
		}

	}

	/**
	 * Retrieve all ground terms and their subterms occurring in the list of
	 * constant(ground) terms
	 * 
	 * @param termList
	 *            AST node representing the term list
	 * @param terms
	 *            the set where all found terms will be written
	 * @throws ParseException 
	 */
	public static void checkNoKeyWordOccurrences(ASTconstantTermList termList) throws ParseException {
		for (int i = 0; i < termList.jjtGetNumChildren(); i++) {
		   ASTconstantTerm term = (ASTconstantTerm) termList.jjtGetChild(0);
		   if(term.image.equals("not")) {
			   throw new ParseException("ERROR: constant \"" + term.image + "\" at line " + 
		                term.getBeginLine() + ", column " + term.getBeginColumn() + " is a reserved keyword.");
		  
		   }
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
	 * @param functionalSymbols a map from function symbol names to an array of their supported arities
	 *                          (will be filled in by the function)
	 */
	public static void retrieveAllFunctionalSymbols(
			ASTcurlyBrackets curlyBrackets, HashMap<String, ArrayList<Integer>> functionalSymbols) {
         retrieveAllFunctionalSymbolsR(curlyBrackets, functionalSymbols);
	}
	
	/**
	 * Retrieve all functional symbols in the abstract syntax tree node
	 * @param curlyBrackets
	 * @param functionalSymbols a map from function symbol names to an array of their supported arities
	 *                          (will be filled in by the function)
	 */                          
	private static void retrieveAllFunctionalSymbolsR(SimpleNode n, HashMap<String, ArrayList<Integer>> functionalSymbols) {
		if(n.getId()==SparcTranslatorTreeConstants.JJTCONSTANTTERM) {
			if(n.image.indexOf('(')!=-1) {
				ASTconstantTerm term = (ASTconstantTerm)n;
				ASTconstantTermList termArgList = (ASTconstantTermList)term.jjtGetChild(0);
				
				String recordName = n.image.substring(0,n.image.indexOf('('));
				ArrayList<Integer>knownArities;
				if(!functionalSymbols.containsKey(recordName)) {
					knownArities = new ArrayList<Integer>();
					functionalSymbols.put(recordName, knownArities);
				} else {
				  knownArities = functionalSymbols.get(recordName);
				};
				int functionSymbolArity = termArgList.jjtGetNumChildren();
				if(!knownArities.contains(functionSymbolArity)) {
					knownArities.add(functionSymbolArity);
				}
				
			}
		}
		for(int i=0;i<n.jjtGetNumChildren();i++) {
			retrieveAllFunctionalSymbolsR((SimpleNode)n.jjtGetChild(i), functionalSymbols);
		}
	}
}
