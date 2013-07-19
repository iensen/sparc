package warnings;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Clingcon implements ExternalSolver {
    String program;
    private String pathToClingcon;

    public Clingcon(String program) throws FileNotFoundException {
        this.program = program;
        //TODO:REMOVE THIS!
   //    System.err.println(program);
        pathToClingcon = searchForExe();
        if (pathToClingcon == null) {
            throw new FileNotFoundException("Clingcon not found. "
                    + "You should have clingcon or clingcon.exe available "
                    + "from your path or current folder.");
        }
    }
    public boolean isSatisfiable() {
       String output=run();
       return !output.contains("UNSATISFIABLE");
    }

    public String run() {
        StringBuilder programOutput = new StringBuilder();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(pathToClingcon);
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
                throw new IllegalArgumentException(
                        "clingcon program constructed from a rule for warnings " +
                                "checking contains errors: "
                                + errors.toString());
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
        String[] candidates = { "clingcon", "./clingcon", "clingcon.exe",
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
