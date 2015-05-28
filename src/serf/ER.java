package serf;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import serf.data.Record;
import serf.data.SimpleRecordFactory;
import serf.data.YahooMatcherMerger;
import serf.data.io.XMLifyYahooData;
import serf.data.storage.impl.GetRecordsFromYahooXML;
import serf.deduplication.RSwoosh;
import serf.test.TestException;

import java.util.Properties;
import java.io.*;
import java.lang.reflect.*;


public class ER {
	static String fileSource = "products.xml";
	static String configFile = "example.conf";
	static String outputFile = null;
	static Class matcherMerger;
	static Class algorithm;
	static final String MATCHER_MERGER_INTERFACE = "serf.data.MatcherMerger";
	static Properties properties = new Properties();	
	
	//main function reads in a property file which specifies FileSource, MatcherMerger, and Algorithm(probably next step)
	public static void main(String[] args)
	throws Exception
	{
		if (args.length > 0){
			configFile = args[0];
		}
		//Load properties from property file
		properties.load(new FileInputStream(configFile));
		//Read FileSource, MatcherMerger properties
		fileSource = properties.getProperty("FileSource");
		if (fileSource == null){
			throw (new TestException("No File Source specified!"));
		}
		matcherMerger = Class.forName(properties.getProperty("MatcherMerger"));
		if (matcherMerger == null){
			throw (new TestException("No MatcherMerger Class specified!"));
		}
		if (checkMatcherMergerInterface(matcherMerger) != true){
			throw (new TestException("Given MatcherMerger class does not implement SimpleMatcherMerger interface!"));
			
		}
		runRSwoosh();		
	}
	
	//Recursively checks if testClass or any of its ancestor class implements MathcerMerger interface
	private static boolean checkMatcherMergerInterface(Class testClass){
		Class[] interfaces = testClass.getInterfaces();
		Class superClass = testClass.getSuperclass();
		try{
			for (int i = 0; i < interfaces.length; i++){
				if (interfaces[i] == Class.forName(MATCHER_MERGER_INTERFACE)){
					return true;					
				}
			}
			if (superClass!= null && checkMatcherMergerInterface(superClass)){
				return true;	
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	
	private static void runRSwoosh() throws SAXException, IOException, ParserConfigurationException
	{	
		GetRecordsFromYahooXML yds = new GetRecordsFromYahooXML(fileSource);
		yds.parseXML();
		Set<Record> records = yds.getAllRecords();		
		Class[] mmPartypes = new Class[1];
		try{
			//Can we assume MatcherMerger's contructor will always take in RecordFactory object? 
			//TODO: JOHNSON
			mmPartypes[0] = Properties.class;
		    Constructor mmConstructor = matcherMerger.getConstructor(mmPartypes);
			Object matcherMerger = mmConstructor.newInstance(properties);
			System.out.println("Running RSwoosh on " + records.size() + " records.");
			Set<Record> result = RSwoosh.execute((serf.data.MatcherMerger)matcherMerger, records);
			System.out.println("After running RSwoosh, there are " + result.size() + " records.");
			
			if ((outputFile = properties.getProperty("OutputFile")) != null)
			{
				FileWriter fw = new FileWriter(outputFile);
				fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				XMLifyYahooData.openRecordSet(fw);
				for (Record r : result) {
					
					XMLifyYahooData.serializeRecord(r, fw);
				}	
				XMLifyYahooData.closeRecordSet(fw);
				fw.close();
			}

		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}
