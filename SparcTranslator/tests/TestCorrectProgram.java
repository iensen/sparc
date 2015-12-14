package tests;



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
import solving.Runner;
import translating.InstanceGenerator;
import translating.Translator;
import typechecking.TypeChecker;
import static org.junit.Assert.*;

public class TestCorrectProgram {

	public ASPSolver solver = ASPSolver.DLV;
	@Test
	public void test1sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("-p(a)", "q(a)","#s1(a)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/1.sp", anss);
	}
	
	@Test
	public void testHugesp() throws FileNotFoundException, ParseException {
		
		String options = (solver == ASPSolver.Clingo?" 1 ": " -n=1 "); 
		testFile("../test/programs/huge.sp", new AnswerCheckerH(), options);
	}
	
	
	@Test
	public void test2sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("-p(a)", "p(b)","#s1(a)", "#s1(b)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/2.sp", anss);
	}
	
	
	
	@Test
	public void test3sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("s(1)","s(2)","s(3)","s(4)","s(5)",
				"#s(1)","#s(2)","#s(3)","#s(4)","#s(5)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/3.sp", anss);
	}
	
	
	@Test
	public void test3_2sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("s(1)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/3_2.sp", anss);
	}
	
	@Test
	public void test4sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("-p(a)","p(c)","-r(a)", "p(b)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/4.sp", anss);
	}

	
	@Test
	public void test5sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>();
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/5.sp", anss);
	}
	
	
	
	@Test
	public void testZhang1sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("#sp(startPoint(1,1,1))","#sp(startPoint(1,1,2))","#sp(startPoint(1,1,3))",
						      "#sp(startPoint(1,2,1))","#sp(startPoint(1,2,2))","#sp(startPoint(1,2,3))",
						      "#sp(startPoint(1,3,1))","#sp(startPoint(1,3,2))","#sp(startPoint(1,3,3))",
						      "#sp(startPoint(2,1,1))","#sp(startPoint(2,1,2))","#sp(startPoint(2,1,3))",
						      "#sp(startPoint(2,2,1))","#sp(startPoint(2,2,2))","#sp(startPoint(2,2,3))",
						      "#sp(startPoint(2,3,1))","#sp(startPoint(2,3,2))","#sp(startPoint(2,3,3))",
						      "#sp(startPoint(3,1,1))","#sp(startPoint(3,1,2))","#sp(startPoint(3,1,3))",
						      "#sp(startPoint(3,2,1))","#sp(startPoint(3,2,2))","#sp(startPoint(3,2,3))",
						      "#sp(startPoint(3,3,1))","#sp(startPoint(3,3,2))","#sp(startPoint(3,3,3))",
						      "object(id(1),ccline(startPoint(1,2,2),endPoint(1,2,3)))")
						);
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/zhang1.sp", anss);
	}
	
	
	@Test
	public void testZhang2sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("father(bob,sara)")
						);
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/zhang2.sp", anss);
	}

	
	


	/*
	
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
	*/



	 private void testFile(String filePath, IAnswerChecker checker, String options) throws ParseException, FileNotFoundException
	 {
		 testFile(filePath, options,  null, checker);
	 }
	 
	 
	 
	 private void testFile(String filePath, HashSet<HashSet<String>> cAnswers) throws ParseException, FileNotFoundException
	 {
		 String options = (solver == ASPSolver.Clingo?" 0 ": ""); 
		 testFile(filePath,options,  cAnswers,null);
	 }
	 
	 
	 

	 private void testFile(String filePath, String options, HashSet<HashSet<String>> cAnswers, IAnswerChecker checker) throws ParseException, FileNotFoundException
	 {
		  Reader sr = null;
		  try {  
		        sr = new FileReader(filePath);
		  } catch (FileNotFoundException e) {
		        e.printStackTrace();
		 
		  }
		  Settings.setSolver(ASPSolver.DLV);
		  SparcTranslator p= new SparcTranslator(sr);
		  SimpleNode e=p.program();
	      InstanceGenerator gen = new InstanceGenerator(p.sortNameToExpression);
	      TypeChecker tc = new TypeChecker(p.sortNameToExpression, p.predicateArgumentSorts, p.constantsMapping, p.curlyBracketTerms, p.definedRecordNames, gen);
	      Translator tr = new Translator(null, p, gen, false, false);
	      tc.checkRules((ASTprogramRules) e.jjtGetChild(2));
	      StringBuilder translatedProgram=new StringBuilder();
	      translatedProgram.append(tr.translateProgram((ASTprogram) e, p.generatingSorts, p.sortRenaming, true));
	      System.out.println(translatedProgram);
	     // ExternalSolver solver = new DLVSolver(translatedProgram.toString());
	    
	      ExternalSolver solver= new DLVSolver(translatedProgram.toString());
	      Settings.getSingletonInstance().setOptions(options);
	      HashSet<HashSet<String>> oAnswers = new Runner().computeAnswerSets(e, solver);
	      if(checker == null) {
	    	  assertEquals("the answers do not match", cAnswers,oAnswers);
	      } else {
	    	  boolean check = checker.check(oAnswers);
	    	  assertEquals("checker fails", true,check);
	      }
	      

	      
	 }
}
