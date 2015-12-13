package translating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import parser.ASTadditiveSetExpression;
import parser.ASTbasicSort;
import parser.ASTconcatenation;
import parser.ASTcondition;
import parser.ASTconstantTerm;
import parser.ASTconstantTermList;
import parser.ASTcurlyBrackets;
import parser.ASTfunctionalSymbol;
import parser.ASTidentifierRange;
import parser.ASTmultiplicativeSetExpression;
import parser.ASTnumericRange;
import parser.ASTsetExpression;
import parser.ASTsortExpression;
import parser.ASTsortExpressionList;
import parser.ASTsortName;
import parser.ASTunarySetExpression;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;
import sorts.Condition;

/**
 * Generator of instances (strings) satisfying given sort expression
 */
public class InstanceGenerator {
	ArrayList<GSort> generatedSorts; // generated and included into translation
										// sorts
	HashMap<String, ASTsortExpression> sortNameToExpression;
	int ruleBeginLine;
	int ruleBeginColumn;
	HashSet<String> addedSorts;

	public ArrayList<GSort> getGeneratedSorts() {
		return generatedSorts;
	}

	/**
	 * Constructor
	 * 
	 * @param sortNameToExpression
	 *            a mapping from sort names to expressions assigned to them
	 */
	public InstanceGenerator(
			HashMap<String, ASTsortExpression> sortNameToExpression) {
		this.sortNameToExpression = sortNameToExpression;
		generatedSorts = new ArrayList<GSort>();
	}

	/**
	 * Add new sort to the set of generated sorts
	 * 
	 * @param sortName
	 *            name of the sort
	 * @param expr
	 *            expression to be assigned to the name
	 * @throws ParseException
	 *             if the sort has too many instances (over limit)
	 */
	public void addSort(String sortName, ASTsortExpression expr,boolean generateSorts)
			throws ParseException {

		// check if sort was already generated
		for (GSort s : generatedSorts) {
			if (s.sortName.equals(sortName)) {
				return;
			}
		}
		// generate new sort
		HashSet<String> instances = generateInstances_p(expr,generateSorts);

		generatedSorts.add(new GSort(expr, sortName, instances));
	}

	public HashSet<String> generateInstances(ASTsortExpression expr,
			boolean generateRecords) {
		HashSet<String> result = null;
		try {
			result = generateInstances_p(expr, generateRecords);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Get sort instances of given sort
	 * 
	 * @param sortName
	 *            of the sort
	 * @return list of instances
	 */
	public ArrayList<String> getSortInstances(String sortName) {
		for (GSort sort : generatedSorts) {
			if (sort.sortName.equals(sortName)) {
				return new ArrayList<String>(sort.instances);
			}
		}
		return null;
	}

	/**
	 * Intersection of two sets
	 * 
	 * @param s1
	 *            first set
	 * @param s2
	 *            second set
	 * @return s1*s2
	 */
	private HashSet<String> intersectSets(HashSet<String> s1, HashSet<String> s2) {
		HashSet<String> res = new HashSet<String>();
		res.addAll(s1);
		for (String s : s1) {
			if (!s2.contains(s)) {
				res.remove(s);
			}
		}
		return res;
	}

	/**
	 * Intersection of sorts named by sort names.
	 * 
	 * @param sortNames
	 * @return Set of strings each of which belongs to intersection.
	 */
	public HashSet<String> intersectGeneratedSorts(ArrayList<String> sortNames) {
		HashSet<String> result = new HashSet<String>();
		boolean initialized = false;
		for (String sortName : sortNames) {
			for (GSort candidateSort : generatedSorts) {
				if (candidateSort.sortName.equals(sortName)) {
					if (!initialized) {
						initialized = true;
						result.addAll(candidateSort.instances);
					} else {
						result = intersectSets(result, candidateSort.instances);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Move to a lower level of AST to produce all instances satisfying given
	 * sort expression
	 * 
	 * @param se
	 *         expression
	 * @param generateRecords
	 *         records are generated if the flag is true
	 * @return set of instances satisfying expression
	 * @throws ParseException
	 *             if se produces too many instances
	 */
	private HashSet<String> generateInstances_p(ASTsortExpression se,
			boolean generateRecords) throws ParseException {
		int id = ((SimpleNode) se.jjtGetChild(0)).getId();
		switch (id) {
		case SparcTranslatorTreeConstants.JJTSETEXPRESSION:
			return generateInstances((ASTsetExpression) se.jjtGetChild(0),
					generateRecords);
		case SparcTranslatorTreeConstants.JJTNUMERICRANGE:
			return generateInstances((ASTnumericRange) se.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTIDENTIFIERRANGE:
			return generateInstances((ASTidentifierRange) se.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTCONCATENATION:
			return generateInstances((ASTconcatenation) se.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTFUNCTIONALSYMBOL:
			if (generateRecords)
				return generateInstances((ASTfunctionalSymbol) se
						.jjtGetChild(0));
			else
				return new HashSet<String>();
		}

		return generateInstances((ASTadditiveSetExpression) se.jjtGetChild(0),generateRecords);

	}

	private HashSet<String> generateInstances(ASTfunctionalSymbol funcSymbol)
			throws ParseException {
		ASTsortExpressionList elist = (ASTsortExpressionList) (funcSymbol
				.jjtGetChild(0));
		ArrayList<HashSet<String>> alist = new ArrayList<HashSet<String>>();
		for (int i = 0; i < elist.jjtGetNumChildren(); i++) {
			ASTsortName sname = (ASTsortName) elist.jjtGetChild(i);
			alist.add(generateInstances_p(
					sortNameToExpression.get(sname.toString()), true));
		}

		String initprefix = funcSymbol.image.substring(0,
				funcSymbol.image.indexOf('('));
		ASTcondition cond = null;
		if (funcSymbol.jjtGetNumChildren() > 1) {
			cond = (ASTcondition) funcSymbol.jjtGetChild(1);
		}
		return generateInstances(initprefix, null, alist, 0, cond);
	}

	/**
	 * Generate instances of the concatenation of basic sorts from sortlist
	 * starting from given index
	 * 
	 * @param sortList
	 *            list of basic sorts
	 * @param index
	 *            starting index
	 * @return set of generated instances
	 * @throws ParseException
	 */
	private HashSet<String> generateInstances(ArrayList<ASTbasicSort> sortList,
			int index) throws ParseException {
		if (index == sortList.size()) {
			return new HashSet<String>();
		}
		HashSet<String> instancesPrefix = generateInstances(sortList.get(index));
		HashSet<String> instancesSuffix = generateInstances(sortList, index + 1);
		if (instancesSuffix.size() == 0) {
			return instancesPrefix;
		} else {
			HashSet<String> result = new HashSet<String>();
			for (String prefix : instancesPrefix)
				for (String suffix : instancesSuffix)
					result.add(prefix + suffix);

			return result;
		}

	}

	/**
	 * Generate all instances of given basic sort
	 * 
	 * @param asTbasicSort
	 *            AST node represeting given basic sort
	 * @return set of generated instances
	 * @throws ParseException
	 */
	private HashSet<String> generateInstances(ASTbasicSort basicSortExp)
			throws ParseException {
		SimpleNode child = (SimpleNode) basicSortExp.jjtGetChild(0);
		switch (child.getId()) {
		case SparcTranslatorTreeConstants.JJTNUMERICRANGE:
			return generateInstances((ASTnumericRange) basicSortExp
					.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTIDENTIFIERRANGE:
			return generateInstances((ASTidentifierRange) basicSortExp
					.jjtGetChild(0));
		case SparcTranslatorTreeConstants.JJTSORTNAME:
			ASTsortExpression expr = sortNameToExpression
					.get(child.image);
			// from parsing phase we know that "expr" is restricted to basic
			// sorts,
			// but we still follow the general procedure
			return generateInstances_p(expr,false);
		case SparcTranslatorTreeConstants.JJTCONSTANTTERMLIST:
			return generateInstances((ASTconstantTermList) basicSortExp
					.jjtGetChild(0),false);
		}
		return null;
	}

	/**
	 * Generate all instances of concatenation
	 * 
	 * @param jjtGetChild
	 *            AST node describing the concatenation
	 * @return all the instances obtained by the concatenation
	 * @throws ParseException
	 */
	private HashSet<String> generateInstances(ASTconcatenation concatenationExpr)
			throws ParseException {
		ArrayList<ASTbasicSort> sortList = new ArrayList<ASTbasicSort>();
		for (int i = 0; i < concatenationExpr.jjtGetNumChildren(); i++) {
			sortList.add((ASTbasicSort) concatenationExpr.jjtGetChild(i));
		}
		return generateInstances(sortList, 0);
	}

	/**
	 * Generate lexicographically next string with length <=maxLength
	 * 
	 * @param currentString
	 * @param maxLength
	 * @return
	 */
	String generateNextString(String currentString, int maxLength) {
		int currentIndex = currentString.length() - 1;
		while (currentIndex >= 0 && currentString.charAt(currentIndex) == 'z') {
			currentIndex--;
		}
		if (currentIndex == -1) {
			// need to increase length:
			if (currentString.length() == maxLength) {
				return null;
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < maxLength + 1; i++) {
					sb.append('a');
				}
				return sb.toString();
			}
		} else {
			StringBuilder nextString = new StringBuilder();
			if (currentIndex - 1 >= 0) {
				nextString.append(currentString.substring(0, currentIndex));
			}
			nextString.append((char) (currentString.charAt(currentIndex) + 1));
			for (int i = currentIndex + 1; i < currentString.length(); i++) {
				nextString.append('a');
			}
			return nextString.toString();
		}
	}

	/**
	 * Generate all strings s ,such that from<=s<=to and |from|<=|s|<=|to|
	 * 
	 * @param from
	 *            first string from the range
	 * @param to
	 *            last string from the range
	 * @return set of generated strings
	 */
	public HashSet<String> generateStrings(String from, String to) {
		HashSet<String> result = new HashSet<String>();
		result.add(from);
		String currentString = from;
		int maxLength = to.length();
		while ((currentString = generateNextString(currentString, maxLength)) != null && currentString.compareTo(to)<=0) {
			result.add(currentString);
		}
		return result;
	}

	/**
	 * Generate all strings s of given identifier range from..to ,such that
	 * from<=s<=to and |from|<=|s|<=|to| @ * @return set of generated strings
	 */
	private HashSet<String> generateInstances(ASTidentifierRange identifierRange) {
		String[] range = identifierRange.image.split(" ");
		return generateStrings(range[0], range[1]);
	}

	/**
	 * Generate all instances of a set expression
	 * 
	 * @param setExpr
	 *            node of abstract syntax tree representing set expression
	 * @param generateRecords
	 *            if the flag is true records are generated
	 * @return set of strings belonging to language described by se
	 * @throws ParseException
	 *             if setExpr produces too many instances
	 */
	private HashSet<String> generateInstances(ASTsetExpression setExpr,
			boolean generateRecords) throws ParseException {
		return generateInstances(
				(ASTadditiveSetExpression) setExpr.jjtGetChild(0),
				generateRecords);
	}

	/**
	 * @param adde
	 *            additive expression
	 * @return set of strings belonging to language described by se
	 * @param generateRecords
	 *            if the flag is true records are generated
	 * @throws ParseException
	 *             if se produces too many instances
	 */
	private HashSet<String> generateInstances(ASTadditiveSetExpression adde,
			boolean generateRecords) throws ParseException {
		// TODO Auto-generated method stub
		HashSet<String> result = new HashSet<String>();
		for (int i = 0; i < adde.jjtGetNumChildren(); i++) {
			HashSet<String> newInstances = generateInstances(
					(ASTmultiplicativeSetExpression) (adde.jjtGetChild(i)),
					generateRecords);
			if (newInstances == null) {
				throw new ParseException("Rule at " + ruleBeginLine
						+ ", column " + ruleBeginColumn
						+ " requires too much memory to be processed, "
						+ " shorten your sorts or avoid unsafety");
			}
			if (adde.image.charAt(i) == '+') {
				result.addAll(newInstances);
			} else {
				result.removeAll(newInstances);
			}
		}
		return result;
	}

	/**
	 * @param multe
	 *            multiplicative expression
	 * @param generateRecords
	 *            if the flag is true records are generated
	 * @return set of strings belonging to language described by multe
	 * @throws ParseException
	 *             if se produces too many instances
	 */
	private HashSet<String> generateInstances(
			ASTmultiplicativeSetExpression multe, boolean generateRecords)
			throws ParseException {
		HashSet<String> result = new HashSet<String>();
		for (int i = 0; i < multe.jjtGetNumChildren(); i++) {
			if (i == 0) {
				result = generateInstances((ASTunarySetExpression) (multe
						.jjtGetChild(i)),generateRecords);
			} else {
				result = intersectSets(result,
						generateInstances((ASTunarySetExpression) (multe
								.jjtGetChild(i)),generateRecords));
				;
			}
		}
		return result;
	}

	/**
	 * Create term string from provided predicate names and arguments
	 * 
	 */
	private String createTermString(String termName, ArrayList<String> arguments) {
		StringBuilder result = new StringBuilder();
		result.append(termName);
		if (arguments.size() != 0) {
			result.append("(");
			boolean first = true;
			for (String argument : arguments) {
				if (first) {
					first = false;
				} else {
					result.append(',');
				}
				result.append(argument);
			}
			result.append(")");
		}
		return result.toString();
	}

	// TODO: Condition needs to be redone

	/**
	 * Recursively generate strings with given prefix and suffix composed from
	 * all possible combinations of strings from alist ended by closing
	 * parenthesis
	 * 
	 * @param prefix
	 *            prefix to be added to every list
	 * @param alist
	 *            list of possible strings
	 * @param index
	 *            start index of alist from which strings will be taken
	 * @return set of generated strings
	 */
	private HashSet<String> generateInstances(String termName,
			ArrayList<String> arguments, ArrayList<HashSet<String>> alist,
			int index, ASTcondition cond) {
		if (arguments == null)
			arguments = new ArrayList<String>();
		HashSet<String> result = new HashSet<String>();
		// last element
		if (index == alist.size() - 1) {

			for (String s : alist.get(index)) {
				// closing parenthesis
				arguments.add(s);
				if (cond == null || new Condition().check(cond, arguments)) {
					result.add(createTermString(termName, arguments));
				}

				arguments.remove(index);
			}
		} else {
			// recursive call
			for (String s : alist.get(index)) {
				arguments.add(s);
				result.addAll(generateInstances(termName, arguments, alist,
						index + 1, cond));
				arguments.remove(index);
			}
		}
		return result;
	}

	/**
	 * Generate set of strings described by expression une
	 * 
	 * @param une
	 *            unary expression
	 * @param generateRecords
	 *            if the flag is true records are generated
	 * @return set of strings belonging to language described by une
	 * @throws ParseException
	 *             there are too many instances
	 */
	private HashSet<String> generateInstances(ASTunarySetExpression une,boolean generateRecords)
			throws ParseException {
		// t=< POUND_SIGN > sortName()
		// n=curlyBrackets()
		// setExpression() < CP >
		SimpleNode child = (SimpleNode) une.jjtGetChild(0);
		if (child.getId() == SparcTranslatorTreeConstants.JJTSORTNAME) {
			return generateInstances_p(sortNameToExpression.get(child
					.toString()),generateRecords);
		} else if (child.getId() == SparcTranslatorTreeConstants.JJTCURLYBRACKETS) {
			ASTcurlyBrackets curlyBrackets = (ASTcurlyBrackets) child;
			ASTconstantTermList termList = (ASTconstantTermList) curlyBrackets
					.jjtGetChild(0);
			return generateInstances(termList,generateRecords);
		} else if(child.getId() == SparcTranslatorTreeConstants.JJTSETEXPRESSION) { // setExpression
			return generateInstances((ASTsetExpression) child,generateRecords);
		} else  { //Functional symbol
			if(generateRecords) {
				return generateInstances((ASTfunctionalSymbol)child);
			} else {
				return new HashSet<String>();
			}
		}
	}

	public HashSet<String> generateInstances(ASTconstantTermList termList,boolean generateRecords) {
		HashSet<String> result = new HashSet<String>();
		for (int i = 0; i < termList.jjtGetNumChildren(); i++) {
			ASTconstantTerm constantTerm = (ASTconstantTerm) termList
					.jjtGetChild(i);
			String constantString=constantTerm.toString();
			boolean isRecord=constantString.indexOf('(')!=-1;
			if(isRecord && generateRecords || !isRecord)
			    result.add(constantTerm.toString());
		}
		return result;
	}

	/**
	 * Generate instances of a numeric range
	 * 
	 * @param r
	 *            AST node describing the numeric range
	 * @return set of instances belonging to the sort
	 */
	HashSet<String> generateInstances(ASTnumericRange r) {
		String[] range = r.image.split(" ");
		int from = Integer.parseInt(range[0]);
		int to = Integer.parseInt(range[1]);
		return generateInstances(from, to);
	}

	/**
	 * Generate instances of a numeric range, consisting of consecutive numbers
	 * [from..to]
	 * 
	 * @param from
	 *            first number from numeric range
	 * @param to
	 *            last number
	 * @return
	 */

	HashSet<String> generateInstances(int from, int to) {
		HashSet<String> result = new HashSet<String>();
		for (int i = from; i <= to; i++) {
			result.add(Integer.toString(i));
		}
		return result;
	}

	/**
	 * class representing sort name and instances which belong to the sort
	 */
	class GSort {
		ASTsortExpression se;
		String sortName;
		HashSet<String> instances;

		public GSort(ASTsortExpression se, String sortName,
				HashSet<String> instances) {
			this.se = se;
			this.sortName = sortName;
			this.instances = instances;
		}
	}
}
