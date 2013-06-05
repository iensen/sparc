package tests;
import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import org.junit.Test;
import parser.ASTprogram;
import parser.ASTprogramRules;
import parser.ParseException;
import parser.RChoiceRemover;
import parser.RIdentifierSplitVisitor;
import parser.RRepeatitionRemover;
import parser.SparcTranslator;
import typechecking.TypeChecker;
public class TestTypeChecker {
	
	 @Test 
	 public void testUsaSmartPart() throws ParseException
	 {	
			testFile("test/usa_part.sp");  
	 }
	 
	 
	 @Test 
	 public void testRegularArithm() throws ParseException
	 {	
			testFile("test/reg_arterm_check.sp");  
	 }
	 
	 
	 @Test 
	 public void testAggregatesAndChoices() throws ParseException
	 {	
			testFile("test/choices_and_aggregates(s).sp");  
	 }
	 
	 @Test 
	 public void testSimpleFunction() throws ParseException
	 {	
			testFile("test/simpleFunctionTypeCheck.sp");  
	 }
	 
	 @Test 
	 public void testSimpleFunction2() throws ParseException
	 {	
			testFile("test/simpleFunctionTypeCheck2.sp");  
	 }
	 
	 @Test 
	 public void testUndeclaredSort() 
	 {	
			try {
				testFile("test/undeclaredSortCheck.sp");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				assertTrue("undeclared sorts in sort definition are not allowed",
						e.getClass().toString().equals("class parser.ParseException"));
			}  
	 }
	 
	 @Test 
	 public void testMysteryPuzzle() throws ParseException
	 {	
		testFile("test/mys.sp");  
	 }
	 
	 @Test public void testHamiltonPath() throws ParseException
	 {
		
		testFile("test/ham.sp");
		  
	 }
	 
	 @Test 
	 public void testSudoku() throws ParseException
	 {
		
		testFile("test/sudoku.sp");
		  
	 }
	 
	 @Test 
	 public void testUsaSmart() throws ParseException
	 {
		
		testFile("test/usa.sp");
		  
	 }
	 
	 @Test 
	 public void testRange() throws ParseException
	 {	
		testFile("test/rangetypecheck1.sp");  
	 }
	 
	 private void testFile(String filePath) throws ParseException
	 {
		  Reader sr = null;
		  try {  
		        sr = new FileReader(filePath);
		  } catch (FileNotFoundException e) {
		        e.printStackTrace();
		  }
		  SparcTranslator p= new SparcTranslator(sr);
		  ASTprogram program=(ASTprogram) p.program();
		  RIdentifierSplitVisitor v=new RIdentifierSplitVisitor();
		  program.jjtAccept(v,null);
		  RRepeatitionRemover removeNMrepeatitions=new  RRepeatitionRemover();
		  program.jjtAccept(removeNMrepeatitions,null);
		  RChoiceRemover choiceremover=new RChoiceRemover();
		  program.jjtAccept(choiceremover,null);
		  TypeChecker tc=new TypeChecker(p.sortNameToExpression, p.predicateArgumentSorts,p.constantsMapping,null);
		  tc.checkRules((ASTprogramRules)program.jjtGetChild(2));
	 }

	 
}


