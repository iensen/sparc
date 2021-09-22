package typechecking;
import java.util.Objects;

import utilities.Pair;

public class RecordInfo {
   
    @Override
	public int hashCode() {
		return Objects.hash(data);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordInfo other = (RecordInfo) obj;
		return Objects.equals(data, other.data);
	}
	Pair<String, Integer> data;   
	public RecordInfo(String recordName, int arity) {
	   data = new Pair<String, Integer>(recordName, arity);
    }
     
}
