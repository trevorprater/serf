package serf.utils;

public class HashMix
{
	/**
	 * Mix the bits around in an integer to make them more "random".
	 * This function comes from Thomas Wang:
	 *   http://www.concentric.net/~Ttwang/tech/inthash.htm
	 * @param key
	 * @return
	 */
	public static int mix(int key) 
	{
		key += ~(key << 15);
		key ^=  (key >>> 10);
		key +=  (key << 3);
		key ^=  (key >>> 6);
		key += ~(key << 11);
		key ^=  (key >>> 16);
		return key;
	}
}
