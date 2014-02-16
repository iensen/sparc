package warnings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import parser.ASTadditiveSetExpression;
import parser.ASTandCondition;
import parser.ASTcondition;
import parser.ASTconstantTermList;
import parser.ASTcurlyBrackets;
import parser.ASTfunctionalSymbol;
import parser.ASTmultiplicativeSetExpression;
import parser.ASTorCondition;
import parser.ASTpredSymbol;
import parser.ASTprogramRule;
import parser.ASTsetExpression;
import parser.ASTsortExpression;
import parser.ASTsortExpressionList;
import parser.ASTsortName;
import parser.ASTsymbolicFunction;
import parser.ASTsymbolicTerm;
import parser.ASTterm;
import parser.ASTtermList;
import parser.ASTunaryCondition;
import parser.ASTunarySetExpression;
import parser.ASTvar;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;
import translating.InstanceGenerator;
import utilities.Pair;

/**
 * This class if for creation of a formula from an ASP rule The formula will be
 * translated to clingcon and checked for satisfability
 * 
 */
public class RuleReducer {
	// mapping from sort names to sort expressions assigned to the sorts
	public HashMap<String, ASTsortExpression> sortNameToExpression;

	// mapping from predicate names to a list of names of sorts describing
	// arguments
	public HashMap<String, ArrayList<String>> predicateArgumentSorts;

	private Formula currentFormula;

	private InstanceGenerator gen;

	UniqueNameGenerator uniqueVariables;

	public RuleReducer(HashMap<String, ASTsortExpression> sortNameToExpression,
			HashMap<String, ArrayList<String>> predicateArgumentSorts,
			InstanceGenerator gen) {
		this.sortNameToExpression = sortNameToExpression;
		this.predicateArgumentSorts = predicateArgumentSorts;
		this.gen = gen;

	}

	public Formula reduceRule(ASTprogramRule rule) {
		currentFormula = null;
		uniqueVariables = new UniqueNameGenerator();
		uniqueVariables.addUsedNames(fetchVariables(rule));
		reduceRule((SimpleNode) rule);
		return currentFormula;
	}

	/**
	 * Recursively search for all non-relational atoms of the form p(t1,...,t_n)
	 * 
	 * @param n
	 *            -current node of abstract syntax tree built from the rule
	 */
	private void reduceRule(SimpleNode n) {
		if (n.getId() == SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM
				|| n.getId() == SparcTranslatorTreeConstants.JJTNONRELATOM) {
			if (n.jjtGetNumChildren() == 2) {
				// if the predicate has arguments (i.e, it is not of zero
				// arity).
				ASTpredSymbol predicate = (ASTpredSymbol) n.jjtGetChild(0);
				ASTtermList termList = (ASTtermList) n.jjtGetChild(1);
				ArrayList<String> sorts = predicateArgumentSorts
						.get(predicate.image);
				for (int i = 0; i < termList.jjtGetNumChildren(); i++) {
					ASTterm term = (ASTterm) termList.jjtGetChild(i);
					ASTsortExpression expr = sortNameToExpression.get(sorts
							.get(i));
					Formula F = reduceTerm(term, expr);
					if (currentFormula == null) {
						currentFormula = F;
					} else {
						currentFormula = currentFormula.and(F);
					}
				}
			}
		}

		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			reduceRule((SimpleNode) n.jjtGetChild(i));
		}
	}

	private Formula reduceTerm(ASTterm term, String sortName) {
		return reduceTerm(term, sortNameToExpression.get(sortName));
	}

	private Formula reduceTerm(ASTterm term, ASTsortExpression expr) {

		SimpleNode exprChild = (SimpleNode) expr.jjtGetChild(0);
		// expr is set expression
		if (exprChild.getId() == SparcTranslatorTreeConstants.JJTSETEXPRESSION) {
			return reduceTerm(term, (ASTsetExpression) exprChild);
		}
		// expr is functionalSymbol()
		else if (exprChild.getId() == SparcTranslatorTreeConstants.JJTFUNCTIONALSYMBOL) {
			return reduceTerm(term, (ASTfunctionalSymbol) exprChild);
		}

		else {
			HashSet<String> sortInstances = gen.generateInstances(expr, true);
			Formula result = null;
			if (term.isRecord() && term.hasVariables()) {
				result = new PrimitiveFormula(false);
				for (String termInSort : sortInstances) {
					Formula F = matchTerms(term, termInSort);
					if (F != null) {
						result = result.or(F);
					}
				}
				return result;
			} else {
				GroundSet set = null;
				if (exprChild.getId() == SparcTranslatorTreeConstants.JJTNUMERICRANGE) {
					String[] range = exprChild.image.split(" ");
					Integer lowest = Integer.parseInt(range[0]);
					Integer highest = Integer.parseInt(range[1]);
					set = new NumericRangeGroundSet(lowest, highest);
				} else {
					set = new MaterializedGroundSet(gen.generateInstances(expr,
							true));
				}
				return new PrimitiveFormula(new Term(term), set);
			}
		}
	}

	private Formula matchTerms(ASTterm term,String groundTerm) {
		if(term.isRecord()) {
			Pair<String, ArrayList<String>> groundRecord = StringListUtils
					.splitTerm(groundTerm.toString());
			if(groundRecord==null) {
				return null;
			}
			ASTsymbolicTerm symTerm = (ASTsymbolicTerm) term.jjtGetChild(0);
			ASTsymbolicFunction symFun = (ASTsymbolicFunction) symTerm
					.jjtGetChild(0);
			ASTtermList termList = (ASTtermList) symTerm.jjtGetChild(1);
			String recordName = symFun.image;
		    
			if (groundRecord.first.equals(recordName)
					&& groundRecord.second.size() == termList
							.jjtGetNumChildren()) {
				Formula conjFormula = null;
				for (int i = 0; i < groundRecord.second.size(); i++) {
					ASTterm rTerm = (ASTterm) termList
							.jjtGetChild(i);
					Formula matching=matchTerms(rTerm,groundRecord.second.get(i));
					if(matching==null)
						return null;
					if (i == 0) {
						conjFormula = matching;
					} else {
						conjFormula = conjFormula.and(matching);
					}
				}
				return conjFormula;
			}
			else {
				return null;
			}
		}
		else {
			return new PrimitiveFormula(new Term(
					term), new Term(new ASTterm(groundTerm)), Relation.eqasgn );
		}
	}

	private Formula reduceTerm(ASTterm term, ASTfunctionalSymbol expr) {
		if (!term.isVariable() && !term.isRecord()) {
			return new PrimitiveFormula(false);
		}

		ASTsortExpressionList list = (ASTsortExpressionList) expr
				.jjtGetChild(0);
		String recordName = expr.image.substring(0, expr.image.indexOf('('));
		ArrayList<String> newVariables = null;
		Formula result = null;
		ArrayList<ASTterm> terms=new ArrayList<ASTterm>();
		if (term.isVariable()) {
			 newVariables=uniqueVariables.generateNNewNames(list
						.jjtGetNumChildren());
			result = new PrimitiveFormula(new Term(term), new Term(recordName,
					newVariables), Relation.eqasgn);
			for(int i=0;i<newVariables.size();i++) {
				ASTvar var=new ASTvar(SparcTranslatorTreeConstants.JJTVAR);
				var.image=newVariables.get(i);
				ASTterm newTerm=new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
				newTerm.jjtAddChild(var, 0);
				terms.add(newTerm);
			}
			
		} else { // term is a record
			ASTsymbolicTerm symTerm = (ASTsymbolicTerm) (term.jjtGetChild(0));
			ASTsymbolicFunction symFunc = (ASTsymbolicFunction) (symTerm
					.jjtGetChild(0));
			String symFuncName = symFunc.image.substring(0,
					symFunc.image.indexOf('('));
			ASTtermList termList = (ASTtermList) symTerm.jjtGetChild(1);
			if (!symFuncName.equals(recordName)) {
				return new PrimitiveFormula(false);
			} else {
				
				for (int i = 0; i < termList.jjtGetNumChildren(); i++) {
					terms.add((ASTterm)termList.jjtGetChild(i));
				}
			}
		}

		if (expr.jjtGetNumChildren() > 1) {// if condition presents
			ASTcondition cond = (ASTcondition) expr.jjtGetChild(1);
			Formula reducedCondition = reduceCondition(cond, terms);
			if (result == null)
				result = reducedCondition;
			else {
				result = result.and(reducedCondition);
			}
		}
       
		for (int i = 0; i < list.jjtGetNumChildren(); i++) {
			ASTsortName sortName = (ASTsortName) list.jjtGetChild(i);
			ASTterm termToPass = null;
			if (term.isVariable()) {
				ASTvar var = new ASTvar(SparcTranslatorTreeConstants.JJTVAR);
				var.image = newVariables.get(i);
				ASTterm newTerm = new ASTterm(
						SparcTranslatorTreeConstants.JJTTERM);
				newTerm.jjtAddChild(var, 0);
				termToPass = newTerm;
			} else { // term is record
				ASTsymbolicTerm symTerm = (ASTsymbolicTerm) (term
						.jjtGetChild(0));
				ASTtermList termList = (ASTtermList) symTerm.jjtGetChild(1);
				 if(list.jjtGetNumChildren()!=termList.jjtGetNumChildren()) {
					 return new PrimitiveFormula(false);
				 }
				termToPass = (ASTterm) termList.jjtGetChild(i);
			}
			Formula reducedTerm=reduceTerm(termToPass, sortName.image);
			if(result==null) {
				result=reducedTerm;
			}
			else {
			    result = result.and(reducedTerm);
			}
		}
		return result;
	}

	private Formula reduceCondition(ASTcondition cond,
			ArrayList<ASTterm> terms) {

		return reduceCondition((ASTorCondition) cond.jjtGetChild(0),
				terms);
	}

	private Formula reduceCondition(ASTorCondition cond,
			ArrayList<ASTterm> terms) {
		Formula F = null;
		for (int i = 0; i < cond.jjtGetNumChildren(); i++) {
			Formula reducedAndCondition = reduceCondition(
					(ASTandCondition) cond.jjtGetChild(i), terms);
			if (i == 0) {
				F = reducedAndCondition;
			} else {
				F = F.or(reducedAndCondition);
			}
		}
		return F;
	}

	private Formula reduceCondition(ASTandCondition cond,
			ArrayList<ASTterm> terms) {
		Formula F = null;
		for (int i = 0; i < cond.jjtGetNumChildren(); i++) {
			Formula reducedUnaryCondition = reduceCondition(
					(ASTunaryCondition) cond.jjtGetChild(i), terms);
			if (i == 0) {
				F = reducedUnaryCondition;
			} else {
				F = F.and(reducedUnaryCondition);
			}
		}
		return F;
	}

	private Formula reduceCondition(ASTunaryCondition cond,
			ArrayList<ASTterm> terms) {
		if(terms.size()<2) {
			return new PrimitiveFormula(false);
		}
		if (cond.jjtGetNumChildren() == 1) {
			ASTcondition childCond = (ASTcondition) cond.jjtGetChild(0);
			if(cond.image.trim().equals("not(")) {
			    return reduceCondition(childCond, terms).negate();
			} else {
				return reduceCondition(childCond, terms);
			}
		} else {
			String[] relationContents = cond.image.split(" ");
			int term1Index = Integer.parseInt(relationContents[0]);
			int term2Index = Integer.parseInt(relationContents[2]);
			String relationStr = relationContents[1];
			Relation rel = null;
			if (relationStr.equals(">")) {
				rel = Relation.greater;
			} else if (relationStr.equals(">=")) {
				rel = Relation.greatereq;
			} else if (relationStr.equals("<")) {
				rel = Relation.less;
			} else if (relationStr.equals("<=")) {
				rel = Relation.lesseq;
			} else if (relationStr.equals("!=")) {
				rel = Relation.noteq;
			} else if (relationStr.equals("==")) {
				rel = Relation.eqrel;
			} else if (relationStr.equals("=")) {
				rel = Relation.eqasgn;
			}
		
			return new PrimitiveFormula(new Term(terms.get(term1Index)), new Term(terms.get(term2Index)),
					rel);
		}
	}

	private Formula reduceTerm(ASTterm term, ASTsetExpression expr) {
		ASTadditiveSetExpression exprChild = (ASTadditiveSetExpression) expr
				.jjtGetChild(0);
		return reduceTerm(term, exprChild);
	}

	private Formula reduceTerm(ASTterm term, ASTadditiveSetExpression expr) {
		Formula F = reduceTerm(term,
				(ASTmultiplicativeSetExpression) expr.jjtGetChild(0));
		for (int i = 1; i < expr.jjtGetNumChildren(); i++) {
			Formula nextF = reduceTerm(term,
					(ASTmultiplicativeSetExpression) expr.jjtGetChild(i));
			switch (expr.image.charAt(i)) {
			case '+':
				F = F.or(nextF);
				break;
			case '-':
				F = F.and(nextF.negate());
				break;
			}
		}
		return F;
	}

	private Formula reduceTerm(ASTterm term, ASTmultiplicativeSetExpression expr) {
		Formula F = reduceTerm(term,
				(ASTunarySetExpression) expr.jjtGetChild(0));
		for (int i = 1; i < expr.jjtGetNumChildren(); i++) {
			Formula nextF = reduceTerm(term,
					(ASTunarySetExpression) expr.jjtGetChild(i));
			F = F.and(nextF);
		}
		return F;
	}

	private Formula reduceTerm(ASTterm termNode, ASTunarySetExpression expr) {
		SimpleNode child = (SimpleNode) expr.jjtGetChild(0);
		switch (child.getId()) {
		case SparcTranslatorTreeConstants.JJTSORTNAME:
			return reduceTerm(termNode, sortNameToExpression.get(child.image));
		case SparcTranslatorTreeConstants.JJTCURLYBRACKETS:
			ASTcurlyBrackets curlyBrackets = (ASTcurlyBrackets) child;
			ASTconstantTermList termList = (ASTconstantTermList) curlyBrackets
					.jjtGetChild(0);

			GroundSet gset = new MaterializedGroundSet(gen.generateInstances(
					termList, true));
			return new PrimitiveFormula(new Term(termNode), gset);
		case SparcTranslatorTreeConstants.JJTSETEXPRESSION:
			return reduceTerm(termNode, (ASTsetExpression) child);
		case SparcTranslatorTreeConstants.JJTFUNCTIONALSYMBOL:
			return reduceTerm(termNode, (ASTfunctionalSymbol)child);
		default:
			return null;
		}

	}

	private HashSet<String> fetchVariables(ASTprogramRule rule) {
		HashSet<String> variables = new HashSet<String>();
		fetchVariables((SimpleNode) rule, variables);
		return variables;
	}

	private void fetchVariables(SimpleNode n, HashSet<String> fetchedVariables) {
		if (n.getId() == SparcTranslatorTreeConstants.JJTVAR) {
			fetchedVariables.add(n.toString());
		}

		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			fetchVariables((SimpleNode) n.jjtGetChild(i), fetchedVariables);
		}
	}
}
