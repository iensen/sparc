package translating;

import parser.ASTadditiveSortExpression;
import parser.ASTmultiplicativeSortExpression;
import parser.ASTsortExpression;
import parser.ASTunarySortExpression;
import parser.SparcTranslatorTreeConstants;

/**
 * Class for manipulating with sort expressions Allows addition and intersection
 * of expressions and dynamically changes current sort expression when one of
 * these operations occurs.
 */
public class DynamicSortExpression {
	ASTsortExpression root;

	public DynamicSortExpression() {
		root = new ASTsortExpression(
				SparcTranslatorTreeConstants.JJTSORTEXPRESSION);
		root.image = "";
	}

	public DynamicSortExpression(ASTsortExpression expr) {
		root = expr;
	}

	/**
	 * Union this expression with
	 * 
	 * @param expr
	 *            additive expression
	 */
	private void add(ASTadditiveSortExpression expr) {
		ASTadditiveSortExpression addexpr = null;
		if (root.jjtGetNumChildren() == 0) {
			addexpr = new ASTadditiveSortExpression(
					SparcTranslatorTreeConstants.JJTADDITIVESORTEXPRESSION);
			addexpr.image = "";
			root.jjtAddChild(addexpr, 0);
		} else {
			addexpr = (ASTadditiveSortExpression) root.jjtGetChild(0);
		}

		for (int i = 0; i < expr.jjtGetNumChildren(); i++) {
			if (i == 0) {
				addexpr.image = addexpr.image + "+";
			} else {
				addexpr.image = addexpr.image + expr.image.charAt(i - 1);
			}
			addexpr.jjtAddChild(expr.jjtGetChild(i),
					addexpr.jjtGetNumChildren());
		}
	}

	/**
	 * Union of given sort expression and
	 * 
	 * @param expr
	 *            expression
	 */
	public void add(ASTsortExpression expr) {
		if (expr == this.root)
			return;
		add((ASTadditiveSortExpression) expr.jjtGetChild(0));
	}

	/**
	 * Subtract given expression from this expression
	 * 
	 * @param expr
	 *            expression
	 */
	public void substract(ASTsortExpression expr) {
		substract((ASTadditiveSortExpression) expr.jjtGetChild(0));
	}

	/**
	 * Subtract given additive expression from this expression
	 * 
	 * @param expr
	 *            expression
	 */
	public void substract(ASTadditiveSortExpression expr) {
		ASTadditiveSortExpression addexpr = null;
		if (root.jjtGetNumChildren() == 0) {
			addexpr = new ASTadditiveSortExpression(
					SparcTranslatorTreeConstants.JJTADDITIVESORTEXPRESSION);
			addexpr.image = "";
			root.jjtAddChild(addexpr, 0);
		} else {
			addexpr = (ASTadditiveSortExpression) root.jjtGetChild(0);
		}

		for (int i = 0; i < expr.jjtGetNumChildren(); i++) {
			if (i == 0) {
				addexpr.image = addexpr.image + "-";
			} else {
				addexpr.image = addexpr.image
						+ (expr.image.charAt(i - 1) == '+' ? '-' : '+');
			}
			addexpr.jjtAddChild(expr.jjtGetChild(i),
					addexpr.jjtGetNumChildren());
		}
	}

	/**
	 * Multiply given additive expression from this expression
	 * 
	 * @param expr
	 *            given expression
	 */
	public void multiply(ASTsortExpression expr) {
		if (expr == this.root)
			return;
		if (root.jjtGetNumChildren() == 0) {
			root = expr;
			return;
		}
		// create new summand
		ASTadditiveSortExpression newAdd = new ASTadditiveSortExpression(
				SparcTranslatorTreeConstants.JJTADDITIVESORTEXPRESSION);
		newAdd.image = "+";
		// create new multiplier
		ASTmultiplicativeSortExpression newMult = new ASTmultiplicativeSortExpression(
				SparcTranslatorTreeConstants.JJTMULTIPLICATIVESORTEXPRESSION);
		// create new unary expression
		newMult.image = "";
		ASTunarySortExpression newUn = new ASTunarySortExpression(
				SparcTranslatorTreeConstants.JJTUNARYSORTEXPRESSION);
		newUn.image = "()";
		// create abstract syntax subtree:
		newUn.jjtAddChild(expr, 0);
		ASTunarySortExpression newUnForOld = null;
		if (root.jjtGetNumChildren() > 0) {
			newUnForOld = new ASTunarySortExpression(
					SparcTranslatorTreeConstants.JJTUNARYSORTEXPRESSION);
			newUnForOld.image = "()";
			newUnForOld.jjtAddChild(root, 0);
		}
		newMult.jjtAddChild(newUn, 0);
		if (newUnForOld != null) {
			newMult.jjtAddChild(newUnForOld, 1);
		}
		newAdd.jjtAddChild(newMult, 0);
		ASTsortExpression newRoot = new ASTsortExpression(
				SparcTranslatorTreeConstants.JJTSORTEXPRESSION);
		newRoot.jjtAddChild(newAdd, 0);
		root = newRoot;
	}

	public ASTsortExpression getRoot() {
		return root;
	}
}
