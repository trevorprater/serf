package serf.data;

/**
 * 
 * Compares two prices
 *
 */
public class PriceMatcher implements AtomicMatch {

	float threshold = 0.33F;
	public PriceMatcher(float thresh) {
		threshold = thresh;
	}

	public PriceMatcher() {}
	
	/**
	 * Compare two prices.
	 * Note: While both parameters are of type String, their values should be
	 * a sequence of characters that represnt floating point numbers (e.g "1.20").
	 * @param s1 price 1
	 * @param s2 price 2
	 * @return true if min_price/max_price > threshold (threshold defaults to 0.33)
	 */
	public boolean valuesMatch(String s1, String s2) {

		float f1 = Float.parseFloat(s1);
		float f2 = Float.parseFloat(s2);
		
		float sim = Math.min(f1,f2)/Math.max(f1,f2);
		
		if (sim > threshold)
			return true;
		return false;
	}

}
