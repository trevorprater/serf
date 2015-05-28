package serf.data;



import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimpleMatcherMerger implements MatcherMerger
{
	private RecordFactory _factory;
	
	// Defined in the Swoosh paper.  k is the width of the matching range, d is the domain of possible values
	private int _d;
	private int _k;
	
	private long _matchCount = 0;
	private long _mergeCount = 0;
	
	private long _matchTime = 0;
	private long _mergeTime = 0;
	
	/**
	 * This creates a SimpleMatcherMerger that checks for proximity within k, but doesn't look for wraparound
	 * around a particular domain.
	 * @param factory
	 * @param k
	 */
	public SimpleMatcherMerger(RecordFactory factory, int k)
	{
		this(factory, 0, k);
	}
	/**
	 * @param factory
	 * @param d - the domain of possible values  [1, domain].  Wraparounds will be detected and considered matches.
	 * @param k - the width of the matching range.  if k == 1, then only exact matches count.  if k == 2 then 0 
	 *            matches with -1, 0, and 1.
	 */
	public SimpleMatcherMerger(RecordFactory factory, int d, int k)
	{
		_factory = factory;
		_d = d;
		_k = k;
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
		for (Map.Entry<String, Attribute> entry : r1.getAttributes().entrySet())
		{
			String attrName = entry.getKey();
			Attribute a1 = entry.getValue();
			Attribute a2 = r2.getAttribute(attrName);
			
			if (attrMatch(a1, a2))
				return true;
		}
		return false;
	}
	

	private boolean attrMatch(Attribute a1, Attribute a2)
	{
		if (a1.getValuesCount() == 0 || a2.getValuesCount() == 0)
			return false;
			
		double [] a1values = new double[a1.getValuesCount()];
		double [] a2values = new double[a2.getValuesCount()];

		Iterator<String> iter = a1.iterator();		
		for (int i = 0; iter.hasNext(); i++)
		{
			a1values[i] = Double.parseDouble(iter.next());
		}

		iter = a2.iterator();		
		for (int i = 0; iter.hasNext(); i++)
		{
			a2values[i] = Double.parseDouble(iter.next());
		}
		
		Arrays.sort(a1values);
		Arrays.sort(a2values);
		
		int i1 = 0, i2 = 0;
		
		// This loop searches for values within _k of each other.
		while (i1 < a1values.length && i2 < a2values.length)
		{
			// while the value in a1 is lower, keep moving to the next until you find one
			// within range or surpassing the value in a2
			while (i1 < a1values.length && a1values[i1] <= a2values[i2])
			{
				if (a1values[i1] + _k > a2values[i2])
					return true;
				i1++;
			}
			
			if (i1 >= a1values.length)
				break;
			
			// now do the same while the value in a2 is lower.
			while (i2 < a2values.length && a2values[i2] <= a1values[i1])
			{
				if (a2values[i2] + _k > a1values[i1])
					return true;
				i2++;
			}
		}
		
		// Don't check for wraparound if _d is 0.
		if (_d != 0)
		{
			// Now we need to check for values that are within _k of each other
			// due to wraparound
			if (a2values[0] < _k)
			{
				if (a1values[a1values.length - 1] + _k - 1 >= _d)
					if (((a1values[a1values.length - 1] + _k - 1) % _d) + 1 > a2values[0])
						return true;
			}
			if (a1values[0] < _k)
			{
				if (a2values[a2values.length - 1] + _k - 1 >= _d)				
					if (((a2values[a2values.length - 1] + _k - 1) % _d) + 1 > a1values[0])
						return true;				
			}
		}
		
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
	
	public long getMatchCount()
	{
		return _matchCount;
	}

	public long getMergeCount()
	{
		return _mergeCount;
	}
}
