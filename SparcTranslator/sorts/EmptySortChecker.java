package sorts;

import java.util.HashMap;

import parser.ASTsortExpression;
import translating.InstanceGenerator;

public class EmptySortChecker {
	public static boolean isEmpty(
			ASTsortExpression se,
			HashMap<String,ASTsortExpression> sortNameToExpression) {
		InstanceGenerator gen=new InstanceGenerator(null, sortNameToExpression);
		return gen.generateInstances(se).size()==0;
	}
}
