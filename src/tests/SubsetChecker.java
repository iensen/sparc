package tests;

import java.util.HashSet;

/***
 * This class is used to check if certain set of answer set is a subset of another larger set
 * This is useful for testing SPARC with an option -n=k, where it only computes k of the 
 * answer sets of the given program.
 */
public class SubsetChecker implements IAnswerChecker {

	private HashSet<HashSet<String>> superSet;
	private int numberOfRequiredSetsInSubset;
	public SubsetChecker(HashSet<HashSet<String>> superSet, int numberOfRequiredSetsInSubSet) {
		this.superSet = superSet;
		this.numberOfRequiredSetsInSubset = numberOfRequiredSetsInSubSet;
	}
	@Override
	public boolean check(HashSet<HashSet<String>> answerSet) {
		return answerSet.size() == this.numberOfRequiredSetsInSubset &&
				superSet.containsAll(answerSet);
	}

}
