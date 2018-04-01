package translating;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import configuration.ASPSolver;
import configuration.Settings;
import parser.ASTaggregateElement;
import parser.ASTatom;
import parser.ASTbody;
import parser.ASTchoice_element;
import parser.ASTdisplay;
import parser.ASTextendedNonRelAtom;
import parser.ASTextendedSimpleAtomList;
import parser.ASTnonRelAtom;
import parser.ASTpredSymbol;
import parser.ASTprogram;
import parser.ASTprogramRule;
import parser.ASTprogramRules;
import parser.ASTsimpleAtom;
import parser.ASTsortDefinitions;
import parser.ASTsortExpression;
import parser.ASTsymbolicConstant;
import parser.ASTsymbolicFunction;
import parser.ASTsymbolicTerm;
import parser.ASTterm;
import parser.ASTtermList;
import parser.ASTunlabeledProgramCrRule;
import parser.ASTunlabeledProgramRule;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslator;
import parser.SparcTranslatorTreeConstants;
import sorts.BuiltIn;
import sorts.CurlyBracketsExpander;
import translating.InstanceGenerator.GSort;
import utilities.Pair;
import warnings.ExpandSolve;
import warnings.Formula;
import warnings.RuleReducer;
import warnings.StringListUtils;
import warnings.WarningRuleCreator;

public class Translator {
	// mapping from sort names to sort expressions assigned to the sorts
	private HashMap<String, ASTsortExpression> sortNameToExpression;
	// mapping from predicate names to a list of names of sorts describing
	private HashMap<String, ArrayList<String>> predicateArgumentSorts;
	// consistency restoring rule labels
	private HashSet<String> ruleLabels;
	// renaming of original sort names to their ASP translation
	private HashMap<String,String> sortRenaming;
	
	private Writer out;// output
	private String inputFileName;// name of file being parsed( for error
									// reporting)
	private SparcTranslator mainTranslator;
	// name of atoms to be added for generating combinations of cr-rules
	final String crAuxAtomName = "appl";
	// id of cr-rule label, used for generating unique labels
	int labelId;
	private InstanceGenerator gen;
	// count of choice rules and aggregate elements
	private int localElemCount = 0;
	private StringBuilder translatedOutput;
	private LocalVariableRenamer renamer;

	// flags indicating whether or not warnings need to be generated
	private boolean generateASPWarnings;
	private boolean generateClingconWarnings;
	
	
	private RuleReducer ruleReducer;

	/**
	 * Constructor
	 * 
	 * @param sortNameToExpression
	 * @param predicateArgumentSorts
	 * @param ruleLabels
	 * @param out
	 * @param mainTranslator
	 */
	public Translator(

	Writer out, SparcTranslator mainTranslator, InstanceGenerator gen,
			boolean generateASPWarnings, boolean generateClingconWarnings) {

		this.mainTranslator = mainTranslator;
		this.sortNameToExpression = mainTranslator.sortNameToExpression;
		this.predicateArgumentSorts = mainTranslator.predicateArgumentSorts;
		this.ruleLabels = mainTranslator.crRuleLabels;
		this.out = out;
		this.gen = gen;
		renamer = new LocalVariableRenamer();
		this.generateASPWarnings = generateASPWarnings;
		this.generateClingconWarnings = generateClingconWarnings;
		if(generateClingconWarnings) {
			ruleReducer=new RuleReducer(sortNameToExpression, predicateArgumentSorts, gen);
		}
		
	}

	/**
	 * Input file name setter (file name is used for error reporting)
	 * 
	 * @param inputFileName
	 */
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	/**
	 * Translate program given by means of Abstract syntax tree node
	 * 
	 * @param program
	 *            to be translated.
	 * @param generatingSorts
	 *            sorts from which all instances will be generating(those which
	 *            occur in program rules explicitly).
	 * @param writeWarningsToSTDERR
	 *            flag, errors are written to stderr if it is true and ignored
	 *            otherwise.
	 * @throws ParseException
	 *             if sort of some unsafe variable cannot be determined (may be
	 *             the case in aggregate or choice rules where there is no
	 *             constraints for variable in the body).
	 */
	public String translateProgram(ASTprogram program,
			HashSet<String> generatingSorts,
			HashMap<String,String> sortsRenaming,
			boolean writeWarningsToSTDERR)
			throws ParseException {
		this.sortRenaming = sortsRenaming;
		
		translatedOutput = new StringBuilder();
		localElemCount = 0;
		labelId = 0;
        // if we need warnings, we need to shift curlyBrackets:
	    if(generateClingconWarnings) {
	    	CurlyBracketsExpander cExpander= new CurlyBracketsExpander(sortNameToExpression); 
	    	cExpander.ExpandCurlyBrackets((ASTsortDefinitions)program.jjtGetChild(0));
	    }
	    
	  
	    
	    
	    // generate sorts
		for (String s : generatingSorts) {
			gen.addSort(s, sortNameToExpression.get(s), true);
		}

		writeDirectives(program);
		translateRules((ASTprogramRules) program.jjtGetChild(2),writeWarningsToSTDERR);
		
		// translate display:
		HashSet<String> usedNames = new HashSet<String>();
		usedNames.addAll(sortRenaming.values());
		usedNames.addAll(predicateArgumentSorts.keySet());
	
		// append instances of generating sorts to the resulting program store
		for (GSort sort : gen.generatedSorts) {
			for (String instance : sort.instances) {
				String name = sort.sortName;
				if(sortsRenaming.containsKey(name))
					name = sortRenaming.get(name);
				appendStringToTranslation(name);
				appendStringToTranslation("(");
				appendStringToTranslation(instance);
				appendStringToTranslation(").");
				appendNewLineToTranslation();
			}
		}
		
		if(program.jjtGetNumChildren()>3)
		       translateDisplay(usedNames, (ASTdisplay) program.jjtGetChild(3));

		
		// write warnings if the flag was set to true
		if (writeWarningsToSTDERR) {
			for (String warning : mainTranslator.getWarnings()) {
				System.err.println("%WARNING: " + warning);
			}
		} else {
			StringBuilder warningStrings = new StringBuilder();
			// warningsStrings.
			warningStrings.append("%WARNINGS");
			for (String warning : mainTranslator.getWarnings()) {
				warningStrings.append(" %WARNING: " + warning);
			}
			if (mainTranslator.getWarnings().size() > 0) {
				throw new ParseException(warningStrings.toString());
			}
		}
		
	    //System.out.println(translatedOutput.toString());
		if(Settings.getSolver()== ASPSolver.Clingo) {
			addClingoOptimizations();
		}
		return translatedOutput.toString();

	}
	
	/**
	 * The method extends translatedOutput with some show statements 
	 * so that the output of the solver is restricted
	 */
	private void addClingoOptimizations() {
		String commandLineQuery = Settings.getCommandLineQuery();
		if(commandLineQuery != null) {
			// todo: compute the arity later
			translatedOutput.append("#show " + commandLineQuery + "/0." );
			appendNewLineToTranslation();
		}
		//translatedOutput.append(b)
	}
	
	
	/**
	 * Takes display section and expands shorthands #s. to #s(X) and p. to p(X1,...,Xn),
	 * where n is the arity of the predicate p
	 * @param display (note, this input is being modified)
	 */
	private void ExpandShortHands(ASTdisplay display) {
		for(int i=0 ; i< display.jjtGetNumChildren(); i++) {
			ASTnonRelAtom atom = (ASTnonRelAtom) display.jjtGetChild(i);			
			ASTpredSymbol pred = (ASTpredSymbol)atom.jjtGetChild(0);
			// if atom is a sort name, and it does not have any arguments, we append an argument "X" to it:
			if(pred.hasPoundSign()) {
			    if(atom.jjtGetNumChildren() == 1) {
			    	ASTtermList tlist= new ASTtermList(SparcTranslatorTreeConstants.JJTTERMLIST);
			    	tlist.jjtAddChild(new ASTterm("X"), 0);
			    	atom.jjtAddChild(tlist, 1);
			    }
			}
			
			if(!pred.hasPoundSign() && atom.jjtGetNumChildren()==1
					&& predicateArgumentSorts.get(pred.image).size()>0) {
				ASTtermList tlist= new ASTtermList(SparcTranslatorTreeConstants.JJTTERMLIST);
				for(int j=0; j<predicateArgumentSorts.get(pred.image).size(); j++) {
					tlist.jjtAddChild(new ASTterm("X"+Integer.toString(j+1)), j);
				}
		    	atom.jjtAddChild(tlist, 1);				
			}			
		}
			
	}

	/**
	 * 
	 * @param usedNames - predicate names already used in the program
	 * @param display - the display section of the program (represented by ast) 
	 *                  consisting of the display keyword
	 *                  followed by a sequence of statements of the form [-]p(t_1,...,t_n).
	 *                  or of the form p, where [-]p is a shorthand for [-]p(X_1,...,X_n),
	 *                  and both [-]p(t_1,...,t_n) and [-] p(X_1,...,X_n) are literals of P
	 * @result function extends translatedOutput with a sequence of rules of the form
	 *         p'(t_1,...,t_n) :- p(t_1,...,t_n), where p' is a fresh predicate name
	 *         for each p(t_1,...,t_n) in the display 
	 *         followed by a comment % p1',....,pn', where p1',...,pn' are all fresh predicates used        
	 */
	private void translateDisplay(HashSet<String> usedNames, ASTdisplay display) {
		
		ExpandShortHands(display);
		ArrayList<String> displayPredicateNames = new ArrayList<String>();
		for(int i=0 ; i< display.jjtGetNumChildren(); i++) {
			ASTnonRelAtom atom = (ASTnonRelAtom) display.jjtGetChild(i);	
			// create a rule  p'(t_1,...,t_n) :- p(t_1,...,t_n)
			
			ASTpredSymbol pred = (ASTpredSymbol)atom.jjtGetChild(0);
			
			String body = null;
			if(pred.hasPoundSign() && sortRenaming.containsKey(pred.image)) {
                	body = atom.toStringWithPredicateRenamed(sortRenaming.get(pred.image));
            } else {
                	body = atom.toString();
            }
	
			String newName ="";
			boolean prevNegative = pred.negative;
			boolean prevPoundSign = pred.hasPoundSign();
			pred.negative = false;
			
			if(pred.hasPoundSign() && sortRenaming.containsKey(pred.image)) {
				newName = extendtoUniqueName(sortRenaming.get(pred.image), usedNames);
			} else {
				newName =  extendtoUniqueName(pred.image, usedNames);
			}
			
			usedNames.add(newName);
			displayPredicateNames.add(newName);
			pred.setPoundSign(false);
			pred.negative = false;
			String head = atom.toStringWithPredicateRenamed(newName);
			appendStringToTranslation(head.toString() + ":-" + body.toString() + ".");
			appendNewLineToTranslation();
			// restore the pound sign and negativity:
			pred.setPoundSign(prevPoundSign);
			pred.negative = prevNegative;
		}
		
	    // add the comment	
		StringBuilder sb = new StringBuilder();
		sb.append("%");
		for(String s: displayPredicateNames) {
			sb.append(" " + s);
		}
		appendStringToTranslation(sb.toString());
		appendNewLineToTranslation();
	}
	
	/**
	 * 
	 * @param s a string
	 * @param usedNames - a set of strings
	 * @return a new string s' such that s' = s + x, where x is a shortest sequence of underscores 
	 *         such that s + x is not in usedNames
	 */
	private String extendtoUniqueName(String s, HashSet<String> usedNames) {
		while (usedNames.contains(s)) {
			s = s + "_";
		}
		return s;
	}

	/**
	 * Rewrite #const and #maxint directives from SPARC to DLV program
	 * 
	 * @param root
	 *            of program abstract syntax tree
	 */
	private void writeDirectives(ASTprogram program) {
		//add #maxint if the solver is DLV:
		if(Settings.getSolver()==ASPSolver.DLV) {
			appendStringToTranslation("#maxint="+BuiltIn.getMaxInt()+".");
		}
		
		appendNewLineToTranslation();
		//add other directives
		for (String s : program.getdirectives()) {
			appendStringToTranslation(s);
			appendNewLineToTranslation();
		}
	}

	/**
	 * Unique cr-rule label generator
	 * 
	 * @return string containing unique label
	 */
	private String generateUniqueRuleLabel() {
		while (ruleLabels.contains("r_" + labelId))
			++labelId;
		ruleLabels.add("r_" + labelId);
		return "r_" + labelId;
	}

	/**
	 * Translate a collection of program rules and append the new content to
	 * translatedOutput
	 * 
	 * @param rules
	 *            to translate
	 * @throws ParseException
	 *             if sort of some unsafe variable cannot be determined (may be
	 *             the case in aggregate or choice rules where there is no
	 *             constraints for variable in the body).
	 */
	private void translateRules(ASTprogramRules rules,boolean writeWarningsToSTDERR) throws ParseException {
		for (int i = 0; i < rules.jjtGetNumChildren(); i++) {
			translateRule((ASTprogramRule) rules.jjtGetChild(i),writeWarningsToSTDERR);
		}
	}


	
	public String translateAndWriteRules(ASTprogramRules rules,
			boolean writeWarningsToSTDERR) throws ParseException {
		translatedOutput = new StringBuilder();

		translateRules(rules,writeWarningsToSTDERR);
		writeTranslatedProgram();
		if (writeWarningsToSTDERR) {
			for (String warning : mainTranslator.getWarnings()) {
				System.err.println("%WARNING: " + warning);
			}
		}
		return translatedOutput.toString();
	}

	/**
	 * Add atoms to the body of the rule given by AST node
	 * 
	 * @param rule
	 *            AST node
	 * @param atoms
	 *            collections of atoms to add
	 */
	private void addAtomsToRulesBody(ASTprogramRule rule,
			ArrayList<ASTatom> atoms) {
		if (atoms.size() == 0)
			return;
		SimpleNode child = (SimpleNode) rule.jjtGetChild(0);
		if (child.getId() == SparcTranslatorTreeConstants.JJTUNLABELEDPROGRAMRULE) {
			addAtomsToRulesBody((ASTunlabeledProgramRule) child, atoms);
		} else {
			addAtomsToRulesBody((ASTunlabeledProgramCrRule) child, atoms);
		}
	}

	/**
	 * Add atoms to the body given by AST node
	 * 
	 * @param body
	 *            AST node
	 * @param atoms
	 *            collection of atoms to add
	 */
	private void addAtomsToBody(ASTbody body, ArrayList<ASTatom> atoms) {
		HashSet<String> addedAtoms = new HashSet<String>();
		for(int i=0;i<body.jjtGetNumChildren();i++) {
			addedAtoms.add(((ASTatom)body.jjtGetChild(i)).toString(sortRenaming));
		}
		for (int i = 0; i < atoms.size(); i++) {
			if (!addedAtoms.contains(atoms.get(i).toString(sortRenaming))) {
				body.jjtAddChild(atoms.get(i), body.jjtGetNumChildren());
				addedAtoms.add((atoms.get(i).toString(sortRenaming)));
			}
		}
	}

	/**
	 * Create an abstract syntax tree body node from given list of atoms
	 * 
	 * @param atoms
	 *            components of created body
	 * @return abstract syntax tree body node
	 */
	private ASTbody createBody(ArrayList<ASTatom> atoms) {
		ASTbody body = new ASTbody(SparcTranslatorTreeConstants.JJTBODY);
		for (int i = 0; i < atoms.size(); i++) {
			body.jjtAddChild(atoms.get(i), i);
		}
		return body;
	}

	/**
	 * Add atoms to the body of cr-rule given by AST node
	 * 
	 * @param rule
	 *            AST node
	 * @param atoms
	 *            collections of atoms to add
	 */
	private void addAtomsToRulesBody(ASTunlabeledProgramCrRule rule,
			ArrayList<ASTatom> atoms) {
		if (rule.jjtGetNumChildren() == 1
				&& ((SimpleNode) rule.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTBODY) {
			addAtomsToBody((ASTbody) rule.jjtGetChild(0), atoms);
		} else if (rule.jjtGetNumChildren() > 1) {
			addAtomsToBody((ASTbody) rule.jjtGetChild(1), atoms);
		} else {
			ASTbody createdBody = createBody(atoms);
			rule.jjtAddChild(createdBody, rule.jjtGetNumChildren());
		}
	}

	/**
	 * Add atoms to the body of standard rule given by AST node
	 * 
	 * @param rule
	 *            AST node
	 * @param atoms
	 *            collections of atoms to add
	 */

	private void addAtomsToRulesBody(ASTunlabeledProgramRule rule,
			ArrayList<ASTatom> atoms) {
		if (((SimpleNode) rule.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTPREDSYMBOL) {
			ASTbody createdBody = createBody(atoms);
			rule.jjtAddChild(createdBody, rule.jjtGetNumChildren());
		} else if (((SimpleNode) rule.jjtGetChild(0)).getId() == SparcTranslatorTreeConstants.JJTBODY) {
			addAtomsToBody(((ASTbody) rule.jjtGetChild(0)), atoms);
		} else if (rule.jjtGetNumChildren() > 1
				&& ((SimpleNode) rule.jjtGetChild(1)).getId() == SparcTranslatorTreeConstants.JJTBODY) {
			addAtomsToBody(((ASTbody) rule.jjtGetChild(1)), atoms);
		} else {
			ASTbody createdBody = createBody(atoms);
			rule.jjtAddChild(createdBody, rule.jjtGetNumChildren());

		}
	}

	/**
	 * Add atoms to choice element's body example: by adding p(Y) to choice
	 * element X: p(X) we get X:p(X),p(Y).
	 * 
	 * @param node
	 *            choice_element
	 * @param newAtoms
	 *            atoms to be added
	 */
	private void addAtomsToChoiceElement(ASTchoice_element node,
			ArrayList<ASTatom> newAtoms) {
		if (newAtoms.size() == 0)
			return;
		if (node.jjtGetNumChildren() == 2) {
			addAtomsToList((ASTextendedSimpleAtomList) node.jjtGetChild(1),
					newAtoms);
		}

		else {
			ASTextendedSimpleAtomList newList = new ASTextendedSimpleAtomList(
					SparcTranslatorTreeConstants.JJTEXTENDEDSIMPLEATOMLIST);
			node.jjtAddChild(newList, 1);
			addAtomsToList(newList, newAtoms);
		}
	}

	/**
	 * Convert atom to simpleAtom (before calling this method make sure that
	 * atom a follows the grammar given for simple atoms, otherwise results are
	 * unpredictable)
	 * 
	 * @param a
	 *            atom to be converted
	 * @return converted atom
	 */
	private ASTsimpleAtom ConvertAtomToSimple(ASTatom a) {
		ASTsimpleAtom res = new ASTsimpleAtom(
				SparcTranslatorTreeConstants.JJTSIMPLEATOM);
		res.image = a.image;

		for (int i = 0; i < a.jjtGetNumChildren(); i++) {
			res.jjtAddChild(a.jjtGetChild(i), i);
		}
		return res;
	}

	/**
	 * Add atoms to aggregate element's body example: by adding p(Y) to
	 * aggregate element X: p(X) we get X:p(X),p(Y).
	 * 
	 * @param node
	 *            aggregate element
	 * @param newAtoms
	 *            atoms to be added
	 */
	private void addAtomsToAggregateElement(ASTaggregateElement node,
			ArrayList<ASTatom> newAtoms) {
		if (newAtoms.size() == 0)
			return;
		ASTextendedSimpleAtomList simpleList = null;
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			if (((SimpleNode) (node.jjtGetChild(i))).getId() == SparcTranslatorTreeConstants.JJTEXTENDEDSIMPLEATOMLIST) {
				simpleList = ((ASTextendedSimpleAtomList) (node.jjtGetChild(i)));
				break;
			}
		}
		if (simpleList == null) {
			simpleList = new ASTextendedSimpleAtomList(
					SparcTranslatorTreeConstants.JJTEXTENDEDSIMPLEATOMLIST);
			node.jjtAddChild(simpleList, node.jjtGetNumChildren());
		}
		addAtomsToList(simpleList, newAtoms);

	}

	/**
	 * Add all of the atoms from ArrayList of atoms as children to
	 * simpleList(given by AST node).
	 * 
	 * @param simpleList
	 * @param newAtoms
	 */
	private void addAtomsToList(ASTextendedSimpleAtomList simpleList,
			ArrayList<ASTatom> newAtoms) {
		HashSet<String> addedAtoms = new HashSet<String>();
		for (ASTatom a : newAtoms) {
			if (!addedAtoms.contains(a.toString(sortRenaming))) {
				simpleList.jjtAddChild(ConvertAtomToSimple(a),
						simpleList.jjtGetNumChildren());
				addedAtoms.add(a.toString(sortRenaming));
			}
		}

	}

	/**
	 * 1.Move expressions from predicate arguments to the body of the rule
	 * Example: p(X+1):-q(X+2). becomes p(Y):-p(Z),Y=X+1,Z=X+2, where Y and Z
	 * are new variables in the rule 2. Add all newly added atoms of the form
	 * [variable]=[expression] to newBodyAtoms list
	 * 
	 * @param rule
	 *            to be processed
	 * @param newBodyAtoms
	 *            the list where all newly added atoms are stored
	 */
	private void fetchGlobalExpressions(ASTprogramRule rule,
			ArrayList<ASTatom> newBodyAtoms) {

		ArrayList<ASTatom> newAtoms = new ArrayList<ASTatom>();
		VariableFetcher vf = new VariableFetcher();
		ExpressionFetcher ef = new ExpressionFetcher(vf.fetchVariables(rule));
		newAtoms.addAll(ef.fetchGlobalExpressions(rule));
		//addAtomsToRulesBody(rule, newAtoms);
		newBodyAtoms.addAll(newAtoms);
	}

	/**
	 * Move local expressions from predicates from aggregates and choice rules
	 * arguments to bodies Example: p(X+1):q(X+2). becomes
	 * p(Y):p(Z),Y=X+1,Z=X+2, where Y and Z are new variables in the rule
	 * 
	 * @param rule
	 *            to be processed
	 */
	private void fetchLocalExpressions(ASTprogramRule rule) {
		VariableFetcher vf = new VariableFetcher();
		fetchLocalExpressions(rule, vf.fetchVariables(rule));
	}

	/**
	 * Recursively search for expressions in aggregates and choice rules and
	 * move them from predicates from aggregates and choice rules arguments to
	 * bodies Example: p(X+1):q(X+2). becomes p(Y):p(Z),Y=X+1,Z=X+2, where Y and
	 * Z are new variables in the rule
	 * 
	 * @param rule
	 *            to be processed
	 */
	private void fetchLocalExpressions(SimpleNode node,
			HashSet<String> variables) {

		if (node.getId() == SparcTranslatorTreeConstants.JJTAGGREGATEELEMENT
				|| node.getId() == SparcTranslatorTreeConstants.JJTCHOICE_ELEMENT) {
			// add expressions to the set of new atoms which will be added to
			// the node's body
			ExpressionFetcher ef = new ExpressionFetcher(variables);
			if (node.getId() == SparcTranslatorTreeConstants.JJTAGGREGATEELEMENT) {
				addAtomsToAggregateElement((ASTaggregateElement) node,
						ef.fetchLocalExpressions((ASTaggregateElement) node));
			} else {
				addAtomsToChoiceElement((ASTchoice_element) node,
						ef.fetchLocalExpressions((ASTchoice_element) node));
			}
		}
		// recursively search for aggregate and choice elements
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			fetchLocalExpressions((SimpleNode) node.jjtGetChild(i), variables);
		}
	}

	// find all global variables occurring in the body of the rule
	
	private HashSet<String> findBodyGlobalVariables(SimpleNode node) {
		HashSet<String> vars = new HashSet<String>();
		
		if(node.getId() == SparcTranslatorTreeConstants.JJTBODY) {	
			for(int i=0;i<node.jjtGetNumChildren();i++) {
				SimpleNode atom = (SimpleNode)node.jjtGetChild(i);
		        SimpleNode atomChild = (SimpleNode) atom.jjtGetChild(0);
		        if(atomChild.getId() != SparcTranslatorTreeConstants.JJTAGGREGATE) {
		        	VariableFetcher vf = new VariableFetcher();
					vf.fetchVariables(atomChild,vars);
		        }
			}
			
		}
		
		for(int i=0;i<node.jjtGetNumChildren();i++) {
		 vars = findBodyGlobalVariables((SimpleNode)node.jjtGetChild(i));
			if(vars!= null)
				return vars;			
		}
		return null;
	}
	
	private void renameLocalVariables(SimpleNode n,
			HashMap<String, String> originalNameMapping) {
		
		if(n.getId() == SparcTranslatorTreeConstants.JJTPROGRAMRULE) {
			HashSet<String> bodyVariables = findBodyGlobalVariables(n);
			if(bodyVariables == null)
				bodyVariables = new HashSet<String>();
			renamer.setBodyVariables(bodyVariables);
			
		}
		if (n.getId() == SparcTranslatorTreeConstants.JJTAGGREGATEELEMENT) {
			renamer.renameLocalVariables((ASTaggregateElement) n,
					localElemCount++, originalNameMapping);
		} else {
			for (int i = 0; i < n.jjtGetNumChildren(); i++) {
				renameLocalVariables((SimpleNode) n.jjtGetChild(i),
						originalNameMapping);
			}
		}
	}

	/**
	 * Recursively fetch terms with variables from aggregates and choice rules
	 * of the rule and add atoms, specifying sort of the terms to the body of
	 * the corresponding aggregates and choice rules.
	 * 
	 * @param rule
	 * @param fetchedTerms
	 *            mapping between string representation of terms and arrayList
	 *            of all sort the term must belong to.
	 */
	private void fetchLocalTerms(SimpleNode node) throws ParseException {
		boolean isAggregateElement = node.getId() == SparcTranslatorTreeConstants.JJTAGGREGATEELEMENT;
		boolean isChoiceRuleElement = node.getId() == SparcTranslatorTreeConstants.JJTCHOICE_ELEMENT;
		TermFetcher tf = null;
		if (isAggregateElement || isChoiceRuleElement) {
			tf = new TermFetcher(predicateArgumentSorts,sortNameToExpression);
			HashMap<ASTterm, String> localFetchedTerms = null;
			if (isAggregateElement) {
				localFetchedTerms = tf
						.fetchTermSorts((ASTaggregateElement) node);
			} else {
				localFetchedTerms = tf.fetchTermSorts((ASTchoice_element) node);
			}
			ArrayList<ASTatom> newAtoms = new ArrayList<ASTatom>();
			for (ASTterm term : localFetchedTerms.keySet()) {
				String sortName = localFetchedTerms.get(term);
				newAtoms.add(createSortAtom(sortName, term));
				gen.addSort(sortName, sortNameToExpression.get(sortName), true);
			}
			if (isAggregateElement) {
				addAtomsToAggregateElement((ASTaggregateElement) node, newAtoms);
			} else {
				addAtomsToChoiceElement((ASTchoice_element) node, newAtoms);
			}

		} else {
			for (int i = 0; i < node.jjtGetNumChildren(); i++) {
				fetchLocalTerms((SimpleNode) node.jjtGetChild(i));
			}

		}
	}

	/**
	 * Fetch terms with variables from aggregates and choice rules of the rule
	 * and add atoms, specifying sort of the terms to the body of the
	 * corresponding aggregates and choice rules.
	 * 
	 * @param rule
	 * @param fetchedTerms
	 *            mapping between string representation of terms and arrayList
	 *            of all sort the term must belong to.
	 */
	private void fetchLocalTerms(ASTprogramRule rule) throws ParseException {
		fetchLocalTerms((SimpleNode) rule);
	}

	/**
	 * Fetch terms with variables from the rule and add atoms, specifying sort
	 * of the terms to the body of the rule.
	 * 
	 * @param rule
	 * @param fetchedTerms
	 *            mapping between string representation of terms and arrayList
	 *            of all sort the term must belong to.
	 */
	private void fetchGlobalTerms(ASTprogramRule rule,
			ArrayList<ASTatom> newBodyAtoms) throws ParseException {
		TermFetcher tf = new TermFetcher(predicateArgumentSorts, sortNameToExpression);
		HashMap<ASTterm, String> globalFetchedTerms = tf.fetchTermSorts(rule);
		ArrayList<ASTatom> newAtoms = new ArrayList<ASTatom>();
		for (ASTterm term : globalFetchedTerms.keySet()) {
			String sortName = globalFetchedTerms.get(term);
			newAtoms.add(createSortAtom(sortName, term));
			gen.addSort(sortName, sortNameToExpression.get(sortName), true);
		}
		//addAtomsToRulesBody(rule, newAtoms);
		newBodyAtoms.addAll(newAtoms);
	}

     /**
	 * Go over the AST node and fill sets of unbounded and bounded variables
	 * 
	 * @param unboundedVariables
	 *            set of found unbounded variables
	 * @param boundedVariables
	 *            set of found bounded variables
	 * @param node
	 *            node to explore
	 * @param scope
	 *            true if node is a child of a simpleAtom(either
	 */
	private void classifyVariables(HashSet<String> allVariables,
			HashSet<String> simpleOccurVariables, HashSet<String> arithmeticVariables, SimpleNode node, boolean predicateScope, boolean arithmeticScope) {
		if (node.getId() == SparcTranslatorTreeConstants.JJTVAR) {
			if(predicateScope && ! arithmeticScope) 
				simpleOccurVariables.add(node.toString());
			if(arithmeticScope) 
				arithmeticVariables.add(node.toString());
			allVariables.add(node.toString());
		}
		if (node.getId() == SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM
				|| node.getId() == SparcTranslatorTreeConstants.JJTNONRELATOM) {
			predicateScope = true;// root of predicate;
		}
		
		if(node.getId()==SparcTranslatorTreeConstants.JJTARITHMETICTERM) {
			String nodeImage=node.toString();
			if(nodeImage.indexOf('+')!=-1 || nodeImage.indexOf('-')!=-1 || nodeImage.indexOf('*')!=-1) {
				arithmeticScope=true;
			}
		}


		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			classifyVariables(allVariables, simpleOccurVariables, arithmeticVariables,
					(SimpleNode) node.jjtGetChild(i),predicateScope,arithmeticScope);
		}
	}

	/**
	 * Translate program rule given by means of AST node
	 * 
	 * @param rule
	 *            rule to be translated
	 * @throws ParseException
	 *             if sort of some variable cannot be detected
	 */
	private void translateRule(ASTprogramRule rule,boolean writeWarningsToSTDERR) throws ParseException {
		
		String originalRule = rule.toString(new HashMap<String,String>());
		int lineNumber = rule.getBeginLine();
		int columnNumber = rule.getBeginColumn();
		
		// write warnings to STDERR if the flag was set to true
		if (generateClingconWarnings) {
	      Formula ruleF=ruleReducer.reduceRule(rule);
	     // System.err.println(ruleF.toString());
	     
	      if(ruleF!=null && !ExpandSolve.run(ruleF)) {
	    	 if(writeWarningsToSTDERR) {
	    	    System.err.println("%WARNING: Rule "+originalRule+" at line "+lineNumber+
	    			 ", column "+columnNumber+" is an empty rule");
	    	 } else {
	    		 mainTranslator.addWarning("Rule "+originalRule+" at line "+lineNumber+
		    			 ", column "+columnNumber+" is an empty rule");
	    	 }
	      }
		}
		
		
		// renameLocalVariables
		HashMap<String, String> originalNameMapping = new HashMap<String, String>();
		appendToVariableNamesIn(rule, "_G", originalNameMapping);
		renameLocalVariables(rule,originalNameMapping);
		ArrayList<ASTatom> newSortAtoms=new ArrayList<ASTatom>();
		
		ensureVariableSafety(rule, originalRule, originalNameMapping,
				newSortAtoms);

	
		// fetch expressions:
		fetchGlobalExpressions(rule,newSortAtoms);
		fetchLocalExpressions(rule);
		// fetch terms:
		fetchGlobalTerms(rule,newSortAtoms);
		fetchLocalTerms(rule);
		
		addAtomsToRulesBody(rule,newSortAtoms);
		// add rules for warnings
		if(generateASPWarnings)
		{
		  ArrayList<String> warningRules=
					WarningRuleCreator.createWarningRules(originalRule, lineNumber, columnNumber, newSortAtoms, sortRenaming);
		  for(String warningRule:warningRules) {
			appendStringToTranslation(warningRule);
			appendNewLineToTranslation();
	      }
		}
	
		RuleAnalyzer ra = new RuleAnalyzer(rule);
		// add new weak constraints and rules for a CR-rule
		if (ra.isCrRule()) {
			String ruleName = getRuleName(rule);
			ASTatom applyAtom = getApplAtom(ruleName);
			ASTbody body = ra.getBody();
			appendStringToTranslation(applyAtom.toString(sortRenaming));
			appendStringToTranslation("|-");
			appendStringToTranslation(applyAtom.toString(sortRenaming));
			if (body != null) {
				appendStringToTranslation(":-");
				appendStringToTranslation(body.toString(sortRenaming));
			}
			appendStringToTranslation(".");
			appendNewLineToTranslation();
			appendStringToTranslation(":~");
			appendStringToTranslation(applyAtom.toString(sortRenaming));
			if (body != null) {
				appendStringToTranslation(",");
				appendStringToTranslation(body.toString(sortRenaming));
			}			
			appendStringToTranslation(".");
			if(Settings.getSolver() == ASPSolver.Clingo) {
				   appendStringToTranslation(" [1," + ruleName + "]");  	
			}	
			appendNewLineToTranslation();
			ArrayList<ASTatom> newAtoms = new ArrayList<ASTatom>();
			newAtoms.add(applyAtom);
			addAtomsToRulesBody(rule, newAtoms);
		}
		appendStringToTranslation(rule.toString(sortRenaming));
		appendNewLineToTranslation();
	}

	private void ensureVariableSafety(ASTprogramRule rule, String originalRule,
			HashMap<String, String> originalNameMapping,
			ArrayList<ASTatom> newSortAtoms) throws ParseException {
		HashSet<String> simpleOccurVariables =new HashSet<String>();
		HashSet<String> allVariables=new HashSet<String>();
		HashSet<String> arithmeticVariables=new HashSet<String>();
		final boolean arithmeticScope=false;
		final boolean predicateScope=false;
		
		classifyVariables(allVariables, simpleOccurVariables, arithmeticVariables, 
				rule, predicateScope, arithmeticScope);
		
		
		allVariables.removeAll(simpleOccurVariables);
		allVariables.removeAll(arithmeticVariables);
		
		if(!allVariables.isEmpty()) {
			Pair<ArrayList<String>,ArrayList<String>> unrestrictedVariablesLists=splitLocalGlobalVariables(allVariables);
			renameVariables(unrestrictedVariablesLists.first, originalNameMapping);
			renameVariables(unrestrictedVariablesLists.second,originalNameMapping);
			throw new ParseException(inputFileName + ": "
					+ "program rule "+originalRule
					+ " at line " + rule.getBeginLine() + ", column "
					+ rule.getBeginColumn()
					+ " contains "
					+(unrestrictedVariablesLists.first.size()>0?
					  "unrestricted global variables "+ StringListUtils.getSeparatedList(unrestrictedVariablesLists.first, ","):"") +
					(unrestrictedVariablesLists.second.size()>0 ?
							((unrestrictedVariablesLists.first.size()>0)? " and ":"")+
					"unrestricted local variables "+ StringListUtils.getSeparatedList(unrestrictedVariablesLists.second, ","):""));
					
		}
		arithmeticVariables.removeAll(simpleOccurVariables);
		if(!arithmeticVariables.isEmpty()) {
			gen.addSort("nat", sortNameToExpression.get("nat"), true);
			Pair<ArrayList<String>,ArrayList<String>> unrestrictedArithmVariablesLists=splitLocalGlobalVariables(arithmeticVariables);
			//add some #nat atoms to the body:
			for(int i=0;i<unrestrictedArithmVariablesLists.first.size();i++)
			{
			       newSortAtoms.add(createSortAtom(sortRenaming.get("nat"), new ASTterm(unrestrictedArithmVariablesLists.first.get(i))));
			}
		    if(!unrestrictedArithmVariablesLists.second.isEmpty()) {
		    	//add some nat atoms to the body of local elements
		    	 addNatAtomsForLocalVariables(rule,unrestrictedArithmVariablesLists.second);
		    }
		}
	}
     /**
      * Split the set of variables into two array lists of local and global variables
      * @return Pair<ArrayList<String>, ArrayList<String> > 
      * the pair of lists. the first list will contain global variables
      * and the second list will contain local variables.
      */
	Pair<ArrayList<String>, ArrayList<String>> splitLocalGlobalVariables(HashSet<String> variables) {
		Pair<ArrayList<String>,ArrayList<String>> result=new Pair<ArrayList<String>,ArrayList<String>>(new ArrayList<String>(), new ArrayList<String>());
		for(String varName:variables) {
			if(varName.endsWith("_G"))
			  result.first.add(varName);
			else 
			  result.second.add(varName);
		}
		return result;
	}
	/**
	 * Recursively search for aggregates and choice rules and add #nat atoms for corresponding 
	 * bodies
	 * @param n
	 */
	private void addNatAtomsForLocalVariables(SimpleNode n,ArrayList<String> variablesToAdd) {
		if(n.getId()==SparcTranslatorTreeConstants.JJTAGGREGATEELEMENT || 
				n.getId()==SparcTranslatorTreeConstants.JJTCHOICE_ELEMENT) {
			VariableFetcher vf=new VariableFetcher();
			HashSet<String> variablesInElement=new HashSet<String>();
			vf.fetchVariables(n,variablesInElement);
			for(String varName:variablesToAdd) {
				if(variablesInElement.contains(varName)) {
					ASTatom atomToAdd=createSortAtom(sortRenaming.get("nat"),new ASTterm(varName));
					ArrayList<ASTatom> atomListToAdd=new ArrayList<ASTatom>( Arrays.asList(atomToAdd));
					
					if(n.getId()==SparcTranslatorTreeConstants.JJTAGGREGATEELEMENT) {
						addAtomsToAggregateElement((ASTaggregateElement)n, atomListToAdd);
					}
					else {
						addAtomsToChoiceElement((ASTchoice_element)n, atomListToAdd);					}
				}
			}
		}
		
		for(int i=0;i<n.jjtGetNumChildren();i++) {
			addNatAtomsForLocalVariables((SimpleNode)n.jjtGetChild(i), variablesToAdd);
		}
	}
	/**
	 * Rename variables in the list according to provided mapping
	 */
	private void renameVariables(ArrayList<String> variables, HashMap<String,String> nameMapping) {
		for(int i=0;i<variables.size();i++) {
			variables.set(i,nameMapping.get(variables.get(i)));
		}
	}
	/**
	 * Create sort atom (consisting of sort name and one argument)
	 * 
	 * @param name
	 *            name of the new atom
	 * @param term
	 *            term to be inserted as an argument of the new atom
	 * @return created Atom
	 */
	ASTatom createSortAtom(String name, ASTterm term) {
		ASTatom atom = new ASTatom(SparcTranslatorTreeConstants.JJTATOM);
		ASTextendedNonRelAtom exatom = new ASTextendedNonRelAtom(
				SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM);
		exatom.image = "";
		atom.jjtAddChild(exatom, 0);
		ASTpredSymbol pred = new ASTpredSymbol(
				SparcTranslatorTreeConstants.JJTPREDSYMBOL);
		pred.image = name;
		pred.setPoundSign(true);
		exatom.jjtAddChild(pred, 0);
		ASTtermList termList = new ASTtermList(
				SparcTranslatorTreeConstants.JJTTERMLIST);
		exatom.jjtAddChild(termList, 1);
		termList.jjtAddChild(term, 0);
		return atom;
	}

	/**
	 * Retrieve appl atom for given rule name.
	 * 
	 * @param ruleName
	 *            of the form r_id(Var_1,Var_2,..Var_n).
	 * @return AST node for the new atom of the form appl(ruleName).
	 */
	ASTatom getApplAtom(String ruleName) {
		ASTatom atom = new ASTatom(SparcTranslatorTreeConstants.JJTATOM);
		ASTextendedNonRelAtom exatom = new ASTextendedNonRelAtom(
				SparcTranslatorTreeConstants.JJTEXTENDEDNONRELATOM);
		exatom.image = "";
		atom.jjtAddChild(exatom, 0);
		ASTpredSymbol pred = new ASTpredSymbol(
				SparcTranslatorTreeConstants.JJTPREDSYMBOL);
		pred.image = crAuxAtomName;
		exatom.jjtAddChild(pred, 0);
		ASTtermList mainList = new ASTtermList(
				SparcTranslatorTreeConstants.JJTTERMLIST);
		exatom.jjtAddChild(mainList, 1);
		ASTterm mainTerm = new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
		mainList.jjtAddChild(mainTerm, 0);
		ASTsymbolicTerm mainSymbTerm = new ASTsymbolicTerm(
				SparcTranslatorTreeConstants.JJTSYMBOLICTERM);
		mainTerm.jjtAddChild(mainSymbTerm, 0);

		if (ruleName.indexOf('(') != -1) { // there are variables in the rule
											// name

			// create term list containing the variables
			ASTtermList tlist = new ASTtermList(
					SparcTranslatorTreeConstants.JJTTERMLIST);
			// exatom.jjtAddChild(tlist, 1);
			String[] vars = ruleName.substring(ruleName.indexOf('(') + 1,
					ruleName.indexOf(')')).split(",");
			for (String var : vars) {
				ASTterm term = new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
				term.image = var;
				tlist.jjtAddChild(term, tlist.jjtGetNumChildren());
			}
			ASTsymbolicFunction func = new ASTsymbolicFunction(
					SparcTranslatorTreeConstants.JJTSYMBOLICFUNCTION);
			func.image = ruleName.substring(0, ruleName.indexOf('(') + 1);
			mainSymbTerm.jjtAddChild(func, 0);
			mainSymbTerm.jjtAddChild(tlist, 1);
		} else {
			ASTsymbolicConstant cons = new ASTsymbolicConstant(
					SparcTranslatorTreeConstants.JJTSYMBOLICCONSTANT);
			cons.image = ruleName;
			mainSymbTerm.jjtAddChild(cons, 0);
		}
		return atom;
	}

	/**
	 * Get rule name for the rule given by AST node
	 * 
	 * @param rule
	 * @return String, containing rule name of the form
	 *         r_id(Var_1,Var_2,...Var_n).
	 */
	private String getRuleName(ASTprogramRule rule) {
		String label = rule.getLabel();
		if (label.equals("")) {
			label = generateUniqueRuleLabel();
			rule.setLabel(label);
		}
		VariableFetcher vf = new VariableFetcher();
		HashSet<String> vars = vf.fetchVariables(rule);
		HashSet<String> varsToRemove = new HashSet<String>();
		for (String var : vars) {
			// remove local variables
			if (!var.endsWith("_G")) {
				varsToRemove.add(var);
			}
		}
		vars.removeAll(varsToRemove);
		String ruleName = label;
		if (vars.size() != 0) {
			ruleName += "(";
		}
		boolean first = true;
		for (String var : vars) {
			if (!first)
				ruleName += ",";
			first = false;
			ruleName += var;

		}
		if (vars.size() != 0) {
			ruleName += ")";
		}
		return ruleName;
	}

	/**
	 * Write new line symbol to output
	 */
	private void appendNewLineToTranslation() {
		String eol = System.getProperty("line.separator");
		appendStringToTranslation(eol);
	}

	/**
	 * Write new string to output
	 * 
	 * @param s
	 *            string to be written
	 */
	private void appendStringToTranslation(String s) {
		this.translatedOutput.append(s);
	}

	/**
	 * Write program from internal string buffer to output
	 */
	public void writeTranslatedProgram() {
		try {
			if (out != null && this.translatedOutput!=null) {
				out.write(this.translatedOutput.toString());
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Append suffix to all variables occurring in Abstract syntax subtree with
	 * root n.
	 * 
	 * @param n
	 *            root of the tree to be explored
	 * @param suffix
	 *            string to be added
	 */
	void appendToVariableNamesIn(SimpleNode n, String suffix,
			HashMap<String, String> originalNameMapping) {

		if (n.getId() == SparcTranslatorTreeConstants.JJTVAR) {
			originalNameMapping.put(n.image + suffix, n.image);
			n.image += suffix;
		} else

			for (int i = 0; i < n.jjtGetNumChildren(); i++) {
				appendToVariableNamesIn((SimpleNode) (n.jjtGetChild(i)),
						suffix, originalNameMapping);
			}

		if (n.getId() == SparcTranslatorTreeConstants.JJTTERM) {
			originalNameMapping.put(n.toString(false), n.toString(true));
		}
	}
}
