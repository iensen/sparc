package externaltools;

import java.io.IOException;
import java.io.InputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

class StreamGobbler
  extends Thread {

  InputStream is;
  String type;

  StreamGobbler (InputStream is, String type) {
    this.is = is;
    this.type = type;
  }

  @Override
public void run () {
    try {
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line;
      while ((line = br.readLine()) != null) {
    	
        // System.out.println(type + ">" + line);
    	if(type.equals("OUTPUT")) {
    		 OsUtils.result.append(line).append("\n");
    	}
    	else {
    		OsUtils.errors.append(line).append("\n");
    	}
       
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}



public class OsUtils {

  public static StringBuilder result;
  public static StringBuilder errors;
  public static int runCommand (String[] command, String param)
    throws Throwable {
	//System.out.println("coomand ->+"+command+"+<-");
    result = new StringBuilder();
    errors=new StringBuilder();
    
    Runtime rt = Runtime.getRuntime();
    Process proc = rt.exec(command);
    // Any error message?
    os = proc.getOutputStream();
    writeString(param);
    StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
    // Any output?
    StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
    // Kick them off.
    errorGobbler.start();
    outputGobbler.start();
    // Any error?
    proc.waitFor();


    System.out.println(result.toString());
    return proc.exitValue();
  }

private static OutputStream os;
  
  public static void writeString (String param){
	  PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
	  pw.println(param);
	  pw.flush();
	  pw.close();
  }
}
