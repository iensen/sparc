package sorts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import parser.ASTadditiveSetExpression;
import parser.ASTconstantTerm;
import parser.ASTconstantTermList;
import parser.ASTcurlyBrackets;
import parser.ASTmultiplicativeSetExpression;
import parser.ASTsetExpression;
import parser.ASTsortDefinitions;
import parser.ASTsortExpression;
import parser.ASTsortName;
import parser.ASTunarySetExpression;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;
import utilities.Pair;

// this class implements search and replacement of curly brackets in the sort definition
// with equivalent sort definitions s.t no curly brackets will contain a functional symbol
public class CurlyBracketsExpander {
	// mapping from sort names to sort expressions assigned to the sorts
    private HashMap<String, ASTsortExpression> sortNameToExpression;
    // names which should not be used for the newly created sorts
    private HashSet<String> usedSortNames;
	public CurlyBracketsExpander(HashMap<String, ASTsortExpression> sortNameToExpression) {
		this.sortNameToExpression = sortNameToExpression;		
		this.usedSortNames = new HashSet<String>();
		for(String s: sortNameToExpression.keySet()) {
			usedSortNames.add(s);
		}
	}
	
	// replace curly brackets with equivalent sort definitions
	public  void ExpandCurlyBrackets(ASTsortDefinitions sortDefs) {
		ExpandCurlyBracketsRec((SimpleNode) sortDefs,null);
	}
	
	
	// recursively search for curly brackets and do the replacement
	public void ExpandCurlyBracketsRec(SimpleNode node,SimpleNode parent) {
		if(node.getId()==SparcTranslatorTreeConstants.JJTCURLYBRACKETS) {
			ArrayList<Pair<String,ASTsortExpression>> newSorts = expand((ASTcurlyBrackets)node);
			for (Pair<String,ASTsortExpression> newSort: newSorts) {
				sortNameToExpression.put(newSort.first, newSort.second);
			}
			// remove curly brackets
			parent.removeIthChild(0);
			ASTsortName newSort = new ASTsortName(SparcTranslatorTreeConstants.JJTSORTNAME);
			newSort.image = newSorts.get(newSorts.size()-1).first;
			parent.jjtAddChild(newSort, 0);
		} else {
			for(int i=0;i<node.jjtGetNumChildren();i++) {
				ExpandCurlyBracketsRec((SimpleNode)node.jjtGetChild(i),node);
			}
		}
			
		
	}
	
	// construct the necessary new sorts for the found curly brackets
    private   ArrayList<Pair<String,ASTsortExpression>> expand(ASTcurlyBrackets curlyBrackets) {
    	
    	ArrayList<SortGroupTree> groups = new ArrayList<SortGroupTree> ();
    	
    	// find matching group 
    	ASTconstantTermList termList = (ASTconstantTermList)curlyBrackets.jjtGetChild(0);
    	for(int termId = 0;termId< termList.jjtGetNumChildren();termId++) {
    		ASTconstantTerm curTerm = (ASTconstantTerm)termList.jjtGetChild(termId);
    		boolean groupFound = false;
    		for(int i=0;i<groups.size();i++) {
        		if(groups.get(i).root.matchesTo(new SortGroupTreeNode(curTerm))) {
        			groups.get(i).addInstance(curTerm);
        			groupFound = true;
        		}
        	}
    		if(!groupFound) {
    			groups.add(new SortGroupTree(curTerm,usedSortNames));
    		}
    	}
    	ArrayList<Pair<String,ASTsortExpression>> result = new ArrayList<Pair<String,ASTsortExpression>>();
    	for(int i=0;i<groups.size();i++) {
    		result.addAll(groups.get(i).produceNewSorts(usedSortNames));
    	}
    	
    	// we need one more sort combining everything which was produced from groups
    	ASTsortExpression expr = new ASTsortExpression(SparcTranslatorTreeConstants.JJTSORTEXPRESSION);
    	ASTsetExpression setExpr = new ASTsetExpression(SparcTranslatorTreeConstants.JJTSETEXPRESSION);
    	ASTadditiveSetExpression addSetExpr =
    			new ASTadditiveSetExpression(SparcTranslatorTreeConstants.JJTADDITIVESETEXPRESSION);
    	expr.jjtAddChild(setExpr, 0);
    	setExpr.jjtAddChild(addSetExpr, 0);
    	addSetExpr.image="";
    	for(int i=0;i<groups.size();i++) {
    		addSetExpr.image+="+";
    		ASTmultiplicativeSetExpression multExpr =
    				new ASTmultiplicativeSetExpression(SparcTranslatorTreeConstants.JJTMULTIPLICATIVESETEXPRESSION);
    		ASTunarySetExpression unExpr = 
    				new ASTunarySetExpression(SparcTranslatorTreeConstants.JJTUNARYSETEXPRESSION);
    		ASTsortName sortName = new ASTsortName( SparcTranslatorTreeConstants.JJTSORTNAME);
    		sortName.image = groups.get(i).sortName;
    		multExpr.jjtAddChild(unExpr, 0);
    		unExpr.jjtAddChild(sortName, 0);
    		addSetExpr.jjtAddChild(multExpr, i);
    	}
    	
    	String sortName = SortUtils.generateNewSortName(usedSortNames);
    	result.add(new Pair<String, ASTsortExpression>(sortName, expr));
    	return result;    	
    }
}
