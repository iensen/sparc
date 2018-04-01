package typechecking;

import parser.ASTadditiveArithmeticTerm;
import parser.ASTarithmeticTerm;
import parser.ASTatomicArithmeticTerm;
import parser.ASTmultiplicativeArithmeticTerm;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;

/**
 * Evaluate given term (if it is valuable, i.e, it has no variables)
 * 
 */
public class TermEvaluator {
	ASTarithmeticTerm aterm;

	/**
	 * Constructor
	 * 
	 * @param aterm
	 *            term to be stored in class field
	 */
	public TermEvaluator(ASTarithmeticTerm aterm) {
		this.aterm = aterm;
	}

	/**
	 * @return true if term stored in aterm field is valuable
	 */
	public boolean isEvaluable() {
		return isEvaluable(aterm);
	}

	/**
	 * Find value of the term stored in aterm field.
	 * 
	 * @return value of term
	 * @throws ParseException
	 *             if aterm contains variables or division by zero occurs
	 */
	public long evaluate() throws ParseException {
		return evaluate((ASTadditiveArithmeticTerm) aterm.jjtGetChild(0));
	}

	/**
	 * Find value of the term
	 * 
	 * @param aterm
	 *            additive arithmetic term
	 * @return value of term
	 * @throws ParseException
	 *             if term contains variables or division by zero occurs
	 */
	private long evaluate(ASTadditiveArithmeticTerm aterm)
			throws ParseException {
		long result = 0;
		for (int i = 0; i < aterm.jjtGetNumChildren(); i++) {
			if (aterm.image.charAt(i) == '+') {
				result += evaluate((ASTmultiplicativeArithmeticTerm) aterm
						.jjtGetChild(i));
			} else {
				result -= evaluate((ASTmultiplicativeArithmeticTerm) aterm
						.jjtGetChild(i));
			}
		}
		return result;
	}

	/**
	 * Find value of the term
	 * 
	 * @param mterm
	 *            multiplicative arithmetic term
	 * @return value of term
	 * @throws ParseException
	 *             if term contains variables or division by zero occurs
	 */
	private long evaluate(ASTmultiplicativeArithmeticTerm mterm)
			throws ParseException {
		long value = evaluate((ASTatomicArithmeticTerm) mterm.jjtGetChild(0));
		for (int i = 1; i < mterm.jjtGetNumChildren(); i++) {
			ASTatomicArithmeticTerm child = (ASTatomicArithmeticTerm) mterm
					.jjtGetChild(i);
			switch (mterm.image.charAt(i - 1)) {
			case '*':
				value *= evaluate((ASTatomicArithmeticTerm) mterm
						.jjtGetChild(i));
				break;
			case '/':
				long div = evaluate(child);
				if (div == 0)
					throw new ParseException("Division By Zero at line "
							+ child.getBeginLine() + " column "
							+ child.getBeginColumn());
				value /= div;
				break;
			case '%':
				long remdiv = evaluate(child);
				if (remdiv == 0)
					throw new ParseException("Division By Zero at line "
							+ child.getBeginLine() + " column "
							+ child.getBeginColumn());
				value %= remdiv;
				break;
			}
		}
		return value;
	}

	/**
	 * Find value of the term
	 * 
	 * @param atterm
	 *            atomic arithmetic term
	 * @return value of term
	 * @throws ParseException
	 *             if term contains variables or division by zero occurs
	 */
	private long evaluate(ASTatomicArithmeticTerm atterm) throws ParseException {
		if (!atterm.image.matches("([1-9][0-9]*)|0")
				&& !atterm.image.startsWith("(")) {
			throw new ParseException("term at line " + atterm.getBeginLine()
					+ " column " + atterm.getBeginColumn()
					+ " contains variables and not evaluable");
		} else if (atterm.image.startsWith("(")) {
			TermEvaluator te = new TermEvaluator(
					(ASTarithmeticTerm) atterm.jjtGetChild(0));
			return te.evaluate();
		} else
			return Integer.parseInt(atterm.image);

	}

	/**
	 * Check if given term is valuable
	 * 
	 * @param n
	 *            term represented in AST node
	 * @return true if term is valuable (has no variables)
	 */
	private boolean isEvaluable(SimpleNode n) {

		if (n.getId() == SparcTranslatorTreeConstants.JJTATOMICARITHMETICTERM) {
			if (n.jjtGetNumChildren() > 0
					&& ((SimpleNode) n.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTVAR) {
				return false;
			}
		}
		boolean result = true;
		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			if (!isEvaluable((SimpleNode) n.jjtGetChild(i))) {
				result = false;
			}
		}
		return result;

	}
}
