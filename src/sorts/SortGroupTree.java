package sorts;

import java.util.ArrayList;
import java.util.HashSet;

import parser.ASTconstantTerm;
import parser.ASTsortExpression;
import parser.SimpleNode;
import utilities.Pair;
/**
 * This class represents a collection of sorts having the same structure of functional symbols
 * For example, the sorts f(g(a)) and f(g(c)) would belong to the same group.
 * @author Evgenii Balai
 *
 */
public class SortGroupTree {
        SortGroupTreeNode root;
        ArrayList<ArrayList<String> > instances;
        String sortName;
        HashSet<String> usedSortNames;
		public SortGroupTree(ASTconstantTerm term, HashSet<String> usedSortNames) {
        	this.root = new SortGroupTreeNode(term);
        	this.usedSortNames = usedSortNames;
        	// initialize array of instances
        	instances = new ArrayList<ArrayList<String>>();
        	
        	// add the first instance
        	this.addInstance(term);
        }
        
        /**
         * Add new instances (constant term) to the group
         * @param term
         */
        public void addInstance(ASTconstantTerm term) {
        	ArrayList<String> instance = new ArrayList<String>();
        	addInstanceRec(term,instance);
        	instances.add(instance);
        }
        
        /**
         * Recursion implementing addInstance
         * @param term current node in the tree traversal process
         * @param leafIndex the number of leaves of the tree we have already seen
         */
        private void addInstanceRec(SimpleNode term, ArrayList<String>instance) {
        	if(term.jjtGetNumChildren() == 0) {
        		instance.add(term.image);
        	}
        	
        	for(int i=0;i<term.jjtGetNumChildren();i++) {
        		addInstanceRec((SimpleNode) term.jjtGetChild(i),instance);
        	}
        }
        
        /**
         * 
         * @param usedSortNames
         * @return
         */
        public ArrayList<Pair<String,ASTsortExpression>> produceNewSorts(HashSet<String> usedSortNames) {
        	ArrayList<Pair<String,ASTsortExpression>> result = new ArrayList<Pair<String,ASTsortExpression>>();
        	ArrayList<ArrayList<String>> convertedInstances = vhConvert();
        	this.sortName = root.fillNewSortDefs(result,convertedInstances,usedSortNames,new IntCounter());
	        return result;
        }
        
        private ArrayList<ArrayList<String>> vhConvert() {
        	ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>> ();
        	for(int i=0;i<instances.get(0).size();i++) {
        		result.add(new ArrayList<String>());
        	}
        	for(int i=0;i<instances.size();i++) {
        		for(int j=0;j<instances.get(0).size();j++) {
        			result.get(j).add(i, instances.get(i).get(j));
        		}
        	}
        	return result;
        	
        }
        
}

