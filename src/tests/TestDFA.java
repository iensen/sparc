package tests;

import static org.junit.Assert.assertTrue;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import parser.ASTregularExpression;
import parser.ParseException;
import parser.RChoiceRemover;
import parser.RIdentifierSplitVisitor;
import parser.RRepeatitionRemover;
import parser.SimpleNode;
import parser.SparcTranslator;
import automata.DFA;

import re.RegularExpression;


public class TestDFA {

	@Test
	public void testDeadState() {
		DFA dfa1 =getDFA("abc|d");
	//	HashSet<State> final=dfa1.getFinalStates();
		assertTrue("dead state is not dead initially",dfa1.isDead(dfa1.getDeadState()));
		automata.State dead=dfa1.addEdgesToDeadState();
		assertTrue("dead state is not dead",dfa1.isDead(dead));
	}
	
	
    private DFA getDFA(String test)
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
			//e.dump("");
		} catch (ParseException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		}
	    return new RegularExpression((ASTregularExpression)e).getDFA();
    }
}
