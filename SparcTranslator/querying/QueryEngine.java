package querying;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
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
import querying.parsing.query.QueryParser;
import warnings.Pair;
import warnings.StringListUtils;

public class QueryEngine {
	Scanner sc;
	QueryParser parser;
	ArrayList<AnswerSet> answerSets;
	private ExternalSolver solver;
	private AnswerSetParser answerSetParser;
	private HashSet<String> queryVars;

	public QueryEngine(ArrayList<AnswerSet> answerSets) {
		sc = new Scanner(System.in);
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
		QASTatom query;
		while (true) {
			try {
				query = readQuery();
				if (query == null) {
					break;
				}
				answerQuery(query);
			} catch (ParseException ex) {
				System.err.println(ex.getMessage());
			}

		}
	}

	private QASTatom readQuery() throws ParseException {
		System.out.println("? ");
		String query = sc.nextLine();
		StringReader sr = new StringReader(query);
		parser = new QueryParser(sr);
		QASTatom atom = parser.parseQuery();
		return atom;
	}

	private void answerQuery(QASTatom query) {
		queryVars = query.fetchVariables();
		query.evaluateAllArithmetics();

		if (query.isGround()) {
			 answerGroundQuery(query);
		} else {
			answerNonGroundQuery(query);
		}
	}

	private  void answerNonGroundQuery(QASTatom query) {

		AnswerSet theOnlyAnswerSet =getAnswerSetOfCorrespondingASPProgram(query);
		boolean answerFound=false;
		for (String atom:theOnlyAnswerSet.atoms) {
			if (atom.startsWith("true_in_all_models")) {
				answerFound=true;
				Pair<String,ArrayList<String>> recordContent=StringListUtils.splitTerm(atom);
				System.out.println(buildAnswer(recordContent.second));
				String response=sc.next();
				while(!response.equals("") && !response.toLowerCase().equals("q")) {
					System.err.println("Press Enter to continue or input \'q\' to interrupt the query");
					response=sc.next();
				}
				if(response.toLowerCase().equals("q")) {
					break;
				}
			}
		}
		if(!answerFound) {
			System.out.println("no");
		}

	}

	private String buildAnswer(ArrayList<String> terms) {
		StringBuilder answer=new StringBuilder();
		int index=0;
		for(String var:queryVars) {
            if(index!=0)
            	answer.append(" ");
			answer.append(var+" = "+terms.get(index));
            ++index;
		}
		return answer.toString();
	}

	private void answerGroundQuery(QASTatom query) {
		
		AnswerSet theOnlyAnswerSet =getAnswerSetOfCorrespondingASPProgram(query);
	     // if there is an atom "true_in_all_models, it is yes"
		// if there is an atom "false_in_all_models, it is no"
		for (String atom : theOnlyAnswerSet.atoms) {
			if (atom.startsWith("true_in_all_models")) {
				System.out.println("yes");
				return;
			}
			if (atom.startsWith("false_in_all_models")) {
				System.out.println("no");
				return;
			}
		}
		System.out.println("unknown");

	}

	private AnswerSet getAnswerSetOfCorrespondingASPProgram(QASTatom query) {
		String program = constructASPProgram(query);
		solver.setProgram(program);
		String solverOutPut = solver.run();
		ArrayList<AnswerSet> answerSets = answerSetParser
				.getAnswerSets(solverOutPut);
		// should be exactly oonstructASPProgramPrefix(query);ne answer set:
		AnswerSet theOnlyAnswerSet = answerSets.get(0);
		return theOnlyAnswerSet;

	}
	
	private String constructASPProgram(QASTatom query) {
		int answerSetIndex = 1;
		StringBuilder prefix = new StringBuilder();
		for (AnswerSet aSet : answerSets) {
			for (String atom : aSet.atoms) {
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
}
