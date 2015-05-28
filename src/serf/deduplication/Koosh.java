package serf.deduplication;

import java.util.HashSet;
import java.util.Set;

import serf.data.MatcherMerger;
import serf.data.Record;

public class Koosh
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

	// +++ Need to remove below threshold records to begin with.
	public Set<Record> execute(MatcherMerger mm, Set<Record> records)
	{
		// If we're removing dominated records, create the index and add all records to it
		DominationIndex domIndex = _removeDominated ? new DominationIndex() : null;
		records = DominationIndex.prune(records, _threshold, domIndex);

		Set<Record> rprime = new HashSet<Record>();
int i = 0;
		while (!records.isEmpty())
		{
//		if (i % 50 == 0)
//			System.out.println("R size: " + records.size() + ", R' size: " + rprime.size());

			// Remove one element from R
			Record current = records.iterator().next();
			records.remove(current);
			
			Set<Record> buddies = new HashSet<Record>();
			for (Record r : rprime)
			{
				if (mm.match(current, r))
				{
					buddies.add(r);
				}
			}

			for (Record buddy : buddies)
			{
				// We know that they match, so merge them
				Record merged = mm.merge(current, buddy);
				
				// The logic is complicated.  We want to add the merged record if:
				//    its confidence is above threshold
				//    it is not the same as the current record, and is not already in rprime
				//    it is not dominated by other records (if we're removing dominated records)
				if (merged.getConfidence() > _threshold && 
						!rprime.contains(merged) &&
						!merged.equals(current) &&
						(domIndex == null || !domIndex.isDominated(merged)))
				{
					records.add(merged);
					if (domIndex != null)
						domIndex.add(merged);
				}
			}
			
			// We've compared with everything in rprime and created all the merged records, so
			// we add the current one to rprime.
			rprime.add(current);
			i++;
		}
		
		// Remove any dominated records we missed.
		if (domIndex != null)
		{
			HashSet<Record> dominated = new HashSet<Record>();
			for (Record r : rprime)
			{
				if (domIndex.isDominated(r, false))
				{
					dominated.add(r);
					domIndex.remove(r);
				}
			}
			rprime.removeAll(dominated);
		}
		
		
		return rprime;
	}
}
