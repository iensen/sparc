package warnings;

import java.io.FileNotFoundException;

import sorts.BuiltIn;
import utilities.Pair;

public class ExpandSolve {
    public static int CountOK=0;
	public static boolean run(Formula F) {
		boolean result = false;
		try {
			result = run(F, null, null);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		return result;
	}

	private static boolean run(Formula F, Formula TODO, Formula C)
			throws FileNotFoundException {

		if (F == null && TODO == null && C == null) {
			throw new IllegalArgumentException(
					"One of the formulas F,TODO,C must be non-empty");
		}

		if (F == null) {
			if (TODO == null) {

				return Solve.run(Simplify.run(C.convertToPrimitiveConjunction(),
								BuiltIn.getMaxInt()));
			} else {
				Pair<Formula, Formula> G1Grem = TODO.getFirstConjunct();
				return run(G1Grem.first, G1Grem.second, C);
			}
		} else if (F.type == FormulaType.negation
				&& F.children.get(0).type == FormulaType.and) {
			Formula child1 = F.children.get(0).children.get(0);
			Formula child2 = F.children.get(0).children.get(1);
			child1 = child1.negate();
			child2 = child2.negate();
			Formula newFormula = new OrFormula(child1, child2);
			return run(newFormula, TODO, C);
		}

		else if (F.type == FormulaType.negation
				&& F.children.get(0).type == FormulaType.or) {
			Formula child1 = F.children.get(0).children.get(0);
			Formula child2 = F.children.get(0).children.get(1);
			child1 = child1.negate();
			child2 = child2.negate();
			Formula newFormula = new AndFormula(child1, child2);
			return run(newFormula, TODO, C);
		}

		else if (F.type == FormulaType.or) {
			Formula child1 = F.children.get(0);
			Formula child2 = F.children.get(1);
			if (!run(child1, TODO, C)) {
				return run(child2, TODO, C);
			} else {
				return true;
			}
		}

		else if (F.type == FormulaType.and) {
			Formula newTODO=null;
            if(TODO==null) {
               newTODO=F.children.get(1);            	
            }
            else {
            	newTODO = new AndFormula(F.children.get(1), TODO);	
            }
			
			return run(F.children.get(0), newTODO, C);
		}

		else {// F is primitive
			if(C==null)
				return run(null,TODO,F);
			else
				return run(null, TODO, C.and(F));
		}

	}

}
