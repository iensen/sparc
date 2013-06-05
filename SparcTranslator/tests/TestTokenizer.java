package tests;
import static org.junit.Assert.*;
import java.io.StringReader;
import org.junit.Test;

import parser.SimpleCharStream;
import parser.SparcTranslatorConstants;
import parser.SparcTranslatorTokenManager;
import parser.Token;



public class TestTokenizer {

	@Test
	public void tokenizeSimpleProgram() {
		String cmd=  "sort   \r definitions\n s=$a|b.   \n"+
				     "predicate       \r    declarations p(s).\n"+
				     "program       \r   \n  rules p(a). ";
	
		SimpleCharStream cs=new SimpleCharStream(new StringReader(cmd));
		SparcTranslatorTokenManager stm=new SparcTranslatorTokenManager(cs);
		Token t=stm.getNextToken();
		assertTrue("Sort definition keyword was not recognized",t.kind==SparcTranslatorConstants.SORTDEFKEYWORD);
		t=stm.getNextToken();
		assertTrue("Identifier(sort name) was not recognized",t.kind==SparcTranslatorConstants.IDENTIFIER);
		t=stm.getNextToken();
		assertTrue("Equal sign was not recognized",t.kind==SparcTranslatorConstants.EQ);
		t=stm.getNextToken();
		assertTrue("Dollar was not recognized",t.kind==SparcTranslatorConstants.DOLLAR);
		t=stm.getNextToken();
		assertTrue("Identifier(object name) was not recognized",t.kind==SparcTranslatorConstants.IDENTIFIER);
		t=stm.getNextToken();
		assertTrue("Disjunction was not recognized",t.kind==SparcTranslatorConstants.OR);
		t=stm.getNextToken();
		assertTrue("Identifier(object name) was not recognized",t.kind==SparcTranslatorConstants.IDENTIFIER);
		t=stm.getNextToken();
		assertTrue("Dot was not recognized",t.kind==SparcTranslatorConstants.DOT);
		t=stm.getNextToken();
		assertTrue("predicate declarations keyword was not recognized",t.kind==SparcTranslatorConstants.PREDDEFKEYWORD);
		t=stm.getNextToken();
		
		assertTrue("Identifier(predicate name)  was not recognized",t.kind==SparcTranslatorConstants.IDENTIFIER);
		t=stm.getNextToken();
		assertTrue("Left bracket was not recognized",t.kind==SparcTranslatorConstants.OP);
	
		t=stm.getNextToken();
		assertTrue("Identifier(sort name) was not recognized",t.kind==SparcTranslatorConstants.IDENTIFIER);
		t=stm.getNextToken();
		assertTrue("Right bracket was not recognized",t.kind==SparcTranslatorConstants.CP);
		t=stm.getNextToken();
		assertTrue("Dot was not recognized",t.kind==SparcTranslatorConstants.DOT);
		t=stm.getNextToken();
		assertTrue("program rules keyword was not recognized",t.kind==SparcTranslatorConstants.PROGRULDEFKEYWORD);
		t=stm.getNextToken();
		assertTrue("Identifier(predicate name) was not recognized",t.kind==SparcTranslatorConstants.IDENTIFIER_WITH_OP);
		t=stm.getNextToken();
		assertTrue("Identifier(object name) with open bracket was not recognized",t.kind==SparcTranslatorConstants.IDENTIFIER);
		t=stm.getNextToken();
		assertTrue("Right bracket was not recognized",t.kind==SparcTranslatorConstants.CP);
		t=stm.getNextToken();
		assertTrue("Dot was not recognized",t.kind==SparcTranslatorConstants.DOT);
		
	}

}
