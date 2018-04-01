package warnings;

import utilities.Pair;



public class AndFormula extends Formula {

	

	@Override
	public Pair<Formula, Formula> getFirstConjunct() {
		return new Pair<Formula,Formula>(this.children.get(0),this.children.get(1));
	}

	public AndFormula(Formula child1, Formula child2) {
	
		this.type=FormulaType.and;
		this.children.add(child1);
		this.children.add(child2);
	}

	@Override
	public String toString() {
		StringBuilder result=new StringBuilder();
		result.append("(");
		for(int i=0;i<children.size();i++) {
			if(i!=0)
				result.append(" and ");
			result.append(children.get(i).toString());
		}
		result.append(")");
		return result.toString();
	}
}
