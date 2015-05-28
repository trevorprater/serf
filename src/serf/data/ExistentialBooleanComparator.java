package serf.data;



import java.util.Iterator;

import com.wcohen.secondstring.StringWrapper;

/**
 * ExistentialBooleanComparator compares two attributes by comparing their attribute values.
 *
 */
public class ExistentialBooleanComparator {

	AtomicMatch valueMatcher;
	
	public ExistentialBooleanComparator(AtomicMatch am) {
		valueMatcher = am;
	}
	
	public boolean attributesMatch(Attribute p1, Attribute p2)
	{
		return attributesMatch(p1, p2, valueMatcher);
	}
	
	/**
	 * Compare two attributes. Return true if they match.
	 * attributesMatch takes the cross product of values from each Attribute and 
	 * invokes valuesMatch() on each pair.  It returns true if any invocation of valuesMatch
	 * returns true.
	 * @param p1 Attribute 1
	 * @param p1 Attribute 2
	 * @param vm an object that compares Attribute values.
	 * @return true if any pair of attribute values matches.
	 * Note: if either Attribute parameter is null this method returns false.
	 * 
	 */
	public static boolean attributesMatch(Attribute p1, Attribute p2, AtomicMatch vm)
	{
		if (p1 == null || p2 == null)
			return false;
		
		Iterator i1 = p1.iterator();
		
		while(i1.hasNext())
		{
			String s1 = (String)i1.next();
			Iterator i2 = p2.iterator();
			
			while(i2.hasNext())
			{
				String s2 = (String)i2.next();
				
				if (vm.valuesMatch(s1, s2))
					return true;
			}
		}
		
		return false;
		
	}

}
