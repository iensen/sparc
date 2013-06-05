package automata;

import java.util.HashMap;
import java.util.HashSet;

import parser.SparcConstants;

/**
 * Class for automaton states
 */
public class State {
	private HashMap<Character, HashSet<State>> map; // transition table
	int id;

	public State() {
		map = new HashMap<Character, HashSet<State>>();
	}

	public void setId(int id) {
		this.id = id;
	}

	public HashMap<Character, HashSet<State>> getmap() {
		return map;
	}

	/**
	 * Add transition to the state
	 * 
	 * @param symbol
	 *            character by which the transition is activated
	 * @param state
	 *            State to be selected after the new transition
	 */
	public void addTransition(Character symbol, State state) {
		if (!this.map.containsKey(symbol)) {
			this.map.put(symbol, new HashSet<State>());
		}
		this.map.get(symbol).add(state);
	}

	/**
	 * @return closure of the State under empty symbol (marked as '$').
	 */
	HashSet<State> getClosure() {
		HashSet<State> result = new HashSet<State>();
		result.add(this);
		if (!this.map.containsKey('$')) {
			return result;
		}
		HashSet<State> immediate = this.map.get('$');
		for (State s : immediate) {
			if (!result.contains(s)) {
				result.add(s);
				result.addAll(s.getClosure());

			}
		}
		return result;
	}

	/**
	 * Output closure of given state under all possible transitions.
	 */
	public void dump(String s) {

		for (Character c : (SparcConstants.letters + "$").toCharArray()) {
			if (this.map.containsKey(c)) {
				HashSet<State> to = this.map.get(c);

				System.out
						.println(s + "We ara now at state " + this.toString());
				for (State state : to) {

					System.out.println(s + "Transit by symbol " + c.toString());
					if (state != this)
						state.dump(s + "N");
				}
			}
		}
	}

	/**
	 * most likely this overriding is deprecated, need to double check
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return this == obj;
	}

}