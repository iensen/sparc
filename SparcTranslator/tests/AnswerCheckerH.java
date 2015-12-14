package tests;

import java.util.HashSet;

public class AnswerCheckerH implements IAnswerChecker {

	@Override
	public boolean check(HashSet<HashSet<String>> answerSets) {
		for (HashSet<String> a : answerSets) {
			for (String lit :a) {
				if(!lit.matches("-?p[(][1-9][0-9]?[0-9]?[)]")) {
					return false;
				}
			}
		}
		return true;
		
	}
	
}
