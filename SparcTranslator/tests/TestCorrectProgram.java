package tests;



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.junit.Test;

import configuration.ASPSolver;
import configuration.Settings;


import externaltools.ClingoSolver;
import externaltools.DLVSolver;
import externaltools.ExternalSolver;

import parser.ASTprogram;
import parser.ASTprogramRules;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslator;
import translating.InstanceGenerator;
import translating.Translator;
import typechecking.TypeChecker;


public class TestCorrectProgram {

	@Test
	public void test1sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/1.sp");
	}
	
	@Test
	public void test2sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/2.sp");
	}

	
	@Test
	public void test3sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/3.sp");
	}
	
	@Test
	public void test3_2sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/3_2.sp");
	}
	
	@Test
	public void test4sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/4.sp");
	}

	
	@Test
	public void test5sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/5.sp");
	}

	
	@Test
	public void test6sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/6.sp");
	}

	
	@Test
	public void test7sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/7.sp");
	}

	
	@Test
	public void test8sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/8.sp");
	}

	
	@Test
	public void test9sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/9.sp");
	}

	
	@Test
	public void test10sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/10.sp");
	}

	
	@Test
	public void test11sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/11.sp");
	}
	
	@Test
	public void test12sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/12.sp");
	}
	
	@Test
	public void test13sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/13.sp");
	}
	
	@Test
	public void test14sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/14.sp");
	}
	
	@Test
	public void test15sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/15.sp");
	}
	
	
	@Test
	public void testArithmetics1sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/arithmetics1.sp");
	}
	
	@Test
	public void testArithmetics2sp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/arithmetics2.sp");
	}
	

	@Test
	public void testBlockssp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/blocks.sp");
	}
	
	@Test
	public void testConstantssp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/constants.sp");
	}
	
	@Test
	public void testHam() throws FileNotFoundException, ParseException {
		testFile("../test/programs/ham.sp");
	}
	
	@Test
	public void testMaxint() throws FileNotFoundException, ParseException {
		testFile("../test/programs/maxint.sp");
	}
	
	@Test
	public void testMys() throws FileNotFoundException, ParseException {
		testFile("../test/programs/mys.sp");
	}
	
	@Test
	public void testQSystemProgram() throws FileNotFoundException, ParseException {
		testFile("../test/programs/qsystem_program.sp");
	}
	
	
	@Test
	public void testRange() throws FileNotFoundException, ParseException {
		testFile("../test/programs/range.sp");
	}
	
	
	@Test
	public void testSudoku() throws FileNotFoundException, ParseException {
		testFile("../test/programs/sudoku.sp");
	}
	
	@Test
	public void testTeacher() throws FileNotFoundException, ParseException {
		testFile("../test/programs/teacher.sp");
	}
	
	@Test
	public void testUsaSP1() throws FileNotFoundException, ParseException {
		testFile("../test/programs/usaSP1.sp");
	}
	
	@Test
	public void testWeakConstraints() throws FileNotFoundException, ParseException {
		testFile("../test/programs/weakc.sp");
	}
	
	@Test
	public void testzeroArity() throws FileNotFoundException, ParseException {
		testFile("../test/programs/zeroArity.sp");
	}
	
	@Test
	public void testG() throws FileNotFoundException, ParseException {
		testFile("../test/programs/g.sp");
	}
	
	@Test
	public void testUnrestr() throws FileNotFoundException, ParseException {
		testFile("../test/programs/unrestr.sp");
	}
	
	@Test
	public void testEquation() throws FileNotFoundException, ParseException {
		testFile("../test/programs/equation.sp", "-pfilter=p");
		
	}
	


	 private void testFile(String filePath) throws ParseException, FileNotFoundException
	 {
		 testFile(filePath,"");
	 }
	 
	 
	 

	 private void testFile(String filePath, String options) throws ParseException, FileNotFoundException
	 {
		  Reader sr = null;
		  try {  
		        sr = new FileReader(filePath);
		  } catch (FileNotFoundException e) {
		        e.printStackTrace();
		 
		  }
		  Settings.setSolver(ASPSolver.Clingo);
		  SparcTranslator p= new SparcTranslator(sr);
		  SimpleNode e=p.program();
	      InstanceGenerator gen = new InstanceGenerator(p.sortNameToExpression);
	      TypeChecker tc = new TypeChecker(p.sortNameToExpression, p.predicateArgumentSorts, p.constantsMapping, p.curlyBracketTerms, p.definedRecordNames, gen);
	      Translator tr = new Translator(null, p, gen, true, false);
	      tc.checkRules((ASTprogramRules) e.jjtGetChild(2));
	      StringBuilder translatedProgram=new StringBuilder();
	      translatedProgram.append(tr.translateProgram((ASTprogram) e, p.generatingSorts, true));
	      System.out.println(translatedProgram);
	     // ExternalSolver solver = new DLVSolver(translatedProgram.toString());
	    
	      ExternalSolver solver= new ClingoSolver(translatedProgram.toString());
	      Settings.getSingletonInstance().setOptions(" 0  "+options);
	      System.out.println(solver.run(true));
	 }
}
