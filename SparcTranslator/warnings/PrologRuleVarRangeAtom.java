package warnings;

import java.util.HashSet;

import sorts.BuiltIn;

public class PrologRuleVarRangeAtom extends PrologRuleAtom{

	HashSet<String> arithmeticVariables;
	
	public PrologRuleVarRangeAtom(HashSet<String> arithmeticVariables) {
		this.type=PrologRuleAtomType.varrange;
		this.arithmeticVariables = arithmeticVariables;
	}

	@Override
	public HashSet<String> getVariables() {
	    return this.arithmeticVariables;
	}

	@Override
	public String toString() {
		
	    return "["+ StringListUtils.getSeparatedList(arithmeticVariables, ",")+ "] ins 0.."+BuiltIn.getMaxInt();
	}

}

