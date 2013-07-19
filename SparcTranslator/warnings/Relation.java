package warnings;

public enum Relation {
 less("<"),
 greater(">"),
 lesseq("<="),
 greatereq(">="),
 eqasgn("="),
 eqrel("=="),
 noteq("!=");
 String sign;
 Relation(String sign){
	 this.sign=sign;
 }
 
 public String toString(){
     return sign;
  }
 
 public static Relation getOppositeRelation(Relation rel) {
	 switch(rel) {
	   case eqrel:return noteq;
	   case eqasgn:return noteq;
	   case greatereq: return less;
	   case less: return greatereq;
	   case greater: return lesseq;
	   case lesseq: return greater;
	   case noteq: return eqrel;
	   default: return null;
	 }
 }
}
