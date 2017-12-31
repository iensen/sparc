package tests;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

import parser.ASTsortExpression;
import parser.ParseException;
import parser.SparcTranslatorTreeConstants;
import parser.SparcTranslator;


public class testConditions {
	
	@Test
	public void test1() {
		getRenaming("on(#block(X),#block(Y)):X!=Y");
	}
	
	private void getRenaming(String test)
    {
    	Reader sr= new StringReader(test);
    	SparcTranslator p= new SparcTranslator(sr);
	    p.sortNameToExpression=new HashMap<String, ASTsortExpression>();
	    p.definedRecordNames = new HashSet<String>();
	    ASTsortExpression h=new ASTsortExpression(SparcTranslatorTreeConstants.JJTSORTEXPRESSION);
	    p.sortNameToExpression.put("block", h);
	   try {
			p.unarySetExpression();
		} catch (ParseException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		}
    }
	
	
}
