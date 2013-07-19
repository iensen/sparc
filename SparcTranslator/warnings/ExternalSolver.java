package warnings;

public abstract interface ExternalSolver {
   public abstract String run();
   public abstract boolean isSatisfiable();
}
