/* Generated By:JJTree: Do not edit this line. ASTconstantTermList.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTconstantTermList extends SimpleNode {
  public ASTconstantTermList(int id) {
    super(id);
  }

  public ASTconstantTermList(SparcTranslator p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SparcTranslatorVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public String toString() {
	  StringBuilder result=new StringBuilder();
	  for(int i=0;i<this.jjtGetNumChildren();i++) {
		  if(i!=0)
			  result.append(',');
		  result.append((ASTconstantTerm)this.jjtGetChild(i));
	  }
	  return result.toString();
  }
}
/* JavaCC - OriginalChecksum=1282260016371ecbaf0a0598bf7492a0 (do not edit this line) */