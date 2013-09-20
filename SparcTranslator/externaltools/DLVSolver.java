package externaltools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import configuration.Settings;
/**
 * This class implements operations which can be executed on DLV
 */
public class DLVSolver extends ExternalSolver{
	
	    /**
	     * Path to DLV in the system
	     */
	    private String pathToDlv;
        
	    /**
	     * Constuctor
	     * @throws FileNotFoundException if dlv was not found in the system
	     */
	    public DLVSolver() throws FileNotFoundException {
	    	this(null);
	    }
	    
	    /**
	     * Constructor
	     * @param program to be stored in program field of the solver object
	     * @throws FileNotFoundException if dlv was not found in the system
	     */
	    public DLVSolver(String program) throws FileNotFoundException {
	        this.program = program;    
	        pathToDlv = searchForExe();
	        if (pathToDlv == null) {
	            throw new FileNotFoundException("dlv not found. "
	                    + "You should have dlv or dlv.exe available "
	                    + "from your path, current folder, or the path specifiend by program argument.");
	        }
	    }
	    
	    /**
	     * The method returns true if the program is satisfiable
	     */
	    public boolean isSatisfiable() {
	       String output=run(true);
	       return !output.contains("UNSATISFIABLE");
	    }
        
	    /**
	     * This method returns the result of execution of the program on DLV
	     * @param ignoreWarnings flag indicates whether warnings from DLV will be ignored
	     * @return the output of DLV after running the program
	     */
	    public String run(boolean ignoreWarnings) {
	        
	    	   StringBuilder programOutput = new StringBuilder();
		        Process process = null;
		        String options=" -- ";
		        //check for option passed as sparc arguments
	        	if(Settings.getSingletonInstance().getOptions()!=null)
	        		options+=Settings.getSingletonInstance().getOptions();
		        try {
		        	//create a new process for dlv
		            process = Runtime.getRuntime().exec(pathToDlv+options);
		        } catch (IOException e) {
		            System.err.println(e.getMessage());
		        }
		        
		        OutputStream stdin = process.getOutputStream();
		        InputStream stderr = process.getErrorStream();
		        InputStream stdout = process.getInputStream();
		        try {
		            // write program to DLV:
		            stdin.write(program.getBytes(), 0, program.length());
		            stdin.flush();
		            stdin.close();

		            // read errors:
		            BufferedReader brCleanUp = new BufferedReader(
		                    new InputStreamReader(stderr));

		            String line;
		            StringBuilder errors = new StringBuilder();
		            while ((line = brCleanUp.readLine()) != null) {
		                   errors.append(line);
		            }

		            if (errors.length() > 0 && !ignoreWarnings) {
		            	System.out.println(program);
		                throw new IllegalArgumentException(
		                        "constructed dlv program constructed contains errors: "
		                                + errors.toString());
		            }
		            // read standard output and append it to programOutput
		            brCleanUp = new BufferedReader(new InputStreamReader(stdout));
		            while ((line = brCleanUp.readLine()) != null) {
		                programOutput.append(line+System.getProperty("line.separator"));
		            }
		            brCleanUp.close();
		        } catch (IOException ex) {
		            ex.printStackTrace(); // this exception should not occur!
		        }
		        return programOutput.toString();
			
	    }

	    /**
	     * Search for DLV executable in the system
	     * @return the path to found executable or null if dlv was not found
	     */
	    private static String searchForExe() {
	        String[] candidates = { "dlv", "./dlv", "./dlv.bin",
	                "./dlv.exe","./dlv.i386-linux-elf-static.bin","./dlv.i386-apple-darwin.bin",
	                "./dlv.x86-64-linux-elf-static.bin","./dlv.mingw.exe"};
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
