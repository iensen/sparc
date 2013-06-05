package translating;

import parser.ASTadditiveSortExpression;
import parser.ASTmultiplicativeSortExpression;
import parser.ASTsortExpression;
import parser.ASTunarySortExpression;
import parser.SparcTranslatorTreeConstants;

/**
 * Build AST sort expression from objects which are lower in hierarchy described
 * by grammar
 */
public class SortExpressionBuilder {
	/**
	 * Build sortExpression from unary sort expression
	 * 
	 * @param expr
	 *            unary sort expression
	 * @return sort expression built from expr
	 */
	public ASTsortExpression buildFrom(ASTunarySortExpression expr) {
		ASTsortExpression se = new ASTsortExpression(
				SparcTranslatorTreeConstants.JJTSORTEXPRESSION);
		ASTadditiveSortExpression addse = new ASTadditiveSortExpression(
				SparcTranslatorTreeConstants.JJTADDITIVESORTEXPRESSION);
		// we have the only one summand
		addse.image = "+";
		// and one multiplier
		ASTmultiplicativeSortExpression multse = new ASTmultiplicativeSortExpression(
				SparcTranslatorTreeConstants.JJTMULTIPLICATIVESORTEXPRESSION);
		// attach new nodes to sort expression root
		se.jjtAddChild(addse, 0);
		addse.jjtAddChild(multse, 0);
		multse.jjtAddChild(expr, 0);
		return se;
	}

}
