package serf.deduplication;

import serf.data.Attribute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import serf.data.Record;

public class DominationIndex
{
	private Map <Attribute, Set<Record>> _map;
	
	public DominationIndex()
	{
		_map = new HashMap <Attribute, Set<Record>>();
	}
	
	public void add(Record r)
	{
		Map<String, Attribute> attrs = r.getAttributes();
		
		for (Map.Entry<String, Attribute> entry : attrs.entrySet())
		{
			String attrName = entry.getKey();
			Attribute values = entry.getValue();
			for (String value : values)
			{
				add(attrName, value, r);
			}
		}
	}
	
	public void remove(Record r)
	{
		Map<String, Attribute> attrs = r.getAttributes();
		
		for (Map.Entry<String, Attribute> entry : attrs.entrySet())
		{
			String attrName = entry.getKey();
			Attribute values = entry.getValue();
			for (String value : values)
			{
				remove(attrName, value, r);
			}
		}
	}
	
	public boolean isDominated(Record r)
	{
		return isDominated(r, true);
	}
	
	public boolean isDominated(Record r, boolean allowSelf)
	{
		Map<String, Attribute> attrs = r.getAttributes();
		Set<Record> dominators = null; 
		
		for (Map.Entry<String, Attribute> entry : attrs.entrySet())
		{
			String attrName = entry.getKey();
			Attribute values = entry.getValue();
			for (String value : values)
			{
				Attribute attr = new Attribute(attrName, value);
				Set<Record> attrHolders = _map.get(attr);
				// If nothing has that attribute/value, then we're not dominated.
				if (attrHolders == null)
					return false;

				// Take the intersection of the dominators and attrHolders sets
				// (a null dominators set represents the universe of all records)
				if (dominators == null)
				{
					dominators = new HashSet<Record>();
					
					// Add all the ones that have greater confidence.
					for (Record attrHolder : attrHolders)
					{
						if (attrHolder.getConfidence() >= r.getConfidence())
							dominators.add(attrHolder);
					}

					if (!allowSelf)
						dominators.remove(r);
				}
				else
				{
					dominators.retainAll(attrHolders);
				}

				if (dominators.isEmpty())
					return false;
			}
		}
		
		return true;
	}
	
	
	private void remove(String attrName, String value, Record r)
	{
		Attribute attr = new Attribute(attrName, value);
		Set<Record> records = _map.get(attr);
		records.remove(r);
		if (records.isEmpty())
		{
			_map.remove(attr);
		}
	}
	
	private void add(String attrName, String value, Record r)
	{
		Attribute attr = new Attribute(attrName, value);
		Set<Record> records = _map.get(attr);
		if (records != null)
		{
			records.add(r);
		}
		else
		{
			records = new HashSet<Record>();
			records.add(r);
			_map.put(attr, records);
		}
	}
	
	public static Set<Record> prune(Set<Record> records, double threshold, DominationIndex domIndex)
	{
		if (domIndex != null)
		{
			for (Record r : records)
				domIndex.add(r);
		}
		
		// Create a new set prunedRecords that has records that are above threshold and not dominated
		Set<Record> prunedRecords = new HashSet<Record>(); 
		for (Record r : records)
		{
			if (r.getConfidence() > threshold &&
					(domIndex == null || !domIndex.isDominated(r, false)))
			{
				prunedRecords.add(r);
			}
			else
			{
				if (domIndex != null)
					domIndex.remove(r);
			}
		}

		return prunedRecords;
	}
}
