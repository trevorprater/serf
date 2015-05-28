package serf.data;

/*
 * Test two attribute values for equality. 
 */
public class EqualityMatcher implements AtomicMatch{

	public EqualityMatcher() {
		super();

	}

	/**
	 * Test if two values are equal
	 * @param s1
	 * @param s2 
	 * @return true if parameters s1 and s2 are equal
	 */
	public boolean valuesMatch(String s1, String s2)
	{
		if (s1.equals(s2))
			return true;
		else
			return false;
	}

}
