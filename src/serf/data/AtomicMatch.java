package serf.data;

/**
 * AtomicMatch is defines an interface for comparing attribute values.
 */
public interface AtomicMatch {

	/**
	 * Determine whether or not two attribute values match.  
	 * Implementing classes are free to use any criteria to test for a match. 
	 * @param s1 parameter 1
	 * @param s2 parameter 2
	 * @return <code>true</code> if the parameters match.   
	 */
	public boolean valuesMatch(String s1, String s2);
}
