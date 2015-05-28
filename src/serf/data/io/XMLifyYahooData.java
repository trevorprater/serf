package serf.data.io;

import serf.data.Attribute;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import serf.data.Record;
import java.util.Collections;

public class XMLifyYahooData extends ParseYahooData {

	FileWriter fw;
	String xmlOutputFile;
	
	final static String RECORD_SET_OPEN = "<recordSet>\n";
	final static String RECORD_SET_CLOSED = "</recordSet>";
	
	public static void main(String[] args)
	throws Exception
	{
		if  (args.length < 2)
		{
			System.err.println("usage: XMLifyYahooData {src}+ {output}");
			System.exit(1);
	
		}
		
		String[] srcs = new String[args.length - 1];
		System.arraycopy(args, 0, srcs, 0, args.length-1);
		XMLifyYahooData xyd = new XMLifyYahooData(srcs, args[args.length-1]);
		
	}
	
	public XMLifyYahooData(String[] srcs, String xmlOutputFile) throws IOException 
	{
		super(srcs);
		this.xmlOutputFile = xmlOutputFile;

		parseDataSources();
	}
	
	@Override
	protected void parseBegin()
	throws IOException
	{	
		fw = new FileWriter(xmlOutputFile);
		openRecordSet(fw);
	}
	
	protected void parseEnd()
	throws IOException
	{
		closeRecordSet(fw);
		fw.close();
	}
	
	public static void openRecordSet(Writer fw) 
	throws IOException
	{
		fw.write(RECORD_SET_OPEN);
	}
	
	public static void closeRecordSet(Writer fw) 
	throws IOException
	{
		fw.write(RECORD_SET_CLOSED);
	}
	
	public static void serializeRecord(Record r, Writer fw)
	throws IOException
	{
		final String RECORD_OPEN = "<record>\n";
		final String RECORD_CLOSE = "</record>\n";
		final String ATTR_OPEN = "<attribute>\n";
		final String ATTR_CLOSE = "</attribute>\n";
		final String NAME_OPEN = "<name>";
		final String NAME_CLOSE = "</name>\n";
		final String VALUE_OPEN = "<value>";
		final String VALUE_CLOSE = "</value>\n";
		Map<String, Attribute> attrs = r.getAttributes();
		
		fw.write(RECORD_OPEN);
		Iterator<String> it = attrs.keySet().iterator();
		while (it.hasNext())
		{
			//fw.write("\t"+ATTR_OPEN);
			String key = it.next();
			Attribute attr = attrs.get(key);
			//Omar: Made the name an XML attribute
			fw.write("\t" + "<attribute name=\"" + key + "\">\n");
			//fw.write("\t\t"+NAME_OPEN + key + NAME_CLOSE);
			Iterator<String> vIt = attr.iterator();
			while(vIt.hasNext())
			{
				String value = vIt.next();
				fw.write("\t\t"+VALUE_OPEN+escape(value) + VALUE_CLOSE);
			}
			fw.write("\t"+ATTR_CLOSE);
		}
		fw.write(RECORD_CLOSE);
		
	}
	
	@Override
	protected void action(Record r)
	throws IOException
	{		
		serializeRecord(r, fw);
	}

	static String escape(String in)
	{
		in = in.replaceAll("& ", "&amp; ");
		in = in.replaceAll("\"", "&quot;");
		in = in.replaceAll("'", "&apos;");
		
		return in;
	}
	
}
