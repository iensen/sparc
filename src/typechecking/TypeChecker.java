package typechecking;
//TODO:
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import parser.ASTadditiveSetExpression;
import parser.ASTaggregate;
import parser.ASTaggregateElement;
import parser.ASTaggregateElements;
import parser.ASTarithmeticTerm;
import parser.ASTatom;
import parser.ASTbasicSort;
import parser.ASTbody;
import parser.ASTchoice_element;
import parser.ASTchoice_elements;
import parser.ASTchoice_rule;
import parser.ASTconcatenation;
import parser.ASTcondition;
import parser.ASTconstantTerm;
import parser.ASTconstantTermList;
import parser.ASTcurlyBrackets;
import parser.ASTdisjunction;
import parser.ASTdisplay;
import parser.ASTextendedNonRelAtom;
import parser.ASTextendedSimpleAtomList;
import parser.ASTfunctionalSymbol;
import parser.ASThead;
import parser.ASTidentifierRange;
import parser.ASTmultiplicativeSetExpression;
import parser.ASTnonRelAtom;
import parser.ASTnumericRange;
import parser.ASTpredSymbol;
import parser.ASTprogramRule;
import parser.ASTprogramRules;
import parser.ASTsetExpression;
import parser.ASTsimpleAtom;
import parser.ASTsortExpression;
import parser.ASTsortExpressionList;
import parser.ASTsymbolicFunction;
import parser.ASTsymbolicTerm;
import parser.ASTterm;
import parser.ASTtermList;
import parser.ASTunarySetExpression;
import parser.ASTunlabeledProgramCrRule;
import parser.ASTunlabeledProgramRule;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;
import sorts.BuiltIn;
import sorts.Condition;
import translating.InstanceGenerator;


/**
 * Recursive descent typechecking implementation
 */
public class TypeChecker {
	// mapping from sort names to sort expressions assigned to the sorts
	private HashMap<String, ASTsortExpression> sortNameToExpression;
	// mapping from predicate names to a list of names of sorts describing
	// arguments
	private HashMap<String, ArrayList<String>> predicateArgumentSorts;
	// mapping from constants to their values
	protected HashMap<String, Long> constantsMapping;
	// name of SPARC file (used for error reporting).
	private String inputFileName = "";
	// List of all terms occuring in brackets
	private HashSet<String> curlyBracketTerms;
	// List of all defined record names
	private HashSet<String> definedRecordNames;
    // used for checking if sort contains a number
	private InstanceGenerator gen;
	
	public boolean ignoreLineNumbers;
	/**
	 * Constructor
	 * 
	 * @param sortNameToExpression
	 * @param predicateArgumentSorts
	 * @param constantsMapping
	 */
	public TypeChecker(HashMap<String, ASTsortExpression> sortNameToExpression,
			HashMap<String, ArrayList<String>> predicateArgumentSorts,
			HashMap<String, Long> constantsMapping,
			HashSet<String> curlyBracketTerms,
			HashSet<String> definedRecordNames,
			InstanceGenerator gen
			) {
		this.sortNameToExpression = sortNameToExpression;
		this.predicateArgumentSorts = predicateArgumentSorts;
		this.constantsMapping = constantsMapping;
		this.curlyBracketTerms = curlyBracketTerms;
		this.definedRecordNames = definedRecordNames;
		this.gen=gen;
		this.ignoreLineNumbers=false;
	}

	/**
	 * Input file name setter (file name is used for error reporting)
	 * 
	 * @param inputFileName
	 */
	public void setInputFileName(String fileName) {
		this.inputFileName = fileName;
	}

	/**
	 * Do typechecking of collections of rules given by parent AST node (each of
	 * the rules is a child of given rules node)
	 * 
	 * @param rules
	 * @throws ParseException
	 *             if type violation occurs
	 */
	public void checkRules(ASTprogramRules rules) throws ParseException {
		for (int i = 0; i < rules.jjtGetNumChildren(); i++) {
			checkProgramRule((ASTprogramRule) rules.jjtGetChild(i));
		}
	}

	/**
	 * Do typechecking of a rule given by AST node.
	 * 
	 * @param rule
	 *            to be checked
	 * @throws ParseException
	 *             if type violation occurs
	 */
	private void checkProgramRule(ASTprogramRule rule) throws ParseException {
		if (((SimpleNode) rule.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTUNLABELEDPROGRAMRULE) {
			checkUnlabeledProgramRule((ASTunlabeledProgramRule) rule
					.jjtGetChild(0));
		} else if (((SimpleNode) rule.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTUNLABELEDPROGRAMCRRULE) {
			checkUnlabeledProgramCrRule((ASTunlabeledProgramCrRule) rule
					.jjtGetChild(0));
		}
	}

	/**
	 * Fetch variables from given node
	 * 
	 * @param n
	 *            node to explore
	 *         describes a language of string each of which may be used as a
	 *         substitution for given variable
	 */
	private HashSet<String> fetchVariableNames(SimpleNode n) {
		HashSet<String> result = new HashSet<String>();
		if (n.getId() == SparcTranslatorTreeConstants.JJTVAR) {
			result.add(n.image);
		}
		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			result.addAll((fetchVariableNames((SimpleNode) (n.jjtGetChild(i)))));
		}
		return result;
	}

	/**
	 * Do typechecking of an unlabeled rule given by AST node.
	 * 
	 * @param rule
	 *            to be checked
	 * @throws ParseException
	 *             if type violation occurs
	 */
	private void checkUnlabeledProgramRule(ASTunlabeledProgramRule rule)
			throws ParseException {
		if (rule.image.trim().equals(":~")) {// weak constraint

			HashSet<String> varsInParams = null;
			// System.out.println(rule.jjtGetNumChildren());
			if (rule.jjtGetNumChildren() > 1)
				varsInParams = fetchVariableNames((SimpleNode) rule
						.jjtGetChild(1));
			else
				varsInParams = new HashSet<String>();
			HashSet<String> varsInBody = fetchVariableNames((SimpleNode) rule
					.jjtGetChild(0));
			for (String s : varsInParams) {
				if (!varsInBody.contains(s)) {
					throw new ParseException(inputFileName + ": "
							+ "variable in weak constraint paramethers"
							+(ignoreLineNumbers ?"": " at line " + rule.getBeginLine() + ", column "
							+ rule.getBeginColumn())
							+ " does not occur in the body");
				}

			}
			checkBody(((ASTbody) (rule.jjtGetChild(0))));
		} else if (rule.jjtGetNumChildren() > 0
				&& ((SimpleNode) rule.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTPREDSYMBOL) {
			/*
			 * image contain the name of a predicate and range of integers from
			 * its argument, p(1..3) results into "p 1 3" image
			 */
			ASTpredSymbol pred = (ASTpredSymbol) rule.jjtGetChild(0);
			String[] range = rule.image.split(" ");
			String predicateName = range[0];
			if (!predicateArgumentSorts.containsKey(predicateName)
					|| predicateArgumentSorts.get(predicateName).size() > 1) {
				throw new ParseException(inputFileName + ": " + "predicate "
						+ predicateName + " of arity 1 at "+(ignoreLineNumbers?"": "line "
						+ pred.getBeginLine() + ", column "
						+ pred.getBeginColumn()) + " was not declared");
			}
			int from = Integer.parseInt(range[1]);
			int to = Integer.parseInt(range[2]);
			for (int i = from; i <= to; i++) {
				ASTterm term = new ASTterm((long)i);
				if (!checkTerm(
						term,
						sortNameToExpression.get(predicateArgumentSorts.get(
								predicateName).get(0)))) {
					throw new ParseException(inputFileName + ": "
							+ "Argument number " + 1 + " of predicate "
							+ predicateName + "/1" + ", \"" + from + ".." + to
							+ "\"," + (ignoreLineNumbers?"":" at line " + pred.getBeginLine()
							+ ", column " + pred.getBeginColumn())
							+ " violates definition of sort " + "\""
							+ predicateArgumentSorts.get(predicateName).get(0)
							+ "\"");
				}
			}
		} else if (rule.jjtGetNumChildren() > 1) {
			checkHead((ASThead) (rule.jjtGetChild(0)));
			checkBody((ASTbody) (rule.jjtGetChild(1)));
		} else if (((SimpleNode) rule.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTHEAD) {
			checkHead((ASThead) (rule.jjtGetChild(0)));
		} else {
			checkBody((ASTbody) (rule.jjtGetChild(0)));
		}
	}

	/**
	 * Do typechecking of a rule body given by AST node.
	 * 
	 * @param body
	 *            to be checked
	 * @throws ParseException
	 *             if type violation occurs
	 */
	private void checkBody(ASTbody body) throws ParseException {
		for (int i = 0; i < body.jjtGetNumChildren(); i++) {
			checkAtom((ASTatom) (body.jjtGetChild(i)));
		}
	}

	/**
	 * Do typechecking of an atom given by AST node.
	 * 
	 * @param atom
	 *            to be checked
	 * @throws ParseException
	 *             if type violation occurs
	 */
	public void checkAtom(ASTatom atom) throws ParseException {
		if (((SimpleNode) atom.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTAGGREGATE) {
			checkAggregate((ASTaggregate) atom.jjtGetChild(0));
		} else if (((SimpleNode) atom.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM) {
			checkExtendedNonRelAtom((ASTextendedNonRelAtom) atom.jjtGetChild(0));
		}
	}

	/**
	 * Do typechecking of an extended non -relational atom given by AST node.
	 * Relational atom is atom of the form [term_1] [relation] [term_2].
	 * 
	 * @param atom
	 *            to be checked
	 * @throws ParseException
	 *             if type violation occurs
	 */
	private void checkExtendedNonRelAtom(ASTextendedNonRelAtom atom) //HERE
			throws ParseException {
		ASTpredSymbol pred = (ASTpredSymbol) atom.jjtGetChild(0);
		ASTtermList list = null;
		if (atom.jjtGetNumChildren() == 1) {// 0- arity predicate
			list = new ASTtermList(SparcTranslatorTreeConstants.JJTTERMLIST);
		} else {
			list = (ASTtermList) (atom.jjtGetChild(1));
		}
		checkAtomTermList(list, (pred.hasPoundSign()?"#":"")+ pred.image, pred.getBeginLine(),
				pred.getBeginColumn());
	}
	
	boolean isNumber(String s) {
		for(int i=0;i<s.length();i++) {
			if(!Character.isDigit(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	boolean containsNumber(HashSet<String> termSet) {
		for (String s:termSet) {
			if(isNumber(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Do typechecking of a predicate given by name and given in form of a term
	 * list
	 * 
	 * @param termList
	 *            list of arguments of given predicate
	 * @param predicateName
	 *            name of predicate
	 * @param beginLine
	 *            line of the source file where predicate occurs
	 * @param beginColumn
	 *            column of the source file where predicate occurs
	 * @throws ParseException
	 *             if an undeclared predicate found
	 */
	private void checkAtomTermList(ASTtermList termList, String predicateName,
			int beginLine, int beginColumn) throws ParseException {
		if (!predicateArgumentSorts.containsKey(predicateName)
				|| predicateArgumentSorts.get(predicateName).size() != termList
						.jjtGetNumChildren()) {
			throw new ParseException(inputFileName + ": " + "predicate "
					+ predicateName + " of arity "
					+ termList.jjtGetNumChildren() + (ignoreLineNumbers?"":" at line " + beginLine
					+ ", column " + beginColumn) + " was not declared");
		}
		ArrayList<String> sortNames=new ArrayList<String>();
		for (String sortName : predicateArgumentSorts.get(predicateName)) {
			sortNames.add(sortName);
		}

		checkTermListWithEx(termList, sortNames, predicateName, beginLine,
				beginColumn);
	}
	// mapping between sort names and boolean values
	// isNumeric[s] is true iff s contains at least one number
     HashMap<String, Boolean> isNumeric=null;
	/**
	 * Do typechecking of a predicate given by name and given in form of a term
	 * list
	 * 
	 * @param termList
	 *            list of arguments of given predicate
	 * @param predicateName
	 *            name of predicate
	 * @param sortExpressionList
	 *            list of sort expressions to be used for typechecking of given
	 *            term list
	 * @param beginLine
	 *            line of the source file where predicate occurs
	 * @param beginColumn
	 *            column of the source file where predicate occurs
	 * @throws ParseException
	 *             if sort violation occurs
	 */
	private void checkTermListWithEx(ASTtermList termList,
			ArrayList<String> sortNames, String predicateName,
			int beginLine, int beginColumn) throws ParseException {
		for (int i = 0; i < termList.jjtGetNumChildren(); i++) {
			ASTterm termToCheck = (ASTterm) termList.jjtGetChild(i);
			String sortName = predicateArgumentSorts.get(predicateName).get(i);
			ASTsortExpression sortExpression=sortNameToExpression.get(sortName);
			SimpleNode child=(SimpleNode) termToCheck.jjtGetChild(0);
			if(child.getId()==SparcTranslatorTreeConstants.JJTARITHMETICTERM) {
				// Check if term is evaluable arithmetic term and replace it
				// by its value if it is true
				TermEvaluator te=new TermEvaluator((ASTarithmeticTerm)child);
				if(te.isEvaluable()) {
				    termToCheck=new ASTterm(te.evaluate());	
				}
				
				// check if corresponding sort contains at least one number!
				boolean hasNumber=false;
				if(isNumeric==null) {
					isNumeric=new HashMap<String,Boolean>();
				}
				if(isNumeric.containsKey(sortName)) {
					hasNumber=isNumeric.get(sortName);
				}
				else {
					HashSet<String> instances=gen.generateInstances(sortExpression, false);
					hasNumber=containsNumber(instances);
					isNumeric.put(sortName, hasNumber);
				}
				if(!hasNumber)
				{
				  throw new ParseException(inputFileName + ": "
							+ "argument number " + (i + 1) + " of predicate "
							+ predicateName + "/"
							+ termList.jjtGetNumChildren() + ", \""
							+ ((ASTterm) termList.jjtGetChild(i)).toString()
							+ "\"," + (ignoreLineNumbers?"":" at line " + +beginLine + ", column "
							+ beginColumn) + " is an arithmetic term and not a variable, but "
							+ "\"" + "#" + sortName + "\""+" does not contain a number");
				}
				
			}
			boolean isGround = termToCheck.isGround();
			if (!isGround
					&& !checkNonGroundTerm(termToCheck)
					|| isGround
					&& !checkTerm(termToCheck,sortExpression)) {
				if (isGround)
					throw new ParseException(inputFileName + ": "
							+ "argument number " + (i + 1) + " of predicate "
							+ predicateName + "/"
							+ termList.jjtGetNumChildren() + ", \""
							+ ((ASTterm) termList.jjtGetChild(i)).toString()
							+ "\"," + (ignoreLineNumbers?"": " at line " + +beginLine + ", column "
							+ beginColumn) + " violates definition of sort "
							+ "\"" + "#"+ sortName + "\"");
				else {
					throw new ParseException(inputFileName + ": "
							+ "non-ground term \"" + termToCheck.toString()
							+ "\" occuring  in program as " + +(i + 1)
							+ " argument of predicate " + predicateName + "/"
							+ termList.jjtGetNumChildren()
						    + (ignoreLineNumbers?"": " at line " + +beginLine + ", column "
									+ beginColumn)+ " is not a program term");
				}
			}
		}
	}

	/**
	 * Check if the term with variables is a term of given progam
	 * 
	 * @param termToCheck
	 * @param sortName
	 * @return true if termToCheck is a program term
	 * @throws ParseException
	 */
	private boolean checkNonGroundTerm(ASTterm termToCheck) {
		SimpleNode child = (SimpleNode) termToCheck.jjtGetChild(0);
		switch(child.getId()) {
	    	case SparcTranslatorTreeConstants.JJTARITHMETICTERM:
	    		return checkConstants(termToCheck);
	    	case SparcTranslatorTreeConstants.JJTSYMBOLICTERM:
	    		return checkNonGroundTerm((ASTsymbolicTerm) termToCheck
						.jjtGetChild(0));
	    	default:
	    		return true;
		}
	}

	private boolean checkNonGroundTerm(ASTsymbolicTerm symTerm) {
		SimpleNode child0 = (SimpleNode) symTerm.jjtGetChild(0);
		if (child0.getId() == SparcTranslatorTreeConstants.JJTSYMBOLICCONSTANT) {
			return isDomainElement(new ASTterm(child0.image));
		} else {// symbolic Function

			String functionSymbol = child0.image.substring(0,
					child0.image.length() - 1);
			if (!definedRecordNames.contains(functionSymbol))
				return false;

			ASTtermList child1 = (ASTtermList) symTerm.jjtGetChild(1);

			for (int i = 0; i < child1.jjtGetNumChildren(); i++) {
				ASTterm term = (ASTterm) child1.jjtGetChild(i);
				if (term.isGround()) {
					if (!isDomainElement(term))
						return false;
				} else {
					if (!checkNonGroundTerm(term))
						return false;

				}
			}
			return true;

		}
	}

	/**
	 * Check all constants in given n (a subnode of a root of arithmetic term)
	 * 
	 * @param n
	 * @return true if all constants are in the range 0..maxint and false
	 *         otherwise
	 */
	private boolean checkConstants(SimpleNode n) {
		if (n.getId() == SparcTranslatorTreeConstants.JJTATOMICARITHMETICTERM) {
			if (n.jjtGetNumChildren() == 0) {
				int integer = Integer.parseInt(n.image);
				return integer >= 0 && integer <= BuiltIn.getMaxInt();
			}
		}
		boolean result = true;
		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			result = result & checkConstants((SimpleNode) n.jjtGetChild(i));
		}
		return result;
	}

	/**
	 * Check if term t belongs to program domain
	 * 
	 * @param t
	 * @return true if t is an element of program domain and false otherwise
	 */
	private boolean isDomainElement(ASTterm t) {
		String termString = t.toString();
		if (curlyBracketTerms.contains(termString))
			return true;
		for (ASTsortExpression se : sortNameToExpression.values()) {
			if (checkTerm(t, se))
				return true;
		}
		return false;
	}

	/**
	 * Do typechecking of an aggregate given by AST node
	 * 
	 * @param agr
	 *            aggregate to be checked
	 * @throws ParseException
	 *             if sort violation occurs or undeclared predicate found
	 */
	private void checkAggregate(ASTaggregate agr) throws ParseException {
		for (int i = 0; i < agr.jjtGetNumChildren(); i++) {
			if (((SimpleNode) (agr.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTAGGREGATEELEMENTS) {
				checkAggregateElements((ASTaggregateElements) (agr
						.jjtGetChild(i)));
			}
		}
	}

	/**
	 * Do typechecking of an aggregate given by AST node
	 * 
	 * @param agr
	 *            aggregate to be checked
	 * @throws ParseException
	 *             if sort violation occurs or undeclared predicate found
	 */
	private void checkAggregateElements(ASTaggregateElements argelems)
			throws ParseException {

		for (int i = 0; i < argelems.jjtGetNumChildren(); i++) {
			checkAggregateElement((ASTaggregateElement) (argelems
					.jjtGetChild(i)));
		}

	}

	/**
	 * Do typechecking of an aggregate element given by AST node
	 * 
	 * @param agrelem
	 *            aggregate element to be checked
	 * @throws ParseException
	 *             if sort violation occurs or undeclared predicate found
	 */
	private void checkAggregateElement(ASTaggregateElement argelem)
			throws ParseException {
		for (int i = 0; i < argelem.jjtGetNumChildren(); i++) {
			 if (((SimpleNode) (argelem.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDSIMPLEATOMLIST) {
				checkExtendedSimpleAtomList((ASTextendedSimpleAtomList) argelem
						.jjtGetChild(i));
			}
		}
	}

	/**
	 * Do typechecking of an extended simple atom list given by AST node
	 * 
	 * @param exList
	 *            extended simple atom list to be checked
	 * @throws ParseException
	 *             if sort violation occurs or undeclared predicate found
	 */
	private void checkExtendedSimpleAtomList(ASTextendedSimpleAtomList exList)
			throws ParseException {

		for (int i = 0; i < exList.jjtGetNumChildren(); i++) {
			checkSimpleAtom((ASTsimpleAtom) exList.jjtGetChild(i));
		}
	}

	/**
	 * Do typechecking of a simple atom given by AST node
	 * 
	 * @param simpleAtom
	 *            to be checked
	 * @throws ParseException
	 *             if sort violation occurs or undeclared predicate found
	 */
	private void checkSimpleAtom(ASTsimpleAtom simpleAtom)
			throws ParseException {
		if (simpleAtom.jjtGetNumChildren() > 0
				&& ((SimpleNode) (simpleAtom.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM) {
			checkExtendedNonRelAtom((ASTextendedNonRelAtom) (simpleAtom
					.jjtGetChild(0)));
		}
	}

	/**
	 * Do typechecking of a non -relational atom given by AST node. Relational
	 * atom is atom of the form [term_1] [relation] [term_2].
	 * 
	 * @param nonRelAtom
	 * @throws ParseException
	 *             if sort violation occurs or undeclared predicate found
	 */
	private void checkNonRelAtom(ASTnonRelAtom nonRelAtom)
			throws ParseException {
		ASTpredSymbol pred = (ASTpredSymbol) nonRelAtom.jjtGetChild(0);
		ASTtermList list = null;
		if (nonRelAtom.jjtGetNumChildren() == 1) {// 0- arity predicate
			list = new ASTtermList(SparcTranslatorTreeConstants.JJTTERMLIST);
		} else {
			list = (ASTtermList) (nonRelAtom.jjtGetChild(1));
		}
		checkAtomTermList(list,  (pred.hasPoundSign()?"#":"")+pred.image, pred.getBeginLine(),
				pred.getBeginColumn());
	}

	/**
	 * Do typechecking of a rule head given by AST node
	 * 
	 * @param head
	 *            to be checked
	 * @throws ParseException
	 *             if sort violation occurs or undeclared predicate found
	 */
	private void checkHead(ASThead head) throws ParseException {
		if (((SimpleNode) (head.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTDISJUNCTION) {
			checkDisjunction((ASTdisjunction) (head.jjtGetChild(0)));
		} else {
			checkChoiceRule((ASTchoice_rule) (head.jjtGetChild(0)));
		}
	}
	
	/**
	 * Do typechecking of choice rule given by AST node
	 * 
	 * @param choice_rule
	 *            to be checked
	 * @throws ParseException
	 *             if sort violation occurs or undeclared predicate found
	 */
	private void checkChoiceRule(ASTchoice_rule choice_rule)
			throws ParseException {
		for (int i = 0; i < choice_rule.jjtGetNumChildren(); i++) {
			if (((SimpleNode) (choice_rule.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTCHOICE_ELEMENTS) {
				checkChoiceElements((ASTchoice_elements) (choice_rule
						.jjtGetChild(i)));
			}
		}
	}

	/**
	 * Do typechecking of choice rule elements given by AST node
	 * 
	 * @param choice_elements
	 *            to be checked
	 * @throws ParseException
	 *             if sort violation occurs
	 */
	private void checkChoiceElements(ASTchoice_elements choice_elements)
			throws ParseException {
		for (int i = 0; i < choice_elements.jjtGetNumChildren(); i++) {
			checkChoiceElement((ASTchoice_element) (choice_elements
					.jjtGetChild(i)));
		}
	}

	/**
	 * Do typechecking of choice rule element given by AST node
	 * 
	 * @param choice_element
	 *            to be checked
	 * @throws ParseException
	 *             if sort violation occurs or undeclared predicate found
	 */
	private void checkChoiceElement(ASTchoice_element choice_element)
			throws ParseException {
		for (int i = 0; i < choice_element.jjtGetNumChildren(); i++) {
			if (((SimpleNode) (choice_element.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTNONRELATOM) {
				checkNonRelAtom((ASTnonRelAtom) choice_element.jjtGetChild(i));
			}
			
			if (((SimpleNode) (choice_element.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDSIMPLEATOMLIST) {
				checkExtendedSimpleAtomList((ASTextendedSimpleAtomList) choice_element.jjtGetChild(i));
			}
			
		
		}

	}

	/**
	 * Do typechecking of disjunction in the head of some rule given by AST node
	 * 
	 * @param disjunction
	 *            to be checked
	 * @throws ParseException
	 *             if sort violation occurs or undeclared predicate found
	 */
	private void checkDisjunction(ASTdisjunction disjunction)
			throws ParseException {

		for (int i = 0; i < disjunction.jjtGetNumChildren(); i++) {

			checkNonRelAtom((ASTnonRelAtom) disjunction.jjtGetChild(i));
		}
	}

	/**
	 * Do typechecking of unlabeled program consistency restoring rule given by
	 * AST node
	 * 
	 * @param crrule
	 *            consistency restoring rule to be checked
	 * @throws ParseException
	 *             if sort violation occurs
	 */
	private void checkUnlabeledProgramCrRule(ASTunlabeledProgramCrRule crrule)
			throws ParseException {
		for (int i = 0; i < crrule.jjtGetNumChildren(); i++) {
			if (((SimpleNode) (crrule.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTHEAD) {
				checkHead((ASThead) crrule.jjtGetChild(i));
			} else {
				checkBody((ASTbody) crrule.jjtGetChild(i));
			}
		}
	}

	/**
	 * Check arithmetic term for satisfying given regular expression
	 * 
	 * @param term
	 *            to be checked
	 * @param reg
	 *            regular expression
	 * @return true if term is not valuable(i.e, it has variables) or it
	 *         evaluates to a value satisfying pattern described by regular
	 *         expression and false otherwise
	 */

	/**
	 * Check term for satisfying given sort expression
	 * 
	 * @param symterm
	 *            to be checked
	 * @param expr
	 *            sort expression
	 * @return true if given term is not valuable(i.e, it has variables) or it
	 *         evaluates to a value satisfying pattern described by sort
	 *         expression and false otherwise
	 */

	private boolean checkTerm(ASTterm symterm, ASTsortExpression expr) {
		if (((SimpleNode) (symterm.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTVAR) {
			return true;
		}
		//
		int id = ((SimpleNode) expr.jjtGetChild(0)).getId();
		switch (id) {
		case SparcTranslatorTreeConstants.JJTSETEXPRESSION:
			return checkTerm(symterm, (ASTsetExpression) expr.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTNUMERICRANGE:
			return checkTerm(symterm, (ASTnumericRange) expr.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTIDENTIFIERRANGE:
			return checkTerm(symterm, (ASTidentifierRange) expr.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTCONCATENATION:
			return checkTerm(symterm, (ASTconcatenation) expr.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTFUNCTIONALSYMBOL:
			return checkTerm(symterm, (ASTfunctionalSymbol) expr.jjtGetChild(0));
		default:
			return false;
		}
	}

	private boolean checkTerm(ASTterm symterm, ASTfunctionalSymbol funcSymbol) {
		ASTsortExpressionList elist = (ASTsortExpressionList) (funcSymbol
				.jjtGetChild(0));
	
		String initprefix = funcSymbol.image.substring(0, funcSymbol.image.indexOf('('));
		ASTcondition cond = null;
		if(funcSymbol.jjtGetNumChildren()>1) {
			cond=(ASTcondition)funcSymbol.jjtGetChild(1);
		}
		return checkTerm(symterm,initprefix,elist,cond);
	}

	private boolean checkTerm(ASTterm symterm, ASTconcatenation conc) {
		String termString = symterm.toString();
		if (termString.indexOf(')') != -1)
			return false;
		int termLength = termString.length();
		int basicSortLength = conc.jjtGetNumChildren();
		int calculatedDp[][] = new int[termLength][basicSortLength];
		for (int i = 0; i < termLength; i++) {
			Arrays.fill(calculatedDp[i], -1);
		}
		return checkConcatenation(termString, conc, 0, 0, calculatedDp);
	}

	private boolean checkConcatenation(String term, ASTconcatenation conc,
			int stringIndex, int basicSortIndex, int calculatedDp[][]) {


		// length
		int termLength = term.length();
		int concLength = conc.jjtGetNumChildren();
		int result = 0;

		if (basicSortIndex == concLength && stringIndex == termLength) {
			return true;
		}
		if (basicSortIndex == concLength || stringIndex == termLength) {
			return false;
		}
		if (calculatedDp[stringIndex][basicSortIndex] != -1)
			return calculatedDp[stringIndex][basicSortIndex] != 0;
		
		for (int tryLength = 1; termLength - tryLength >= concLength-basicSortIndex-1 && stringIndex + tryLength<=termLength; tryLength++) {
			if (checkTerm(new ASTterm(term.substring(stringIndex,
					stringIndex + tryLength)),
					(ASTbasicSort) conc.jjtGetChild(basicSortIndex))
					&& checkConcatenation(term, conc, stringIndex + tryLength,
							basicSortIndex + 1, calculatedDp)) {
				result = 1;
				break;
			}
		}
		return (calculatedDp[stringIndex][basicSortIndex] = result) > 0;
	}

	private boolean checkTerm(ASTterm term, ASTbasicSort basicSort) {
		int id = ((SimpleNode) basicSort.jjtGetChild(0)).getId();
		switch (id) {
		case SparcTranslatorTreeConstants.JJTNUMERICRANGE:
			return checkTerm(term, (ASTnumericRange) basicSort.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTIDENTIFIERRANGE:
			return checkTerm(term,
					(ASTidentifierRange) basicSort.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTCONCATENATION:
			return checkTerm(term, (ASTconcatenation) basicSort.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTSORTNAME:
			return checkTerm(term,
					sortNameToExpression.get(((ASTsortExpression) basicSort
							.jjtGetChild(0)).image));
		case SparcTranslatorTreeConstants.JJTCONSTANTTERMLIST:
			return checkTerm(term,(ASTconstantTermList)basicSort
							.jjtGetChild(0));
		default:
			return false;
		}

	}

	private boolean checkTerm(ASTterm symterm, ASTidentifierRange range) {
		String termString = symterm.toString();
		if (termString.indexOf('(') != -1)
			return false;
		String[] rangeStrings = range.image.split(" ");
		return termString.length() >= rangeStrings[0].length()
				&& termString.length() <= rangeStrings[1].length()
				&& termString.compareTo(rangeStrings[0]) >= 0
				&& termString.compareTo(rangeStrings[1]) <= 0;
	}

	private boolean checkTerm(ASTterm symterm, ASTnumericRange range) {
		String termString = symterm.toString();
		if (termString.indexOf('(') != -1)
			return false;
		for (char c : termString.toCharArray()) {
			if (Character.isLetter(c))
				return false;
		}
		String[] rangeStrings = range.image.split(" ");
		int value = Integer.parseInt(symterm.toString());
		return value >= Integer.parseInt(rangeStrings[0])
				&& value <= Integer.parseInt(rangeStrings[1]);
	}

	private boolean checkTerm(ASTterm symterm, ASTsetExpression setExpr) {
		// TODO Auto-generated method stub
		return checkTerm(symterm,
				(ASTadditiveSetExpression) setExpr.jjtGetChild(0));
	}

	/**
	 * Check term for satisfying given sort expression
	 * 
	 * @param term
	 *            to be checked
	 * @param expr
	 *            sort expression
	 * @return true if given term is not valuable(i.e, it has variables) or it
	 *         evaluates to a value satisfying pattern described by additive
	 *         sort expression and false otherwise
	 */
	private boolean checkTerm(ASTterm symterm, ASTadditiveSetExpression expr) {
		boolean result = false;
		for (int i = 0; i < expr.jjtGetNumChildren(); i++) {
			boolean curBelong = (expr.image.charAt(i) == '+');
			if (curBelong) {
				if (checkTerm(symterm,
						(ASTmultiplicativeSetExpression) expr.jjtGetChild(i))) {
					result = true;
				}
			} else {
				if (checkTerm(symterm,
						(ASTmultiplicativeSetExpression) expr.jjtGetChild(i))) {
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * Check term for satisfying given sort expression
	 * 
	 * @param term
	 *            to be checked
	 * @param expr
	 *            sort expression
	 * @return true if given term is not valuable(i.e, it has variables) or it
	 *         evaluates to a value satisfying pattern described by
	 *         multiplicative sort expression and false otherwise
	 * @throws ParseException
	 *             if comparison between incomparable elements occurs (e.g, 1>a)
	 */
	private boolean checkTerm(ASTterm term, ASTmultiplicativeSetExpression expr){
		boolean result = true;
		for (int i = 0; i < expr.jjtGetNumChildren(); i++) {
			if (!checkTerm(term, (ASTunarySetExpression) expr.jjtGetChild(0))) {
				result = false;
			}
		}
		return result;

	}

	/**
	 * Check term for satisfying given unary sort expression
	 * 
	 * @param term
	 *            to be checked
	 * @param expr
	 *            unary sort expression
	 * @return true if given term is not valuable(i.e, it has variables) or it
	 *         evaluates to a value satisfying pattern described by unary sort
	 *         expression and false otherwise
	 * @throws ParseException
	 *             if comparison between incomparable elements occurs (e.g, 1>a)
	 */
	boolean checkTerm(ASTterm term, ASTunarySetExpression expr) {
		SimpleNode child = (SimpleNode) expr.jjtGetChild(0);
		switch (child.getId()) {
		case SparcTranslatorTreeConstants.JJTSORTNAME:
			return checkTerm(term,
					(ASTsortExpression) sortNameToExpression.get(child.image));
		case SparcTranslatorTreeConstants.JJTCURLYBRACKETS:
			return checkTerm(term, (ASTcurlyBrackets) child);
		case SparcTranslatorTreeConstants.JJTSETEXPRESSION:
			return checkTerm(term, (ASTsetExpression) child);
		case SparcTranslatorTreeConstants.JJTFUNCTIONALSYMBOL:
			return checkTerm(term,(ASTfunctionalSymbol)child);
		default:
			return false;
		}
	}

	private boolean checkTerm(ASTterm term, ASTcurlyBrackets curLyBrackets) {

		ASTconstantTermList constList = (ASTconstantTermList) curLyBrackets
				.jjtGetChild(0);
	    return checkTerm(term,constList);
	}
	
	private boolean checkTerm(ASTterm term, ASTconstantTermList constList) {
		String termString = term.toString();
		boolean result = false;
		for (int i = 0; i < constList.jjtGetNumChildren(); i++) {
			ASTconstantTerm curTerm = (ASTconstantTerm) constList
					.jjtGetChild(i);
			if (curTerm.toString().compareTo(termString) == 0) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Check term for being a symbolic function with given name and arguments
	 * satisfying patterns specified by sort expression list
	 * 
	 * @param term
	 *            to be checked
	 * @param termName
	 *            pattern name
	 * @param exprList
	 *            sort expression list of patterns
	 * @return false if term is not symbolic or it has name different from
	 *         termName or some of its arguments does not satisfy corresponding
	 *         expression from the provided list
	 * @throws ParseException
	 *             if order comparison between two unordered elements occurs
	 */
	boolean checkTerm(ASTterm term, String termName,
			ASTsortExpressionList exprList,ASTcondition cond) {
		if (((SimpleNode) term.jjtGetChild(0)).getId() != SparcTranslatorTreeConstants.JJTSYMBOLICTERM) {
			return false;
		} else {
			return checkTerm((ASTsymbolicTerm) term.jjtGetChild(0), termName,
					exprList,cond);
		}
	}

	/**
	 * Check term for being a symbolic function with given name and arguments
	 * satisfying patterns specified by sort expression list
	 * 
	 * @param term
	 *            to be checked
	 * @param termName
	 *            pattern name
	 * @param exprList
	 *            sort expression list of patterns
	 * @return false it has name different from termName or some of its
	 *         arguments does not satisfy corresponding expression from the
	 *         provided list
	 * @throws ParseException
	 *             if order comparison between two unordered elements occurs
	 */
	private boolean checkTerm(ASTsymbolicTerm term, String termName,
			ASTsortExpressionList exprList,ASTcondition cond) {
		if (term.jjtGetNumChildren() == 1) {
			return false;
		} else {
			// TODO:
		
			boolean result = checkSymbolicTermName(
					(ASTsymbolicFunction) (term.jjtGetChild(0)), termName)
					&& checkTermList((ASTtermList) term.jjtGetChild(1),
							exprList)
					&& checkCondition(cond,
							(ASTtermList) term.jjtGetChild(1));
			return result;
		}
	}

	/**
	 * Check if condition is met for given term list Condition specifies
	 * relation between some two elements of term list
	 * 
	 * @param conditionStr
	 *           AST node representing the condition
	 * @param termList
	 * @return true if condition is met
	 * @throws ParseException
	 *             if condition specify less or greater relation between
	 *             different types of terms (arithmetic and symbolic)
	 */
	private boolean checkCondition(ASTcondition cond, ASTtermList termList) {
       //create list of strings from term list:
		if(cond!=null) {
			
		ArrayList<String> terms=new ArrayList<String>();
		for(int i=0;i<termList.jjtGetNumChildren();i++) {
			ASTterm curTerm=(ASTterm)termList.jjtGetChild(i);
			terms.add(curTerm.toString());
		}
		Condition condition=new Condition();
		return condition.check(cond, terms);
		}
		else 
			return true;
	}

	/**
	 * Check each element of term list for satisfying pattern described by
	 * corresponding element of sort expression list
	 * 
	 * @param termList
	 *            term list
	 * @param exprList
	 *            sort expression list (consisting of special sort expession, sortNames)
	 * @return true if each element of term list satisfies pattern described by
	 *         corresponding element of
	 * @throws ParseException
	 */
	private boolean checkTermList(ASTtermList termList,
			ASTsortExpressionList exprList) {

		if (termList.jjtGetNumChildren() != exprList.jjtGetNumChildren()) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; i < termList.jjtGetNumChildren(); i++) {
				if (!checkTerm((ASTterm) termList.jjtGetChild(i),
						sortNameToExpression.get(((SimpleNode)exprList.jjtGetChild(i)).image))) {
					result = false;
				}
			}
			return result;
		}
	}

	private boolean checkSymbolicTermName(ASTsymbolicFunction symbol,
			String termName) {

		return symbol.image.substring(0, symbol.image.length() - 1).equals(
				termName);

	}

	
	/**
	 * Given an ast representing display section of a program, typecheck every atom occuring in it
	 * @param display - AST representing display section of the program
	 * @throws ParseException 
	 */
	public void checkDisplay(ASTdisplay display) throws ParseException{
	    for(int i=0;i<display.jjtGetNumChildren(); i++) {
        	ASTnonRelAtom atom = (ASTnonRelAtom) display.jjtGetChild(i);
        	ASTpredSymbol ps = (ASTpredSymbol) atom.jjtGetChild(0);
        	if(atom.jjtGetNumChildren() ==1) {
        		// we only need to check that corresponding predicate or sort name exists
        			// we need to check that such a sort name was defined!
        			if(!predicateArgumentSorts.containsKey((ps.hasPoundSign()?"#":"") + ps.image)) {
        				  throw new ParseException(inputFileName + ": "
      							+ "the " + (ps.hasPoundSign()?"sort ":"predicate ") + "\"" + ps.image
      							+ "\"," + (ignoreLineNumbers?"":"occurring at line " + +ps.getBeginLine() + ", column "
      							+ ps.getBeginColumn()) + " is not defined by the program");
      				}			
        	} else {
        		checkNonRelAtom(atom);
        	}
        	
	    }
	}
}


