// ========================================================
// Tuple
// againessmith13, jpham14, mmillian15, cbottum15
// Fall 2013
// ========================================================

// ========================================================
/**
 * The Tuple class holds a collection of exactly two 
 * objects, for situations where using a List is either
 * cumbersome, unclear, or could lead to undesired side-
 * effects.
 * 
 * @author mmillian15
 * 
 * @param <F>
 * @param <S>
 */
public class Tuple<F,S> { 
// ========================================================
	
	public F first; 
	public S second; 
	
	// ====================================================
	/**
	 * Constructs a newly allocated Tuple object.
	 * @param first
	 * @param second
	 */
	public Tuple(F first, S second) { 
		this.first=first; 
		this.second=second; 
	}
	// ====================================================
	
	// ====================================================
	/**
	 * Returns the first element in this Tuple.
	 * @return
	 */
	public F getFirst() {
		return first;
	}
	//=====================================================
	
	//=====================================================
	/**
	 * Replaces the first element in this Tuple with the 
	 * specified element.
	 * @param first
	 */
	public void setFirst(F first) {
		this.first = first;
	}
	//=====================================================
	
	//=====================================================
	/**
	 * Returns the second element in this Tuple.
	 * @return
	 */
	public S getSecond() {
		return second;
	}
	//=====================================================
	
	//=====================================================
	/**
	 * Replaces the second element in this Tuple with the 
	 * specified element.
	 * @param second
	 */
	public void setSecond(S second) {
		this.second = second;
	} 
	//=====================================================
	
	//=====================================================
	/**
	 * Returns a string representation of this Tuple. The 
	 * string representation consists of the string 
	 * representation of the first element and the second 
	 * element enclosed in triangle brackets ("<>")  and 
	 * separated by the characters ", ". Elements are 
	 * converted to strings as by their own toString() 
	 * method.
	 */
	public String toString(){
		return "<"+this.first.toString()+", "+this.second.toString()+">";
	}
// ========================================================
} //Tuple
// ========================================================