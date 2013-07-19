package warnings;

import java.util.ArrayList;
import java.util.HashSet;

public class VariablesUtils {
	public static HashSet<String> fetchArithmeticVariables(
			ArrayList<PrimitiveFormula> conj) {
     
		HashSet<String> arithmeticVariables = new HashSet<String>();
		// X occurs in an arithmetic term containing at least one arithmetic
		// operation.
		if(conj==null) { // it means the conjunction is unsatisfiable
	         return arithmeticVariables; //return empty set
	    }
		for (int i = 0; i < conj.size(); i++) {
			PrimitiveFormula iFormula = conj.get(i);
			if(iFormula.primitiveType==PrimitiveFormulaType.sat || iFormula.primitiveType== PrimitiveFormulaType.unsat) 
				continue;
			Term term1 = (Term) iFormula.operands[0];
			arithmeticVariables.addAll(term1.fetchArithmeticVariables());

			if (iFormula.primitiveType == PrimitiveFormulaType.relation) {
				Term term2 = (Term) iFormula.operands[0];
				arithmeticVariables.addAll(term2.fetchArithmeticVariables());
			}
		}
		// one of conj is of the form X in D, where D is a range of natural
		// numbers.

		for (int i = 0; i < conj.size(); i++) {
			PrimitiveFormula iFormula = conj.get(i);
			if (iFormula.primitiveType == PrimitiveFormulaType.membership) {
				GroundSet set = (GroundSet) iFormula.operands[1];
				if (set.isNumeric()) {
					Term term1 = (Term) iFormula.operands[0];
					if (term1.toString().indexOf('(') == -1) {
						arithmeticVariables.addAll(term1.fetchVariables());
					}
				}
			}
		}

		// one of Gi is of the form X rel Y or Y rel X, where Y is an arithmetic
		// variable and

		int oldCount = -1;
		while (oldCount != arithmeticVariables.size()) {
			oldCount=arithmeticVariables.size();
			for (int i = 0; i < conj.size(); i++) {
				PrimitiveFormula iFormula = conj.get(i);
				if (iFormula.primitiveType == PrimitiveFormulaType.relation) {
					Term term1 = (Term) iFormula.operands[0];
					Term term2 = (Term) iFormula.operands[1];
					if (term1.toString().indexOf('(') == -1
							&& term2.toString().indexOf('(') == -1) {
						HashSet<String> term1ArithmeticVariables = term1
								.fetchArithmeticVariables();
						HashSet<String> term1Variables = term1.fetchVariables();
						HashSet<String> term2ArithmeticVariables = term2
								.fetchArithmeticVariables();
						HashSet<String> term2Variables = term2.fetchVariables();
						if (term1Variables.size() == term1ArithmeticVariables
								.size()) {
							Term buf = term1;
							term1 = term2;
							term2 = buf;
						}

						if (term2Variables.size() == term2ArithmeticVariables
								.size()) {
							// all variables in term1 must be arithmetic
							term1Variables.removeAll(term1ArithmeticVariables);
							arithmeticVariables.addAll(term1Variables);
						}

					}
				}
			}
		}
		return arithmeticVariables;
	}

}
