package externaltools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        int count = 0;
        final int maxCount = 100000000;
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null ) {
				count += line.length();
				if(count > maxCount) {
			    	 System.err.println("ERROR: the output from the solver exceeds "  + Integer.toString(maxCount)+  " characters."
			    	 		+ "\n Your program has too many answer sets and we can't process all of them.");
			    	 // finish him!
			    	 Runtime.getRuntime().halt(0);	
				}
				//System.out.println(type + ">" + line);
				if(type.equals("STDOUT")) {	
					OsUtils.result.append(line).append("\n");  		
				}
				else if(type.equals("ERROR")) {	
					    OsUtils.stderr.append(line).append("\n");
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
	public static StringBuffer stderr;

	public static void runCommand(String path, String options,String input) {
		Object lockStdOut = new Object();
		Object lockStdErr = new Object();
		Process process = null;
		OsUtils.stderr=new StringBuffer();
		OsUtils.result = new StringBuffer();
		try {
			//create a new process for dlv
			process = Runtime.getRuntime().exec(path+options);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}   
		      
		StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(),"ERROR",lockStdErr);
		StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(),"STDOUT",lockStdOut);
		// kick them off
		errorGobbler.start();
		outputGobbler.start();
		try {
			// write the provided input to the process
			if(input != null) {
				OutputStream stdin = process.getOutputStream();	 
				stdin.write(input.getBytes(), 0, input.length());
				stdin.flush();
				stdin.close();
			}
			try {
				process.waitFor();			            				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// wait until error stream is ready:
			synchronized(lockStdErr){
				while (!errorGobbler.isReady()){
					try {
						lockStdErr.wait();
					} catch (InterruptedException e) {
						// This should never happen!
						e.printStackTrace();
					}
				}
			}

			// wait until std out stream is ready:
			synchronized(lockStdOut){
				while (!outputGobbler.isReady()){
					try {
						lockStdOut.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}    
		} catch (IOException ex) {
			ex.printStackTrace(); // this exception should not occur!
		}
	}

}
