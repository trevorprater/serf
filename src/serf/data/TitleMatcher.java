package serf.data;

import com.wcohen.secondstring.Jaro;
import com.wcohen.secondstring.StringWrapper;

public class TitleMatcher implements AtomicMatch {

	float threshold = 0.9F;
	
	public TitleMatcher() {

	}

	public TitleMatcher(float th)
	{
		threshold = th;
	}
	
	/**
	 * Test if titles match
	 * In this implementation the jaro similarity metric is used to compare string values.
	 * Very similar strings will have a jaro score close to 1.  Different strings have a 
	 * jaro score close to 0.
	 * @param s1 title 1
	 * @param s2 title 2
	 * @return true if jaro.score(title1, title2) > threshold (threshold defaults to 0.9)
	 */
	public boolean valuesMatch(String s1, String s2) 
	{
		
		StringWrapper sw1 = new StringWrapper(s1);
		StringWrapper sw2 = new StringWrapper(s2);
	
		Jaro jro = new Jaro();
		double tmp = jro.score(sw1, sw2);

		if (tmp > threshold)
			return true;
		else
			return false;
		
	}

}
