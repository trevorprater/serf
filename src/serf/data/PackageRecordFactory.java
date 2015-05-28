package serf.data;



import java.util.Map;

public class PackageRecordFactory implements RecordFactory
{
	public Record create(double confidence, Map<String, Attribute> attrs, Record r1, Record r2)
	{
		return new PackageRecord(attrs, r1, r2);
	}
}
