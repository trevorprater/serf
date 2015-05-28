package serf.data;



import java.util.Iterator;
import com.wcohen.secondstring.*;
import java.util.Properties;

/**
 * 
 * Use this class to compare Record objects containing yahoo shopping data.
 * Specifically these records should contain the following attributes:
 * 
 * stprice - the price of the item being sold
 * spath - a string representing the item's category
 * title - the title of the item being sold
 *
 */
public class YahooMatcherMerger extends BasicMatcherMerger 
{
	
	PriceMatcher priceMatcher;
	TitleMatcher titleMatcher;
	
	public YahooMatcherMerger(Properties props)
	{
		_factory = new SimpleRecordFactory();
		String tt = props.getProperty("TitleThreshold");
		String pt = props.getProperty("PriceThreshold");
		
		float tf = tt == null ? 0.9F : Float.parseFloat(tt);
		float pf = pt == null ? 0.33F : Float.parseFloat(pt);
		
		titleMatcher = new TitleMatcher(tf);
		priceMatcher = new PriceMatcher(pf);
		
	}
	
	public YahooMatcherMerger(RecordFactory factory)
	{
		//VALUE OF SECOND ARGUMENT DOES NOT MATTER
		_factory = factory;
		
		titleMatcher = new TitleMatcher(0.9F);
		priceMatcher = new PriceMatcher(0.33F);
		
	}
	
	/**
	 * 
	 * @param factory a Record factory
	 * @param tt the title threshold
	 * @param pt the price threshold
	 */
	public YahooMatcherMerger(RecordFactory factory, float tt, float pt)
	{
		_factory = factory;
		
		titleMatcher = new TitleMatcher(tt);
		priceMatcher = new PriceMatcher(pt);
		
		
		
	}
	
	protected double calculateConfidence(double c1, double c2)
	{
		return 1.0;
	}
	
	protected boolean matchInternal(Record r1, Record r2)
	{
		
		ExistentialBooleanComparator equals = new ExistentialBooleanComparator(new EqualityMatcher());

	
		//spath
			Attribute sp1 = r1.getAttribute("spath");
			Attribute sp2 = r2.getAttribute("spath");
			
			if (sp1 != null && sp2 != null && 
					!equals.attributesMatch(sp1, sp2))
				return false;
		

			Attribute p1 = r1.getAttribute("stprice");
			Attribute p2 = r2.getAttribute("stprice");
		
			if (!ExistentialBooleanComparator.attributesMatch(p1, p2, priceMatcher))
				return false;


			Attribute t1 = r1.getAttribute("title");
			Attribute t2 = r2.getAttribute("title");
		
			return ExistentialBooleanComparator.attributesMatch(t1, t2, titleMatcher);
	
	}
}
