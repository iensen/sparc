//TODO:
package warnings;

import java.util.ArrayList;
import java.util.HashSet;

import parser.ASTunaryCondition;
import parser.SparcTranslatorTreeConstants;
import sorts.Condition;

public class Simplify {

	public static ArrayList<PrimitiveFormula> run(
			ArrayList<PrimitiveFormula> conj, long maxInt) {
		boolean[] toRemove = new boolean[conj.size()];
		HashSet<String> arithmeticVariables = VariablesUtils.fetchArithmeticVariables(conj);
		for (int i = 0; i < conj.size(); i++) {
			PrimitiveFormula iFormula = conj.get(i);
			if(iFormula.primitiveType==PrimitiveFormulaType.sat) {
				toRemove[i]=true;
			}
			
			if(iFormula.primitiveType==PrimitiveFormulaType.unsat) {
				return null;
			}
			
			if (iFormula.primitiveType == PrimitiveFormulaType.membership) {
				Term term = (Term) iFormula.operands[0];
				GroundSet set = (GroundSet) iFormula.operands[1];
				if (term.isGround()) {
					boolean contains=set.containsElement(term.toString());
					if (contains && !iFormula.negated || !contains && iFormula.negated) {
						toRemove[i] = true;
					} else {
						return null;
					}
				} else // term is not ground
				if (!term.isRecord()) {
					// if it is not ground (after calculation of all arithmetic
					// terms)
					// and it does not contain '(', it is either an arithmetic
					// terms with operations and variables, or a variable
					// in both cases if all the variables are arithmetic, it is
					// arithmetic.
					// and the corresponding set must be numeric:
					// 1. Check if it is arithmetic,i.e, if all the variables
					// are arithmetic:
					HashSet<String> vars = term.fetchVariables();
					boolean isArithmetic = true;
					for (String var : vars) {
						if (!arithmeticVariables.contains(var)) {
							isArithmetic = false;
						}
					}

					if (isArithmetic && !set.hasNumber() && !iFormula.negated) {
						return null;
					}
				} else// term is a record
				{
					// if t is a record and set is numeric, return null
					if (set.isNumeric() && !iFormula.negated)
						return null;
				}
			} else if (iFormula.primitiveType == PrimitiveFormulaType.relation) {
				Term term1 = (Term) iFormula.operands[0];
				Term term2 = (Term) iFormula.operands[1];
				// if both term1 and term2 are ground terms, check the relation
				if (term1.isGround() && term2.isGround()) {
					Condition cond = new Condition();
					ArrayList<String> arguments = new ArrayList<String>();
					arguments.add(term1.toString());
					arguments.add(term2.toString());
                    boolean conditionSatisfied=cond.checkCondition(
							createUnaryCondition("0", "1",
									iFormula.rel.toString()), arguments);
					if (conditionSatisfied && !iFormula.negated ||
							!conditionSatisfied && iFormula.negated) {
						toRemove[i] = true;
					} else {
						return null;
					}
				} else {
					// if they are not ground, check their compatibility
					if (iFormula.rel != Relation.noteq) {
						if (!comparable(term1, term2, iFormula.rel,
								arithmeticVariables)) {
							return null;
						}
					}
				}

			}
		}
		
		ArrayList<PrimitiveFormula> simplifiedConj=new ArrayList<PrimitiveFormula>();
		
		for(int i=0;i<conj.size();i++) {
			if(!toRemove[i])
				simplifiedConj.add(conj.get(i));
		}
		return simplifiedConj;
		
	}

	private static boolean comparable(Term term1, Term term2, Relation rel,
			HashSet<String> arithmeticVariables) {
		if(rel==Relation.noteq) {
			return true;
		}
		boolean term1IsRecord = term1.toString().indexOf('(') != -1;
		boolean term2IsRecord = term2.toString().indexOf('(') != -1;
		if (term1IsRecord && term2IsRecord)
			return  rel == Relation.eqasgn || rel==Relation.eqrel;
		if (term1IsRecord ^ term2IsRecord) {
			//lets make term1 to be a record
			if(term2IsRecord) {
				Term buf=term1;
				term1=term2;
				term2=buf;
			}
			// the only case when term1 and term2 are comparable is when term2 is a variable
			
			return term2.isVariable();
		}
			
		
		boolean term1IsNumeric=isNumeric(term1, arithmeticVariables);
		boolean term2IsNumeric=isNumeric(term2, arithmeticVariables);
		
		// if they are both numeric, or both symbolic, and non of them is a record they are clearly comparable:
		if(!(term1IsNumeric ^ term2IsNumeric)) {
			return true;
		}
		
		// else we need to consider cases when only one of them is numeric, let it always be t2:
		
		//we swap the terms if it is t1
		if(term1IsNumeric) {
			Term buf=term1;
			term1=term2;
			term2=buf;
		}
		
		//now we know that t2 is numeric. It can be compatible with the symbolic term iff it is a variable!
		return term1.isVariable();
	}

	private static boolean isNumeric(Term term1,
			HashSet<String> arithmeticVariables) {
		boolean isNumeric = false;
		isNumeric |= arithmeticVariables.contains(term1.toString());
		isNumeric |= isNumber(term1.toString());
		// we are assuming that term1 is not a record
		boolean hasAllArithmeticVariables = true;
		HashSet<String> variables = term1.fetchVariables();
		for (String variable : variables) {
			if (!arithmeticVariables.contains(variable)) {
				hasAllArithmeticVariables = false;
			}
		}
		isNumeric |= hasAllArithmeticVariables;
		return isNumeric;
	}

	private static boolean isNumber(String s) {
		boolean isNumber = true;
		for (char c : s.toCharArray()) {
			if (!Character.isDigit(c))
				isNumber = false;
		}
		return isNumber;
	}

	
	private static ASTunaryCondition createUnaryCondition(String arg1,
			String arg2, String rel) {
		ASTunaryCondition cond = new ASTunaryCondition(
				SparcTranslatorTreeConstants.JJTUNARYCONDITION);
		cond.image = arg1 + " " + rel + " " + arg2;
		return cond;
	}
}
