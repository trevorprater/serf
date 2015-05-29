package serf.deduplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import serf.data.MatcherMerger;
import serf.data.Record;

public class RSwoosh
{
	public static List<Record> execute(MatcherMerger mm, List<Record> recordsOrig)
	{
		List<Record> records = new ArrayList<Record>(recordsOrig);
		List<Record> rprime = new ArrayList<Record>();

		while (!records.isEmpty())
		{
			System.out.println("R size: " + records.size() + ", R' size: " + rprime.size());
		
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
