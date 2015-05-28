package serf.data;



import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PackageRecord extends Record
{
	private Record[] _records;
		
	public PackageRecord(Map<String, Attribute> attrs, Record... records)
	{
		super(1.0, attrs);
		
		_records = records;
	}
	
	private void addBaseRecordsToSet(Set<Record> base)
	{
		for (Record r : _records)
		{
			if (r instanceof PackageRecord)
				((PackageRecord) r).addBaseRecordsToSet(base);
			else
				base.add(r);
		}
	}
	
	public Set<Record> getBaseSet()
	{
		Set<Record> base = new HashSet<Record>();
		addBaseRecordsToSet(base);

		return base;
	}
	
	
	public static Set<Record> getBaseRecords(Record r)
	{
		Set<Record> bs1;
		if (r instanceof PackageRecord)
			bs1 = ((PackageRecord)r).getBaseSet();
		else
		{
			bs1 = new HashSet<Record>();
			bs1.add(r);
		}
		
		return bs1;

	}
}
