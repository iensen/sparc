package tests;



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;


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

	@Test
	public void test1sp() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("-p(a)", "q(a)","#s1(a)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/1.sp", anss);
	}

	@Test
	public void testHugesp() throws FileNotFoundException, ParseException {
		testFile("../test/programs/huge.sp", new AnswerCheckerH(), 1);
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
	public void testEquation() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(2)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/equation.sp", anss);
	}

	@Test
	public void testG() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(
				Arrays.asList("p(1)","p(2)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/g.sp", anss);
	}

	@Test
	public void testGelfond() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>();
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/gelfond.sp", anss);
	}


	@Test
	public void testHam() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList(
				"in(5,6)", "in(2,3)", "in(1,2)", "in(4,5)", "in(3,4)", "in(6,1)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/ham.sp", anss);
	}

	@Test
	public void testIgnite() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList(
				"connected_to_tank(s1)","connected(s1,v1,s2)","connected(s2,v2,s3)",
				"connected_to_burner(s3)","fluent(inertial,burner_on)","fluent(inertial,opened(v1))",
				"fluent(inertial,opened(v2))","fluent(defined,pressurized(s1))",
				"fluent(defined,pressurized(s2))","fluent(defined,pressurized(s3))",
				"#section(s1)","#section(s2)",
				"#section(s3)","#valve(v1)","#valve(v2)","#ov(opened(v1))",
				"#ov(opened(v2))","#ps(pressurized(s1))","#ps(pressurized(s2))","#ps(pressurized(s3))",
				"#fluent(pressurized(s1))","#fluent(pressurized(s2))","#fluent(pressurized(s3))",
				"#fluent(opened(v1))","#fluent(opened(v2))","#fluent(burner_on)",
				"#ft(inertial)","#ft(defined)","#action(ignite)","#action(open(v1))",
				"#action(open(v2))","#action(close(v1))","#action(close(v2))","#step(1)",
				"#step(2)","#step(3)","#step(4)", "#step(0)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/ignite.sp", anss);
	}

	@Test
	public void testInconsistent() throws FileNotFoundException, ParseException {
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		testFile("../test/programs/inconsistent.sp", anss);
	}

	@Test
	public void testInfinite() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("limit(3)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/infinite_program.sp", anss);
	}



	@Test
	public void testMaxInt() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("p(1)","p(2)","p(3)","p(4)","p(5)","p(6)",
				"p(7)","p(8)","p(9)","p(10)","p(0)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/maxint.sp", anss);
	}

	@Test
	public void testMurderer() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("murderer(ben)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/mys.sp", anss);
	}

	@Test
	public void testNat() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("#nat(1)","#nat(2)","#nat(3)","#nat(4)","#nat(5)",
				"nat(1)","#nat(0)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/nat.sp", anss);
	}

	@Test
	public void testParan() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("p(a)","p(1)"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/paran.sp", anss);
	}

	@Test
	public void testQsystemProgram() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("x","-p","a","-b"));
		HashSet<String> ans2 = new HashSet<String>(Arrays.asList("x","p","a","-b"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		anss.add(ans2);
		testFile("../test/programs/qsystem_program.sp", anss);
	}

	@Test
	public void testRange() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("q(bi)", "q(ba)", "q(ca)", "q(bu)", "q(be)", 
				"q(bt)", "q(by)", "q(al)", "q(ae)", "q(ay)", "q(au)", "q(bp)", "q(ai)", "q(ap)", "q(bj)",
				"q(as)", "q(bb)", "q(bf)", "q(cb)", "q(af)", "q(bs)", "q(ab)", "q(bx)", "q(ax)", "q(ao)", 
				"q(at)", "q(bo)", "q(ak)", "q(aj)", "q(cc)", "q(bk)", "q(ac)", "q(bg)", "q(bc)", "q(bw)", 
				"q(an)", "q(br)", "q(ar)", "q(bn)", "q(ag)", "q(aw)", "q(cd)", "q(bl)", "q(bh)", "q(bd)", 
				"q(az)", "q(bv)", "q(ad)", "q(bq)", "q(am)", "q(bm)", "q(aq)", "q(ah)", "q(av)", "q(bz)"));

		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);

		testFile("../test/programs/range.sp", anss);
	}

	@Test
	public void testRegions() throws FileNotFoundException, ParseException {
				

		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("has(tx,green)", "has(co,blue)",  "has(ok,red)"));
		HashSet<String> ans2 = new HashSet<String>(Arrays.asList("has(tx,green)", "has(co,red)",  "has(ok,blue)"));
		HashSet<String> ans3 = new HashSet<String>(Arrays.asList("has(tx,blue)", "has(co,green)",  "has(ok,red)"));
		HashSet<String> ans4 = new HashSet<String>(Arrays.asList("has(tx,blue)", "has(co,red)",  "has(ok,green)"));
		HashSet<String> ans5 = new HashSet<String>(Arrays.asList("has(tx,red)", "has(co,blue)",  "has(ok,green)"));
		HashSet<String> ans6 = new HashSet<String>(Arrays.asList("has(tx,red)", "has(co,green)",  "has(ok,blue)"));
		

		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		anss.add(ans2);
		anss.add(ans3);
		anss.add(ans4);
		anss.add(ans5);
		anss.add(ans6);
		testFile("../test/programs/regions.sp", anss);
	}



	@Test
	public void testSimple123() throws FileNotFoundException, ParseException {


	

		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("p(2)"));

		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);

		testFile("../test/programs/simple123.sp", anss);
	}

	@Test
	public void testSudoku() throws FileNotFoundException, ParseException {

		HashSet<String> ans1 = new HashSet<String>(Arrays.asList(
				"pos(6,c(2,4))", "pos(6,c(5,5))", "pos(5,c(1,9))", "pos(6,c(8,7))", 
				"pos(8,c(6,4))", "pos(8,c(4,7))", "pos(3,c(5,7))", "pos(8,c(8,6))", 
				"pos(9,c(8,5))", "pos(8,c(9,1))", "pos(4,c(2,9))", "pos(6,c(4,3))", 
				"pos(5,c(4,8))", "pos(7,c(7,6))", "pos(6,c(1,1))", "pos(9,c(7,3))", 
				"pos(4,c(6,7))", "pos(1,c(1,7))", "pos(6,c(9,6))", "pos(7,c(2,1))", 
				"pos(3,c(2,8))", "pos(5,c(3,5))", "pos(3,c(6,2))", "pos(7,c(3,7))", 
				"pos(6,c(3,9))", "pos(3,c(3,3))", "pos(9,c(6,9))", "pos(9,c(3,8))", 
				"pos(4,c(9,5))", "pos(1,c(9,2))", "pos(2,c(9,9))", "pos(2,c(3,1))", 
				"pos(1,c(2,3))", "pos(1,c(4,1))", "pos(7,c(4,9))", "pos(3,c(4,5))", 
				"pos(4,c(8,1))", "pos(3,c(1,6))", "pos(3,c(8,9))", "pos(9,c(2,6))", 
				"pos(1,c(3,6))", "pos(2,c(2,7))", 
				"pos(9,c(1,2))", "pos(9,c(5,1))", "pos(2,c(5,8))", "pos(7,c(6,3))", 
				"pos(8,c(2,5))", "pos(6,c(7,2))", "pos(5,c(2,2))", "pos(1,c(8,8))", 
				"pos(9,c(9,7))", "pos(8,c(1,8))", "pos(4,c(5,2))", "pos(2,c(4,2))", 
				"pos(2,c(6,6))", "pos(1,c(7,4))", "pos(1,c(5,9))", "pos(3,c(9,4))", 
				"pos(5,c(6,1))", "pos(7,c(9,8))", "pos(5,c(9,3))", "pos(5,c(8,4))", 
				"pos(5,c(5,6))", "pos(8,c(7,9))", "pos(7,c(5,4))", "pos(2,c(8,3))", 
				"pos(4,c(7,8))", "pos(1,c(6,5))", "pos(8,c(3,2))", "pos(7,c(1,5))", 
				"pos(9,c(4,4))", "pos(7,c(8,2))", 
				"pos(2,c(7,5))", "pos(6,c(6,8))", "pos(3,c(7,1))", "pos(4,c(3,4))", 
				"pos(8,c(5,3))", "pos(5,c(7,7))", "pos(2,c(1,4))", "pos(4,c(4,6))", 
				"pos(4,c(1,3))"));

		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);

		testFile("../test/programs/sudoku.sp", anss);
	}
	
	@Test
	public void testNumAns0() throws FileNotFoundException, ParseException {

		HashSet<String> ans1 = new HashSet<String>(Arrays.asList("p"));
		HashSet<String> ans2 = new HashSet<String>(Arrays.asList("q"));
		HashSet<String> ans3 = new HashSet<String>(Arrays.asList("r"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		anss.add(ans2);
		anss.add(ans3);
				
		testFile("../test/programs/testNumOfAns.sp", anss);
		testFile("../test/programs/testNumOfAns.sp", new SubsetChecker(anss,1), 1);
		testFile("../test/programs/testNumOfAns.sp", new SubsetChecker(anss,2), 2);
		testFile("../test/programs/testNumOfAns.sp", new SubsetChecker(anss,3), 3);
	}
	
	
	
	
	
	
	@Test
	public void testMohan() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList(
				"val(loc(text0,aux_library),true,0)", "val(loc(text0,aux_library),true,1)", 
				"val(loc(text0,main_library),false,0)", "val(loc(text0,office),false,0)", 
				"val(loc(text0,office),false,1)", "val(loc(text0,main_library),false,1)", 
				"ab(d1(text0))", "ab(d3(text0))"));
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/mohan.sp", anss);
	}
	
	@Test
	public void testIssue28() throws FileNotFoundException, ParseException {
		HashSet<String> ans1 = new HashSet<String>(Arrays.asList(
				"father(f(a,bob),f(a,sara))", "father(f(a,bob),f(b,sara))"));
		
		HashSet<HashSet<String>> anss = new HashSet<HashSet<String>>();
		anss.add(ans1);
		testFile("../test/programs/issue28.sp", anss);
	}
	






	private void testFile(String filePath, IAnswerChecker checker, int numberOfAnswerSets) throws ParseException, FileNotFoundException
	{
		testFile(filePath,  numberOfAnswerSets, null, checker);
	}



	private void testFile(String filePath, HashSet<HashSet<String>> cAnswers) throws ParseException, FileNotFoundException
	{
		
		testFile(filePath, 0, cAnswers,null);
	}




	private void testFile(String filePath, int numberOfAnswerSets, HashSet<HashSet<String>> cAnswers, IAnswerChecker checker) throws ParseException, FileNotFoundException
	{
		for (int solverId = 0; solverId < 2 ; solverId++) {
			
			
			// Exceptions:
			
			// ignore, this is a DLV bug
			if(filePath.equals("../test/programs/mohan.sp") && solverId==1)
				continue;

			// no choice rules in DLV
			if(filePath.equals("../test/programs/regions.sp") && solverId==1) {
				return;
			}
			
			// no negative arithmetics in DLV
			if(filePath.equals("../test/programs/simple123.sp") && solverId==1) {
				return;
			}
			


			BuiltIn.setMaxInt(5000);		  
			
			Reader sr = null;
			try {  
				sr = new FileReader(filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();

			}
			
			

			Settings.setSolver(solverId==0?ASPSolver.Clingo:ASPSolver.DLV);
			SparcTranslator p= new SparcTranslator(sr);
			SimpleNode e=p.program();
			InstanceGenerator gen = new InstanceGenerator(p.sortNameToExpression);
			TypeChecker tc = new TypeChecker(p.sortNameToExpression, p.predicateArgumentSorts, p.constantsMapping, p.curlyBracketTerms, p.definedRecordNames, gen);
			Translator tr = new Translator(null, p, gen, false, false);
			tc.checkRules((ASTprogramRules) e.jjtGetChild(2));
			StringBuilder translatedProgram=new StringBuilder();
			translatedProgram.append(tr.translateProgram((ASTprogram) e, p.generatingSorts, p.sortRenaming, true));
			System.out.println(translatedProgram);
			ExternalSolver solver = null;
			if(Settings.getSolver() == ASPSolver.DLV)
				solver= new DLVSolver(translatedProgram.toString());
			else if(Settings.getSolver() == ASPSolver.Clingo) {
				solver= new ClingoSolver(translatedProgram.toString());
			}
			
			Settings.setRequiredNumberOfComputedAnswerSets(numberOfAnswerSets);
			HashSet<HashSet<String>> oAnswers = new Runner().computeAnswerSets(e, solver);
			if(checker == null) {
				assertEquals("the answers do not match", cAnswers,oAnswers);
			} else {
				boolean check = checker.check(oAnswers);
				assertEquals("checker fails", true,check);
			}
		}



	}
}
