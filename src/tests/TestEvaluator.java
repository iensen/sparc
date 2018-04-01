package tests;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import org.junit.Test;

import parser.ASTarithmeticTerm;
import parser.ParseException;
import parser.SparcTranslator;
import typechecking.TermEvaluator;

public class TestEvaluator {

	@Test
	public void testExpressions() {
		assertTrue("1+1!=2",evaluate("1+1")==2);
		assertTrue("3+5",evaluate("3+5")==8);
		assertTrue("(1)*10",evaluate("1*10")==10);
		assertTrue("((3+5)*10-1*5)*2+1!=151",evaluate("((3+5)*10-1*5)*2+1")==151);
	}
	
	
	@Test
	public void testNonEvaluable()
	{
		assertFalse("X is evaluable",isEvaluable("X"));
		assertTrue("1 is not evaluable",isEvaluable("1"));
		assertTrue("3 is evaluable",isEvaluable("3"));
		assertFalse("((3+5)*Y-1*5)*2+1 is evaluable",isEvaluable("((3+5)*Y-1*5)*2+1"));
		
	}
	
	
	private long evaluate(String test)
	{
		 TermEvaluator te=CreateValuator(test);
		 try {
			return te.evaluate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return 0;
	}
	
	private boolean isEvaluable(String test)
	{
	 TermEvaluator te=CreateValuator(test);
	 return te.isEvaluable();
	}
	
	private TermEvaluator CreateValuator(String s)
	{
		Reader sr= new StringReader(s);
		SparcTranslator p= new SparcTranslator(sr);
		p.constantsMapping = new HashMap<String,Long>();
	    try {
			ASTarithmeticTerm t=(ASTarithmeticTerm)p.arithmeticTerm();
			TermEvaluator teval=new TermEvaluator(t);
			return teval;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}

}
