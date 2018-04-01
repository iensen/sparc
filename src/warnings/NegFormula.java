package warnings;

import utilities.Pair;



public class NegFormula extends Formula {


	@Override
	public Pair<Formula, Formula> getFirstConjunct() {
		return new Pair<Formula,Formula>(this,null);
	}
	
	public NegFormula(Formula child) {
		this.type=FormulaType.negation;
		this.children.add(child);
	}

	@Override
	public String toString() {
		StringBuilder result=new StringBuilder();
		result.append("(");
		result.append(" not");
		result.append(this.children.get(0).toString());
		result.append(")");
		return result.toString();
	}
	
	
	

}