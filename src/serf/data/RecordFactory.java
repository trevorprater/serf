package serf.data;



import java.util.Map;

public interface RecordFactory
{
	Record create(double confidence, Map<String, Attribute> attrs, Record r1, Record r2);
}
