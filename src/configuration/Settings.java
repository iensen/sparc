package configuration;


public class Settings {
	
private static final Settings singletonInstance = new  Settings();
ASPSolver solver;
boolean lout;
boolean web;
boolean disabledEmptySortChecking;

String commandLineQuery = null;
int requiredNumberOfComputedAnswerSets;


//  prevents any other class from instantiating
private Settings(){
	solver=ASPSolver.Clingo;
}
 
// Providing Global point of access
public static Settings getSingletonInstance() {
return singletonInstance;
}
 
public static void setSolver(ASPSolver solver) {
	singletonInstance.solver=solver;
}


public static void setCommandLineQuery(String query) {
	singletonInstance.commandLineQuery = query;
}

public static String getCommandLineQuery() {
	return singletonInstance.commandLineQuery;
}

public static int getRequiredNumberOfComputedAnswerSets() {
	return singletonInstance.requiredNumberOfComputedAnswerSets;
}

public static boolean isEmptySortCheckingEnabled() {
	return !singletonInstance.disabledEmptySortChecking;
}

public static void setEmptySortCheckingDisabled(boolean escd) {
	singletonInstance.disabledEmptySortChecking = escd;
}

public static void setRequiredNumberOfComputedAnswerSets( int numberOfAnswerSets) {
	singletonInstance.requiredNumberOfComputedAnswerSets = numberOfAnswerSets;
}


public static ASPSolver getSolver() {
	return singletonInstance.solver;
}

public static boolean isLOutputFormat() {
	return singletonInstance.lout;
}

public static void setLOutputFormat(boolean lout)
{
	singletonInstance.lout = lout;
}

public static void setWebMode(boolean web)
{
	singletonInstance.web = web;
}

public static boolean isWebMode()
{
    return singletonInstance.web;
}


}