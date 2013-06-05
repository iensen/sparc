package translating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


import parser.ASTadditiveSortExpression;
import parser.ASTmultiplicativeSortExpression;
import parser.ASTregularExpression;
import parser.ASTsortExpression;
import parser.ASTsortExpressionList;

import parser.ASTunarySortExpression;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;
import re.RegularExpression;
import sorts.BuiltInSorts;
import sorts.Condition;

/**
 * Generator of instances (strings) satisfying given sort expression
 */
public class InstanceGenerator {
	ArrayList<GSort> generatedSorts; // generated and included into translation
										// sorts
	HashSet<String> busySortName; // sort names which were already used
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
	 * @param predicateArgumentSorts
	 *            a mapping from predicate names to the sorts of it's arguments
	 * @param sortNameToExpression
	 *            a mapping from sort names to expressions assigned to them
	 */
	public InstanceGenerator(
			HashMap<String, ArrayList<String>> predicateArgumentSorts,
			HashMap<String, ASTsortExpression> sortNameToExpression) {
		this.sortNameToExpression = sortNameToExpression;
		busySortName = new HashSet<String>();
		generatedSorts = new ArrayList<GSort>();
		// add predicates from program as busy sort names to avoid collisions:
		for (String sortName : predicateArgumentSorts.keySet())
			busySortName.add(sortName);
		HashMap<String, ASTsortExpression> bsorts = BuiltInSorts
				.getBuiltInSorts();
		for (String sortName : bsorts.keySet()) {
			busySortName.add(sortName);
		}
		// for generating new sort names;
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
	public void addSort(String sortName, ASTsortExpression expr)
			throws ParseException {

		// check if sort was already generated
		for (GSort s : generatedSorts) {
			if (s.sortName.equals(sortName)) {
				return;
			}
		}
		// generate new sort
		HashSet<String> instances = generateInstances_p(expr);
		generatedSorts.add(new GSort(expr, sortName, instances));
	}
	/**
	 * Get sort instances of given sort 
	 * @param sortName  of the sort
	 * @return list of instances
	 */
	public ArrayList<String> getSortInstances(String sortName) {
		for(GSort sort:generatedSorts) {
			if(sort.sortName.equals(sortName)) {
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
	 *            expression
	 * @return set of instances satisfying expression
	 * @throws ParseException
	 *             if se produces too many instances
	 */
	private HashSet<String> generateInstances_p(ASTsortExpression se)
			throws ParseException {

		return generateInstances((ASTadditiveSortExpression) se.jjtGetChild(0));

	}

	/**
	 * @param adde
	 *            additive expression
	 * @return set of strings belonging to language described by se
	 * @throws ParseException
	 *             if se produces too many instances
	 */
	private HashSet<String> generateInstances(ASTadditiveSortExpression adde)
			throws ParseException {
		// TODO Auto-generated method stub
		HashSet<String> result = new HashSet<String>();
		for (int i = 0; i < adde.jjtGetNumChildren(); i++) {
			HashSet<String> newInstances = generateInstances((ASTmultiplicativeSortExpression) (adde
					.jjtGetChild(i)));
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
	 * @return set of strings belonging to language described by multe
	 * @throws ParseException
	 *             if se produces too many instances
	 */
	private HashSet<String> generateInstances(
			ASTmultiplicativeSortExpression multe) throws ParseException {
		HashSet<String> result = new HashSet<String>();
		for (int i = 0; i < multe.jjtGetNumChildren(); i++) {
			if (i == 0) {
				result = generateInstances((ASTunarySortExpression) (multe
						.jjtGetChild(i)));
			} else {
				result = intersectSets(result,
						generateInstances((ASTunarySortExpression) (multe
								.jjtGetChild(i))));
				;
			}
		}
		return result;
	}
    /**
     * Create term string from provided predicate names and arguments
     * 
     */
	private String createTermString(String termName,ArrayList<String> arguments) {
		StringBuilder result=new StringBuilder();
		result.append(termName);
		if(arguments.size()!=0) {
			result.append("(");
			boolean first=true;
			for(String argument:arguments) {
				if(first) {
					first=false;
				}
				else {
					result.append(',');
				}
				result.append(argument);
			}
			result.append(")");
	   }
	   return result.toString();
	}
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
	private HashSet<String> generateInstances(String termName,ArrayList<String> arguments,
			ArrayList<HashSet<String>> alist, int index,Condition cond) {
		if(arguments==null) arguments=new ArrayList<String>();
		HashSet<String> result = new HashSet<String>();
		// last element
		if (index == alist.size() - 1) {

			for (String s : alist.get(index)) {
				// closing parenthesis
				//TODO: add condition check!
			
				arguments.add(s);
				if(cond==null || 
					cond.checkStrings(arguments.get(cond.getFirstArgument()),
							arguments.get(cond.getSecondArgument())))
				{
					result.add(createTermString(termName, arguments));
				}

				arguments.remove(index);
			}
		} else {
			// recursive call
			for (String s : alist.get(index)) {
				arguments.add(s);
				result.addAll(generateInstances(termName ,arguments, alist,
						index + 1,cond));
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
	 * @return set of strings belonging to language described by une
	 * @throws ParseException
	 *             there are too many instances
	 */
	private HashSet<String> generateInstances(ASTunarySortExpression une)
			throws ParseException {
		if (une.jjtGetNumChildren() > 0
				&& ((SimpleNode) (une.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTSORTEXPRESSIONLIST) {
			ASTsortExpressionList elist = (ASTsortExpressionList) (une
					.jjtGetChild(0));
			ArrayList<HashSet<String>> alist = new ArrayList<HashSet<String>>();
			for (int i = 0; i < elist.jjtGetNumChildren(); i++) {
				alist.add(generateInstances_p((ASTsortExpression) (elist
						.jjtGetChild(i))));
			}
			
			String initprefix = une.image.substring(0,
					une.image.indexOf('('));
			Condition cond=null;
			
			//remove condition:
			if(initprefix.indexOf('{')!=-1) {
			    cond=new Condition(initprefix.substring(initprefix.indexOf('{'),initprefix.indexOf('}')+1));
				initprefix=
						initprefix.substring(0,initprefix.indexOf('{'));
			}
		
			return generateInstances(initprefix,null, alist, 0,cond);
		} else if (une.image.endsWith(")")) {
			return generateInstances_p((ASTsortExpression) une.jjtGetChild(0));
		} else if (une.image.matches("[a-z][a-z,A-Z,0-9,_]*")) {// sort name
			return generateInstances_p(sortNameToExpression.get(une.image));
		} else if (une.image.startsWith("$")) {// regular expression
			RegularExpression re = new RegularExpression(
					(ASTregularExpression) une.jjtGetChild(0));
			return re.generate();
		} else { // range
			HashSet<String> result = new HashSet<String>();
			String[] range = une.image.split(" ");
			int from = Integer.parseInt(range[0]);
			int to = Integer.parseInt(range[1]);
			for (int i = from; i <= to; i++) {
				result.add(Integer.toString(i));
			}
			return result;
		}
	}

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
