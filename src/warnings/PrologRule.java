package warnings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import parser.ASTvar;
import parser.SparcTranslatorTreeConstants;

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

	public void addRangeAtom(Term term, long lowest, long highest) {
		bodyAtoms.add(new PrologRuleRangeAtom(term, lowest, highest));
	}

	public void addLabelingAtom(HashSet<String> arithmeticVariables) {
		bodyAtoms.add(new PrologRuleLabelingAtom(arithmeticVariables));
	}

	public void addVarRangeAtom(HashSet<String> arithmeticVariables) {
		bodyAtoms.add(new PrologRuleVarRangeAtom(arithmeticVariables));
	}

	public void addIsAtom(String variableName, Term term) {
		bodyAtoms.add(new PrologRuleIsAtom(variableName, term));
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

	/**
	 * Replace all arithmetic atoms occuring in non-relation atoms
	 * by new variables and add "is" statements to the body
	 * @return the set of the variables which were introduced during the process
	 */
	public HashSet<String> fetchArithmeticsIntoIsAtoms() {
		HashSet<String> newVariables=new HashSet<String>();
		HashSet<String> usedVariables = new HashSet<String>();
		for (PrologRuleAtom atom : bodyAtoms) {
			usedVariables.addAll(atom.getVariables());
		}

		UniqueNameGenerator ugen = new UniqueNameGenerator();
		ugen.addUsedNames(usedVariables);
		ArrayList<PrologRuleAtom> isAtoms = new ArrayList<PrologRuleAtom>();
		HashMap<String, String> termToVariable = new HashMap<String, String>();
		for (PrologRuleAtom atom : bodyAtoms) {
			if (atom.type == PrologRuleAtomType.nonrel) {

				String argumentString = ((PrologRuleNonRelAtom) atom).argument
						.toString();
				if (argumentString.indexOf('-') != -1
						|| argumentString.indexOf('+') != -1
						|| argumentString.indexOf('*') != -1
						|| argumentString.indexOf('/') != -1) {
					String varName;
					if (!termToVariable.containsKey(argumentString)) {
						varName = ugen.generateNewName();
						newVariables.add(varName);
						termToVariable.put(argumentString, varName);
						isAtoms.add(new PrologRuleIsAtom(varName,
								((PrologRuleNonRelAtom) atom).argument));
					} else {
						varName = termToVariable.get(argumentString);
					}
					ASTvar var = new ASTvar(SparcTranslatorTreeConstants.JJTVAR);
					var.image = varName;
					((PrologRuleNonRelAtom) atom).argument = new Term(var);
				}
			}
		}
		for(PrologRuleAtom isAtom:isAtoms) {
			this.bodyAtoms.add(isAtom);
		}
		return newVariables;
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
				was[peek] = true;
				stack.pop();
				orderedBodyAtoms.add(bodyAtoms.get(peek));
				for (int j = 0; j < bodyAtoms.size(); j++) {
					if (!was[j] && connected[i][j]) {
						stack.push(j);
					}
				}

			}
		}
		bodyAtoms = orderedBodyAtoms;
	}

}
