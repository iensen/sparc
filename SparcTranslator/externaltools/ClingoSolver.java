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
		String options=" 0 --shift ";
		if(Settings.getSingletonInstance().getOptions()!=null)
			options+=Settings.getSingletonInstance().getOptions();

		OsUtils.runCommand(pathToClingo, options, program);
		String[] errorLines = OsUtils.errors.toString().split("\\n");
		StringBuilder errors = new StringBuilder();
		for (String line: errorLines) {
			line = line.trim();
			if(ignoreWarnings && !line.toLowerCase().startsWith("%") || !ignoreWarnings)
				errors.append(line);

		}
		return OsUtils.result.toString();
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
