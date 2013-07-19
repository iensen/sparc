package warnings;

import java.util.ArrayList;
import parser.ASTatom;

public class WarningRuleCreator {

	public static ArrayList<String> createWarningRules(String originalRule,
			int lineNumber, int columnNumber, ArrayList<ASTatom> bodySortAtoms) {
		
		String ruleId = "\"" + originalRule + " ( line: " + lineNumber
				+ ", column: " + columnNumber + ")\"";

		StringBuilder rule1 = new StringBuilder("has_grounding(" + ruleId
				+ "):-");

		for (int i = 0; i < bodySortAtoms.size(); i++) {
			if (i != 0)
				rule1.append(",");
			rule1.append(bodySortAtoms.get(i).toString());
		}
		rule1.append(".");

		String rule2 = "warning(" + ruleId + "):- not has_grounding(" + ruleId
				+ ").";
		ArrayList<String> warningRules = new ArrayList<String>();
		warningRules.add(rule1.toString());
		warningRules.add(rule2);
		return warningRules;

	}
}
