package sorts;

import java.util.HashMap;
import parser.ASTsortExpression;
import translating.InstanceGenerator;

//TODO: переписать:)
public class EmptySortChecker {
	public static boolean isEmpty(
			ASTsortExpression se,
			HashMap<String,ASTsortExpression> sortNameToExpression) {
		InstanceGenerator gen=new InstanceGenerator(sortNameToExpression);
		return gen.generateInstances(se,true).size()==0;
	}
}
