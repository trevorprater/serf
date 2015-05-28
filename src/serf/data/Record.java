package serf.data;



import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import serf.utils.HashMix;

/**
 * 
 * Records are the fundamental object operated on by all of the SERF algorithms.
 * Records can contain attributies and confidences.  Not all SERF algorithms make use of Record confidences.  
 *
 */
public class Record
{
	private int _id;
	private double _confidence;
	private int _hashCode;
	private Map<String, Attribute> _attributes;
	
	private static int s_instancecount = 0;
	private static synchronized int nextInstanceId() 
	{
		return s_instancecount++;
	}
	
	private static Map<String, Attribute> setToMap(Set<Attribute> attributeSet)
	{
		Map<String, Attribute> map = new HashMap<String, Attribute>();

		// Turn the set into a map from "type" (attribute name) to Attribute (name and value)
		// We assume the Set has at most one attribute of a given type.
		for (Attribute a : attributeSet)
			map.put(a.getType(), a);

		return map;		
	}
	
	public int getID()
	{
		return _id;
	}
	
	public Record(double confidence, Set<Attribute> attributeSet)
	{
		this(nextInstanceId(), confidence, setToMap(attributeSet));
	}
	
	
	public Record(double confidence, Map<String, Attribute> attributes)
	{
		this(nextInstanceId(), confidence, attributes);
	}
	
	
	private Record(int id, double confidence, Map<String, Attribute> attributes)
	{
		_id = id;
		_confidence = confidence;
		_attributes = attributes;
		_hashCode = calculateHashCode();
	}
	
	public double getConfidence()
	{
		return _confidence;
	}
	
	public Map<String, Attribute> getAttributes()
	{
		return _attributes;
	}
	
	public Attribute getAttribute(String type)
	{
		return _attributes.get(type);
	}
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		for (Attribute a : _attributes.values())
		{
			buf.append(a);
			buf.append(", ");
		}
		return buf.toString();
	}
	
	/**
	 * value based record equality.
	 * @param o an object representing another record.
	 * @return true if two records contain the same confidence value and attribute set.  
	 */
	public boolean equals(Object o) 
    {
		if (o == this)
			return true;
		
    	if (!(o instanceof Record))
    		return false;
    	
    	Record that = (Record) o;
    		
    	// Make sure all the fields are the same, except _id
    	if (_confidence != that._confidence)
    		return false;
    	
    	// HashMap.equals() actually makes sure all the values in the map are equal().
    	if (!_attributes.equals(that._attributes))
    		return false;
    	
    	// If we've made it this far, then the confidence and attributes
    	// are the same.  So this and that are "equal".
    	return true;
    }
    
	public int hashCode()
	{
		return _hashCode;
	}
	
    public int calculateHashCode() 
    {
    	int hash = 0;
    	hash += _attributes.hashCode();
    	hash += _confidence * 10000000;
    	return HashMix.mix(hash);
    }
}
