package warnings;

import java.util.ArrayList;

import utilities.Pair;

public abstract class Formula {
	FormulaType type;
	ArrayList<Formula> children;

	public ArrayList<PrimitiveFormula> convertToPrimitiveConjunction() {
		ArrayList<PrimitiveFormula> result = new ArrayList<PrimitiveFormula>();
		if (this.type == FormulaType.primitive) {
			result.add((PrimitiveFormula) this);
			return result;
		} else if (this.type == FormulaType.and) {
			result.addAll(this.children.get(0).convertToPrimitiveConjunction());
			result.addAll(this.children.get(1).convertToPrimitiveConjunction());
			return result;
		} else {
			throw new IllegalArgumentException(
					"Formula is not a primitive conjunction");
		}

	}

	public abstract Pair<Formula, Formula> getFirstConjunct();

	public Formula() {
		children = new ArrayList<Formula>();
	}

	public Formula negate() {
		if(this.type==FormulaType.negation) {
			return this.children.get(0);
		}
		if(this.type==FormulaType.primitive) {
			PrimitiveFormula F=(PrimitiveFormula)this;
			F.negated=!F.negated;
			return F;
		}
		else return new NegFormula(this);
	}

	public Formula and(Formula F) {
		return new AndFormula(F, this);
	}
	
	public Formula or(Formula F) {
		return new OrFormula(F,this);
	}
	
	public abstract String toString();
}
