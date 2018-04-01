package tests;
import java.io.StringReader;
import org.junit.Test;

import parser.SimpleCharStream;
import parser.SparcTranslatorTokenManager;




public class TestTokenizer {

	@Test
	public void tokenizeSimpleProgram() {
		String cmd="#maxint=3.\n sorts \n#block=[b][0..7]. #fluent=on(#block,#block).";
	
		SimpleCharStream cs=new SimpleCharStream(new StringReader(cmd));
		SparcTranslatorTokenManager stm=new SparcTranslatorTokenManager(cs);
		for(int i=0;i<100;i++) {
			System.out.println("LOL");
			System.out.println(stm.getNextToken());
		}
		
	}

}
