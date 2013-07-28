package configuration;

public class Settings {
	
private static final Settings singletonInstance = new  Settings();
ASPSolver solver;
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
}