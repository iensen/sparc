/* Generated By:JJTree: Do not edit this line. QASTadditiveArithmeticTerm.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=QAST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package querying.parsing.query;



public
class QASTadditiveArithmeticTerm extends SimpleNode {
  public QASTadditiveArithmeticTerm(int id) {
    super(id);
  }

  public QASTadditiveArithmeticTerm(QueryParser p, int id) {
    super(p, id);
  }
  
  public String toString() {
	  String result="";
	  for(int i=0;i<this.jjtGetNumChildren();i++) {
		  if(i!=0 || i==0 && this.image.charAt(0)=='-') {
			  result+=image.charAt(i);
		  }
		  result+=((SimpleNode)(this.jjtGetChild(i))).toString();
	  }
	  return result;
  }
  
	
}
/* JavaCC - OriginalChecksum=1b2925c768949163b666b160862fd249 (do not edit this line) */