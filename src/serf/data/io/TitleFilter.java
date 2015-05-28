package serf.data.io;

import serf.data.Attribute;
import serf.data.Record;

import java.util.Iterator;
import java.util.regex.*;

public class TitleFilter extends RecordFilter {
	
	public String regex;
	public Pattern pat;
	

	/* 
	 * pass in pattern that must match part of title.  Case in pattern will be ignored.  
	 */
	public TitleFilter(String regex)
	{
		this.regex = regex;
		pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);		
	}
	
	public boolean includeRecord(Record r) {
	
		Attribute attr = r.getAttribute("title");
		if (attr == null) return false;
		
		Iterator ai = attr.iterator();
		while (ai.hasNext())
		{
			String title = (String)ai.next();
			Matcher m = pat.matcher(title);
			if (m.find())
				return true;
		}
		
		return false;
	}

}
