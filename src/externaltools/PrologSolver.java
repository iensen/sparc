package externaltools;

import java.io.*;
import java.security.SecureRandom;


public class PrologSolver extends ExternalSolver {

	private String pathToProlog;
	public PrologSolver() throws FileNotFoundException {
		this(null);
	}

	public PrologSolver(String program) throws FileNotFoundException {
		this.program = program;
		pathToProlog = searchForExe();
		if (pathToProlog == null) {
			throw new FileNotFoundException("Swi-prolog not found. "
					+ "You should have swipl or swipl.exe available "
					+ "from your path or current folder.");
		}
	}

	public boolean isSatisfiable() {
		String output = run(true);
		return output.equals("yes");
	}
	//TODO: Implement ignore warnings properly
	public String run(boolean ignoreWarnings) {
		// Create a temporary file: FileWriter fw = new
		// FileWriter(file.getAbsoluteFile());
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFile = generateFile("prolog_q",
				Long.toString(System.currentTimeMillis()), tempDir);
		// write the program to the temporary file:
		FileWriter fw = null;
		try {
			fw = new FileWriter(tempFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(program);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String options = " -s " + tempFile.getAbsolutePath()
				+ " -t " + " main " + " -q ";

		OsUtils.runCommand(pathToProlog, options,null);

		if (OsUtils.stderr.length()>0) {
			throw new IllegalArgumentException(
					"prolog program constructed from a rule for warnings "
							+ "checking contains errors: " + OsUtils.stderr.toString());

		}
		return OsUtils.result.toString().trim();
	}

	private static String searchForExe() {
		String[] candidates = { "swipl.exe", "swipl" };
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


	private static final SecureRandom random = new SecureRandom();

	static File generateFile(String prefix, String suffix, File dir) {
		long n = random.nextLong();
		if (n == Long.MIN_VALUE) {
			n = 0;      // corner case
		} else {
			n = Math.abs(n);
		}
		return new File(dir, prefix +"_"+ Long.toString(n) + "_"+suffix);
	}

	@Override
	public String getOptions() {
		throw new UnsupportedOperationException();
	}
}
