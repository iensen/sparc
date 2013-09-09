package warnings;

import java.util.HashSet;

public class PrologRuleNonRelAtom extends PrologRuleAtom {
    String atomName;
    Term argument;
    boolean negated;
    
    public PrologRuleNonRelAtom(String atomName,Term argument,boolean negated) {
    	this.type=PrologRuleAtomType.nonrel;
    	this.atomName=atomName;
    	this.argument=argument;
    	this.negated=negated;
    }
    
	@Override
	public HashSet<String> getVariables() {
		return argument.fetchVariables();
	}

	@Override
	public String toString() {
		return (negated? "\\+ ":"")+atomName+"("+argument.toString()+")";
	}
}
