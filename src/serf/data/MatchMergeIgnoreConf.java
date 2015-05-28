package serf.data;

public class MatchMergeIgnoreConf extends SimpleMatcherMerger {

	public MatchMergeIgnoreConf(RecordFactory factory, int k)
	{
		super(factory, 0, k);
	}
	/**
	 * @param factory
	 * @param d - the domain of possible values  [1, domain].  Wraparounds will be detected and considered matches.
	 * @param k - the width of the matching range.  if k == 1, then only exact matches count.  if k == 2 then 0 
	 *            matches with -1, 0, and 1.
	 */
	public MatchMergeIgnoreConf(RecordFactory factory, int d, int k)
	{
		super(factory, d, k);
		
	}
	
	protected double calculateConfidence(double c1, double c2)
	{
		return 1.0;
	}
	
	
	
}
