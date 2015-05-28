package serf.data.io;

import java.util.Set;

import serf.data.Record;
import serf.data.storage.impl.GetRecordsFromYahooData;
import serf.data.storage.impl.PrintRecordsFromYahooData;

public class ExtractRecordsByTitle 
{
	String titlePat = "\\bipod\\b";
	
	/*static String[] srcs = { 
		"/dfs/dbhome2/4/telarson/yahooData/product.t2g_6_0",
		"/dfs/dbhome2/4/telarson/yahooData/product.t2g_6_4",
		};
	*/
	static String[] srcs = { 
		"/mnt/cdrom/yahoo_data/product.t2ka_1_0.gz",
		//"/mnt/cdrom/yahoo_data/product.t2g_6_0.gz",
		//"/mnt/cdrom/yahoo_data/product.t2g_6_1.gz",
		//"/mnt/cdrom/yahoo_data/product.t2g_6_2.gz",
		//"/mnt/cdrom/yahoo_data/product.t2g_6_3.gz",
		//"/mnt/cdrom/yahoo_data/product.t2g_6_4.gz", 
		};
	
	public static void main(String[] args)
	throws Exception
	{
		ExtractRecordsByTitle erbt = new ExtractRecordsByTitle();
		
		if (args.length >= 1)
			erbt.titlePat = args[0];
		if (args.length > 1)
		{
			String[] srcs2 = new String[args.length -1];
			System.arraycopy(args,0,srcs2,0, args.length -1);
			erbt.srcs = srcs2;
		}		
		
		erbt.getMatchingRecords();
	}
	
	public void getMatchingRecords() throws Exception
	{
		for (String src: srcs)
		{
			String[] imp = {src};
			
			TitleFilter tf = new TitleFilter(titlePat);
			PrintRecordsFromYahooData yds = new PrintRecordsFromYahooData(imp, tf);

		}
	}
}
