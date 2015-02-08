package preprocessing;

public class TF implements Comparable<TF>{

	public String keyword;
	public int count;
	
	public TF(String k, int c){
		keyword=k;
		count=c;
	}

	public int compareTo(TF o) {
		return o.count-this.count;
	}
}
