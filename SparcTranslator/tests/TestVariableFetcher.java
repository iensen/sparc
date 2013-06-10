package tests;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import parser.ASTprogram;
import parser.ASTprogramRule;
import parser.ASTprogramRules;
import parser.ASTsortExpression;
import parser.ASTterm;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslator;
import parser.SparcTranslatorTreeConstants;
import translating.TermFetcher;


public class TestVariableFetcher {

	HashMap<String, ASTsortExpression> sortNameToExpression;
	ASTprogramRules programRules;
	HashMap<String, ArrayList<String>> predicateArgumentSorts;

	@Test
	public void testUsa()  {
		testFile("test/usa.sp");
	}
	
	@Test
	public void testPrimitive()  {
		testFile("test/fetcher1.sp");
	}
    
	@Test
	public void testFetcher2() {
		testFile("test/fetcher2.sp");
	}

	private void testFile(String filePath) {
		try {
			setUpFile(filePath);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<programRules.jjtGetNumChildren();i++) {
	//		if(i==1)break;
			System.out.println("RULE NUMBER "+(i+1));
			ASTprogramRule rule=(ASTprogramRule)programRules.jjtGetChild(i);
			TermFetcher vf=new TermFetcher (predicateArgumentSorts);
			HashMap<ASTterm, String> out=   vf.fetchTermSorts(rule);
			System.out.println(out.toString());
			
		}
	}
	private void setUpFile(String filePath) throws ParseException {

		Reader sr = null;
		try {
			sr = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SparcTranslator p = new SparcTranslator(sr);

		ASTprogram program = (ASTprogram) p.program();
		appendToVariableNamesIn(program, "_G");
		this.sortNameToExpression = p.sortNameToExpression;
		this.predicateArgumentSorts = p.predicateArgumentSorts;
		programRules = (ASTprogramRules) program.jjtGetChild(2);
	
	}

	void appendToVariableNamesIn(SimpleNode n, String suffix) {

		if (n.getId()==SparcTranslatorTreeConstants.JJTVAR) {
			n.image = n.image + suffix;
		}
		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			appendToVariableNamesIn((SimpleNode) (n.jjtGetChild(i)), suffix);
		}
	}
}
