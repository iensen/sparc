package utilities;

/**
* Created with IntelliJ IDEA.
* User: iensen
* Date: 1/13/13
* Time: 7:46 PM
* To change this template use File | Settings | File Templates.
*/
 public class Pair<FIRST, SECOND>{

        public final FIRST first;
        public final SECOND second;

        public Pair(FIRST first, SECOND second) {
            this.first = first;
            this.second = second;
        }
}