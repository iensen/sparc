package warnings;

import utilities.Pair;

public class PrimitiveFormula extends Formula {
	PrimitiveFormulaType primitiveType;
	Operand[] operands;
	Relation rel;
	boolean negated;

	public PrimitiveFormula(Term t1, Term t2, Relation rel) {
		negated = false;
		type = FormulaType.primitive;
		primitiveType = PrimitiveFormulaType.relation;
		operands = new Operand[2];
		operands[0] = t1;
		operands[1] = t2;
		this.rel = rel;
	}

	public PrimitiveFormula(boolean value) {
		type = FormulaType.primitive;
		if (value) {
			primitiveType = PrimitiveFormulaType.sat;
		} else {
			primitiveType = PrimitiveFormulaType.unsat;
		}
	}

	public PrimitiveFormula(Term term, GroundSet gset) {
		type = FormulaType.primitive;
		operands = new Operand[2];
		negated = false;
		operands[0] = term;
		operands[1] = gset;
		primitiveType = PrimitiveFormulaType.membership;
	}

	@Override
	public Pair<Formula, Formula> getFirstConjunct() {
		return new Pair<Formula, Formula>(this, null);
	}

	@Override
	public String toString() {

		if (this.primitiveType == PrimitiveFormulaType.sat) {
			return "true";
		} else if (this.primitiveType == PrimitiveFormulaType.unsat) {
			return "false";
		} else {
			
			String separator=(this.primitiveType==PrimitiveFormulaType.membership)?" in ":
				" "+this.rel.toString()+" ";
				StringBuilder result = new StringBuilder();
				if(this.negated) {
				  result.append("not");	
				}

				result.append("(");
				result.append(this.operands[0].toString());
				result.append(separator);
				result.append(this.operands[1].toString());
				result.append(")");
				if(this.negated) {
					  result.append(")");	
					}
			    return result.toString();
		}

	}

}
