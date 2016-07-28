package translating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import parser.ASTadditiveSetExpression;
import parser.ASTaggregateElement;
import parser.ASTatom;
import parser.ASTbody;
import parser.ASTchoice_element;
import parser.ASTdisjunction;
import parser.ASTextendedNonRelAtom;
import parser.ASTextendedSimpleAtomList;
import parser.ASThead;
import parser.ASTmultiplicativeSetExpression;
import parser.ASTnonRelAtom;
import parser.ASTpredSymbol;
import parser.ASTprogramRule;
import parser.ASTsimpleAtom;
import parser.ASTsortExpression;
import parser.ASTsortExpressionList;
import parser.ASTterm;
import parser.ASTtermList;
import parser.ASTunarySetExpression;
import parser.ASTunlabeledProgramCrRule;
import parser.ASTunlabeledProgramRule;
import parser.SparcTranslatorConstants;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;

/**
 * Term fetcher finds all terms in given AST node (which may be a rule, body,
 * head, atom.etc) and returns a mapping from terms to sort expression they
 * should satisfy in order to obey sort definitions
 */
public class TermFetcher {
	// mapping from sort names to sort expressions assigned to the sorts
	// mapping from predicate names to a list of names of sorts describing
	// arguments
	private HashMap<String, ArrayList<String>> predicateArgumentSorts;

	// mapping from sort names to sort expressions assigned to the sorts
	// this map is needed for optimization purposes to avoid generation of certain sorts
	private HashMap<String, ASTsortExpression> sortNameToExpression;
		
	/**
	 * Constructor
	 * 
	 * @param sortNameToExpression
	 * @param predicateArgumentSorts
	 */
	public TermFetcher(
			HashMap<String, ArrayList<String>> predicateArgumentSorts,
			HashMap<String, ASTsortExpression> sortNameToExpression) {
		this.predicateArgumentSorts = predicateArgumentSorts;
		this.sortNameToExpression = sortNameToExpression;		
	}

	/**
	 * Fetch sorts for terms in program rule given by AST node
	 * 
	 * @param rule
	 *            ASTnode to explore
	 * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	public HashMap<ASTterm, String> fetchTermSorts(ASTprogramRule rule) {
		if (((SimpleNode) (rule.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTUNLABELEDPROGRAMRULE) {
			return fetchTermSorts((ASTunlabeledProgramRule) (rule
					.jjtGetChild(0)));
		} else {
			return fetchTermSorts((ASTunlabeledProgramCrRule) (rule
					.jjtGetChild(0)));
		}
	}

	/**
	 * Put all key-value pairs from src map to dest map
	 * 
	 * @param dest
	 *            first map
	 * @param src
	 *            second map
	 */
	private void unionMaps(HashMap<ASTterm, String> dest,
			HashMap<ASTterm, String> src) {
		for (ASTterm term : src.keySet()) {
			dest.put(term, src.get(term));
		}
	}

	/**
	 * Fetch variables from unlabeled program rule given by AST node
	 * 
	 * @param rule
	 *            ASTnode to explore
	 * @return variable->sort_expression mapping, where sort expression
	 *         describes a language of string each of which may be used as a
	 *         substitution for given variable
	 */
	public HashMap<ASTterm, String> fetchTermSorts(ASTunlabeledProgramRule rule) {
		HashMap<ASTterm, String> result = new HashMap<ASTterm, String>();
		String weakSep = SparcTranslatorConstants.tokenImage[SparcTranslatorConstants.WEAKSEP];
		// remove quotes:
		weakSep = weakSep.substring(1, weakSep.length() - 1);
		if (rule.image.equals(weakSep)) {
			// weak constraint can only have body
			unionMaps(result, fetchTermSorts((ASTbody) (rule.jjtGetChild(0))));
		} else if (rule.jjtGetNumChildren() > 0
				&& ((SimpleNode) rule.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTPREDSYMBOL) {
			// fact of the form f(n1..n2).
			return result;
		} else if (rule.jjtGetNumChildren() > 1) {
			// program rule consists of at most one head and at most one body
			unionMaps(result, fetchTermSorts((ASThead) (rule.jjtGetChild(0))));
			unionMaps(result, fetchTermSorts((ASTbody) (rule.jjtGetChild(1))));
		} else if (((SimpleNode) rule.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTHEAD) {
			unionMaps(result, fetchTermSorts((ASThead) (rule.jjtGetChild(0))));
		} else {
			unionMaps(result, fetchTermSorts((ASTbody) (rule.jjtGetChild(0))));
		}
		return result;
	}

	/**
	 * Fetch sorts for terms in rule's body given by AST node
	 * 
	 * @param rule
	 *            ASTnode to explore
	 * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	private HashMap<ASTterm, String> fetchTermSorts(ASTbody body) {
		HashMap<ASTterm, String> result = new HashMap<ASTterm, String>();
		for (int i = 0; i < body.jjtGetNumChildren(); i++) {
			unionMaps(result, fetchTermSorts((ASTatom) (body.jjtGetChild(i))));
		}
		return result;
	}

	/**
	 * Fetch sorts for terms occurring in the atom given by AST node
	 * 
	 * @param rule
	 *            ASTnode to explore
	 * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	private HashMap<ASTterm, String> fetchTermSorts(ASTatom atom) {
		HashMap<ASTterm, String> result = new HashMap<ASTterm, String>();

		if (((SimpleNode) atom.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM) {
			unionMaps(result,
					fetchTermSorts((ASTextendedNonRelAtom) atom.jjtGetChild(0)));
		}
		return result;
	}

	/**
	 * Fetch sorts for terms occurring in the extended non-relational atom given by AST node
	 * 
	 * @param rule
	 *            ASTnode to explore
	 * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	private HashMap<ASTterm, String> fetchTermSorts(
			ASTextendedNonRelAtom atom) {
		ASTpredSymbol pred = (ASTpredSymbol) atom.jjtGetChild(0);
		if (atom.jjtGetNumChildren() == 1) { // 0-arity
			return new HashMap<ASTterm,String>();
		}
		return fetchTermSorts((ASTtermList) (atom.jjtGetChild(1)), (pred.hasPoundSign()? "#":"") + pred.image);
	}

	
	/**
	 * Check if given sort is defined by a sort expression which is a union of disjoint records
	 * (see the details below) 
	 * @param sort sort name
	 * @return true iff sort is defined by an expression of the form #sort= expr_1 + ...expr_n,
	 *         where 1) each of expr_1,...,expr_n is either of the form fi(#si_1,...,#si_ki), or is a sort #si 
	 *         defined as #si = fi(#si_1,...,#si_ki),
	 *         2) fi are pairwise distinct
	 * 
	 */
	private boolean isSumOfDisjointRecordSorts(String sort) {
		ASTsortExpression expr = sortNameToExpression.get(sort);
	    SimpleNode exprChild = (SimpleNode) expr.jjtGetChild(0);
	    if(exprChild.getId()!=SparcTranslatorTreeConstants.JJTSETEXPRESSION)
	    	return false;
	    // from now on we can assume childExpr is a set expression!
	    ASTadditiveSetExpression addExpr = (ASTadditiveSetExpression) exprChild.jjtGetChild(0);
	    
	    if(addExpr.image.indexOf('-')!=-1)
	    	return false; // we should only have additions!
	    
	    // collect record names from the sort definition in this set
	    // this set is used to check if the record names are pairwise distinct 
	    HashSet<String> recordNames = new HashSet<String>();
	    
	    for(int i=0;i<addExpr.jjtGetNumChildren();i++) {
		    ASTmultiplicativeSetExpression multExpr = (ASTmultiplicativeSetExpression) addExpr.jjtGetChild(i);
		    ASTunarySetExpression unaryExpr = (ASTunarySetExpression)multExpr.jjtGetChild(0);
		    SimpleNode unarExprChild = (SimpleNode)unaryExpr.jjtGetChild(0);
		    String recordName = null;   
		    if(unarExprChild.getId() == SparcTranslatorTreeConstants.JJTSORTNAME) {
		    	String argSort = unarExprChild.image;
		    	ASTsortExpression argSortExpression = sortNameToExpression.get(argSort);
		      	SimpleNode argExprChild = (SimpleNode) argSortExpression.jjtGetChild(0);
		      	if(argExprChild.getId()!=SparcTranslatorTreeConstants.JJTSETEXPRESSION)
			    	return false;
		        // from now on we can assume argExprChild is a set expression!
		        ASTadditiveSetExpression argAddExpr = (ASTadditiveSetExpression) argExprChild.jjtGetChild(0);
		   	 	// we must have exactly one child:
		        if(argAddExpr.jjtGetNumChildren()!=1)
		        	return false;
		        // and this child should be a record
		        ASTmultiplicativeSetExpression argMultExpr = (ASTmultiplicativeSetExpression) argAddExpr.jjtGetChild(0);
			    ASTunarySetExpression argUnaryExpr = (ASTunarySetExpression)argMultExpr.jjtGetChild(0);
			    SimpleNode argUnarExprChild = (SimpleNode)argUnaryExpr.jjtGetChild(0);
			    if(argUnarExprChild.getId()!=SparcTranslatorTreeConstants.JJTFUNCTIONALSYMBOL)
			    	return false;
			    if(argUnarExprChild.jjtGetNumChildren()!=1)// if we have a condition, generate the entire sort
			    	return false;
			    recordName = argUnarExprChild.image.substring(0, argUnarExprChild.image.indexOf('('));
			} else if(unarExprChild.getId() == SparcTranslatorTreeConstants.JJTFUNCTIONALSYMBOL) {
				if(unarExprChild.jjtGetNumChildren()!=1) // if we have a condition, generate the entire sort
					return false;
				recordName = unarExprChild.image.substring(0, unarExprChild.image.indexOf('('));
		    } else {
		    	return false; 
		    }
		    // check for pairwise distinction:
		    if(recordNames.contains(recordName))
		    	return false;
		     recordNames.add(recordName);			          	
	    }
		return true;
	}
	
	
	/**
	 * @param sort
	 * @param recordName
	 * @param arity
	 * @return if isSumOfDisjointRecordSorts(sort), and fi(#s1,...,sn) is a part of the sort definition,
	 * such that n = arity, and f = recordName, then return [#s1,...,#sn]
	 * otherwise, return null
	 */
	private ArrayList<String> getRecordSorts(String sort , String recordName, int arity) {
		if(!isSumOfDisjointRecordSorts(sort))
			return null;	
		ASTsortExpression expr = sortNameToExpression.get(sort);
	    SimpleNode exprChild = (SimpleNode) expr.jjtGetChild(0);
	    ASTadditiveSetExpression addExpr = (ASTadditiveSetExpression) exprChild.jjtGetChild(0);
	    // a variable to store the list of sort expression for record's arguments:
	    ASTsortExpressionList sortExprList = null;
	    for(int i=0;i<addExpr.jjtGetNumChildren();i++) {
		    ASTmultiplicativeSetExpression multExpr = (ASTmultiplicativeSetExpression) addExpr.jjtGetChild(i);
		    ASTunarySetExpression unaryExpr = (ASTunarySetExpression)multExpr.jjtGetChild(0);
		    SimpleNode unarExprChild = (SimpleNode)unaryExpr.jjtGetChild(0);
		    if(unarExprChild.getId() == SparcTranslatorTreeConstants.JJTSORTNAME) {
		    	String argSort = unarExprChild.image;
		    	ASTsortExpression argSortExpression = sortNameToExpression.get(argSort);
		      	SimpleNode argExprChild = (SimpleNode) argSortExpression.jjtGetChild(0);
		        ASTadditiveSetExpression argAddExpr = (ASTadditiveSetExpression) argExprChild.jjtGetChild(0);
		   	    ASTmultiplicativeSetExpression argMultExpr = (ASTmultiplicativeSetExpression) argAddExpr.jjtGetChild(0);
			    ASTunarySetExpression argUnaryExpr = (ASTunarySetExpression)argMultExpr.jjtGetChild(0);
			    SimpleNode argUnarExprChild = (SimpleNode)argUnaryExpr.jjtGetChild(0);
			    String sortRecordName = argUnarExprChild.image.substring(0, argUnarExprChild.image.indexOf('('));
			    if(recordName.equals(sortRecordName)) {
			        sortExprList = (ASTsortExpressionList)argUnarExprChild.jjtGetChild(0);		       
			    }			    
			} else if(unarExprChild.getId() == SparcTranslatorTreeConstants.JJTFUNCTIONALSYMBOL) {
				String sortRecordName = unarExprChild.image.substring(0, unarExprChild.image.indexOf('('));
				if(recordName.equals(sortRecordName)) {
			        sortExprList = (ASTsortExpressionList)unarExprChild.jjtGetChild(0);
		        } 			          	
			}
	    }
	    if(sortExprList.jjtGetNumChildren()!=arity)
	    	   return null;
	    return sortExprList.getSortNames();
	    
	}

	
	
	
	
	/**
	 * Fetch sorts for terms with variables occurring in the predicate
	 * 
	 * @param termList
	 *            list of terms which are arguments of the predicate
	 * @param predicateName
	 *            name of the predicate
	 * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	private  HashMap<ASTterm, String> fetchTermSorts(
			ASTtermList termList, String predicateName) {
	
		HashMap<ASTterm, String> result = new HashMap<ASTterm, String>();
		ArrayList<String> argumentSortList=predicateArgumentSorts.get(predicateName);
		for (int i=0;i<termList.jjtGetNumChildren();i++) {
			ASTterm term=(ASTterm)termList.jjtGetChild(i);
			if(term.hasVariables()) {	
				// add new atoms sort(term) iff it is not the same as original atom predicateName(term).
				if(!predicateName.startsWith("#")) {
					
				   // if the term t is of the form f(t1,...,tn) and the sort is of the form
				   // #s = #s1 + ...#sn,
				   // where 1) each of #s1,...,sn is either of the form fi(#si_1,...,#si_ki), or is defined as
			       // #si = fi(#si_1,...,#si_ki),
				   // 2) the records fi  are pairwise distinct 
				   // and there exists fi in {f1,...,fn}  s.t, fi = f and n = ki
				   // then add the sort atoms si_1(f1),...si_n(fn) to the body!
				   String sort = argumentSortList.get(i);
				   // a variable that will store #si_1,..#s_in, in case all the conditions are satisfied:
				   ArrayList<String> recordSorts;
				     if(isSumOfDisjointRecordSorts(sort) && term.isRecord() && 
							   ((recordSorts = getRecordSorts(sort, term.getRecordName(), term.getRecordArgs().size())) !=null)) { 
						   ArrayList<ASTterm> recordArgTerms = term.getRecordArgs();
						   for(int j=0; j < recordArgTerms.size() ;j++) {
							   result.put(recordArgTerms.get(j), recordSorts.get(j));
						   }
					   }
				   
				   else {// add #s(#t) to the body
				       result.put(term,sort);
				   }
				}
			}
		}
		return result;
	}


	
	/**
	 * Fetch sorts for terms with variables occurring in the aggregate Element
	 * 
	 * @param agr
	 *            element to explore
	 * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	//public HashMap<ASTterm,String> fetchTermSorts(
	//		ASTaggregate agr) {
	//	for (int i = 0; i < agr.jjtGetNumChildren(); i++) {
	//		if (((SimpleNode) (agr.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTAGGREGATEELEMENTS) {
	//			return fetchTermSorts((ASTaggregateElements) (agr
	//					.jjtGetChild(i)));
	//		}
	//	}
	//	return null;// we should never be here!
	//}

	/**
	 * Fetch sorts for terms with variables occurring in the aggregate Elements
	 * //assuming all local variables are properly renamed
	 * @param agrelems
	 *            elements to explore
	 * 
	 * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	
	//private HashMap<ASTterm,String> fetchTermSorts(
	//		ASTaggregateElements agrelems) {/

//		HashMap<ASTterm,String>  result = new HashMap<ASTterm,String>();
//		for (int i = 0; i < agrelems.jjtGetNumChildren(); i++) {
	//		unionMaps(result,
		//			fetchTermSorts((ASTaggregateElement) (agrelems
			//				.jjtGetChild(i))));
	//	}
		//return result;
//	}

	/**
	 * Fetch sorts for terms with variables occurring in the aggregate Element
	 * /assuming all local variables are properly renamed
	 * @param agrelem
	 *            element to explore
	 *            /
	 * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	public HashMap<ASTterm,String>  fetchTermSorts(
			ASTaggregateElement argelem) {
		HashMap<ASTterm,String> result = new HashMap<ASTterm,String>();
		for (int i = 0; i < argelem.jjtGetNumChildren(); i++) {
	           if (((SimpleNode) (argelem.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDSIMPLEATOMLIST) {
				  unionMaps(result,
						fetchTermSorts((ASTextendedSimpleAtomList) argelem
								.jjtGetChild(i)));
			}
		}
		return result;
	}

	/**
	 * Fetch sorts for terms with variables occurring in the extended simple atoms(see grammar) given by
	 * AST node
	 * 
	 * @param exList
	 *            AST node to explore
	 * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	private HashMap<ASTterm,String> fetchTermSorts(
			ASTextendedSimpleAtomList exList) {
		HashMap<ASTterm,String> result = new HashMap<ASTterm,String>();
		for (int i = 0; i < exList.jjtGetNumChildren(); i++) {
			unionMaps(result,
					fetchTermSorts((ASTsimpleAtom) exList.jjtGetChild(i)));
		}
		return result;
	}

	/**
	 * Fetch sorts for terms with variables occurring in the simple atom(see grammar) given by
	 * AST node
	 * 
	 * @param atom
	 *            AST node to explore
     * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	private  HashMap<ASTterm,String> fetchTermSorts(
			ASTsimpleAtom atom) {
		HashMap<ASTterm,String> result = new HashMap<ASTterm,String>();
		if (((SimpleNode) atom.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM) {
			unionMaps(result,
					fetchTermSorts((ASTextendedNonRelAtom) atom.jjtGetChild(0)));
		}
		return result;
	}

	/**
	 * Fetch sorts for terms with variables occurring in the  non-relational atom given by AST node. 
	 * Relational atoms are atoms of the form [term1] [relation][term2]
	 * 
	 * @param nonRelAtom
	 *            AST node to explore
     * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	private HashMap<ASTterm,String>fetchTermSorts(
			ASTnonRelAtom nonRelAtom) {

		ASTpredSymbol pred = (ASTpredSymbol) nonRelAtom.jjtGetChild(0);
		if (nonRelAtom.jjtGetNumChildren() == 1) { // 0-arity
			return new HashMap<ASTterm,String>();
		}
		return fetchTermSorts((ASTtermList) (nonRelAtom.jjtGetChild(1)),
				pred.image);
	}


	/**
	 * Fetch sorts for terms with variables occurring in the  head given by AST node
	 * 
	 * @param head
	 *            ASTnode to explore
     * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	private HashMap<ASTterm,String> fetchTermSorts(ASThead head) {
		if (((SimpleNode) (head.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTDISJUNCTION) {
			return fetchTermSorts((ASTdisjunction) (head.jjtGetChild(0)));
		} else {
			return new HashMap<ASTterm,String>();
		}
	}

	/**
	 * Fetch sorts for terms with variables occurring in the choice rule given by AST node
	 * 
	 * @param choice_rule
	 *            ASTnode to explore
     * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
//	public HashMap<ASTterm,String> fetchTermSorts(
//			ASTchoice_rule choice_rule) {
//
//		for (int i = 0; i < choice_rule.jjtGetNumChildren(); i++) {
//			if (((SimpleNode) (choice_rule.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTCHOICE_ELEMENTS) {
//				return fetchTermSorts((ASTchoice_elements) (choice_rule
	//					.jjtGetChild(i)));
	//		}
//		}
//		return null; // DEAD CODE!
//	}

	/**
	 * Fetch sorts for terms with variables occurring in the choice rule elements given by AST node
	 * 
	 * @param choice_elements
	 *            elements to explore
     * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */

//	private HashMap<ASTterm,String>  fetchTermSorts(
//			ASTchoice_elements choice_elements) {
//		HashMap<ASTterm,String> result = new HashMap<ASTterm,String>();
//
//		for (int i = 0; i < choice_elements.jjtGetNumChildren(); i++) {
//			unionMaps(result,
//					fetchTermSorts((ASTchoice_element) (choice_elements
//							.jjtGetChild(i))));
//		}
//		return result;
//	}

	/**
	 * Fetch sorts for terms with variables occurring in the choice rule element given by AST node
	 * 
	 * @param choice_element
	 *            element to explore
     * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	public HashMap<ASTterm,String> fetchTermSorts(
			ASTchoice_element choice_element) {

		HashMap<ASTterm,String> result = new  HashMap<ASTterm,String>();
		for (int i = 0; i < choice_element.jjtGetNumChildren(); i++) {
			if (((SimpleNode) (choice_element.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTNONRELATOM) {
				unionMaps(result,
						fetchTermSorts((ASTnonRelAtom) choice_element
								.jjtGetChild(i)));
			} else if (((SimpleNode) (choice_element.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDSIMPLEATOMLIST) {
				unionMaps(
						result,
						fetchTermSorts((ASTextendedSimpleAtomList) choice_element
								.jjtGetChild(i)));
			}
		}
		return result;
	}

	/**
	 * Fetch sorts for terms with variables occurring in the disjunction given by AST node
	 * 
	 * @param disjunction
	 *            ASTnode to explore
     * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	private  HashMap<ASTterm,String> fetchTermSorts(
			ASTdisjunction disjunction) {
		 HashMap<ASTterm,String> result = new HashMap<ASTterm,String>();
		for (int i = 0; i < disjunction.jjtGetNumChildren(); i++) {
			unionMaps(result,
					fetchTermSorts((ASTnonRelAtom) disjunction.jjtGetChild(i)));
		}
		return result;
	}

	/**
	 * Fetch sorts for terms occurring in the consistency restoring rule given by AST node
	 * 
	 * @param crrule
	 *            rule to explore
     * @return term->sort_name mapping, where sort_name is a name of sort which
	 *         describes a set of strings this term, being grounded, must belong to in order to
	 *         obey sort definitions
	 */
	public HashMap<ASTterm,String> fetchTermSorts(
			ASTunlabeledProgramCrRule crrule) {
		HashMap<ASTterm,String> result = new HashMap<ASTterm,String>();
		for (int i = 0; i < crrule.jjtGetNumChildren(); i++) {
			if (((SimpleNode) (crrule.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTHEAD) {
				unionMaps(result,
						fetchTermSorts((ASThead) crrule.jjtGetChild(i)));
			} else {// body
				unionMaps(result,
						fetchTermSorts((ASTbody) crrule.jjtGetChild(i)));
			}
		}
		return result;
	}
}
