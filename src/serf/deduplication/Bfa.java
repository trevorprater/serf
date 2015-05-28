package serf.deduplication;

import java.util.HashSet;
import java.util.Set;

import serf.data.MatcherMerger;
import serf.data.Record;

public class Bfa
{
	private double _threshold = -1.0;
	private boolean _removeDominated = false;
	
	public void setThreshold(double threshold)
	{
		_threshold = threshold;
	}
	
	public void setDomination(boolean removeDominated)
	{
		_removeDominated = removeDominated;
	}
	
	public Set<Record> execute(MatcherMerger mm, Set<Record> records)
	{
		// If we're removing dominated records, create the index and add all records to it
		DominationIndex domIndex = null;
		if (_removeDominated)
		{
			domIndex = new DominationIndex();
			for (Record r : records)
				domIndex.add(r);
		}
		
		// Create a new set prunedRecords that has records that are above threshold and not dominated
		Set<Record> prunedRecords = new HashSet<Record>(); 
		for (Record r : records)
		{
			if (r.getConfidence() > _threshold
					&& (domIndex == null || !domIndex.isDominated(r, false)))
			{
				prunedRecords.add(r);
			}
			else
			{
				if (domIndex != null)
					domIndex.remove(r);
			}
		}
		
		records = prunedRecords;
		
		Set<Record> rprime = new HashSet<Record>();		
		int i = 0;
		do
		{
			records.addAll(rprime);
			for (Record r1 : records)
			{
				for (Record r2 : records)
				{
					if (mm.match(r1, r2))
					{
						Record merged = mm.merge(r1, r2);
						if (merged.getConfidence() > _threshold 
								&& (domIndex == null || !domIndex.isDominated(merged)))
						{
							rprime.add(merged);
							if (domIndex != null)
								domIndex.add(merged);
						}
					}
				}
			}
			i++;
			System.out.println("round complete: r.size(): " + records.size() + " rprime.size(): " + rprime.size());
		}
		while (!records.containsAll(rprime));

		// Remove any dominated records we missed.
		if (domIndex != null)
		{
			HashSet<Record> dominated = new HashSet<Record>();
			for (Record r : records)
			{
				if (domIndex.isDominated(r, false))
				{
					dominated.add(r);
					domIndex.remove(r);
				}
			}
			records.removeAll(dominated);
		}

		return records;
	}
}
