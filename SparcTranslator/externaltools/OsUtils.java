package externaltools;

import java.io.IOException;
import java.io.InputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;



class StreamGobbler
  extends Thread {

  InputStream is;
  String type;
  Object lock;
  boolean ready = false;
  
  StreamGobbler (InputStream is, String type, Object lock) {
    this.is = is;
    this.type = type;
    this.lock = lock;
    
  }

  @Override
public void run () {	    
    try {
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line;
      while ((line = br.readLine()) != null) {
        //System.out.println(type + ">" + line);
    	if(type.equals("STDOUT")) {	
    		    OsUtils.result.append(line).append("\n");  		
    	}
    	else if(type.equals("ERROR")) {	
    		    OsUtils.errors.append(line).append("\n");		
    	}
       
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
     synchronized (lock) {
    	   ready = true;
    	     lock.notifyAll();
    	    
	}
  
  }
  
  public boolean isReady() {
	  return ready;
  }
}



public class OsUtils {

  public static StringBuffer result;
  public static StringBuffer errors;
  
}
