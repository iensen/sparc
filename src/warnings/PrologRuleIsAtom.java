package warnings;

import java.util.HashSet;
/**
 * Atom of the form var is (Arithmetic term)
 */
public class PrologRuleIsAtom extends PrologRuleAtom {
    String varName;
    Term term;
    
    public PrologRuleIsAtom(String varName,Term term) {
    	this.type=PrologRuleAtomType.is;
    	this.varName=varName;
    	this.term=term;
    }
    
	@Override
	public HashSet<String> getVariables() {
		HashSet<String> variables=term.fetchVariables();
		variables.add(varName);
		return variables;
	}

	@Override
	public String toString() {
		return varName+ " #= "+term.toString();
	}
}
