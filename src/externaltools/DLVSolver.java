package externaltools;

import java.io.FileNotFoundException;
import java.io.IOException;
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
	     * Constructor
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
		        String options = getOptions();
		        OsUtils.runCommand(pathToDlv, options, program);
		        if (OsUtils.stderr.toString().length()>0 && 
		        		(!ignoreWarnings || OsUtils.stderr.toString().indexOf(": syntax error.")!= -1)){
		                throw new IllegalArgumentException(
		                        "ERROR: constructed dlv program contains errors: "
		                                + OsUtils.stderr.toString());
		            }		       
		        return OsUtils.result.toString();
	    }
	    
	    public String getOptions() {
	    	return " -n=" + Settings.getRequiredNumberOfComputedAnswerSets() + " -silent -- ";
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
