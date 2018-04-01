package warnings;

import java.util.ArrayList;
import java.util.HashSet;

public class UniqueNameGenerator {
	private HashSet<String> usedNames;
	int id;
    static private final String prefix="X_";
	public UniqueNameGenerator() {
        id=0;
        usedNames=new HashSet<String> ();
	}
	
	public void addUsedNames(HashSet<String> usedNames) {
		this.usedNames.addAll(usedNames);
	}
	
	public String generateNewName() {
		while(usedNames.contains(prefix+id)) 
			++id;
		String newName=prefix+id;
		usedNames.add(newName);
		return newName;
	}
	
	public ArrayList<String> generateNNewNames(int n) {
		ArrayList<String> newNames=new ArrayList<String>(n);
		for(int i=0;i<n;i++) {
			newNames.add(generateNewName());
		}
		return newNames;
	}
}
