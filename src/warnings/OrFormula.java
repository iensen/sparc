package warnings;

import utilities.Pair;



public class OrFormula extends Formula{


	@Override
	public Pair<Formula, Formula> getFirstConjunct() {
		return new Pair<Formula,Formula>(this,null);
	}
	
	public OrFormula(Formula child1, Formula child2) {
		this.type=FormulaType.or;
		this.children.add(child1);
		this.children.add(child2);
	}
	
	@Override
	public String toString() {
		StringBuilder result=new StringBuilder();
		result.append("(");
		for(int i=0;i<children.size();i++) {
			if(i!=0)
				result.append(" or ");
			result.append(children.get(i).toString());
		}
		
		result.append(")");
		
		return result.toString();
	}

}
