package automata;

import parser.ASTcharacterRegularExpression;
import parser.ASTcomplexRegularExpression;
import parser.ASTcomplexRegularExpressionChoices;
import parser.ASTcomplexRegularExpressionRepeatableUnit;
import parser.ASTcomplexRegularExpressionUnit;
import parser.ASTregularExpression;
import parser.SimpleNode;
import parser.SparcTranslatorConstants;
import parser.SparcTranslatorTreeConstants;

/**
 * Implementation of NFA(nondeterministic finite automaton).
 */
public class NFA extends automaton {
	public NFA(ASTcomplexRegularExpressionChoices node) {
		buildNFA(node);

	}

	/**
	 * Build NFA from abstract syntax tree node representing choice regular
	 * expression (Ex: a|b).
	 */
	private void buildNFA(ASTcomplexRegularExpressionChoices node) {
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			NFA newNFA = new NFA(
					(ASTcomplexRegularExpression) node.jjtGetChild(i));
			initialState.addTransition('$', newNFA.initialState);
			newNFA.getSomeFinalState().addTransition('$',
					this.getSomeFinalState());
		}
	}

	/**
	 * Construct a NFA from given AST node representing regular expression
	 */
	public NFA(ASTregularExpression node) {

		this((ASTcomplexRegularExpressionChoices) node.jjtGetChild(0));
	}

	/**
	 * Construct NFA from given AST node representing complex regular expression
	 */
	public NFA(ASTcomplexRegularExpression tree) {
		State currentState = initialState;

		for (int i = 0; i < tree.jjtGetNumChildren(); i++) {
			NFA newNFA = new NFA(
					(ASTcomplexRegularExpressionRepeatableUnit) tree
							.jjtGetChild(i));
			currentState.addTransition('$', newNFA.initialState);
			currentState = newNFA.getSomeFinalState();
			if (i == tree.jjtGetNumChildren() - 1) // last step
			{
				newNFA.getSomeFinalState().addTransition('$',
						this.getSomeFinalState());
			}
		}

		if (tree.jjtGetNumChildren() == 0) {// empty string
			currentState.addTransition('$', this.getSomeFinalState());
		}

	}

	/**
	 * Construct NFA from given AST node representing complex regular expression
	 * (grammar) unit.
	 */

	public NFA(ASTcomplexRegularExpressionUnit tree) {

		String complementImage = SparcTranslatorConstants.tokenImage[SparcTranslatorConstants.COMPLEMENT];
		complementImage = complementImage.substring(1,
				complementImage.length() - 1);
		if (tree.image != null && tree.image.equals(complementImage)) {

			tree.image = "";
			NFA complementNFA = new NFA(tree);
			DFA complementDFA = new DFA(complementNFA);
			complementDFA.buildComplement();
			initialState = complementDFA.initialState;
			finalStates.clear();
			State finalState = new State();
			for (State s : complementDFA.finalStates) {
				s.addTransition('$', finalState);
			}
			this.finalStates.add(finalState);
		} else {
			switch (((SimpleNode) tree.jjtGetChild(0)).getId()) {
			case SparcTranslatorTreeConstants.JJTCHARACTERREGULAREXPRESSION:
				buildNFA((ASTcharacterRegularExpression) tree.jjtGetChild(0));
				break;
			case SparcTranslatorTreeConstants.JJTCOMPLEXREGULAREXPRESSIONCHOICES:
				buildNFA((ASTcomplexRegularExpressionChoices) tree
						.jjtGetChild(0));
				break;

			}
		}
	}

	/**
	 * Build NFA from abstract syntax tree node representing regular expression
	 * for exactly one character.
	 */

	private void buildNFA(ASTcharacterRegularExpression node) {
		this.initialState.addTransition(node.image.charAt(0),
				this.getSomeFinalState());

	}

	/**
	 * @param node
	 *            abstract syntax tree node representing regular expression
	 *            repetition.(Ex.:a{1}).
	 */
	public NFA(ASTcomplexRegularExpressionRepeatableUnit node) {
		this((ASTcomplexRegularExpressionUnit) node.jjtGetChild(0));
	}

	/**
	 * take symbol c as an input and move to next state according to transition
	 * table
	 * 
	 * @param c
	 *            the symbol to be taken from input
	 */
	@Override
	public void doTransition(Character c) {
		// TODO Auto-generated method stub
	}
}
