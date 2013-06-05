package automata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import parser.ASTregularExpression;
import parser.SparcConstants;

/**
 * implementation of deterministic finite automata
 */
public class DFA extends automaton {

	NFA nfa;
	State currentState;
	State deadState;

	/**
	 * build a DFA from given NFA
	 * 
	 * @param nfa
	 *            automaton to be converted to DFA
	 * 
	 */

	private void buildDFA(NFA nfa) {
		HashMap<HashSet<State>, State> DfaStates = new HashMap<HashSet<State>, State>();
		DfaStates.put(nfa.initialState.getClosure(), this.initialState);
		/*
		 * begin with e-closure of initial state and do depth-first search
		 * through all e-closures of given nfa
		 */
		Stack<HashSet<State>> Q = new Stack<HashSet<State>>();
		Q.push(nfa.initialState.getClosure());
		while (!Q.isEmpty()) {
			HashSet<State> oldStates = Q.pop();

			for (Character c : SparcConstants.letters.toCharArray()) {
				HashSet<State> newStates = new HashSet<State>();
				for (State s : oldStates) {
					if (s.getmap().containsKey(c)) {
						newStates.addAll(s.getmap().get(c));
					}
				}
				@SuppressWarnings("unchecked")
				HashSet<State> ClosedNewStates = (HashSet<State>) newStates
						.clone();
				for (State s : newStates) {
					ClosedNewStates.addAll(s.getClosure());
				}
				/*
				 * add new e-closed set as a new DFA state it case it was not
				 * already included
				 */
				if (!DfaStates.containsKey(ClosedNewStates)) {
					State newState = new State();
					DfaStates.put(ClosedNewStates, newState);
					Q.push(ClosedNewStates);
				}

				DfaStates.get(oldStates).addTransition(c,
						DfaStates.get(ClosedNewStates));
			}

		}
		;

		/*
		 * Determine Final States of constructed DFA: A state is final if its
		 * closure contains at least one final state of the old dfa
		 */
		this.finalStates.clear();
		for (HashSet<State> s : DfaStates.keySet()) {
			for (State nfaState : s) {
				if (nfa.finalStates.contains(nfaState)) {
					this.finalStates.add(DfaStates.get(s));
				}
			}
		}
	}

	/**
	 * Construct a DFA from given regular expression
	 * 
	 * @param node
	 *            AST node of given regular expression
	 */
	public DFA(ASTregularExpression node) {
		this(new NFA(node));

	}

	/**
	 * Construct a DFA from given NFA by doing conversion
	 * 
	 * @param nfa
	 *            automaton to be converted
	 */
	public DFA(NFA nfa) {
		buildDFA(nfa);
		currentState = initialState;
		deadState = new State();
	}

	/**
	 * Take symbol c as an input and move to next state according to transition
	 * table
	 * 
	 * @param c
	 *            the symbol to be taken from input
	 */

	@Override
	public void doTransition(Character c) {
		if (!currentState.getmap().containsKey(c)) {
			currentState = deadState;
		} else
			currentState = currentState.getmap().get(c).iterator().next();
	}

	/**
	 * @return the state of the automaton which was selected after the latest
	 *         transition
	 */
	public State getCurrentState() {
		return currentState;
	}

	/**
	 * Forget input string, reset current state to initial one
	 */
	public void reset() {
		currentState = initialState;
	}

	/**
	 * Generate all strings accepted by the automaton.
	 * 
	 * @param limit
	 *            integer which limits the number of instances
	 * @return set of all possible input instances which moves the automaton to
	 *         an accepting state If there are too many instances (>limit),
	 *         return null;
	 */
	public HashSet<String> generate(int limit) {
		HashSet<String> result = new HashSet<String>();
		HashSet<Configuration> visited = new HashSet<Configuration>();
		LinkedList<Configuration> Q = new LinkedList<Configuration>();
		Q.add(new Configuration(currentState, ""));
		HashSet<State> reachableSet = getAllStatesReachableFromInitial();
		HashMap<State, Boolean> isDeadState = new HashMap<State, Boolean>();
		// determine all dead states reachable from initial
		for (State s : reachableSet) {
			isDeadState.put(s, isDead(s));
		}
		int count = 0;
		// do breadth-first search for all possible configurations of the
		// automata
		while (!Q.isEmpty()) {
			Configuration conf = Q.poll();
			if (isDeadState.containsKey(conf.state)
					&& isDeadState.get(conf.state)) {
				continue;
			}
			visited.add(conf);

			for (Character c : SparcConstants.letters.toCharArray()) {
				if (conf.state.getmap().containsKey(c)) {
					Configuration togo = new Configuration(conf.state.getmap()
							.get(c).iterator().next(), conf.prefix
							+ c.toString());
					if (!visited.contains(togo)) {
						Q.add(togo);
						if (this.finalStates.contains(togo.state)) {
							++count;
							if (count > limit)
								return null;
						}
					}
				}
			}
		}

		for (Configuration conf : visited) {
			if (this.finalStates.contains(conf.state))
				result.add(conf.prefix);
		}
		return result;
	}

	/**
	 * @return all sets which are connected to initial by some number of edges
	 */
	private HashSet<State> getAllStatesReachableFromInitial() {
		HashSet<State> visited = new HashSet<State>();
		Stack<State> S = new Stack<State>();
		S.push(initialState);
		// do depth-first search
		while (!S.isEmpty()) {
			State state = S.pop();
			visited.add(state);
			// iterate through the alphabet
			for (Character c : SparcConstants.letters.toCharArray()) {
				if (state.getmap().containsKey(c)) {
					State togo = state.getmap().get(c).iterator().next();
					if (!visited.contains(togo)) {
						// visit new state
						S.push(togo);
					}
				}
			}
		}
		return visited;
	}

	/**
	 * Checks if given state is dead, i.e, there is no path to a final state
	 * from it.
	 * 
	 * @param s
	 *            State to be checked for being dead
	 * @return true if state is dead
	 */
	public boolean isDead(State s) {
		// iterate through all the state using DFS
		HashSet<State> visited = new HashSet<State>();
		Stack<State> S = new Stack<State>();
		S.push(s);
		while (!S.isEmpty()) {
			State state = S.pop();
			visited.add(state);
			// if the state is finite itself, it is not dead
			if (this.finalStates.contains(state)) {
				return false;
			}
			// go to neighbors
			for (Character c : SparcConstants.letters.toCharArray()) {
				if (state.getmap().containsKey(c)) {
					State togo = state.getmap().get(c).iterator().next();
					if (!visited.contains(togo)) {

						S.push(togo);
					}
				}
			}
		}
		return true;
	}

	/**
	 * builds an automaton which accepts all string rejected by current one and
	 * vice versa
	 */
	public void buildComplement() {
		addEdgesToDeadState();
		HashSet<State> all = getAllStatesReachableFromInitial();
		HashSet<State> newFinalStates = new HashSet<State>();
		for (State s : all) {
			if (!finalStates.contains(s)) {
				newFinalStates.add(s);
			}
		}
		finalStates = newFinalStates;
	}

	/**
	 * Add transitions through all symbols of the alphabet to a dead state
	 * 
	 * @return the only one dead state of the automaton
	 */
	public State addEdgesToDeadState() {
		HashSet<State> all = getAllStatesReachableFromInitial();
		for (State s : all) {
			for (Character c : SparcConstants.letters.toCharArray()) {
				if (!s.getmap().containsKey(c)) {
					s.addTransition(c, deadState);
				}
			}
		}
		for (Character symbol : SparcConstants.letters.toCharArray()) {
			deadState.addTransition(symbol, deadState);
		}
		return deadState;
	}

	public State getDeadState() {
		return deadState;
	}

	public State getInitialState() {
		return initialState;
	}

	/**
	 * Class to define a configuration of the automata. Configuration consists
	 * of current state and prefix of the input which was read
	 */
	class Configuration {
		State state;
		String prefix;

		/*
		 * Construct configuration from given state and prefix.
		 */
		public Configuration(State s, String prefix) {
			this.prefix = prefix;
			this.state = s;
		}
	}
}
