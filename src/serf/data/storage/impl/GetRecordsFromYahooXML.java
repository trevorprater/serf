package serf.data.storage.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import serf.data.Record;
import serf.data.storage.DataSource;

public class GetRecordsFromYahooXML implements DataSource {

	XMLReader xmlReader;
	InputSource input;
	YahooSAXContentHandler yahooContentHandler = new YahooSAXContentHandler();
	
	public GetRecordsFromYahooXML(InputSource is) throws SAXException, 
	ParserConfigurationException
	{
		input = is;
		init();
	}
	
	public GetRecordsFromYahooXML(String fileSrc) throws SAXException, 
	ParserConfigurationException, IOException
	{
		FileReader fr = new FileReader(fileSrc);
		input = new InputSource(fr);
		init();
	}
	
	public void parseXML() throws SAXException, IOException
	{
		xmlReader.parse(input);
	}
	
	/* (non-Javadoc)
	 * @see serf.data.DataSource#getAllRecords()
	 */
	public Set<Record> getAllRecords()
	{
		return yahooContentHandler.getRecords();
		
		//LEFT OFF HERE.  ADD TO EACH RECORD AN ATTRIBUTE THAT STORES IT'S HASH VALUE.
		//WILL USE THIS ATTRIBUTE TO CHECK IT'S ORIGIN
		//CONSIDER ADDING A VALUE THAT MAKES IT EASY TO EVENLY DISTRIBUTE RECORDS TO 
		//MULTIPLE PEERS. COULD JUST BE A COUNTER.
	}
	
	/* (non-Javadoc)
	 * @see serf.data.DataSource#iterator()
	 */
	public Iterator iterator()
	{
		return yahooContentHandler.getRecords().iterator();
	}
	
	protected void init() throws SAXException, ParserConfigurationException
	{
		 //create SAXReader                        
        SAXParserFactory spf = SAXParserFactory.newInstance();
        xmlReader = spf.newSAXParser().getXMLReader();
        xmlReader.setContentHandler(yahooContentHandler);
	}
	
	
	
}
