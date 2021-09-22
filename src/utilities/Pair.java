package utilities;

import java.util.Objects;

/**
* Created with IntelliJ IDEA.
* User: iensen
* Date: 1/13/13
* Time: 7:46 PM
* To change this template use File | Settings | File Templates.
*/
 public class Pair<FIRST, SECOND>{

        @Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		return Objects.equals(first, other.first) && Objects.equals(second, other.second);
	}

		public final FIRST first;
        public final SECOND second;

        public Pair(FIRST first, SECOND second) {
            this.first = first;
            this.second = second;
        }
}