package tests;

import org.junit.Test;

import configuration.ASPSolver;
import configuration.Settings;
import parser.ParseException;
import parser.SparcTranslator;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;



public class TestParser {
 @Test 
 public void testAggregatesAndChoices() throws ParseException
 {
	    ASPSolver current_solver = Settings.getSolver();
	    
	    Settings.setSolver(ASPSolver.Clingo);
		testFile("../test/programs/choices_and_aggregates.sp");
		Settings.setSolver(current_solver);
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
 
 @Test public void testSudoku() throws ParseException
 {
	
	testFile("../test/programs/sudoku.sp");
	  
 }
 
 @Test public void testUsaSmart() throws ParseException
 {
	
	testFile("../test/programs/usaSP1.sp");
	  
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
	  p.program();
 }

 
}
