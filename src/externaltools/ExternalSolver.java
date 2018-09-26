package externaltools;
/**
 * This abstract class implements operations which can be executed on an external solver
 */
public abstract class ExternalSolver {
   /**
    * Program to be passed to external solver
    */
   String program;
   /**
    * This method returns the result of execution of the program
    * @param ignoreWarnings flag indicates whether warnings from the solver will be ignored
    * @return the output of external solver
    */
   public abstract String run(boolean ignoreWarnings);
   /**
    * This method checks if the program is satisfiable
    * @return true if program is satisfiable
    */
   public abstract boolean isSatisfiable();
   /**
    * Setter for program field
    * @param program new program text
    */
   public void setProgram(String program) {
	   this.program=program;
   }
   
   /**
    * @return the options that will be passed to the solver 
    * during SPARC execution
    */
   public abstract String getOptions(); 
   
   public String getProgram() {
	   return program;
   }
}
