package serf.data;



import java.util.Map;

public class SimpleRecordFactory implements RecordFactory
{
	public Record create(double confidence, Map<String, Attribute> attrs, Record r1, Record r2)
	{
		return new Record(confidence, attrs);
	}
}
