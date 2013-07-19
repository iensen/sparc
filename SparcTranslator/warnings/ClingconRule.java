package warnings;

import java.util.ArrayList;
import java.util.HashSet;

public class ClingconRule {

	HashSet<String> variables;
	ArrayList<String> bodyAtoms;

	public ClingconRule() {
		variables = new HashSet<String>();
		bodyAtoms = new ArrayList<String>();
	}

	public void addEqAtom(String capVar) {
		String uncapVar = Term.uncapVariable(capVar);
		bodyAtoms.add(capVar+"$=="+uncapVar);
	}

	public void addSetAtom(Term t, int setId, boolean negative) {
		variables.addAll(t.fetchVariables());
		String atom = ((negative) ? "not " : "") + "set_" + setId + "("
				+ t.toString() + ")";
		bodyAtoms.add(atom);
	}

	public void addRelation(Term t1, Term t2, Relation rel) {
		variables.addAll(t1.fetchVariables());
		variables.addAll(t2.fetchVariables());
		String atom = t1.toString() + rel.toString() + t2.toString();
		bodyAtoms.add(atom);
	}

	public String toString() {
		StringBuilder ruleStr = new StringBuilder();
		ruleStr.append(getHeadAtom());
		if (bodyAtoms.size() != 0) {
			ruleStr.append(":-");
			if (bodyAtoms.size() != 0) {
				ruleStr.append(StringListUtils.getSeparatedList(bodyAtoms,","));
			}
		}
		ruleStr.append(".");
		ruleStr.append(System.getProperty("line.separator"));
		return ruleStr.toString();
	}

	public String getHeadAtom() {
		if (variables.size() == 0) {
			return "r";
		} else {
		}
		return "r(" + StringListUtils.getSeparatedList(variables,",") + ")";
	}

}
