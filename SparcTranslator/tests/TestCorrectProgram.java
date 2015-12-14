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
import sorts.BuiltIn;
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
	
	
	
	@Test
	public void test6sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(1)", "p(2)", "p(3)", "q(2)", "q(3)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/6.sp", anss);
	}
	
	@Test
	public void test7sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(a)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/7.sp", anss);
	}
	
	@Test
	public void test8sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(a)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/8.sp", anss);
	}
	
	@Test
	public void test9sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(a)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/9.sp", anss);
	}
	
	@Test
	public void test10sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("q(6)","p(6)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/10.sp", anss);
	}
	
	@Test
	public void test11sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("node(1)","a(1)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/11.sp", anss);
	}
	
	
	@Test
	public void test12sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(a)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/12.sp", anss);
	}
	
	@Test
	public void test13sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(a)","p(b)","p(1)","p(f(a))"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/13.sp", anss);
	}
	
	
	@Test
	public void test14sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(1)","#s(1)","#s(2)","#s(3)","#s(4)","#s(5)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/14.sp", anss);
	}
	
	@Test
	public void test15sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(f(a,b))", "p(f(c,d))"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/15.sp", anss);
	}
	
	
	@Test
	public void testAggr() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(a)","q(a)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/aggr.sp", anss);
	}
	
	@Test
	public void testArithmetics1() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(32)","q(1)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/arithmetics1.sp", anss);
	}
	
	@Test
	public void testArithmetics2() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(1)","q(1)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/arithmetics2.sp", anss);
	}
	
	
	@Test
	public void testBlock() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("#fluent(on(b1,b6))","#fluent(on(b3,b1))", "#fluent(on(b6,b7))", "#fluent(on(b1,b4))",
						"#fluent(on(b2,t))", "#fluent(on(b3,b2))", "#block(b6)", "#fluent(on(b5,b0))",
						"#fluent(on(b2,b3))", "#block(b2)", "#fluent(on(b2,b5))", "#fluent(on(b4,b5))",
						"#fluent(on(b0,b5))", "#fluent(on(b6,b5))", "#fluent(on(b4,b6))", "#fluent(on(b3,b0))", 
						"#fluent(on(b2,b4))", "#fluent(on(b1,t))", "#fluent(on(b1,b5))", "#fluent(on(b7,t))", 
						"#fluent(on(b0,b7))", "#fluent(on(b4,b1))", "#block(b5)", "#fluent(on(b5,b1))", 
						"#fluent(on(b0,b6))", "#fluent(on(b5,b2))", "#fluent(on(b0,b3))", "#fluent(on(b5,t))", 
						"#block(b1)", "#fluent(on(b0,b4))", "#fluent(on(b4,b3))", "#fluent(on(b4,b2))", 
						"#fluent(on(b0,b2))", "#fluent(on(b6,t))", "#fluent(on(b4,t))", "#fluent(on(b1,b7))", 
						"#fluent(on(b1,b3))", "#fluent(on(b7,b2))", "#fluent(on(b6,b1))", "#fluent(on(b3,b4))", 
						"#fluent(on(b2,b0))", "#fluent(on(b3,b5))", "#block(b4)", "#fluent(on(b6,b2))", 
						"#fluent(on(b7,b0))", "#fluent(on(b3,b7))", "#fluent(on(b1,b0))", "#fluent(on(b0,b1))", 
						"#fluent(on(b5,b4))", "#fluent(on(b1,b2))", "#fluent(on(b6,b3))", "#fluent(on(b7,b1))", 
						"#fluent(on(b4,b0))", "#fluent(on(b3,b6))", "#fluent(on(b5,b6))", "#fluent(on(b5,b3))", 
						"occurs(put(b7,b2),1)", "#fluent(on(b7,b6))", "#fluent(on(b0,t))", "#fluent(on(b6,b4))", 
						"#fluent(on(b4,b7))", "#fluent(on(b3,t))", "occurs(put(b2,t),0)", "#fluent(on(b2,b7))",
						"#fluent(on(b5,b7))", "#fluent(on(b2,b6))", "#block(b3)", "#fluent(on(b7,b5))", 
						"#block(b7)", "#block(b0)", "#fluent(on(b7,b4))", "#fluent(on(b7,b3))", 
						"#fluent(on(b6,b0))", "#fluent(on(b2,b1))"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/blocks.sp", anss);
	}
	
	@Test
	public void testBug() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("holds(loc(rob0,office),0)", "#action(find(rob0,obj0))", "#action(move(rob0,aux_library))", 
					           "#action(grasp(rob0,obj0))", "#action(move(rob0,main_library))", "#action(move(rob0,office))", 
                              "#action(move(rob0,kitchen))", "#action(putdown(rob0,obj0))", "holds(loc(rob0,office),1)", 
                              "holds(loc(rob0,office),2)", "holds(loc(rob0,office),3)", "holds(loc(rob0,office),4)", 
                              "holds(loc(rob0,office),5)", "holds(loc(rob0,office),6)", "holds(loc(obj0,office),1)", 
                              "holds(loc(obj0,office),2)", "holds(loc(obj0,office),3)", "holds(loc(obj0,office),4)", 
                              "holds(loc(obj0,office),5)", "holds(loc(obj0,office),6)", "holds(same_loc(rob0,obj0),1)", 
                              "holds(same_loc(rob0,obj0),2)", "holds(same_loc(rob0,obj0),3)", "holds(same_loc(rob0,obj0),4)", 
                              "holds(same_loc(rob0,obj0),5)", "holds(same_loc(rob0,obj0),6)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/bug.sp", anss);
	}
	
	
	@Test
	public void testBug2() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("#s(a)","#s(b)","#s(c)",
						      "#all(a)","#all(b)","#all(c)", "#all(d)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/bug2.sp", anss);
	}
	
	@Test
	public void testBug3() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("#s(a)","#s(b)","s(a)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/bug3.sp", anss);
	}
	
	
	@Test
	public void testCar() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("#car(c)","-broken(c)","starts(c)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/car.sp", anss);
	}
	

	@Test
	public void testCarSp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("#car(c)","broken(c)","-starts(c)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/car_sp.sp", anss);
	}
	
	@Test
	public void testConstant() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(1)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/constant.sp", anss);
	}
	
	
	@Test
	public void testConstants() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("q(2)","p(1)","t(1)", "t(5)","t(f(5))"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/constants.sp", anss);
	}
	
	@Test
	public void testCurlyBrackets2() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(f(2))","p(3)",
						"p(f(5))","p(f(f(a)))","p(f(f(c)))","p(f(g(x)))","p(f(g(x),c(a)))",
						"p(f(g(b),c(d)))", "p(f(g(x),c(x)))","p(a)","p(b)","p(c)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/curlyBrackets2.sp", anss);
	}
	
	
	@Test
	public void testDisplay1() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("#s(a)","#s(b)","#s(c)","#s(f(a))","#s(f(b))","-q","-p(f(b))","p(a)","p(f(a))"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/display1.sp", anss);
	}
	
	
	@Test
	public void testDisplay2() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("#s(a)","#s(b)","#s(c)","#s(f(a))","#s(f(b))"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/display2.sp", anss);
	}
	
	
	@Test
	public void testEmpty() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>();
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/empty.sp", anss);
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
		  BuiltIn.setMaxInt(5000);
		  
		  
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
