package tests;
import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.junit.Test;

import configuration.ASPSolver;
import configuration.Settings;
import parser.ASTprogram;
import parser.ASTprogramRules;

import parser.ParseException;
import parser.SparcTranslator;

import translating.InstanceGenerator;
import typechecking.TypeChecker;
public class TestTypeChecker {
	
	 @Test 
	 public void testUsaSmartPart() throws ParseException
	 {	
			testFile("../test/programs/usaSP1.sp");  
	 }
	 
	 
	 @Test 
	 public void testRegularArithm() throws ParseException
	 {	
			testFile("../test/programs/reg_arterm_check.sp");  
	 }
	 
	 
	 @Test 
	 public void testAggregatesAndChoices() throws ParseException
	 {	
			testFile("../test/programs/choices_and_aggregates.sp");  
	 }
	 
	 @Test 
	 public void testSimpleFunction() throws ParseException
	 {	
			testFile("../test/programs/simpleFunctionTypeCheck.sp");  
	 }
	 
	 @Test 
	 public void testSimpleFunction2() throws ParseException
	 {	
			testFile("../test/programs/simpleFunctionTypeCheck2.sp");  
	 }
	 
	  
	 @Test 
	 public void testMysteryPuzzle() throws ParseException
	 {	
		testFile("../test/programs/mys.sp");  
	 }
	 
	 @Test public void testHamiltonPath() throws ParseException
	 {
		testFile("../test/programs/ham.sp");  
	 }
	 
	 @Test 
	 public void testSudoku() throws ParseException
	 {
		
		testFile("../test/programs/sudoku.sp");
		  
	 }

	 
	 @Test 
	 public void testRange() throws ParseException
	 {	
		testFile("../test/programs/rangetypecheck1.sp");  
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
	
		  InstanceGenerator gen = new InstanceGenerator(p.sortNameToExpression);
		  TypeChecker tc=new TypeChecker(p.sortNameToExpression, 
				  p.predicateArgumentSorts,p.constantsMapping,
				  p.curlyBracketTerms,
				  p.definedRecordNames,gen);
		  Settings.setSolver(ASPSolver.Clingo); // allow choice rules		
		  tc.checkRules((ASTprogramRules)program.jjtGetChild(2));
	 }

	 
}


