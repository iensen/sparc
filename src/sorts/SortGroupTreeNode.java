package sorts;

import java.util.ArrayList;
import java.util.HashSet;

import parser.ASTconstantTerm;
import parser.ASTconstantTermList;
import parser.ASTsortExpression;
import utilities.Pair;
/**
 * This class represent a node in the group tree (see SortGroupTreeClass for more details)
 * @author Evgenii Balai
 */
public class SortGroupTreeNode {
    ArrayList<SortGroupTreeNode> children;
    String name;
    public SortGroupTreeNode(String name) {
    	this.name = name;
    	children = new ArrayList<SortGroupTreeNode>();  	
    }
    
    
    /**
     * Construct tree from given term
     * @param term
     */
    public SortGroupTreeNode(ASTconstantTerm term) {
    	if(term.jjtGetNumChildren() == 0) {
    		this.name = null;
    		this.children= new ArrayList<SortGroupTreeNode>();  
    	} 
    	else {
    	    this.name = term.image.substring(0,term.image.indexOf('('));
            this.children = new ArrayList<SortGroupTreeNode>();
            ASTconstantTermList termList = (ASTconstantTermList)term.jjtGetChild(0);
            for(int i =0;i<termList.jjtGetNumChildren();i++) {
            	this.children.add(new SortGroupTreeNode((ASTconstantTerm)termList.jjtGetChild(i)));
            }
    	}
        
    	
    }
    
    /**
     * Generate new Sort Definitions from given Group Tree
     * @param defs
     * @param leaveSortInstances collection of the instances(identifiers) attached 
     *         that may be attached to the tree's leaves
     * @param usedSortNames
     * @param leaveIndex
     * @return the name of the sort attached to the root of the tree
     */
    public String fillNewSortDefs(ArrayList<Pair<String,ASTsortExpression>> defs,
    		                ArrayList<ArrayList<String> > leafSortInstances,
    		                HashSet<String> usedSortNames,
    		                IntCounter leafIndex) {
    	String newSortName = SortUtils.generateNewSortName(usedSortNames);
    	if(this.children.size() == 0 ) {
    		// create #s definition
    		defs.add(new Pair<String,ASTsortExpression>(newSortName,new ASTsortExpression(leafSortInstances.get(leafIndex.value))));
    		++leafIndex.value;
    	}
    	else {
    		String funcSymbol = this.name;
    		ArrayList<String> argSortNames = new ArrayList<String>();
    		for(int i = 0;i<children.size();i++) {
    		  String iSortName = children.get(i).fillNewSortDefs(defs,leafSortInstances,usedSortNames,leafIndex);	
    	      argSortNames.add(iSortName);	  
    		}
    		defs.add(new Pair<String,ASTsortExpression>(newSortName,new ASTsortExpression(funcSymbol,argSortNames)));
    	}
    	return newSortName;  	
    }
    /**
     * The method checks of internal structures of this node and other nodes
     * are same up to leaves renaming
     * For example, f(g(a)) matches f(g(c))
     */
    public boolean matchesTo(SortGroupTreeNode otherNode) {
    	if(this.getChildrenCount() > 1) {
    		return false; // WE CANNOT GROUP TERMS HAVING MORE THAN ONE LEAF TOGETHER!!!!!!!!!!!!!!!!!!!!!!
    	}
    	if(this.children.size() != otherNode.children.size()) {
    		return false;
    	}
    	
    	
    	if(this.children.size() == 0) {
    		return true;
    	}
    	if(!this.name.equals(otherNode.name)) 
    		return false;
    	boolean matches = true;
    	for(int i = 0; i<this.children.size();i++) {
    		if(!this.children.get(i).matchesTo(otherNode.children.get(i))) {
    			matches = false;
    			break;
    		}
    	}
    	
    	return matches;
    }
    
    public int getChildrenCount() {
    	if(this.children.size()==0)
    		return 1;
    	int count = 0;
    	for(int i = 0;i<children.size();i++) {
    		count += children.get(i).getChildrenCount();
    	}
    	
    	return count;
    }
    
  
}
