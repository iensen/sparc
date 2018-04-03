//1. Introduce an option to SPARC solver (-getsig) that makes it parse the program, type check it and, if the program is correct,
//   output the information about sorted signature
//2. Create a class and implement a method produceSignature(InstanceGenerator, ParsedProgramTree)   
//3. Change the main function of SPARC to handle the additional option
  


//Data Structure:
//{predicates:[{name:"father", sorts:["person", "person"]}],{name:"father", sorts:["person", "person"]}] ,
// sorts: [{name:'person', objects:["john","bob"]]
//}

package signatureprinting;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import parser.ASTpredicateDeclarations;
import parser.ASTsortExpression;
import parser.SimpleNode;
import translating.InstanceGenerator;

public class SortedSignaturePrinter { 
	
	
	/*Generate Json Object describing program signature*
	  * predicateArgumentSorts contain predicate declarations
	  * sortNameToExpression contain sort definitions
	  gen is used to generate instances
	  
	  Example (by Fox Bolduc):
	  
	      predicateArgumentSorts: father -> [person, person]
	                              mother -> [person, person]
	      sortNameToExpression:   person -> {bob, sara}                         
	      

	  Generated Json:       
      {
         "predicates":[
            {
               "name":"father",
               "sorts":[
                  "person",
                  "person"
               ]
            },
            {
               "name":"mother",
               "sorts":[
                  "person",
                  "person"
               ]
            }
         ],
         "sorts":[
            {
               "name":"person",
               "objects":[
                  "bob",
                  "sara"
               ]
            }
         ]
      }	   
	*/
	
	@SuppressWarnings("unchecked")
	private static JSONObject createJsonObjectForSignature(HashMap < String, ArrayList < String >> predicateArgumentSorts, 
			                                               HashMap < String, ASTsortExpression > sortNameToExpression,
                                                           InstanceGenerator gen) {	
		
		JSONObject jsonObj = new JSONObject();
		
		// add predicate declarations to jsonObj
		JSONArray predicateDeclarations = new JSONArray();
		
		for (String predicate: predicateArgumentSorts.keySet()) {
			if(predicate.startsWith("#"))
				continue;
	
			
			JSONArray sorts = new JSONArray();
			for (String sort:predicateArgumentSorts.get(predicate)) {
				sorts.add(sort);
			}
			HashMap<String, Object> predicateDeclaration = new HashMap<String,Object>();
			
			predicateDeclaration.put("name", predicate);
			predicateDeclaration.put("sorts", sorts);
			predicateDeclarations.add(predicateDeclaration);			
		}
		
		jsonObj.put("predicates", predicateDeclarations);
		
		// add sort definitions to jsonObj
		JSONArray sortDefinitions = new JSONArray();
		
		for (String sort: sortNameToExpression.keySet()) {
		
			
			JSONArray sortObjects = new JSONArray();
		
			HashSet<String> objectStrs = gen.generateInstances(sortNameToExpression.get(sort), true);
			for (String object:objectStrs) {
				sortObjects.add(object);
			}
			
			HashMap<String, Object> sortDefinition = new HashMap<String,Object>();
			
			sortDefinition.put("name", sort);
			sortDefinition.put("objects", sortObjects);
			sortDefinitions.add(sortDefinition);			
		}
		jsonObj.put("sorts", sortDefinitions);
		return jsonObj;
	}
	

	/* Print the signature of a program in JSON format */
	public static void print(HashMap < String, ArrayList < String >> predicateArgumentSorts, 
                             HashMap < String, ASTsortExpression > sortNameToExpression,
                             InstanceGenerator gen) {
		
		JSONObject jsonObj = createJsonObjectForSignature(predicateArgumentSorts, sortNameToExpression, gen);
	   	System.out.println("JSON_BEGIN");
	    System.out.println(jsonObj);
	   	System.out.println("JSON_END");
	   	
	}
}
