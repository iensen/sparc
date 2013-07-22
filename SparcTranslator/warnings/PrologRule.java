package warnings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Stack;

public class PrologRule {

	ArrayList<PrologRuleAtom> bodyAtoms;

	public PrologRule() {

		bodyAtoms = new ArrayList<PrologRuleAtom>();
	}

	public void addRelAtom(Term term1, Term term2, Relation rel,
			boolean isArithmetic) {
		bodyAtoms.add(new PrologRuleRelAtom(term1, term2, rel, isArithmetic));
	}

	public void addNonRelAtom(String atomName, Term term, boolean negated) {
		bodyAtoms.add(new PrologRuleNonRelAtom(atomName, term, negated));
	}
	
	public void addRangeAtom(Term term, long lowest,long highest) {
		bodyAtoms.add(new PrologRuleRangeAtom(term,lowest,highest));
	}
	
	public void addLabelingAtom(HashSet<String> arithmeticVariables) {
		bodyAtoms.add(new PrologRuleLabelingAtom(arithmeticVariables));
	}

	public String toString() {
		StringBuilder ruleStr = new StringBuilder();
		ruleStr.append(getHeadAtom());
		ArrayList<String> atomStrings = new ArrayList<String>(bodyAtoms.size());
		for (PrologRuleAtom atom : bodyAtoms) {
			atomStrings.add(atom.toString());
		}
		if (bodyAtoms.size() != 0) {
			ruleStr.append(" :- ");
			if (bodyAtoms.size() != 0) {
				ruleStr.append(StringListUtils.getSeparatedList(atomStrings,
						","));
			}
		}
		ruleStr.append(".");
		ruleStr.append(System.getProperty("line.separator"));
		return ruleStr.toString();
	}

	public String getHeadAtom() {
		return "p";
	}

	public void reorderBodyAtoms() {

		ArrayList<PrologRuleAtom> orderedBodyAtoms = new ArrayList<PrologRuleAtom>(
				bodyAtoms.size());
		
		boolean connected[][] = new boolean[bodyAtoms.size()][bodyAtoms.size()];
		for (int i = 0; i < bodyAtoms.size(); i++) {
			for (int j = 0; j < bodyAtoms.size(); j++) {
				if (i == j)
					continue;
				if (!Collections.disjoint(bodyAtoms.get(i).getVariables(),
						bodyAtoms.get(j).getVariables())) {
					connected[i][j] = true;
					connected[j][i] = true;
				}
			}
		}

		boolean[] was = new boolean[bodyAtoms.size()];
		for (int i = 0; i < bodyAtoms.size(); i++) {
			if (was[i] == true)
				continue;
			Stack<Integer> stack = new Stack<Integer>();
			stack.push(i);
			while (!stack.isEmpty()) {
				int peek = stack.peek();
				was[peek]=true;
				stack.pop();
				orderedBodyAtoms.add(bodyAtoms.get(peek));
				for (int j = 0; j < bodyAtoms.size(); j++) {
					if (!was[j] && connected[i][j]) {
                       stack.push(j);
					}
				}

			}
		}
		bodyAtoms=orderedBodyAtoms;
	}

}
