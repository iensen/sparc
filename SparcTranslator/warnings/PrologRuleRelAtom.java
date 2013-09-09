package warnings;

import java.util.HashSet;

public class PrologRuleRelAtom extends PrologRuleAtom {

	Term term1;
	Term term2;
	Relation rel;
	boolean isArithmetic;
    
	public PrologRuleRelAtom(Term term1, Term term2, Relation rel,boolean isArithmetic) {
		this.type=PrologRuleAtomType.rel;
		this.term1=term1;
		this.term2=term2;
		this.rel=rel;
		this.isArithmetic=isArithmetic;
	}
	@Override
	public HashSet<String> getVariables() {
		HashSet<String> variables = term1.fetchVariables();
		variables.addAll(term2.fetchVariables());
		return variables;
	}

	@Override
	public String toString() {
		return term1.toString()
				+ " "
				+ (isArithmetic ? translateRelationToCLPFD(rel)
						: translateRelationToProlog(rel)) + " "
				+ term2.toString();
	}

	private static String translateRelationToCLPFD(Relation rel) {
		switch (rel) {
		case eqrel:
			return "#=";
		case eqasgn:
			return "#=";
		case greater:
			return "#>";
		case less:
			return "#<";
		case lesseq:
			return "#=<";
		case greatereq:
			return "#>=";
		case noteq:
			return "#\\=";
		default:
			return null;
		}
	}

	private static String translateRelationToProlog(Relation rel) {
		switch (rel) {
		case eqrel:
			return "==";
		case eqasgn:
			return "=";
		case greater:
			return "@>";
		case less:
			return "@<";
		case lesseq:
			return "@=<";
		case greatereq:
			return "@>=";
		case noteq:
			return "\\==";
		default:
			return null;
		}
	}

}
