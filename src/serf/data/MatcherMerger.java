package serf.data;

/**
 * 
 * MatcherMerger defines the interface for the match and merge operations.
 * 
 * Match and merge functions should obey the following four properties
 * 
 * idempotence
 * commutativity
 * associativity
 * representivity
 * 
 * See <i>Swoosh: A Generic Approach to Entity Resolution</i> for more information
 *
 */
public interface MatcherMerger
{
	
	/**
	 * Compare records.  If they match, return true.
	 * @param r1 Record 1
	 * @param r2 Record 2
	 * @return true if the records match.
	 */
	public boolean match(Record r1, Record r2);
	
	/**
	 * Merge two records into a single record.  Return the merged record
	 * @param r1 Record 1
	 * @param r2 Record 2
	 * @return the record resulting from merging Records 1 and 2.
	 */
	public Record merge(Record r1, Record r2);
	
	
}
