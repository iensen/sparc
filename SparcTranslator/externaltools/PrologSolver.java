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
		StringBuilder programOutput = new StringBuilder();

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

		Process process = null;

		try {

			String cmds = pathToProlog + " -s " + tempFile.getAbsolutePath()
					+ " -t " + " main " + " -q ";
			process = Runtime.getRuntime().exec(cmds);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		InputStream stderr = process.getErrorStream();
		InputStream stdout = process.getInputStream();
		try {

			// stdin.close();
			// read Std_error:
			BufferedReader brCleanUp = new BufferedReader(
					new InputStreamReader(stderr));

			String line;
			// System.out.println(brCleanUp.read());
			
			while ((line = brCleanUp.readLine()) != null) {
				  process.destroy();
				  throw new IllegalArgumentException(
						"prolog program constructed from a rule for warnings "
								+ "checking contains errors: " + line);
				
			}

			brCleanUp = new BufferedReader(new InputStreamReader(stdout));
			while ((line = brCleanUp.readLine()) != null) {
				programOutput.append(line);
				
			}
			brCleanUp.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return programOutput.toString();
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
}
