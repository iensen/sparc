package automata;

import java.util.HashSet;

/**
 * Abstract class for automaton with basic operations
 */
public abstract class automaton {
	HashSet<State> states;
	HashSet<State> finalStates;
	State initialState;

	/**
	 * take symbol c as an input and move to next state according to transition
	 * table
	 * 
	 * @param c
	 *            the symbol to be taken from input
	 */
	public abstract void doTransition(Character c);

	/**
	 * construct an automaton, initialize final and initial states
	 */
	public automaton() {
		initialState = new State();
		finalStates = new HashSet<State>();
		finalStates.add(new State());
	}

	/**
	 * @return some final state of the automaton.
	 */
	public State getSomeFinalState() {
		return finalStates.iterator().next();
	}

	/**
	 * @return all final states of the automaton
	 */
	public HashSet<State> getFinalStates() {
		return finalStates;
	}
}
