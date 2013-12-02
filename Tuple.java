// ========================================================
// Tuple
// againessmith13, jpham14, mmillian15, cbottum15
// Fall 2013
// ========================================================

// ========================================================
// tuple data structure for paired data
public class Tuple<F,S> { 
// ========================================================
	
	public F first; 
	public S second; 
	
	// ====================================================
	// constructor
	public Tuple(F first, S second) { 
		this.first=first; 
		this.second=second; 
	}
	// ====================================================
	
	// ====================================================
	// getters and setters
	public F getFirst() {
		return first;
	}
	public void setFirst(F first) {
		this.first = first;
	}
	public S getSecond() {
		return second;
	}
	public void setSecond(S second) {
		this.second = second;
	} 
	//=====================================================
	
	public String toString(){
		return "<"+this.first.toString()+", "+this.second.toString()+">";
	}
// ========================================================
} //tuple
// ========================================================