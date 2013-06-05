package translating;

import java.util.HashSet;

import parser.ASTaggregateElement;
import parser.ASTbody;
import parser.ASTchoice_element;
import parser.ASTextendedSimpleAtomList;
import parser.ASTprogramRule;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;

/**
 * Fetch a set of unsafe variables from a program rule, choice rule or aggregate
 */
public class UnsafeVariableFetcher {
	/**
	 * Find unsafe variables in aggregate element
	 * 
	 * @param elem
	 *            aggregate element to be explored
	 * @return set of unsafe variables
	 */
	public HashSet<String> fetchUnsafeVariables(ASTaggregateElement elem) {
		HashSet<String> unsafeVariables = new HashSet<String>();
		// Syntax:
		// (nonRelAtom() | arithmeticTerm())
		// (< COMMA > (nonRelAtom() | arithmeticTerm()))* [< COLON >
		// extendedNonRelAtomList()]

		// Add variables before extendedSimpleAtomList to set of unsafe
		// variables
		for (int i = 0; i < elem.jjtGetNumChildren(); i++) {
			if (((SimpleNode) (elem.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDSIMPLEATOMLIST) {
				break;
			}
			unsafeVariables.addAll(fetchAllVariables(
					(SimpleNode) (elem.jjtGetChild(i)), Scope.LOCAL));
		}
		// Remove variables from unsafe set if they occur in the body (i.e,
		// extendedSimpleatomList)
		if (((SimpleNode) (elem.jjtGetChild(elem.jjtGetNumChildren() - 1)))
				.getId() == SparcTranslatorTreeConstants.JJTEXTENDEDSIMPLEATOMLIST) {
			ASTextendedSimpleAtomList list = (ASTextendedSimpleAtomList) (elem
					.jjtGetChild(elem.jjtGetNumChildren() - 1));
			unsafeVariables.addAll(fetchAllVarsFromNegative(list, true,
					Scope.LOCAL));
			unsafeVariables.removeAll(fetchAllVarsFromPositive(list, true,
					Scope.LOCAL));

		}
		return unsafeVariables;
	}

	/**
	 * Find unsafe variables in choice rule element
	 * 
	 * @param elem
	 *            aggregate element to be explored
	 * @return set of unsafe variables
	 */
	public HashSet<String> fetchUnsafeVariables(ASTchoice_element elem) {
		HashSet<String> unsafeVariables = new HashSet<String>();
		// Syntax:
		// nonRelAtom() [< COLON > extendedNonRelAtomList() ]

		// add all variables from nonRelAtom to unsafe variables set
		unsafeVariables.addAll(fetchAllVariables(
				(SimpleNode) (elem.jjtGetChild(0)), Scope.LOCAL));

		if (elem.jjtGetNumChildren() > 1) {
			ASTextendedSimpleAtomList list = (ASTextendedSimpleAtomList) (elem
					.jjtGetChild(1));
			// add all variables from negative part of extendedNonRelAtomList
			// to the set of unsafe variables
			unsafeVariables.addAll(fetchAllVarsFromNegative(list, true,
					Scope.LOCAL));
			// add all variables from negative part of extendedNonRelAtomList
			// to the set of unsafe variables
			unsafeVariables.removeAll(fetchAllVarsFromPositive(list, true,
					Scope.LOCAL));

		}
		return unsafeVariables;
	}

	/**
	 * Find global unsafe variables in a rule
	 * 
	 * @param rule
	 *            AST node of the rule to be explored
	 * @return set of global unsafe variables
	 */
	public HashSet<String> fetchUnsafeGlobalVariables(ASTprogramRule rule) {
		return fetchUnsafeGlobalVariables((SimpleNode) rule.jjtGetChild(0));
	}

	/**
	 * Find global unsafe variables in a rule
	 * 
	 * @param rule
	 *            AST node of the rule to be explored
	 * @return set of global unsafe variables
	 */
	private HashSet<String> fetchUnsafeGlobalVariables(SimpleNode rule) {
		HashSet<String> unsafeVariables = new HashSet<String>();
		for (int i = 0; i < rule.jjtGetNumChildren(); i++) {
			if (((SimpleNode) (rule.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTHEAD) {
				unsafeVariables.addAll(fetchAllVariables(
						(SimpleNode) (rule.jjtGetChild(i)), Scope.GLOBAL));
			}
		}

		ASTbody body = null;
		for (int i = 0; i < rule.jjtGetNumChildren(); i++) {
			if (((SimpleNode) (rule.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTBODY) {
				body = (ASTbody) rule.jjtGetChild(i);
			}
		}

		if (body != null) {
			// add all variables from negative part
			unsafeVariables.addAll(fetchAllVarsFromNegative(body, true,
					Scope.GLOBAL));
			// remove all variables which occur in positive part
			HashSet<String> toremove = fetchAllVarsFromPositive(body, true,
					Scope.GLOBAL);
			for (String s : toremove) {
				if (unsafeVariables.contains(s)) {
					unsafeVariables.remove(s);
				}
			}
		}
		return unsafeVariables;
	}

	/**
	 * Find all variables negative part of program rule, aggregate or choice
	 * rule
	 * 
	 * @param simpleNode
	 *            root of AST subtree to be explored
	 * @param positive
	 *            true if search is currently in atom which belongs to positive
	 *            part of rules body
	 * @param scope
	 * @return set of found variables
	 */
	private HashSet<String> fetchAllVarsFromNegative(SimpleNode simpleNode,
			boolean positive, Scope scope) {
		HashSet<String> result = new HashSet<String>();
		if (simpleNode.getId() == SparcTranslatorTreeConstants.JJTVAR
				&& (scope == Scope.GLOBAL
						&& !simpleNode.image.matches("^.*_L\\d+$") || scope == Scope.LOCAL
						&& !simpleNode.image.matches("^.*_G$")) && !positive) {
			result.add(simpleNode.image);
		}
		boolean curpositive = positive;
		if (simpleNode.getId() == SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM
				&& simpleNode.image != null
				&& simpleNode.image.trim().equals("not")) {
			// change positive to false, if not occurs
			curpositive = false;
		}
		// recursive search:
		for (int i = 0; i < simpleNode.jjtGetNumChildren(); i++) {
			result.addAll(fetchAllVarsFromNegative(
					(SimpleNode) simpleNode.jjtGetChild(i), curpositive, scope));
		}
		return result;
	}

	/**
	 * Find all variables in positive part of program rule, choice rule or
	 * aggregate
	 * 
	 * @param simpleNode
	 *            root of AST subtree to be explored
	 * @param positive
	 *            true if search is currently in atom which belongs to positive
	 *            part of rule's(aggregate's, choice rule's) body
	 * @param scope
	 *            (local or global).
	 * @return set of found variables
	 */
	private HashSet<String> fetchAllVarsFromPositive(SimpleNode simpleNode,
			boolean positive, Scope scope) {
		if (simpleNode.getId() == SparcTranslatorTreeConstants.JJTATOM
				|| simpleNode.getId() == SparcTranslatorTreeConstants.JJTSIMPLEATOM) {
			if (simpleNode.jjtGetNumChildren() == 0)
				return new HashSet<String>();
			SimpleNode child = (SimpleNode) (simpleNode.jjtGetChild(0));
			if (child.getId() != SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM
					&& child.getId() != SparcTranslatorTreeConstants.JJTAGGREGATE) {
				return new HashSet<String>();
			}
		}

		HashSet<String> result = new HashSet<String>();
		// global variables end by suffix _G, local variables -- by _L\\d+.
		if (simpleNode.getId() == SparcTranslatorTreeConstants.JJTVAR
				&& (scope == Scope.GLOBAL
						&& !simpleNode.image.matches("^.*_L\\d+$") || scope == Scope.LOCAL
						&& !simpleNode.image.matches("^.*_G$")) && positive) {
			result.add(simpleNode.image);
		}
		boolean curpositive = positive;
		if (simpleNode.getId() == SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM
				&& simpleNode.image != null
				&& simpleNode.image.trim().equals("not")) {
			curpositive = false;
		}
		// recursive search:
		for (int i = 0; i < simpleNode.jjtGetNumChildren(); i++) {
			result.addAll(fetchAllVarsFromPositive(
					(SimpleNode) simpleNode.jjtGetChild(i), curpositive, scope));
		}
		return result;
	}

	/**
	 * Find all variables under given scope (local or global)
	 * 
	 * @param simpleNode
	 *            AST node to explore
	 * @param scope
	 *            (local or global)
	 * @return set of found variable names
	 */
	private HashSet<String> fetchAllVariables(SimpleNode simpleNode, Scope scope) {
		HashSet<String> result = new HashSet<String>();
		if (simpleNode.getId() == SparcTranslatorTreeConstants.JJTVAR
				&& (scope == Scope.GLOBAL
						&& !simpleNode.image.matches("^.*_L\\d+$") || scope == Scope.LOCAL
						&& !simpleNode.image.matches("^.*_G$")))

		{
			result.add(simpleNode.image);
		}

		for (int i = 0; i < simpleNode.jjtGetNumChildren(); i++) {
			result.addAll(fetchAllVariables(
					(SimpleNode) simpleNode.jjtGetChild(i), scope));
		}
		return result;
	}
}

// Score is local inside aggregates and choice rules
enum Scope {
	LOCAL, GLOBAL
}
