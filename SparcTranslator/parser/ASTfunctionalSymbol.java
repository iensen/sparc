/* Generated By:JJTree: Do not edit this line. ASTfunctionalSymbol.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTfunctionalSymbol extends SimpleNode {
  public ASTfunctionalSymbol(int id) {
    super(id);
  }

  public ASTfunctionalSymbol(SparcTranslator p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SparcTranslatorVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f7854528b2236c71998e10bc3096293d (do not edit this line) */
