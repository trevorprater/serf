package serf.deduplication;

import java.util.HashSet;
import java.util.Set;

import serf.data.MatcherMerger;
import serf.data.Record;

public class RSwoosh
{
	public static Set<Record> execute(MatcherMerger mm, Set<Record> recordsOrig)
	{
		Set<Record> records = new HashSet<Record>(recordsOrig);
		Set<Record> rprime = new HashSet<Record>();

		while (!records.isEmpty())
		{
			//System.out.println("R size: " + records.size() + ", R' size: " + rprime.size());
		
			// Remove one element from R
			Record current = records.iterator().next();
			records.remove(current);
			
			Record buddy = null;
			for (Record r : rprime)
			{
				if (mm.match(current, r))
				{
					buddy = r;
					break;
				}
			}
			
			if (buddy == null)
			{
				rprime.add(current);
			}
			else
			{
				rprime.remove(buddy);
				records.add(mm.merge(current, buddy));
			}
		}
		
		
		return rprime;
	}
	
	
}
