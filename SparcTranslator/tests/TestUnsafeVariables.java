package tests;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import parser.ASTprogramRule;
import parser.ParseException;
import parser.SparcTranslator;

import translating.UnsafeVariableFetcher;

public class TestUnsafeVariables {
    
	@Test
	public void test1() {
		Set<String> s3 = new HashSet<String>(Arrays.asList("X")); 
	    HashSet<String> result=null;
		try {
			result=getUnsafeVariables("p(X).");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(result.toString());
		assertTrue("p(X) has X unsafe variable",s3.equals(result));
	}
	
	@Test
	public void test2() {
		Set<String> s3 = new HashSet<String>(Arrays.asList("X")); 
	    HashSet<String> result=null;
		try {
			result=getUnsafeVariables(":-not p(X).");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(result.toString());
		assertTrue(":-not p(X). has X unsafe variable",s3.equals(result));
	}
	
	@Test
	public void test3() {
		Set<String> s3 = new HashSet<String>(Arrays.asList("Y")); 
	    HashSet<String> result=null;
		try {
			result=getUnsafeVariables("p(Y):-not p(X),q(X).");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(result.toString());
		assertTrue("p(Y):-not p(X),q(X) has Y unsafe variable",s3.equals(result));
	}
	
	@Test
	public void test4() {
		Set<String> s3 = new HashSet<String>(Arrays.asList("Y")); 
	    HashSet<String> result=null;
		try {
			result=getUnsafeVariables("p(X) | q(Y):-p(X).");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(result.toString());
		assertTrue("p(X) | q(Y):-p(X). has Y unsafe variable",s3.equals(result));
	}
	
	
	private HashSet<String> getUnsafeVariables(String test) throws ParseException
    {
		Reader sr= new StringReader(test);
		SparcTranslator p= new SparcTranslator(sr);
	    p.token_source.SwitchTo(1);
		ASTprogramRule rule=(ASTprogramRule) p.programRule();
	   UnsafeVariableFetcher uvf=new UnsafeVariableFetcher();
	   return uvf.fetchUnsafeGlobalVariables(rule);
    }
}
