package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import org.junit.Test;

import configuration.Settings;

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

public class TestWarning {

	@Test
	public void testArithm1() throws FileNotFoundException {
		assertEquals("Warning message was wrong",
				"%WARNINGS %WARNING: Rule p(f(2,X)). at line 14, column 1 is an empty rule",
				getError("../test/warnings/arithm1.sp"));
	}

	@Test
	public void testArithm2() throws FileNotFoundException {
		assertEquals("Warning message was wrong",
				"%WARNINGS %WARNING: Rule p(X+1):-p(X+7). at line 11, column 1 is an empty rule",
				getError("../test/warnings/arithm2.sp"));
	}

	@Test
	public void testDisjArg() throws FileNotFoundException {
		assertEquals("Error message was wrong",
				"%WARNINGS %WARNING: Rule p(X,Y):-q(X). at line 18, column 1 is an empty rule",
				getError("../test/warnings/disjarg.sp"));
	}

	@Test
	public void testDisjoint2() throws FileNotFoundException {
		assertEquals("Error message was wrong",
				"%WARNINGS %WARNING: Rule :-p(X),#count{V:p(V+1),q(V+1)}>0. at line 9, column 2 is an empty rule",
				getError("../test/warnings/disjoint_2.sp"));
	}

	@Test
	public void testWarning() throws FileNotFoundException {
		assertEquals("Error message was wrong",
				"%WARNINGS %WARNING: Rule p(f(X)):-q(X). at line 11, column 1 is an empty rule",
				getError("../test/warnings/warning.sp"));
	}
	
	@Test
	public void testTwoWarning() throws FileNotFoundException {
		assertEquals("Error message was wrong",
				"%WARNINGS %WARNING: Rule p(X+10):-q(X). at line 7, column 1 is an empty rule %WARNING: Rule p(X+20):-p(X). at line 8, column 1 is an empty rule",
				getError("../test/warnings/twoWarnings.sp"));
	}
	

	private String getError(String filePath) throws FileNotFoundException {
		Reader sr = null;
		try {
			sr = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SparcTranslator p = new SparcTranslator(sr);
		try {
			SimpleNode e = p.program();
			InstanceGenerator gen = new InstanceGenerator(
					p.sortNameToExpression);
			TypeChecker tc = new TypeChecker(p.sortNameToExpression,
					p.predicateArgumentSorts, p.constantsMapping,
					p.curlyBracketTerms, p.definedRecordNames, gen);
			Translator tr = new Translator(null, p, gen, true, true);
			tc.checkRules((ASTprogramRules) e.jjtGetChild(2));
			StringBuilder translatedProgram = new StringBuilder();
			translatedProgram.append(tr.translateProgram((ASTprogram) e,
					p.generatingSorts, p.sortRenaming, false));
			
			System.out.println(translatedProgram);
			ExternalSolver solver = new DLVSolver(translatedProgram.toString());
			Settings.setRequiredNumberOfComputedAnswerSets(1);
			System.out.println(solver.run(false));
		} catch (ParseException ex) {
			return ex.getMessage();
		}
		return null;
	}
}
