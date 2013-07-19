package sorts;

import java.util.HashMap;
import java.util.HashSet;

import parser.ASTcondition;
import parser.ASTfunctionalSymbol;
import parser.ASTsortExpression;
import parser.ASTsortExpressionList;
import parser.ASTsortName;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;

public class FunctionalSymbolChecker {
	
   public static boolean checkFunctionalSymbolSorts(ASTfunctionalSymbol funcSymbol, HashMap<String, ASTsortExpression> sortNameToExpression ) {
	   if(funcSymbol.jjtGetNumChildren()<2) // no 
		   return true;
	   ASTcondition cond =(ASTcondition)funcSymbol.jjtGetChild(1);
	   HashSet<Integer> basicSortIndexes=fetchAllBasicSortIndexes(cond);
	   return checkAllBasicSorts((ASTsortExpressionList)funcSymbol.jjtGetChild(0), basicSortIndexes,sortNameToExpression);
   }
   
   
   private static boolean checkAllBasicSorts(ASTsortExpressionList sortList,
		HashSet<Integer> basicSortIndexes,HashMap<String, ASTsortExpression> sortNameToExpression) {
	   
	   for(int i=0;i<sortList.jjtGetNumChildren();i++) {
		   if(!basicSortIndexes.contains(i)) 
			   continue;
		   ASTsortName sortName=(ASTsortName)sortList.jjtGetChild(i);
		   ASTsortExpression sortExpr=(ASTsortExpression)sortNameToExpression.get(sortName.image);
		   if(!BasicSortChecker.isBasic(sortExpr,sortNameToExpression)) {
			   return false;
		   }
	   }
	   return true;
   }


private static HashSet<Integer> fetchAllBasicSortIndexes(SimpleNode n) {
	   
	   HashSet<Integer> result=new HashSet<Integer>();
	   if(n.getId()==SparcTranslatorTreeConstants.JJTUNARYCONDITION  && n.jjtGetNumChildren()==2)
	   {
		   
		    String[] relationArray = n.image.split(" ");
		    int arg1 = Integer.parseInt(relationArray[0]);
			int arg2 = Integer.parseInt(relationArray[2]);
			String relationString = relationArray[1];
			if(relationString.equals(">=") || relationString.equals("<=") ||
					relationString.equals(">") || relationString.equals("<"))
			{
				result.add(arg1);
				result.add(arg2);
			}
	   }
	
	   for(int i=0;i<n.jjtGetNumChildren();i++) {
		   result.addAll(fetchAllBasicSortIndexes((SimpleNode)n.jjtGetChild(i)));	   
	   }
	   return result;
   }
}
