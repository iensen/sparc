package warnings;

import java.util.HashSet;

public class PrologRuleLabelingAtom extends PrologRuleAtom{

	HashSet<String> arithmeticVariables;
	
	public PrologRuleLabelingAtom(HashSet<String> arithmeticVariables) {
		this.type=PrologRuleAtomType.labeling;
		this.arithmeticVariables = arithmeticVariables;
	}

	@Override
	public HashSet<String> getVariables() {
	    return this.arithmeticVariables;
	}

	@Override
	public String toString() {
	    return "labeling([],["+ StringListUtils.getSeparatedList(arithmeticVariables, ",")+ "])";
	}

}
