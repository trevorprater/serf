package serf.data;

import java.util.HashMap;
import java.util.Map;

public abstract class BasicMatcherMerger implements MatcherMerger {

	protected RecordFactory _factory;
	private long _matchCount = 0;
	private long _mergeCount = 0;
	
	private long _matchTime = 0;
	private long _mergeTime = 0;
	
	public BasicMatcherMerger() {
		super();
	
	}
	
	public boolean match(Record r1, Record r2)
	{
		long startTime = System.currentTimeMillis();
		boolean retval = false;

		try
		{
			_matchCount++;
			retval = matchInternal(r1, r2);
			return retval;
		}
		finally
		{
			_matchTime += System.currentTimeMillis() - startTime;
		}
	}
	
	protected boolean matchInternal(Record r1, Record r2) 
	{ 
		return false;
	}
	
	public Record merge(Record r1, Record r2)
	{
		long startTime = System.currentTimeMillis();
		try
		{
			_mergeCount++;
			return mergeInternal(r1, r2);
		}
		finally
		{
			_mergeTime += System.currentTimeMillis() - startTime;
		}
	}
	
	protected double calculateConfidence(double c1, double c2)
	{
		return c1*c2;
	}
	
	private Record mergeInternal(Record r1, Record r2)
	{
		// To guarantee idempotence
		if (r1.equals(r2))
			return r1;
		
		// Otherwise, just multiply the confidences and
		// union the attributes.
		double conf = calculateConfidence(r1.getConfidence(), r2.getConfidence());
	
		HashMap<String, Attribute> attrs = new HashMap<String, Attribute>();
		attrs.putAll(r1.getAttributes());
		
		for (Map.Entry<String, Attribute> entry : r2.getAttributes().entrySet())
		{
			String attrName = entry.getKey();
			Attribute newAttr = entry.getValue();
			
			Attribute oldAttr = attrs.get(attrName);
			if (oldAttr == null)
			{
				attrs.put(attrName, newAttr);
			}
			else
			{
				Attribute mergedAttr = new Attribute(oldAttr.getType());
				for (String val : oldAttr)
					mergedAttr.addValue(val);
				for (String val : newAttr)
					mergedAttr.addValue(val);
				attrs.put(attrName, mergedAttr);
			}
			
		}
				
		return _factory.create(conf, attrs, r1, r2);
	}
	
}
