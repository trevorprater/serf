package serf.data.io;

import serf.data.Attribute;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import serf.data.Record;

public abstract class ParseYahooData {


	protected int recordCount=0;
	RecordFilter filter;
	protected String[] dataSrcs;
	
	public ParseYahooData(String srcs[], RecordFilter rf)
	throws IOException
	{
		dataSrcs = srcs;
		filter = rf;
	}
	
	public ParseYahooData(String srcs[])
	throws IOException
	{
		dataSrcs = srcs;
		filter = new RecordFilter();
	}
	
	protected void parseDataSources()
	throws IOException
	{
		parseDataSources(0);
	}
	
	protected int getRecords(BufferedReader br, int maxCount, int recordCount) throws IOException
	{
		String line;
		while ((line = br.readLine()) != null && 
				(maxCount == 0 || recordCount < maxCount))
		{
			Set<Attribute> aSet = new HashSet<Attribute>();
			Pattern p = Pattern.compile("<([^:]*):([^>]*)>");
			Matcher m = p.matcher(line);
			while (m.find())
			{
				String type = line.substring(m.start(1), m.end(1));
				String value = line.substring(m.start(2), m.end(2));				
				aSet.add(new Attribute(type, value));
				
			}
			Record r = new Record(1.0, aSet);
			if (filter.includeRecord(r))
			{
				recordCount++;
				action(r);
			}
		}
		br.close();
		
		return recordCount;
	}
	
	protected BufferedReader getReader(String src) throws IOException
	{
		BufferedReader br;
		//check file extension figure out if GZip file.
		if (src.endsWith(".gz") || src.endsWith(".gzip"))
			br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(src))));
		else
			br = new BufferedReader(new FileReader(src));
		
		return br;

	}

	protected void parseDataSources(int[] maxPerFile)
	throws IOException
	{
		parseBegin();
		
		for (int i=0;i<dataSrcs.length;i++)
		{
			
			BufferedReader br = getReader(dataSrcs[i]);
			
			getRecords(br, maxPerFile[i], recordCount);			
		}
		
		parseEnd();
	}

	
	protected void parseDataSources(int maxRecordCount)
	throws IOException
	{
		parseBegin();
		
		for (String src : dataSrcs)
		{
			
			BufferedReader br = getReader(src);
			
			//go through file. 
			recordCount+=getRecords(br, maxRecordCount, recordCount);			
		}
		
		parseEnd();
	}

	protected void parseBegin() throws IOException {}
	protected void parseEnd() throws IOException {}
	protected abstract void action(Record r) throws IOException;

}
