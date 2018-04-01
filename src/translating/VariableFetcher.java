package translating;

import java.util.HashSet;

import parser.ASTprogramRule;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;

/**
* Variable fetcher finds all variables in given rule
* and returns hash set of them
*/
public class VariableFetcher {
	
   /**
    * Fetch variables from given rule	
    * @param rule ASTnode representing a rule
    * @return HashSet of found variables
    */
   public HashSet<String> fetchVariables(ASTprogramRule rule) {
	   HashSet<String> vars=new HashSet<String>();
	   fetchVariables(rule,vars);
	   return vars;
   }
   /**
    * Recursively fetch variables from given AST node
    * @param node 
    * @param vars Variables found so far
    */
   public void fetchVariables(SimpleNode node, HashSet<String>vars) {
	   if(node.getId()==SparcTranslatorTreeConstants.JJTVAR) {
		   vars.add(node.toString());
	   }
	   else {
		   //recursive calls
		   for(int i=0;i<node.jjtGetNumChildren();i++) {
			  fetchVariables((SimpleNode)node.jjtGetChild(i),vars);
		   }
	   }
	   
   }
}
