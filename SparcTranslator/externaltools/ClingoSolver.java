package externaltools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

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
	        StringBuilder programOutput = new StringBuilder();
	        Process process = null;
	        try {
	        	String options=" 0 --shift ";
	        	if(Settings.getSingletonInstance().getOptions()!=null)
	        		options+=Settings.getSingletonInstance().getOptions();
	            process = Runtime.getRuntime().exec(pathToClingo+options);
	        } catch (IOException e) {
	            System.err.println(e.getMessage());
	        }
	        
	        OutputStream stdin = process.getOutputStream();
	        InputStream stderr = process.getErrorStream();
	        InputStream stdout = process.getInputStream();
	        try {
	            // write program to sparc translator:
	            stdin.write(program.getBytes(), 0, program.length());

	            stdin.flush();
	            stdin.close();

	            // read Std_error:
	            BufferedReader brCleanUp = new BufferedReader(
	                    new InputStreamReader(stderr));

	            String line;
	            StringBuilder errors = new StringBuilder();
	            while ((line = brCleanUp.readLine()) != null) {

                    if(ignoreWarnings && !line.trim().toLowerCase().startsWith("%") || !ignoreWarnings)
	                   errors.append(line);


	            }

	            if (errors.length() > 0) {
	                throw new IllegalArgumentException(
	                        "constructed clingo program has errors: "
	                                + errors.toString());
	            }
	            brCleanUp = new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                programOutput.append(line+System.getProperty("line.separator"));
	            }
	            brCleanUp.close();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        return programOutput.toString();
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
