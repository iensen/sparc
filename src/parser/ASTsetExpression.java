/* Generated By:JJTree: Do not edit this line. ASTsetExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTsetExpression extends SimpleNode {
  public ASTsetExpression(int id) {
    super(id);
  }

  public ASTsetExpression(SparcTranslator p, int id) {
    super(p, id);
  }
  
  public String toString() {
	  return ((ASTadditiveSetExpression)this.jjtGetChild(0)).toString();
  }

}
/* JavaCC - OriginalChecksum=2a1832d95a4a4ef8ae280ebdd6a11c7c (do not edit this line) */