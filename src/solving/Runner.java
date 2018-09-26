package solving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import configuration.ASPSolver;
import configuration.Settings;
import externaltools.ExternalSolver;
import parser.ASTdisplay;
import parser.ASTnonRelAtom;
import parser.ASTpredSymbol;
import parser.ASTprogram;
import parser.SimpleNode;
import querying.parsing.AnswerSets.AnswerSet;
import querying.parsing.AnswerSets.AnswerSetParser;
import querying.parsing.AnswerSets.ClingoAnswerSetParser;
import querying.parsing.AnswerSets.DLVAnswerSetParser;

public class Runner {
	
	/**
	 * Output answer sets of the SPARC program stored in sparcProgramTree by running the solver instance 
	 * @param sparcProgramTree - AST representing the program
	 * @param solverInstance - an instance of a solver containing a sparc program
	 */
	public void outputAnswerSets(SimpleNode sparcProgramTree, ExternalSolver solverInstance) {

	   String[] displayPredicates = getDisplayPredicates(solverInstance);
	   HashMap<String,String> mapDisplay =  mapDisplayToOrigin(displayPredicates, (ASTprogram)sparcProgramTree);
	   ArrayList<AnswerSet> answerSets = getAnswerSets(solverInstance);
	   outputFilteredAnswerSets(answerSets, mapDisplay);
	
	}
	
	/**
	 * Compute answer sets of the SPARC program stored in sparcProgramTree by running the solver instance 
	 * and store them in a hashset
	 * @param sparcProgramTree  - AST representing the program
	 * @param solverInstance - an instance of a solver containing a sparcProgramTree
	 * @return answer sets filtered by the display section in the A
	 */
	public HashSet<HashSet<String>> computeAnswerSets(SimpleNode sparcProgramTree, ExternalSolver solverInstance) {
		   String[] displayPredicates = getDisplayPredicates(solverInstance);
		   HashMap<String,String> mapDisplay =  mapDisplayToOrigin(displayPredicates, (ASTprogram)sparcProgramTree);
		   ArrayList<AnswerSet> answerSets = getAnswerSets(solverInstance);
		   return filterAnswerSets(answerSets,mapDisplay);
	}
	
	
	/**
	 * Take the answer sets output by solver, and convert them into the set of answer sets
	 * @param answerSets - answer sets obtain from the solver
	 * @param mapDisplayToOrigin - a mapping which maps auxiliary predicates used to their original names
	 */
	private HashSet<HashSet<String>> filterAnswerSets(ArrayList<AnswerSet> answerSets, HashMap<String, String> mapDisplay) {
		HashSet<HashSet<String>> assets = new HashSet<HashSet<String>>();
		for(AnswerSet a: answerSets) {
			HashSet<String> answerSet = new HashSet<String>();
			for (String literal: a.literals) {
				String pname = getPredicateName(literal);
				if(mapDisplay.containsKey(pname)) {
					answerSet.add(setNewName(literal, mapDisplay.get(pname)));
				}	
			}
			assets.add(answerSet);
		}
		return assets;
	}

	/**
	 * Fetch the array of predicates that need to be shown in the answer sets
	 * @param solverInstance (from this we can obtain the translated program, which, in turn, contains
	 *                        a comment containing all necessary predicates)
	 * @return String[] - the array of predicates that need to be shown in the answer sets
	 */
	private String[] getDisplayPredicates(ExternalSolver solverInstance) {
		String program = solverInstance.getProgram();
		StringBuilder comment = new StringBuilder();
		int index = program.length()-1;
		while(program.charAt(index) != '%') {
			comment.append(program.charAt(index));
			--index;
		}
		comment.reverse();
		String [] displayArray = comment.toString().trim().split("\\s+");
		
		// note, if the display section is empty, we, the split returns an empty string
		// which is put into display array as its only element
		// we don't want that, we want an empty array instead!!
		if(displayArray[0].matches("\\s*")) {
			displayArray = new String[0];
		}
		return displayArray;
	}
	
	
	/**
	 * Create a map from display predicates to their original names in the program
	 *
	 * @param displayP - array of the display predicates
	 * @param sparcProgramTree - AST of the program which allows us to find the necessary mapping
	 * @return a map from display predicates to their original names
	 */
	private HashMap<String, String> mapDisplayToOrigin(String [] displayP, ASTprogram sparcProgramTree) {
		
		
		HashMap<String,String> mapDisplayToOrigin = new HashMap<String,String>();
		ASTdisplay display = (ASTdisplay) sparcProgramTree.jjtGetChild(3);
		
		for(int i = 0; i< displayP.length; i++) {
			ASTnonRelAtom atom = (ASTnonRelAtom)display.jjtGetChild(i);
			ASTpredSymbol predS = (ASTpredSymbol) atom.jjtGetChild(0);
			mapDisplayToOrigin.put(displayP[i], (predS.hasPoundSign()?"#":"") + predS.toString());			
		}
		return mapDisplayToOrigin;
	}
	
	
	/**
	 * Compute answer sets of a program loaded into a specific solver
	 * @param solverInstance - a solver instance containing a program
	 * @return a list of answer sets of the program
	 */
	private ArrayList<AnswerSet> getAnswerSets(ExternalSolver solverInstance) {
		AnswerSetParser aParser = null;
		if (Settings.getSolver() == ASPSolver.DLV)
	    {
	        aParser = new DLVAnswerSetParser();
	    }
	    else
	    {
	        aParser = new ClingoAnswerSetParser();
	    }
	 
	    return aParser.getAnswerSets(solverInstance.run(true));	
	}
	
	
	/**
	 * Take the answer sets output by solver and convert it to a user-readable (and understable%) format 
	 * using the mapping
	 * @param answerSets - answer sets obtain from the solver
	 * @param mapDisplayToOrigin - a mapping which maps auxiliary predicates used to their original names
	 */
	private void outputFilteredAnswerSets(ArrayList<AnswerSet> answerSets, HashMap<String, String> mapDisplayToOrigin) {

		if(!Settings.isLOutputFormat()) {
			boolean firstAnswerSet = true;
			for(AnswerSet a: answerSets) {
				if(!firstAnswerSet)
					System.out.println(System.getProperty("line.separator"));
				firstAnswerSet = false;
				System.out.print("{");
				boolean firstLit = true;
				for (String literal: a.literals) {
					String pname = getPredicateName(literal);
					if(mapDisplayToOrigin.containsKey(pname)) {
						if(!firstLit) {
							System.out.print(", ");	
						}
						firstLit = false;
						System.out.print(setNewName(literal, mapDisplayToOrigin.get(pname)));
					}
				}
				System.out.print("}");	
				
			}
			System.out.print(System.getProperty("line.separator"));
		} else {
			for(AnswerSet a: answerSets) {
				boolean firstLit = true;
				for (String literal: a.literals) {
					String pname = getPredicateName(literal);
					if(mapDisplayToOrigin.containsKey(pname)) {
						if(!firstLit) {
							System.out.print(" ");	
						}
						firstLit = false;
						System.out.print(setNewName(literal, mapDisplayToOrigin.get(pname)));
					}
				}
				System.out.print(System.getProperty("line.separator"));
			}
			System.out.println(answerSets.isEmpty()?"UNSATISFIABLE":"SATISFIABLE");
		}
	}
	
	/**
	 * Given a string representing a literal (e.g, -p(a)), obtain the name of the predicate
	 * used to form the literal (for -p(a) it is p)
	 * @param lit - a string representing the literal
	 * @return  the name of the predicate used to form the literal
	 * 
	 */
	private String getPredicateName(String lit) {
		if (lit.startsWith("-")) {
			lit = lit.substring(1);
		}
		StringBuilder predName = new StringBuilder();
		int index = 0;
		while(index < lit.length() && (Character.isLetter(lit.charAt(index)) || 
				Character.isDigit(lit.charAt(index)) || lit.charAt(index) == '_')) {
			    predName.append(lit.charAt(index));
			    ++index;
			
		}
		return predName.toString();
	}
	
	
	/**
	 * Given a string representing a literal, replace it's predicate with a new one
	 * @param lit - a string representing a literal
	 * @param newPredicataName - a new predicate name
	 * @return - a string representing a new literal, where predicate name of lit is replaced with newPredicataName
	 */
	private String setNewName(String lit, String newPredicataName) {
		StringBuilder newLit = new StringBuilder();
		int index = 0;
		if(lit.startsWith("-")) {
		   ++index;
		   newLit.append("-");
		}
		
		while(index < lit.length() && (Character.isLetter(lit.charAt(index)) || 
				Character.isDigit(lit.charAt(index)) || lit.charAt(index) == '_')) {
			    ++index;
		}
		
		newLit.append(newPredicataName);
		newLit.append(lit.substring(index));
		return newLit.toString();
	}
}
