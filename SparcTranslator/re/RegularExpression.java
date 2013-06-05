package re;

import java.util.HashSet;

import parser.ASTregularExpression;
import parser.SparcConstants;
import automata.DFA;

/**
 * Regular expression class. Creates deterministic finite automata instance to check given string for
 * membership to a set described by given regular expression or generate
 * instances of regular expression
 * 
 */
public class RegularExpression {
	DFA dfa;

	/**
	 * Constructor
	 * 
	 * @param tree
	 *            regular expression given in the form Abstract syntax tree.
	 */
	public RegularExpression(ASTregularExpression tree) {
		dfa = new DFA(tree);
	}

	/**
	 * Check if string s belong to a set of string described by regular
	 * expressions
	 * 
	 * @param s
	 *            string to check
	 * @return true if s is in the set of strings described by regular
	 *         expression
	 */
	public boolean check(String s) {

		// set automaton to initial state
		dfa.reset();
		// process each character from input
		for (Character c : s.toCharArray()) {
			dfa.doTransition(c);
		}
		// string is accepted if automaton moves to a final state after
		// processing it
		return dfa.getFinalStates().contains(dfa.getCurrentState());
	}

	/**
	 * Generate instances accepted by DFA built from regular expression
	 * 
	 * @return set of instances or null(if the number of instances if greater
	 *         than maxSortCapacity)
	 */
	public HashSet<String> generate() {
		dfa.reset();
		return dfa.generate(SparcConstants.maxSortCapacity);
	}

	/**
	 * @return deterministic finite automata instance associated with regular
	 *         expression.
	 */
	public DFA getDFA() {
		return dfa;
	}
}
