package serf.data.storage.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import serf.data.Record;
import serf.data.io.ParseYahooData;
import serf.data.io.RecordFilter;
import serf.data.storage.DataSource;

public class GetRecordsFromYahooData extends ParseYahooData implements DataSource {

	protected Set<Record> yahooRecords; 
	
	public GetRecordsFromYahooData(String[] srcs, RecordFilter rf, int maxcount) 
	throws IOException
	{
		super(srcs, rf);
		yahooRecords = new HashSet<Record>();
		
		parseDataSources(maxcount);
	}
	
	public GetRecordsFromYahooData(String[] srcs, RecordFilter rf) 
	throws IOException
	{
		super(srcs, rf);
		yahooRecords = new HashSet<Record>();
		
		parseDataSources();
	}
	
	
	public GetRecordsFromYahooData(String[] srcs) 
	throws IOException
	{
		super(srcs);
		yahooRecords = new HashSet<Record>();
		
		parseDataSources();
	}
	
	public GetRecordsFromYahooData(String[] srcs, int maxcount) 
	throws IOException
	{
		super(srcs);
		yahooRecords = new HashSet<Record>();
		
		parseDataSources(maxcount);
	}
	
	public GetRecordsFromYahooData(String[] srcs, int[] mcPerFile) 
	throws IOException
	{
		super(srcs);
		yahooRecords = new HashSet<Record>();
		
		parseDataSources(mcPerFile);
	}
	
	@Override
	protected void action(Record r) 
	throws IOException
	{
		yahooRecords.add(r);
	
	}

	public Iterator iterator()
	{
		return yahooRecords.iterator();
	}
	
	public Set<Record> getAllRecords()
	{
		return yahooRecords;
	}
	
}
