package parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BuiltInPredicates {
   public static final String[] predicateNames = new String[] { "appl", "has_grounding","warning","exit"};
   public static final Set<String> predicateNamesSet = new HashSet<String>(Arrays.asList(predicateNames));
}
