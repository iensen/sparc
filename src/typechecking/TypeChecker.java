package typechecking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import parser.ASTadditiveSortExpression;
import parser.ASTaggregate;
import parser.ASTaggregateElement;
import parser.ASTaggregateElements;
import parser.ASTarithmeticTerm;
import parser.ASTatom;
import parser.ASTbody;
import parser.ASTchoice_element;
import parser.ASTchoice_elements;
import parser.ASTchoice_rule;
import parser.ASTdisjunction;
import parser.ASTextendedNonRelAtom;
import parser.ASTextendedSimpleAtomList;
import parser.ASThead;
import parser.ASTmultiplicativeSortExpression;
import parser.ASTnonRelAtom;
import parser.ASTpredSymbol;
import parser.ASTprogramRule;
import parser.ASTprogramRules;
import parser.ASTregularExpression;
import parser.ASTsimpleAtom;
import parser.ASTsortExpression;
import parser.ASTsortExpressionList;
import parser.ASTsymbolicConstant;
import parser.ASTsymbolicFunction;
import parser.ASTsymbolicTerm;
import parser.ASTterm;
import parser.ASTtermList;
import parser.ASTunarySortExpression;
import parser.ASTunlabeledProgramCrRule;
import parser.ASTunlabeledProgramRule;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;
import re.RegularExpression;
import sorts.Condition;
import sorts.Relation;
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
    // sort instance generator:
	private InstanceGenerator gen;
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
			InstanceGenerator gen) {
		this.sortNameToExpression = sortNameToExpression;
		this.predicateArgumentSorts = predicateArgumentSorts;
		this.constantsMapping = constantsMapping;
		this.gen=gen;
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
	 * @return variable->sort_expression mapping, where sort expression
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

			HashSet<String> varsInParams=null;
		//	System.out.println(rule.jjtGetNumChildren());
			if(rule.jjtGetNumChildren()>1)
			    varsInParams = 
					  fetchVariableNames((SimpleNode) rule.jjtGetChild(1));
			else varsInParams=new HashSet<String>();
			HashSet<String> varsInBody = 
					  fetchVariableNames((SimpleNode) rule.jjtGetChild(0));
			for (String s : varsInParams) {
				if (!varsInBody.contains(s)) {
					throw new ParseException(inputFileName + ": "
							+ "variable in weak constraint paramethers"
							+ " at line " + rule.getBeginLine() + ", column "
							+ rule.getBeginColumn()
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
						+ predicateName + " of arity 1 at line "
						+ pred.getBeginLine() + ", column "
						+ pred.getBeginColumn() + " was not declared");
			}
			int from = Integer.parseInt(range[1]);
			int to = Integer.parseInt(range[2]);
			for (int i = from; i <= to; i++) {
				TermCreator tc = new TermCreator(Integer.toString(i));
				ASTterm term = tc.createSimpleArithmeticTerm();
				if (!checkTerm(
						term,
						sortNameToExpression.get(predicateArgumentSorts.get(
								predicateName).get(0)))) {
					throw new ParseException(inputFileName + ": "
							+ "Argument number " + 1 + " of predicate "
							+ predicateName + "/1" + ", \"" + from + ".." + to
							+ "\"," + " at line " + pred.getBeginLine()
							+ ", column " + pred.getBeginColumn()
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
	private void checkAtom(ASTatom atom) throws ParseException {
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
	private void checkExtendedNonRelAtom(ASTextendedNonRelAtom atom)
			throws ParseException {
		ASTpredSymbol pred = (ASTpredSymbol) atom.jjtGetChild(0);
		ASTtermList list = null;
		if (atom.jjtGetNumChildren() == 1) {// 0- arity predicate
			list = new ASTtermList(SparcTranslatorTreeConstants.JJTTERMLIST);
		} else {
			list = (ASTtermList) (atom.jjtGetChild(1));
		}
		checkAtomTermList(list, pred.image, pred.getBeginLine(),
				pred.getBeginColumn());
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
					+ termList.jjtGetNumChildren() + " at line " + beginLine
					+ ", column " + beginColumn + " was not declared");
		}
		ASTsortExpressionList sortList = new ASTsortExpressionList(
				SparcTranslatorTreeConstants.JJTSORTEXPRESSIONLIST);
		for (String sortName : predicateArgumentSorts.get(predicateName)) {
			sortList.jjtAddChild(sortNameToExpression.get(sortName),
					sortList.jjtGetNumChildren());
		}

		checkTermListWithEx(termList, sortList, predicateName, beginLine,
				beginColumn);
	}

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
			ASTsortExpressionList exprList, String predicateName,
			int beginLine, int beginColumn) throws ParseException {
		for (int i = 0; i < termList.jjtGetNumChildren(); i++) {
			ASTterm termToCheck=(ASTterm)termList.jjtGetChild(i);
			String sortName= predicateArgumentSorts.get(predicateName).get(i);
			if (
					termToCheck.hasVariables() && 
					!checkTermWithVariables(termToCheck,sortName) ||
					!termToCheck.hasVariables() && 
					!checkTerm(termToCheck,
					(ASTsortExpression) (exprList.jjtGetChild(i)))) {
				throw new ParseException(inputFileName + ": "
						+ "argument number " + (i + 1) + " of predicate "
						+ predicateName + "/" + termList.jjtGetNumChildren()
						+ ", \""
						+ ((ASTterm) termList.jjtGetChild(i)).toString()
						+ "\"," + " at line " + +beginLine + ", column "
						+ beginColumn + " violates definition of sort " + "\""
						+ sortName + "\"");
			}
		}
	}

	private boolean checkTermWithVariables(ASTterm termToCheck, String sortName) throws ParseException {
            gen.addSort(sortName, sortNameToExpression.get(sortName));
            Unifier unif=new Unifier(this);
            return unif.unify(termToCheck,gen.getSortInstances(sortName));
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
			if (((SimpleNode) (argelem.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTNONRELATOM) {
				checkNonRelAtom((ASTnonRelAtom) argelem.jjtGetChild(i));
			} else if (((SimpleNode) (argelem.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDSIMPLEATOMLIST) {
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
		checkAtomTermList(list, pred.image, pred.getBeginLine(),
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
	private boolean checkTerm(ASTarithmeticTerm term, ASTregularExpression reg) {
		TermEvaluator tr = new TermEvaluator(term);
		if (!tr.isEvaluable()) {
			return true;
		} else {
			RegularExpression regex = new RegularExpression(reg);
			try {
				return regex.check(Long.toString(tr.evaluate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Check term for satisfying given sort expression
	 * 
	 * @param term
	 *            to be checked
	 * @param expr
	 *            sort expression
	 * @return true if given term is not valuable(i.e, it has variables) or it
	 *         evaluates to a value satisfying pattern described by sort
	 *         expression and false otherwise
	 * @throws ParseException
	 *             if comparison between incomparable elements occurs (e.g, 1>a)
	 */
	private boolean checkTerm(ASTterm symterm, ASTsortExpression expr)
			throws ParseException {
		if (((SimpleNode) (symterm.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTVAR) {
			return true;
		}
		return checkTerm(symterm,
				(ASTadditiveSortExpression) expr.jjtGetChild(0));
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
	 * @throws ParseException
	 *             if comparison between incomparable elements occurs (e.g, 1>a)
	 */
	private boolean checkTerm(ASTterm symterm, ASTadditiveSortExpression expr)
			throws ParseException {
		boolean result = false;
		for (int i = 0; i < expr.jjtGetNumChildren(); i++) {
			boolean curBelong = (expr.image.charAt(i) == '+');
			if (curBelong) {
				if (checkTerm(symterm,
						(ASTmultiplicativeSortExpression) expr.jjtGetChild(i))) {
					result = true;
				}
			} else {
				// there are may be a better approach to this, but for now--
				// don't substract anything if there are variables in the term!
				if (!symterm.hasVariables() && checkTerm(symterm,
						(ASTmultiplicativeSortExpression) expr.jjtGetChild(i))) {
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
	private boolean checkTerm(ASTterm term, ASTmultiplicativeSortExpression expr)
			throws ParseException {
		boolean result = true;
		for (int i = 0; i < expr.jjtGetNumChildren(); i++) {
			if (!checkTerm(term, (ASTunarySortExpression) expr.jjtGetChild(0))) {
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
	boolean checkTerm(ASTterm term, ASTunarySortExpression expr)
			throws ParseException {
		if (expr.image.endsWith(")")) {
			return checkTerm(term, (ASTsortExpression) expr.jjtGetChild(0));
		} else if (expr.image.matches("[a-z][a-z,A-Z,0-9,_]*")) {
			return checkTerm(term, sortNameToExpression.get(expr.image));
		} else if (expr.image.endsWith("(")) { // functional term
			return checkTerm(term,
					expr.image.substring(0, expr.image.length() - 1),
					(ASTsortExpressionList) expr.jjtGetChild(0));
		} else if (expr.image.startsWith("$")) { // regular expression
			return checkTerm(term, (ASTregularExpression) expr.jjtGetChild(0));
		} else // range
		{
			String[] range = expr.image.split(" ");
			int from = Integer.parseInt(range[0]);
			int to = Integer.parseInt(range[1]);
			return CheckRangeTerm(term, from, to);
		}
	}

	/**
	 * Check term evaluation range
	 * 
	 * @param term
	 *            to be checked
	 * @param from
	 *            range minimal number
	 * @param to
	 *            range maximal number
	 * @return true if term evaluates to a value from range [from..to] or it is
	 *         arithmetic has variables, otherwise return false
	 */
	private boolean CheckRangeTerm(ASTterm term, int from, int to) {
		if (((SimpleNode) term.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTSYMBOLICTERM) {
			if (constantsMapping.containsKey(term.toString())) {
				TermCreator tc = new TermCreator(constantsMapping.get(
						term.toString()).toString());
				return CheckRangeTerm(tc.createSimpleArithmeticTerm(), from, to);
			} else {
				return false;
			}
		} else {
			TermEvaluator teval = new TermEvaluator(
					(ASTarithmeticTerm) term.jjtGetChild(0));
			if (teval.isEvaluable()) {
				long value = 0;
				try {
					value = teval.evaluate();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return from <= value && value <= to;
			} else {
				return true;
			}
		}
	}

	/**
	 * Check if term may evaluate into a string satisfying pattern described by
	 * regular expression.
	 * 
	 * @param term
	 *            term to be checked
	 * @param reg
	 *            regular expression specifying pattern
	 * @return true if term is arithmetic and there are some variables or there
	 *         is a substitution for variables of term such that, being applied,
	 *         it makes the term to evaluate to a string satisfying the pattern
	 *         described by regular expression
	 */
	private boolean checkTerm(ASTterm term, ASTregularExpression reg) {

		if (((SimpleNode) term.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTARITHMETICTERM) {
			return checkTerm((ASTarithmeticTerm) term.jjtGetChild(0), reg);
		} else {
			return checkTerm((ASTsymbolicTerm) term.jjtGetChild(0), reg);
		}
	}

	/**
	 * Check if term may evaluate into a string satisfying pattern described by
	 * regular expression.
	 * 
	 * @param term
	 *            term to be checked
	 * @param reg
	 *            regular expression specifying pattern
	 * @return true if there is a substitution for variables of term such that,
	 *         being applied, it makes the term to evaluate to a string
	 *         satisfying the pattern described by regular expression
	 */
	private boolean checkTerm(ASTsymbolicTerm term, ASTregularExpression reg) {
		if (term.jjtGetNumChildren() > 1) {
			return false;
		} else {
			return checkTerm((ASTsymbolicConstant) term.jjtGetChild(0), reg);
		}
	}

	/**
	 * Check symbolic constant for satisfying the pattern described by regular
	 * expression reg
	 * 
	 * @param term
	 *            to be checked
	 * @param reg
	 *            regular expression specifying the pattern
	 * @return true if symbolic constant satisfies the pattern described by
	 *         regular expression reg
	 */
	private boolean checkTerm(ASTsymbolicConstant term, ASTregularExpression reg) {
		RegularExpression regexp = new RegularExpression(reg);
		return regexp.check(term.image);
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
			ASTsortExpressionList exprList) throws ParseException {
		if (((SimpleNode) term.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTARITHMETICTERM) {
			return false;
		} else {
			return checkTerm((ASTsymbolicTerm) term.jjtGetChild(0), termName,
					exprList);
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
			ASTsortExpressionList exprList) throws ParseException {
		if (term.jjtGetNumChildren() == 1) {
			return false;
		} else {
			// TODO:
			String condition = "";
			if (termName.indexOf('{') != -1) {
				condition = termName.substring(termName.indexOf('{'),
						termName.indexOf('}') + 1);
                  termName=termName.substring(0,termName.indexOf('{'));
				//ASTsymbolicFunction child = (ASTsymbolicFunction) (term
				//		.jjtGetChild(0));
				//child.image = child.image
				//		.substring(0, child.image.length() - 1)
				//		+ termName.substring(termName.indexOf('{')) + '(';
			}
			boolean result= checkSymbolicTermName(
					(ASTsymbolicFunction) (term.jjtGetChild(0)), termName)
					&& checkTermList((ASTtermList) term.jjtGetChild(1),
							exprList)
					&& checkCondition(condition,
							(ASTtermList) term.jjtGetChild(1));
			return result;
		}
	}

	/**
	 * Check if condition is met for given term list Condition specifies
	 * relation between some two elements of term list
	 * 
	 * @param conditionStr
	 *            string denoting the condition, it is of the form [idx1]
	 *            [relation] [idx2] , where idx1 and idx2 are indexes of
	 *            elements of term list which will be checked
	 * @param termList
	 * @return true if condition is met
	 * @throws ParseException
	 *             if condition specify less or greater relation between
	 *             different types of terms (arithmetic and symbolic)
	 */
	private boolean checkCondition(String conditionStr, ASTtermList termList)
			throws ParseException {
		if (conditionStr.equals("")) {
			return true;
		}
		Condition cond = new Condition(conditionStr);
		return checktwoTerms(
				(ASTterm) termList.jjtGetChild(cond.getFirstArgument()),
				(ASTterm) termList.jjtGetChild(cond.getSecondArgument()),
				cond.getRelation());

	}

	/**
	 * Check symbolic and arithmetic terms for satisfying given relation
	 * 
	 * @param t1
	 *            first term to check
	 * @param t2
	 *            second term to check
	 * @param rel
	 *            given relation
	 * @return true if given terms satisfy given relation
	 * @throws ParseException
	 *             if types of terms are different (i.e, one of them is
	 *             arithmetic and the other one is symbolic) and relation is not
	 *             equality or non-equality
	 */
	private boolean checktwoTerms(ASTterm t1, ASTterm t2, Relation rel)
			throws ParseException {
		// if there is one variable
		if (((SimpleNode) t1.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTVAR
				|| ((SimpleNode) t2.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTVAR)
			return true;

		// arithmetic+arithmetic:
		if (((SimpleNode) (t1.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTARITHMETICTERM
				&& ((SimpleNode) (t2.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTARITHMETICTERM) {
			return checkTwoArithmeticTerms(
					(ASTarithmeticTerm) (t1.jjtGetChild(0)),
					(ASTarithmeticTerm) (t2.jjtGetChild(0)), rel);

		}
		// symb+symb:
		else if (((SimpleNode) (t1.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTSYMBOLICTERM
				&& ((SimpleNode) (t2.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTSYMBOLICTERM) {
			return checkTwoSymbolicTerms((ASTsymbolicTerm) (t1.jjtGetChild(0)),
					(ASTsymbolicTerm) (t2.jjtGetChild(0)), rel);
		}
		// symbolic+arithmetic
		else {
			if (((SimpleNode) (t1.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTARITHMETICTERM) {
				ASTterm buf = t1;
				t1 = t2;
				t2 = buf;
			}
			return checkSymbolicAndArithmeticTerm(
					(ASTsymbolicTerm) (t1.jjtGetChild(0)),
					(ASTarithmeticTerm) (t2.jjtGetChild(0)), rel);
		}
	}

	/**
	 * Check symbolic and arithmetic terms for satisfying given relation
	 * 
	 * @param asTsymbolicTerm
	 *            symbolic term to check
	 * @param asTarithmeticTerm
	 *            arithmetic term to check
	 * @param rel
	 *            given relation (may be only equal or not equal)
	 * @return true if given terms satisfy given relation
	 * @throws ParseException
	 *             if relation is not equality or non-equality
	 */
	private boolean checkSymbolicAndArithmeticTerm(
			ASTsymbolicTerm asTsymbolicTerm,
			ASTarithmeticTerm asTarithmeticTerm, Relation rel)
			throws ParseException {
		if (rel != Relation.EQUAL && rel != Relation.NOTEQUAL) {
			throw new ParseException(inputFileName + ": "
					+ "TYPE ERROR: symbolic term at line "
					+ asTsymbolicTerm.getBeginLine() + ", column "
					+ asTsymbolicTerm.getBeginColumn()
					+ " is incomparable with " + " arithmetic term at line "
					+ asTarithmeticTerm.getBeginLine() + ", column "
					+ asTarithmeticTerm.getBeginColumn()
					+ " with respect to relations <,>,<=,>=");

		} else {
			if (rel == Relation.EQUAL) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * Check two symbolic terms for satisfying given relation
	 * 
	 * @param asTsymbolicTerm
	 *            first term to check
	 * @param asTsymbolicTerm2
	 *            second term to check
	 * @param rel
	 *            relation between terms to be checked
	 * @return true if terms may satisfy given relation after making any
	 *         variable/value substitutions
	 */
	private boolean checkTwoSymbolicTerms(ASTsymbolicTerm asTsymbolicTerm,
			ASTsymbolicTerm asTsymbolicTerm2, Relation rel)
			throws ParseException {
		SimpleNode childof1=(SimpleNode)asTsymbolicTerm.jjtGetChild(0);
		SimpleNode childof2=(SimpleNode)asTsymbolicTerm2.jjtGetChild(0);
		
		if (rel != Relation.EQUAL && rel != Relation.NOTEQUAL && 
				(childof1.getId()!=SparcTranslatorTreeConstants.JJTSYMBOLICCONSTANT ||
				 childof2.getId()!=SparcTranslatorTreeConstants.JJTSYMBOLICCONSTANT)) {
			throw new ParseException(inputFileName + ": "
					+ "TYPE ERROR: symbolic term at line "
					+ asTsymbolicTerm.getBeginLine() + ", column "
					+ asTsymbolicTerm.getBeginColumn()
					+ " is incomparable with " + " symbolicTerm term at line "
					+ asTsymbolicTerm2.getBeginLine() + ", column "
					+ asTsymbolicTerm2.getBeginColumn()
					+ " with respect to relations <,>,<=,>=");

		} else {
			if (asTsymbolicTerm.jjtGetNumChildren() != asTsymbolicTerm2
					.jjtGetNumChildren()) {
				if (rel == Relation.EQUAL) {
					return false;
				} else {
					return true;
				}
			} else if (asTsymbolicTerm.jjtGetNumChildren() == 1) {
				ASTsymbolicConstant constant1 = (ASTsymbolicConstant) asTsymbolicTerm
						.jjtGetChild(0);
				ASTsymbolicConstant constant2 = (ASTsymbolicConstant) asTsymbolicTerm2
						.jjtGetChild(0);
				return Condition.checkRelation(rel, constant1.image,constant2.image);

			} else { // two childrens
				ASTsymbolicConstant function1 = (ASTsymbolicConstant) asTsymbolicTerm
						.jjtGetChild(0);
				ASTsymbolicConstant function2 = (ASTsymbolicConstant) asTsymbolicTerm2
						.jjtGetChild(0);

				ASTtermList tlist1 = (ASTtermList) asTsymbolicTerm
						.jjtGetChild(1);
				ASTtermList tlist2 = (ASTtermList) asTsymbolicTerm2
						.jjtGetChild(1);

				boolean listequal = tlist1.jjtGetNumChildren() == tlist2
						.jjtGetNumChildren();
				if (listequal) {
					for (int i = 0; i < tlist1.jjtGetNumChildren(); i++) {
						if (!checktwoTerms((ASTterm) tlist1.jjtGetChild(i),
								(ASTterm) tlist2.jjtGetChild(i), rel)) {
							listequal = false;
						}
					}
				}
				boolean imageequal = function1.image.equals(function2.image);

				return rel == Relation.EQUAL && imageequal && listequal
						|| rel == Relation.NOTEQUAL
						&& (!imageequal || !listequal);

			}

		}
	}

	/**
	 * Check two arithmetic terms for satisfying given relation
	 * 
	 * @param asTarithmeticTerm
	 *            first term to check
	 * @param asTarithmeticTerm2
	 *            second term to check
	 * @param rel
	 *            relation between terms to be checked
	 * @return true if terms may satisfy given relation after making any
	 *         variable/value substitutions
	 */
	private boolean checkTwoArithmeticTerms(
			ASTarithmeticTerm asTarithmeticTerm,
			ASTarithmeticTerm asTarithmeticTerm2, Relation rel) {
		TermEvaluator ev1 = new TermEvaluator(asTarithmeticTerm);
		TermEvaluator ev2 = new TermEvaluator(asTarithmeticTerm2);
		if (!ev1.isEvaluable() || !ev2.isEvaluable()) {
			return true;
		}
		long value1 = 0, value2 = 0;
		try {
			value1 = ev1.evaluate();
			value2 = ev2.evaluate();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		switch (rel) {
		case EQUAL:
			return value1 == value2;
		case NOTEQUAL:
			return value1 != value2;
		case GREATEROREQUAL:
			return value1 >= value2;
		case SMALLEROREQUAL:
			return value1 <= value2;
		case GREATER:
			return value1 > value2;
		case SMALLER:
			return value1 < value2;
		}
		return false;
	}

	/**
	 * Check each element of term list for satisfying pattern described by
	 * corresponding element of sort expression list
	 * 
	 * @param termList
	 *            term list
	 * @param exprList
	 *            sort expression list
	 * @return true if each element of term list satisfies pattern described by
	 *         corresponding element of
	 * @throws ParseException
	 */
	private boolean checkTermList(ASTtermList termList,
			ASTsortExpressionList exprList) throws ParseException {

		if (termList.jjtGetNumChildren() != exprList.jjtGetNumChildren()) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; i < termList.jjtGetNumChildren(); i++) {
				if (!checkTerm((ASTterm) termList.jjtGetChild(i),
						(ASTsortExpression) (exprList.jjtGetChild(i)))) {
					result = false;
				}
			}
			return result;
		}
	}

	/**
	 * Check if symbolic function has name equal to term name
	 * 
	 * @param symbol
	 *            symbolic function
	 * @param termName
	 *            term name
	 * @return true if the names are equal and false otherwise
	 */
	private boolean checkSymbolicTermName(ASTsymbolicFunction symbol,
			String termName) {

		return symbol.image.substring(0, symbol.image.length() - 1).equals(
				termName);

	}
}
