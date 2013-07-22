package externaltools;

public abstract class ExternalSolver {
   String program;
   public abstract String run();
   public abstract boolean isSatisfiable();
   public void setProgram(String program) {
	   this.program=program;
   }
}
