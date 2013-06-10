package sorts;


import parser.ASTsortExpression;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;

//TODO
public class BasicSortChecker {
	public static boolean isBasic(ASTsortExpression se) {
		int id = ((SimpleNode) se.jjtGetChild(0)).getId();
		switch (id) {
		case SparcTranslatorTreeConstants.JJTNUMERICRANGE:
			return true;
		case SparcTranslatorTreeConstants.JJTIDENTIFIERRANGE:
			return true;
		case SparcTranslatorTreeConstants.JJTCONCATENATION:
			return true;
		default:
			return false;
		}
	}
}
