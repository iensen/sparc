package sorts;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import parser.ASTsortExpression;
import parser.ParseException;

import parser.SimpleNode;
import parser.SparcTranslator;

/**
 * Class for creating built-in sorts
 * 
 */
public class BuiltIn {
	// maximal integer
	static private long maxInt = 2000;
	// names of built-in sorts
	public static String[] sortNames = { "nat" };
	// sort expressions for built-in sorts
	static String[] sortExpr = { "0.." + maxInt };

	/**
	 * @return max integer value
	 */
	static public long getMaxInt() {
		return maxInt;
	}

	/**
	 * Set max integer value
	 * 
	 * @param newV
	 *            new max integer value
	 */
	static public void setMaxInt(long newV) {
		maxInt = newV;
		sortExpr[0] = "0.." + maxInt;
	}

	/**
	 * Create a mapping from built in sort names to sort expressions in the form
	 * of AST nodes
	 * 
	 * @return sort_name->sort_expression mapping
	 */
	static public HashMap<String, ASTsortExpression> getBuiltInSorts() {
		HashMap<String, ASTsortExpression> bsorts = new HashMap<String, ASTsortExpression>();
		for (int i = 0; i < sortNames.length; i++) {
			Reader sr = new StringReader(sortExpr[i]);
			SparcTranslator p = new SparcTranslator(sr);
			p.constantsMapping=new HashMap<String,Long>(); // to avoid null pointer exception
			SimpleNode e = null;
			try {
				e = p.sortExpression();
			} catch (ParseException exc) {
				// TODO Auto-generated catch block
				exc.printStackTrace();
			}

			bsorts.put(sortNames[i], (ASTsortExpression) e);

		}
		return bsorts;
	}

}
