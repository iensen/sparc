package warnings;

import java.util.HashSet;

public abstract class PrologRuleAtom {
   HashSet<String> variables;
   public abstract HashSet<String> getVariables();
   public abstract String toString();
}
