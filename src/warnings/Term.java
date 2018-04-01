package warnings;

import java.util.ArrayList;
import java.util.HashSet;

import parser.ASTadditiveArithmeticTerm;
import parser.ASTarithmeticTerm;
import parser.ASTterm;
import parser.ASTvar;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;
import typechecking.TermEvaluator;

public class Term extends Operand{
  ASTterm tree;
  
  public Term(ASTvar var) {
	  ASTterm term=new ASTterm(SparcTranslatorTreeConstants.JJTTERM);
	  term.jjtAddChild(var, 0);
	  this.tree=term;
  }
  
  
  public Term(ASTterm term)
  {
	  evaluateAllArithmetics(term);
	  tree=term;
  }
  
  public Term(long number) {
	     tree= new ASTterm(number);
  }
  
  public  Term(String recordName, ArrayList<String> varArguments) {
	 tree=new ASTterm(recordName, varArguments);
  }
  
  
  
  
  public boolean isGround() {
	  return tree.isGround();
  }
  
  
  private void evaluateAllArithmetics(SimpleNode n) {
	  if(n.getId()==SparcTranslatorTreeConstants.JJTARITHMETICTERM) {
		  TermEvaluator te=new TermEvaluator((ASTarithmeticTerm)n);
		  if(te.isEvaluable()) {
			  try {
				ASTadditiveArithmeticTerm term=new ASTadditiveArithmeticTerm(te.evaluate());
				n.removeChildren();
				n.jjtAddChild(term, 0);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
		  }
		  return;
	  }
	  for(int i=0;i<n.jjtGetNumChildren();i++) {
		  evaluateAllArithmetics((SimpleNode)n.jjtGetChild(i));
	  }
  }
  
  public HashSet<String>fetchArithmeticVariables() {
	  HashSet<String> arithmeticVars=new HashSet<String>();
	  fetchArithmeticVariables(tree, arithmeticVars);
	  return arithmeticVars;
  }
  
  public HashSet<String> fetchVariables() {
	  HashSet<String> Vars=new HashSet<String>();
	  fetchVariables(tree,Vars);
	  return Vars;
  }
  
  
  private void fetchArithmeticVariables(SimpleNode n,HashSet<String> arithmeticVars) {

	if(n.getId()==SparcTranslatorTreeConstants.JJTARITHMETICTERM) {
		String nstr=n.toString();
		if(nstr.indexOf('+')!=-1 || nstr.indexOf('-')!=-1)
		{
			fetchVariables(n,arithmeticVars);
		}
	}
	else {
		 for(int i=0;i<n.jjtGetNumChildren();i++) {
			  fetchArithmeticVariables((SimpleNode)n.jjtGetChild(i),arithmeticVars);
		  }
	}
  }
  
  private void fetchVariables(SimpleNode n, HashSet<String> Vars) {
	  if(n.getId()==SparcTranslatorTreeConstants.JJTVAR) {
		  Vars.add(n.toString());
	  }
	  else {
		  for(int i=0;i<n.jjtGetNumChildren();i++) {
			  fetchVariables((SimpleNode)n.jjtGetChild(i),Vars);
		  }
	  }
  }
  
  public String toClingConArithmeticString() {
	  uncapVariables(tree,null);
	  String result= tree.toString();
	  capVariables(tree,null);
	  StringBuilder resultStr=new StringBuilder();
	  for (char c:result.toCharArray()) {
		  switch(c){
		   case '+':resultStr.append("$+");break;
		   case '-':resultStr.append("$-");break;
		   case '*':resultStr.append("$*");break;
		   case '/':resultStr.append("$/-");break;
		   default:
			   resultStr.append(c);
		  }
	  }
	  return resultStr.toString();
  }
  
  public String toString() {
	  return tree.toString();
  }
  
  public void capVariables(HashSet<String> variablesToCap) {
	  capVariables(tree,variablesToCap);
  }
  
  
  private void capVariables(SimpleNode n,HashSet<String> variablesToCap) {
	  if(n.getId()==SparcTranslatorTreeConstants.JJTVAR) {
		  if(variablesToCap==null || variablesToCap.contains(n.image.toUpperCase())) {
		        n.image=capVariable(n.image);
		  }
	  }
	  else 
		  for(int i=0;i<n.jjtGetNumChildren();i++) {
			  capVariables((SimpleNode)n.jjtGetChild(i),variablesToCap);
		  }
  }
  public void uncapVariables(HashSet<String> variablesToUncap) {
	uncapVariables(tree,variablesToUncap);  
  }
  
  private void uncapVariables(SimpleNode n,HashSet<String> variablesToUncap) {
	  if(n.getId()==SparcTranslatorTreeConstants.JJTVAR) {
		  if(variablesToUncap==null || variablesToUncap.contains(n.image)) {
		      n.image=uncapVariable(n.image);
		  }
	  }
	  else 
		  for(int i=0;i<n.jjtGetNumChildren();i++) {
			  uncapVariables((SimpleNode)n.jjtGetChild(i),variablesToUncap);
		  }
  }
  
  
  public static String uncapVariable(String var) {
		 String uncapVar=Character.toLowerCase(var.charAt(0))+var.substring(1);
		 return uncapVar;
  }
  
  public static String capVariable(String var) {
		 String capVar=Character.toUpperCase(var.charAt(0))+var.substring(1);
		 return capVar;
  }
	
  boolean isPrimitiveArithmetic(HashSet<String> arithmeticVariables) {
	  boolean hasAllArithmeticVariables = true;
		HashSet<String> variables = this.fetchVariables();
		for (String variable : variables) {
			if (!arithmeticVariables.contains(variable)) {
				hasAllArithmeticVariables = false;
			}
		}
		return hasAllArithmeticVariables
				&& this.toString().indexOf('(') == -1; 
  }
  
  boolean isVariable() {
	  return tree.isVariable();
  }
  
  public boolean isRecord() {
	  return tree.isRecord();
  }
}
