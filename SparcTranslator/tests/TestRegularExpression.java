package tests;

import static org.junit.Assert.*;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

import parser.ASTregularExpression;
import parser.ParseException;
import parser.RChoiceRemover;
import parser.RIdentifierSplitVisitor;
import parser.RRepeatitionRemover;
import parser.SimpleNode;
import parser.SparcTranslator;
import re.RegularExpression;


public class TestRegularExpression {
	@Test
	public void testConcatenation() {
		/*RegularExpression regexp =getRegularExpression("ab");
		assertTrue("Some problem with concatenation: ab does not satify regex ab",regexp.check("ab"));
		RegularExpression regexp2 =getRegularExpression("ab");
		assertFalse("abc satisfies regex ab",regexp2.check("abc"));
		RegularExpression regexp3 =getRegularExpression("abc");
		assertFalse("ab satisfies regex abc",regexp3.check("ab"));
		RegularExpression regexp4 =getRegularExpression("abcdefghijklmnopqrstuvwxyz");
		assertTrue("ab satisfies regex abc",regexp4.check("abcdefghijklmnopqrstuvwxyz"));
		*/
		RegularExpression regexp5 =getRegularExpression("a(a{0,2})");
		assertTrue("empty string does not satisfy a{0,0}",regexp5.check("a"));

	}
	
	@Test
	public void testDisjunction()
	{
		RegularExpression regexp =getRegularExpression("andy|vinny|cole|ben");
		assertTrue("andy does not satify regex andy|vinny|cole|ben",regexp.check("andy"));
		assertTrue("cole does not satify regex andy|vinny|cole|ben",regexp.check("cole"));
		assertTrue("ben does not satify regex andy|vinny|cole|ben",regexp.check("ben"));
		assertFalse("benn  satifies regex andy|vinny|cole|ben",regexp.check("benn"));
	}
	
	@Test
	public void testDigits()
	{
		RegularExpression regexp =getRegularExpression("0|1");
		assertTrue("0 does not satisfy 0|1 ",regexp.check("0"));
		assertTrue("1 does not satisfy 0|1",regexp.check("1"));
	}
	
	@Test
	public void testPrimitiveRepeatition()
	{
		RegularExpression regexp =getRegularExpression("a{2}b");
		//RegularExpression regexp=getRegularExpression("a{1,2}ndy{2}");
		assertTrue("andyy does not satify regex a{2}",regexp.check("aab"));

		assertFalse("a does not satify regex a{2}",regexp.check("a"));	
	}
	
	@Test
	public void testRepeatition()
	{
		RegularExpression regexp =getRegularExpression("a{1,2}ndy{2}");
		//RegularExpression regexp=getRegularExpression("a{1,2}ndy{2}");
		assertTrue("andyy does not satify regex a{1,2}ndy{2}",regexp.check("andyy"));
		assertTrue("aandyy does not satify regex a{1,2}ndy{2}",regexp.check("aandyy"));
		assertFalse("aaandyy does not satify regex a{1,2}ndy{2}",regexp.check("aaondyy"));	
	}
	
	@Test
	public void testRange()
	{
		RegularExpression regexp=getRegularExpression("[a-y] | [0-8]");
		assertTrue("t does not satisfy regex [a-y] | [0-8]",regexp.check("t"));
		assertTrue("5 does not satisfy regex [a-y] | [0-8]",regexp.check("t"));
		assertFalse("9 satisfies regex [a-y] | [0-8]",regexp.check("9"));
		assertFalse("z satisfies regex [a-y] | [0-8]",regexp.check("z"));
	}
	
	@Test
	public void testComplement() {
		RegularExpression regexp =getRegularExpression("~a");
		assertTrue("z does not satisfy regex ~a!",regexp.check("z"));
		//assertFalse("a does not satisfy regex a!",regexp.check("a"));
	}
	
	
	@Test
	public void testSingleTon() {
		RegularExpression regexp =getRegularExpression("c");
		assertTrue("a does not satisfy regex a!",regexp.check("c"));
	}

	  
    @Test
    public void testGenerating2()
    {
	    RegularExpression regexp3 =getRegularExpression("a{2}ndy{1,2}|vinny|cole|ben");
	    Set<String> s3 = new HashSet<String>(Arrays.asList("aandy","vinny","cole","ben","aandyy")); 
	    assertTrue(regexp3.generate().equals((s3)));
    }
	@Test 
	public void testGenerating()
	{
		RegularExpression regexp =getRegularExpression("a");
	    Set<String> s = new HashSet<String>(Arrays.asList("a")); 
		assertTrue(regexp.generate().equals((s)));
		
		RegularExpression regexp2 =getRegularExpression("a|b");
		Set<String> s2 = new HashSet<String>(Arrays.asList("a","b")); 
	    assertTrue(regexp2.generate().equals((s2)));
	    

	    final int size=100;
	    RegularExpression regexp4 =getRegularExpression("a{1,"+size+"}");
	    Set<String> s4=new HashSet<String>();
	    String current="";
	    for(int i=0;i<size;++i)
	    {
	    	current=current+"a";
	    	s4.add(current);
	    }
	    assertTrue(regexp4.generate().equals((s4)));    
	}

	
    private RegularExpression getRegularExpression(String test)
    {
    	Reader sr= new StringReader(test);
    	SparcTranslator p= new SparcTranslator(sr);
	    
	    SimpleNode e=null;
		try {
			e=p.regularExpression();
			RIdentifierSplitVisitor v=new RIdentifierSplitVisitor();
			e.jjtAccept(v,null);
			RRepeatitionRemover removeNMrepeatitions=new  RRepeatitionRemover();
			e.jjtAccept(removeNMrepeatitions,null);
			RChoiceRemover choiceremover=new RChoiceRemover();
			e.jjtAccept(choiceremover,null);
		} catch (ParseException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		}
		System.out.flush();
	    return new RegularExpression((ASTregularExpression)e);
    }
}
