/* Generated By:JJTree: Do not edit this line. ASTcharacterRegularExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTcharacterRegularExpression extends SimpleNode {
  public ASTcharacterRegularExpression(int id) {
    super(id);
  }

  public ASTcharacterRegularExpression(SparcTranslator p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SparcTranslatorVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=88204958631ce7c47bc104f8d6260e89 (do not edit this line) */
