//TODO:
package typechecking;

//TO REFACTOR: remove this class and add necessary constructors to ASTterm.
import java.util.ArrayList;

import parser.ASTadditiveArithmeticTerm;
import parser.ASTarithmeticTerm;
import parser.ASTatomicArithmeticTerm;
import parser.ASTmultiplicativeArithmeticTerm;
import parser.ASTsymbolicConstant;
import parser.ASTsymbolicFunction;
import parser.ASTsymbolicTerm;
import parser.ASTterm;
import parser.ASTtermList;
import parser.ASTvar;
import parser.SparcTranslatorTreeConstants;
import warnings.Pair;
import warnings.StringListUtils;

/**
 * Create symbolic or arithmetic term from provided string of image
 */
public class TermCreator {


	
	
	/**
	 * Constructor
	 * 
	 * @param image
	 *            of term to be created
	 */

	public TermCreator() {
	
	}
	/**
	 * Create symbolic constant from provided image
	 * 
	 * @return root of AST for created term
	 */
	public ASTterm createSimpleSymbolicTerm(String image) {
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
	 * Create a record with given name and a list of variables in arguments 
	 * @param recordName name of created record
	 * @param varArgs list of variables in arguments
	 * @return
	 */
    public ASTterm createRecord(String recordName,ArrayList<String> varArgs){
    	ASTsymbolicFunction symFunction = new ASTsymbolicFunction(
				SparcTranslatorTreeConstants.JJTSYMBOLICFUNCTION);
    	symFunction.image=recordName+"(";
    	
    	ASTtermList termList=new ASTtermList(SparcTranslatorTreeConstants.JJTTERMLIST);
    	
    	for(int i=0;i<varArgs.size();i++) {
    		ASTterm term=new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
    		ASTvar var=new ASTvar(SparcTranslatorTreeConstants.JJTVAR);
    		var.image=varArgs.get(i);
    		term.jjtAddChild(var, 0);
    		termList.jjtAddChild(term, i);
    	}
    	
		ASTsymbolicTerm sterm = new ASTsymbolicTerm(
				SparcTranslatorTreeConstants.JJTSYMBOLICTERM);
		sterm.jjtAddChild(symFunction, 0);
		sterm.jjtAddChild(termList, 1);
		ASTterm term = new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
		term.jjtAddChild(sterm, 0);
		return term;
    }
    
    public ASTterm createGroundSymbolicTerm(String image) {
    	Pair<String,ArrayList<String>  >recordContents=StringListUtils.splitTerm(image);
    	if(recordContents!=null) {
    		ASTsymbolicFunction func=new ASTsymbolicFunction(SparcTranslatorTreeConstants.JJTSYMBOLICFUNCTION);
            func.image=recordContents.first+"(";
            ASTtermList termList=new ASTtermList(SparcTranslatorTreeConstants.JJTTERMLIST);
            for(int i=0;i<recordContents.second.size();i++) {
            	ASTterm newTerm=createGroundSymbolicTerm(recordContents.second.get(i));
            	termList.jjtAddChild(newTerm, i);
            }
            
            ASTterm result=new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
            result.jjtAddChild(func, 0);
            result.jjtAddChild(termList, 1);
            return result;
    	}
    	else {
    		return createSimpleSymbolicTerm(image);
    		
    	}
    
        
    	
    }
	/**
	 * Create arithmetic constant from provided image
	 * 
	 * @return root of AST for created term
	 */
	public ASTterm createSimpleArithmeticTerm(String image) {
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
	
	/**
	 * Create arithmetic constant from provided image
	 * 
	 * @return root of AST for created term
	 */
	public ASTadditiveArithmeticTerm createSimpleAdditiveArithmeticTerm(String image) {
		/*
		 * subtree with the following structure is created: 
		 * additiveArithmeticTerm multiplicativeArithmeticTerm
		 * atomicArithmeticTerm image
		 */
		ASTatomicArithmeticTerm aaterm = new ASTatomicArithmeticTerm(
				SparcTranslatorTreeConstants.JJTATOMICARITHMETICTERM);
		ASTmultiplicativeArithmeticTerm materm = new ASTmultiplicativeArithmeticTerm(
				SparcTranslatorTreeConstants.JJTMULTIPLICATIVEARITHMETICTERM);
		// root
		ASTadditiveArithmeticTerm adaterm = new ASTadditiveArithmeticTerm(
				SparcTranslatorTreeConstants.JJTADDITIVEARITHMETICTERM);
	
        aaterm.image=adaterm.image="+";
		aaterm.image=image;
		// attach subtrees to the root
		materm.jjtAddChild(aaterm, 0);
		adaterm.jjtAddChild(materm, 0);

		return adaterm;
		
	}
}
