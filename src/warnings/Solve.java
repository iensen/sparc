package warnings;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;

import externaltools.PrologSolver;


public class Solve {

	public static boolean run(ArrayList<PrimitiveFormula> conj)
			throws FileNotFoundException {
		// check for immediate false conjunct:
		if (conj == null) {
			return false;
		}
		for (PrimitiveFormula F : conj) {
			if (F.primitiveType == PrimitiveFormulaType.unsat) {
				return false;
			}
		}

		HashSet<String> arithmeticVariables = VariablesUtils
				.fetchArithmeticVariables(conj);

		StringBuilder pProlog = new StringBuilder();
		pProlog.append(":- use_module(library(clpfd)).");
		pProlog.append(System.getProperty("line.separator"));
		pProlog.append(":- style_check(-singleton).");
		pProlog.append(System.getProperty("line.separator"));

		PrologRule rule = new PrologRule();
		int setId = 0;
		int gId = 0;
		for (PrimitiveFormula F : conj) {
			if (F.primitiveType == PrimitiveFormulaType.sat) {
				continue;
			}

			Term term1 = (Term) F.operands[0];
			if (F.primitiveType == PrimitiveFormulaType.relation) {
				Term term2 = (Term) F.operands[1];
				if (F.negated) {
					F.rel = Relation.getOppositeRelation(F.rel);
				}
				if (term1.isPrimitiveArithmetic(arithmeticVariables)
						&& ((Term) F.operands[1])
								.isPrimitiveArithmetic(arithmeticVariables)) {
					// for each primitive arithmetic of the form t1 rel t2 do
					// add relation to the body
					rule.addRelAtom(term1, term2, F.rel, true);

				} else {
					rule.addRelAtom(term1, term2, F.rel, false);
				}

			} else if (F.primitiveType == PrimitiveFormulaType.membership) {
				Term term = (Term) F.operands[0];
				GroundSet set = (GroundSet) F.operands[1];
				if (set.type == GroundSetType.NumericRange) {
					// for each g_i of the form t \in n1..n2
					// pArithm := pArithm union t in n1..n2 2.

					NumericRangeGroundSet numericRange = (NumericRangeGroundSet) set;
					if (!F.negated) {
						if (term.isVariable()) {
							rule.addRangeAtom(term, numericRange.lowest,
									numericRange.highest);
						} else {
							rule.addRelAtom(term1,
									new Term(numericRange.lowest),
									Relation.greatereq, true);
							rule.addRelAtom(term1, new Term(
									numericRange.highest), Relation.lesseq,
									true);
						}
					} else {
						++gId;
						pProlog.append("g_" + gId + "(" + term.toString() + ")"
								+ ":-" + term.toString() + " #> "
								+ numericRange.highest + ".");
						pProlog.append("g_" + gId + "(" + term.toString() + ")"
								+ ":-" + term.toString() + " #< "
								+ numericRange.lowest + ".");
						rule.addNonRelAtom("g_" + gId, term, false);
					}
				}

				else {

					if (term1.isPrimitiveArithmetic(arithmeticVariables)) {

						++setId;
						for (String element : set.getElements()) {
							if (isNumber(element)) {
								pProlog.append("set_" + setId + "(" + element
										+ ").");
								pProlog.append(System
										.getProperty("line.separator"));
							}
						}
                     
						rule.addNonRelAtom("set_" + setId, term1, F.negated);

					} else {
                        ++setId;
						for (String element : set.getElements()) {
							pProlog.append("set_" + setId + "(" + element
									+ ").");
							pProlog.append(System.getProperty("line.separator"));
						}

						rule.addNonRelAtom("set_" + setId, term1, F.negated);
					}
				}

			}
		}

		rule.reorderBodyAtoms();
		HashSet<String> newVariables=rule.fetchArithmeticsIntoIsAtoms();
		arithmeticVariables.addAll(newVariables);
		rule.addVarRangeAtom(arithmeticVariables);
		rule.addLabelingAtom(arithmeticVariables);
        
		pProlog.append(rule.toString());
		pProlog.append(System.getProperty("line.separator"));
		pProlog.append("main :-(p -> writeln(yes) ; writeln(no)).");
		pProlog.append(System.getProperty("line.separator"));
		boolean result = true;
		try {
			PrologSolver prologProgram = new PrologSolver(pProlog.toString());
			result = prologProgram.isSatisfiable();
		} catch (FileNotFoundException ex) {
			System.err.println("WARNING:" + ex.getMessage());
			System.err.println("Warnings will not be displayed");

		}
		return result;
	}

	private static boolean isNumber(String s) {
		for (Character c : s.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}
}
