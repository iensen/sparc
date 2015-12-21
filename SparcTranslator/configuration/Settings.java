package configuration;

public class Settings {
	
private static final Settings singletonInstance = new  Settings();
ASPSolver solver;
boolean lout;
boolean web;
String options;


public String getOptions() {
	return options;
}

public void setOptions(String options) {
	this.options = options;
}

//  prevents any other class from instantiating
private Settings(){
	solver=ASPSolver.DLV;
}
 
// Providing Global point of access
public static Settings getSingletonInstance() {
 
return singletonInstance;
}
 
public static void setSolver(ASPSolver solver) {
	singletonInstance.solver=solver;
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