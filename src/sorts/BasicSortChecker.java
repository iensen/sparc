package sorts;

import java.util.HashMap;

import parser.ASTsetExpression;
import parser.ASTsortExpression;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;

//TODO add comments
public class BasicSortChecker {
	public static boolean isBasic(ASTsortExpression se,
			HashMap<String, ASTsortExpression> sortNameToExpression) {
		int id = ((SimpleNode) se.jjtGetChild(0)).getId();
		switch (id) {
		case SparcTranslatorTreeConstants.JJTNUMERICRANGE:
			return true;
		case SparcTranslatorTreeConstants.JJTIDENTIFIERRANGE:
			return true;
		case SparcTranslatorTreeConstants.JJTCONCATENATION:
			return true;
		case SparcTranslatorTreeConstants.JJTSETEXPRESSION:
			return checkSetExpression((ASTsetExpression) se.jjtGetChild(0),
					sortNameToExpression);
		default:
			return false;
		}
	}

	private static boolean checkSetExpression(SimpleNode se,
			HashMap<String, ASTsortExpression> sortNameToExpression) {
		if (se.getId() == SparcTranslatorTreeConstants.JJTSORTNAME) {
			return isBasic(sortNameToExpression.get(se.image),
					sortNameToExpression);
		}
		if (se.getId() == SparcTranslatorTreeConstants.JJTCURLYBRACKETS) {
			return checkCurlyBrackets((SimpleNode) se);
		}
		
		if(se.getId() == SparcTranslatorTreeConstants.JJTFUNCTIONALSYMBOL) {
			return false;
		}
		
		boolean result = true;
		for (int i = 0; i < se.jjtGetNumChildren(); i++) {
			result = result
					& checkSetExpression((SimpleNode) se.jjtGetChild(i),
							sortNameToExpression);
		}
		return result;
	}

	private static boolean checkCurlyBrackets(SimpleNode se) {
		if (se.getId() == SparcTranslatorTreeConstants.JJTCONSTANTTERM) {
			if (se.jjtGetNumChildren() == 1) {
				SimpleNode child = (SimpleNode) se.jjtGetChild(0);
				if (child.getId() == SparcTranslatorTreeConstants.JJTCONSTANTTERMLIST) {
					return false;
				}
			}
			//if(se.jjtGetNumChildren()==0 && se.image.indexOf('_')!=-1)
			//	return false;
		}
		boolean result = true;
		for (int i = 0; i < se.jjtGetNumChildren(); i++) {
			result = result
					& checkCurlyBrackets((SimpleNode) se.jjtGetChild(i));
		}
		return result;
	}
}
