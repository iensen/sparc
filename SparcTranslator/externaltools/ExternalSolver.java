package externaltools;

public abstract class ExternalSolver {
   String program;
   public abstract String run(boolean ignoreWarnings);
   public abstract boolean isSatisfiable();
   public void setProgram(String program) {
	   this.program=program;
   }
}
