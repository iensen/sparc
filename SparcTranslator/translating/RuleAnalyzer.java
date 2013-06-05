package translating;

import parser.ASTbody;
import parser.ASTprogramRule;
import parser.ASTunlabeledProgramRule;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;
import parser.SparcTranslatorConstants;

/**
 * Class checks if rule written in AST node is a consistency restoring rule
 */
public class RuleAnalyzer {
	private ASTprogramRule rule;

	/**
	 * Constructor
	 * 
	 * @param rule
	 */
	public RuleAnalyzer(ASTprogramRule rule) {
		this.rule = rule;
	}

	public ASTbody getBody() {
		return getBody(rule);
	}

	/**
	 * @param node
	 *            root of abstract syntax tree
	 * @return body of the rule, which belong to the tree, or null if the body
	 *         does not present
	 */
	private ASTbody getBody(SimpleNode node) {
		if (node.getId() == SparcTranslatorTreeConstants.JJTBODY) {
			return (ASTbody) node;
		}
		ASTbody body = null;
		// search in descendant nodes
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			SimpleNode child = (SimpleNode) node.jjtGetChild(i);
			body = getBody(child);
			if (body != null) {
				return body;
			}
		}
		return null;
	}

	/**
	 * @return true if rule composed to this class object is consistency
	 *         restoring
	 */
	public boolean isCrRule() {
		if (((SimpleNode) (rule.jjtGetChild(0))).getId() == SparcTranslatorTreeConstants.JJTUNLABELEDPROGRAMCRRULE) {
			return true;
		} else {
			ASTunlabeledProgramRule urule = (ASTunlabeledProgramRule) rule
					.jjtGetChild(0);
			String crsep = SparcTranslatorConstants.tokenImage[SparcTranslatorConstants.CRRSEP];
			// remove quotes:
			crsep = crsep.substring(1, crsep.length() - 1);
			if (urule.image != null && urule.image.equals(crsep)) {
				return true;
			}
			return false;
		}
	}
}
