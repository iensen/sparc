package warnings;

import java.util.HashSet;

public abstract class PrologRuleAtom {
   HashSet<String> variables;
   PrologRuleAtomType type;
   public abstract HashSet<String> getVariables();
   public abstract String toString();
}
