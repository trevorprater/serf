package serf.data;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import serf.utils.HashMix;

/**
 * This class represents the attributes associated with a record.
 * An attribute has a type and one or more values associated with it.
 */
public class Attribute implements Cloneable, Iterable<String> {

    /** String representation of the attribute type */
    private String _type;

    /** The value of the attribute */
    private Set<String> _values;

    public Attribute(String type) {
        _type = type;
        _values = new HashSet<String>();
    }
    
    public Attribute(String type, String... values) {
        this(type);
        
        for (String value : values)
            this.addValue(value);
    }

    public Attribute clone() {
        try {
        	Attribute a = (Attribute)super.clone();
        	// we do not need to clone the values, they are
        	// strings, so they are immutable
        	a._values = new HashSet<String>(_values);
        	return a;
        } catch (CloneNotSupportedException e) {
        	// Attribute implements clonable, so we will never have 
        	// any exception
        	return null;
        }
    }

    public String toString() {
        String data = getType() + ": ";
        for (String value : this) 
            data += value + "; ";
        
        return data;
    }

    /**
     * Get the type of this attribute.
     * 
     * @return The attribute type as given by the data source.
     */
    public String getType() {
        return _type;
    }
    
    /**
     * Sets the attribute type
     * @param type The new attribute type
     */
    public void setType(String type) {
        _type = type;
    }
    
    /**
	 * Retrieves an iterator for this attribute's values.
	 * 
	 * @return Attribute values iterator.
	 */
	public Iterator<String> iterator() {
		return _values.iterator();
	}

	/**
	 * Gets the number of values for this attribute
	 * 
	 * @return The number of values for this attribute
	 */
	public int getValuesCount() {
		return _values.size();
	}

    /**
	 * Adds a value for this attribute.
	 * 
	 * @param value The new attribute value.
	 */
    public void addValue(String text) {
    	if (text == null || text.equals(""))
    		return;
    	
        _values.add(text);
    }
    
    /**
     * Adds a set of values to this attribute
     * @param values the new values
     */
    public void addValues(Collection<String> values) {
        _values.addAll(values);
    }
    
    /**
     * Removes all the attribute values
     */
    public void clear() {
        _values.clear();
    }
    
    public int hashCode()
    {
    	int hash = 0;
    	hash += _type.hashCode();
    	hash += _values.hashCode();
    	return HashMix.mix(hash);
    }
    
    /**
     * Test <code>this</code> object for equality with another object.
     * @param object
     * @return true if <code>this</code> and <code>object</code> have the same type and value
     */
    public boolean equals(Object o) 
    {
    	if (o == this)
    		return true;
    	
    	if (!(o instanceof Attribute))
    		return false;
    	
    	Attribute that = (Attribute) o;
    	if (!_type.equals(that._type))
    		return false;
    	
    	if (!_values.equals(that._values))
    		return false;
    	
    	// the type and values are the same, so we are equal
    	return true;
    		
    }
}
