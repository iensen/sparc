package externaltools;


import java.io.FileNotFoundException;
import java.io.IOException;

import configuration.Settings;

public class ClingoSolver extends ExternalSolver {

	private String pathToClingo;
	public ClingoSolver() throws FileNotFoundException {
		this(null);
	}
	public ClingoSolver(String program) throws FileNotFoundException {
		this.program = program;
		pathToClingo = searchForExe();
		if (pathToClingo == null) {
			throw new FileNotFoundException("Clingo not found. "
					+ "You should have clingo or clingo.exe available "
					+ "from your path or current folder.");
		}
	}
	public boolean isSatisfiable() {
		String output=run(true);
		return !output.contains("UNSATISFIABLE");
	}

	@Override
	public String run(boolean ignoreWarnings) {
		String options = getOptions();
		OsUtils.runCommand(pathToClingo, options, program);
		String[] errorLines = OsUtils.stderr.toString().split("\\n");
		StringBuilder errors = new StringBuilder();
		StringBuilder warnings = new StringBuilder();
		for (String line: errorLines) {
			line = line.trim();
			if(line.indexOf("% warning:")!=-1 || line.indexOf(": warning:")!=-1) {
				warnings.append(line);
			}
			
			if(line.indexOf("% error:")!=-1 || line.indexOf(": error:")!=-1) {
				errors.append(line);
			}
		}
		
		if (warnings.length()>0 && !ignoreWarnings) {
               throw new IllegalArgumentException(
                       "constructed clingo program contains warnings: "
                               + warnings.toString());
        }
		

		if (errors.length()>0) {
               throw new IllegalArgumentException(
                       "ERROR: constructed clingo program contains errors: "
                               + errors.toString());
        }
		   
		return OsUtils.result.toString();
	}
	
	public String getOptions() {
		// !!! this will not work once we introduce quoted strings
		// the last options prints all models, but drops all intermediate
		// models computed during optimization for weak constraints	
		return " " + Settings.getRequiredNumberOfComputedAnswerSets()  + " "
		           + (program.indexOf('~')!=-1? "--opt-mode=optN --quiet=1 ":""); 
		
	}
		private static String searchForExe() {
			String[] candidates = { "clingo", "./clingo", "clingo.exe",
			"clingcon.exe" };
			for (String candidate : candidates) {
				boolean found = true;
				try {
					Process p = Runtime.getRuntime().exec(candidate);
					p.destroy();
				} catch (IOException ex) {
					found = false;
				}
				if (found)
					return candidate;
			}
			return null;
		}

	}
