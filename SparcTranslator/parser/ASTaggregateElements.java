/* Generated By:JJTree: Do not edit this line. ASTaggregateElements.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

import java.util.HashMap;

public
class ASTaggregateElements extends SimpleNode {
  public ASTaggregateElements(int id) {
    super(id);
  }

  public ASTaggregateElements(SparcTranslator p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SparcTranslatorVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  
  // disallow toString() call without sort renaming map
  @Override
  public String toString() {
	  throw new UnsupportedOperationException();
  }
  
  
  public String toString(HashMap<String,String> sortRenaming) {
	  StringBuilder result=new StringBuilder();
	  for(int i=0;i<this.jjtGetNumChildren();i++) {
		  ASTaggregateElement elem=(ASTaggregateElement)this.jjtGetChild(i);
		  if(i!=0) {
			  result.append(';');
		  }
		  result.append(elem.toString(sortRenaming));
	  }
	  return result.toString();
  }
}
/* JavaCC - OriginalChecksum=d4902c57faceab530731746220fb5ac4 (do not edit this line) */
