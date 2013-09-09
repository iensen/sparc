package warnings;

import java.util.HashSet;

public class PrologRuleRangeAtom extends PrologRuleAtom {
    long lowest;
    long highest;
    Term term;
    
    
    public PrologRuleRangeAtom(Term term,long lowest, long highest) {
    	this.type=PrologRuleAtomType.range;
		this.lowest = lowest;
		this.highest = highest;
		this.term = term;
	}

	
	@Override
	public HashSet<String> getVariables() {
        return term.fetchVariables();
	}

	@Override
	public String toString() {
		return term.toString()+ " in "+ Long.toString(lowest)+".."+Long.toString(highest);
	}

}
