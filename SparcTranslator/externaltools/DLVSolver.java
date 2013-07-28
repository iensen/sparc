package externaltools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import configuration.Settings;

public class DLVSolver extends ExternalSolver{
	
	    private String pathToDlv;
        
	    public DLVSolver() throws FileNotFoundException {
	    	this(null);
	    }
	    
	    public DLVSolver(String program) throws FileNotFoundException {
	        this.program = program;
	     
	        //System.out.println(program);
	        pathToDlv = searchForExe();
	        if (pathToDlv == null) {
	            throw new FileNotFoundException("dlv not found. "
	                    + "You should have dlv or dlv.exe available "
	                    + "from your path, current folder, or the path specifiend by program argument.");
	        }
	    }
	    public boolean isSatisfiable() {
	       String output=run(true);
	       return !output.contains("UNSATISFIABLE");
	    }
        
	    public String run(boolean ignoreWarnings) {
	        
	    	   StringBuilder programOutput = new StringBuilder();
		        Process process = null;
		        String options=" -- ";
	        	if(Settings.getSingletonInstance().getOptions()!=null)
	        		options+=Settings.getSingletonInstance().getOptions();
		        try {
		            process = Runtime.getRuntime().exec(pathToDlv+options);
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

	                    
		                   errors.append(line);


		            }

		            if (errors.length() > 0) {
		            	System.out.println(program);
		                throw new IllegalArgumentException(
		                        "constructed dlv program constructed contains errors: "
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
