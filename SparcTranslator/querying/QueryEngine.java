package querying;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Pattern;

import parser.ASTatom;

import parser.TokenMgrError;
import configuration.ASPSolver;
import configuration.Settings;
import externaltools.ClingoSolver;
import externaltools.DLVSolver;
import externaltools.ExternalSolver;
import querying.parsing.AnswerSets.AnswerSet;
import querying.parsing.AnswerSets.AnswerSetParser;
import querying.parsing.AnswerSets.ClingoAnswerSetParser;
import querying.parsing.AnswerSets.DLVAnswerSetParser;
import querying.parsing.query.ParseException;
import querying.parsing.query.QASTatom;
import querying.parsing.query.QASTliteral;
import querying.parsing.query.QueryParser;
import typechecking.TypeChecker;
import utilities.Pair;
import warnings.StringListUtils;

public class QueryEngine {
	Scanner sc;
	QueryParser parser;
	ArrayList<AnswerSet> answerSets;
	private ExternalSolver solver;
	private AnswerSetParser answerSetParser;
	private HashSet<String> queryVars;
	TypeChecker tc;

	public QueryEngine(ArrayList<AnswerSet> answerSets, TypeChecker tc) {
		sc = new Scanner(System.in);
		this.tc = tc;
		this.answerSets = answerSets;

		try {
			if (Settings.getSolver() == ASPSolver.DLV) {
				solver = new DLVSolver();
				answerSetParser = new DLVAnswerSetParser();
			} else if (Settings.getSolver() == ASPSolver.Clingo) {
				solver = new ClingoSolver();
				answerSetParser = new ClingoAnswerSetParser();
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

	}

	public void run() {
		QASTliteral query;
		if(answerSets.size() == 0 ) {
			System.err.println("ERROR: Your program is inconsistent");
			return;
		}
		while (true) {
			try {
				query = readQuery();
				if (query == null || query.toString().equals("exit")
						|| query.toString().equals("exit.")) {
					break;
				}
				answerQuery(query);
			} catch (ParseException ex) {
				System.err
				.println("your query must have syntax [-]p(t1,t2...,tn) (where the list of terms may be omitted)");
		
			} catch (TokenMgrError ex) {
				System.err
				.println("your query must have syntax [-]p(t1,t2...,tn) (where the list of terms may be omitted)");
			}

		}
	}
	
	public void answerGroundQuery(String query) {
		QASTliteral queryTree = null;
		try {
			queryTree = parseQuery(query);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		}
		if(!queryTree.isGround()) {
			System.err
			.println("non-ground queries are not supported in command line mode");		
		}
		queryVars = new HashSet<String>();
		answerGroundQuery(queryTree);
	}
	
	
	private QASTliteral parseQuery(String query) throws ParseException,TokenMgrError {
		StringReader sr = new StringReader(query);
		parser = new QueryParser(sr);
		return parser.parseQuery();
		
	}

	private QASTliteral readQuery() throws ParseException,TokenMgrError {
		System.out.print("?- ");
		String query = sc.nextLine();
		return parseQuery(query);
	}

	private void answerQuery(QASTliteral query) {
		queryVars = query.fetchVariables();

		// check the query:
		try {
			query.evaluateAllArithmetics();
			tc.ignoreLineNumbers = true;
			tc.checkAtom(new ASTatom((QASTatom)query.jjtGetChild(0)));
		} catch (parser.ParseException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			return;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			return;
		}

		if (query.isGround()) {

			answerGroundQuery(query);
		} else {
			answerNonGroundQuery(query);
		}
	}

	private void answerNonGroundQuery(QASTliteral query) {

		AnswerSet theOnlyAnswerSet = getAnswerSetOfCorrespondingASPProgram((QASTatom)query.jjtGetChild(0));
		boolean answerFound = false;
		for (String atom : theOnlyAnswerSet.literals) {
			if (atom.startsWith("true_in_all_models") && !query.negated || 
					atom.startsWith("false_in_all_models") && query.negated) {
				answerFound = true;
				Pair<String, ArrayList<String>> recordContent = StringListUtils
						.splitTerm(atom);
				System.out.print(buildAnswer(recordContent.second));
				if(!Settings.isWebMode()) {
					String response = sc.nextLine();
					while (!response.equals("")
							&& !response.toLowerCase().equals("q")) {
						System.err
						.print("Press Enter to continue or input \'q\' to interrupt the query");
						response = sc.nextLine();
					}
					if (response.toLowerCase().equals("q")) {
						break;
					}
				} else{
					System.out.println();
				}
			}
		}
		if (!answerFound) {
			System.out.println("unknown");
		}

	}

	private String buildAnswer(ArrayList<String> terms) {
		StringBuilder answer = new StringBuilder();
		int index = 0;
		for (String var : queryVars) {
			if (index != 0)
				answer.append(" ");
			answer.append(var + " = " + terms.get(index));
			++index;
		}
		return answer.toString();
	}

	private void answerGroundQuery(QASTliteral query) {

		AnswerSet theOnlyAnswerSet = getAnswerSetOfCorrespondingASPProgram((QASTatom)(query.jjtGetChild(0)));
		// if there is an atom "true_in_all_models, it is yes"
		// if there is an atom "false_in_all_models, it is no"
		for (String atom : theOnlyAnswerSet.literals) {
			if (atom.startsWith("true_in_all_models") && !query.negated || 
					atom.startsWith("false_in_all_models") && query.negated) {
				System.out.println("yes");
				return;
			}
			if (atom.startsWith("false_in_all_models") && !query.negated || 
					atom.startsWith("true_in_all_models") && query.negated)
			{
				System.out.println("no");
				return;
			}
		}
		System.out.println("unknown");

	}

	private AnswerSet getAnswerSetOfCorrespondingASPProgram(QASTatom query) {
		String program = constructASPProgram(query);
		solver.setProgram(program);
		String solverOutPut = solver.run(true);
		ArrayList<AnswerSet> answerSets = answerSetParser
				.getAnswerSets(solverOutPut);
		// should be exactly oonstructASPProgramPrefix(query);ne answer set:
		if(answerSets.size()<1) {
			return null;
		} else {
			AnswerSet theOnlyAnswerSet = answerSets.get(0);
			return theOnlyAnswerSet;
		}
	}

	private String constructASPProgram(QASTatom query) {
		int answerSetIndex = 1;

		StringBuilder prefix = new StringBuilder();
		for (AnswerSet aSet : answerSets) {
		
			for (String atom : aSet.literals) {
			//	System.out.println(atom.toString());
				boolean negative = false;
				if (atom.startsWith("-")) {
					negative = true;
					atom = atom.substring(1);
				}
				if (negative) {
					prefix.append("neg(");
				} else {
					prefix.append("pos(");
				}
				prefix.append(atom + "," + answerSetIndex + ").");
				prefix.append(System.getProperty("line.separator"));
			}
			++answerSetIndex;
		}

		StringBuilder bodyForTrueInAllModels = new StringBuilder();
		StringBuilder bodyForFalseInAllModels = new StringBuilder();
		Pair<QASTatom, ArrayList<QASTatom>> movedOutArithmetics = query
				.moveOutArithmetics();
		for (int i = 1; i <= answerSets.size(); i++) {
			bodyForFalseInAllModels.append(((i > 1) ? "," : "") + "neg("
					+ movedOutArithmetics.first.toString() + "," + i + ")");
			bodyForTrueInAllModels.append(((i > 1) ? "," : "") + "pos("
					+ movedOutArithmetics.first.toString() + "," + i + ")");
		}
		if (answerSets.size() > 0) {
			for (int i = 0; i < movedOutArithmetics.second.size(); i++) {
				bodyForFalseInAllModels.append(","
						+ movedOutArithmetics.second.get(i).toString());
				bodyForTrueInAllModels.append(","
						+ movedOutArithmetics.second.get(i).toString());
			}
		}

		prefix.append("true_in_all_models"
				+ ((queryVars.size() > 0) ? "("
						+ StringListUtils.getSeparatedList(queryVars, ",")
						+ ")" : "") + ":-" + bodyForTrueInAllModels + ".");
		prefix.append("false_in_all_models"
				+ ((queryVars.size() > 0) ? "("
						+ StringListUtils.getSeparatedList(queryVars, ",")
						+ ")" : "") + ":-" + bodyForFalseInAllModels + ".");

		return prefix.toString();

	}

	boolean isNumber(String s) {
		Pattern isInteger = Pattern.compile("[1-9]\\d*");
		return isInteger.matcher(s).matches();

	}

}
