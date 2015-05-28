package serf.data.storage.impl;

import serf.data.Attribute;

import java.util.HashSet;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import serf.data.Record;

public class YahooSAXContentHandler extends DefaultHandler2 {
	
	StringBuffer nameBuf = new StringBuffer();
	StringBuffer valueBuf = new StringBuffer();
	
	Set<Record> records = new HashSet<Record>();
	
	public static final int NAME = 1;
	public static final int VALUE= 2;
	
	protected int state = 0;
	protected Set<Attribute> attrSet; 
	protected Set<String> valueSet;
	
	public static final String RECORD_ELEM = "record";
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
	throws SAXException
	{
		if (qName.equals("recordSet"))
			return;
		else if (qName.equals("record"))
		{
			attrSet = new HashSet<Attribute>();
		}
		else if (qName.equals("attribute"))
		{
			valueSet = new HashSet<String>();
			String nv;
			//for parsing V3 format of our data.
			if ((nv=attributes.getValue("name")) != null)
			{
				nameBuf = new StringBuffer(nv);
			}
			
		}
		else if (qName.equals("name"))
		{
			nameBuf = new StringBuffer();
			state = NAME;
		}
		else if (qName.equals("value"))
		{
			valueBuf = new StringBuffer();
			state = VALUE;
		}
		
    }

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		StringBuffer buf = null;
		
		if (state == NAME)
			buf = nameBuf;
		else if (state == VALUE)
			buf = valueBuf;
		
		if (buf != null)
			buf.append(ch, start, length);
	}

	
	public void endElement(String namespaceURI, String localName, String qName)
     throws SAXException
     {
		if (qName.equals("record"))
		{
			Record r = new Record(1.0, attrSet);
			records.add(r);
		}
		else if (qName.equals("attribute"))
		{
			Attribute attr = new Attribute(nameBuf.toString());
			attr.addValues(valueSet);
			attrSet.add(attr);
		}
		else if (qName.equals("name"))
		{
			state = 0;
		}
		else if (qName.equals("value"))
		{
			valueSet.add(valueBuf.toString());
			state = 0;
		}
     }
	
	public Set<Record> getRecords()
	{
		return records;
	}
}
