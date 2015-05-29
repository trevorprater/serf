package serf.data;

/**
 * Created by trevorprater on 5/28/15.
 */

import java.util.Properties;

/**
 *
 * Use this class to compare Record objects containing yahoo shopping data.
 * Specifically these records should contain the following attributes:
 *
 * price - the price of the item being sold
 * title - the title of the item being sold
 *
 * Note: this class is similar to YahooMatcherMerger. It works with serialized records of a slightly different format.
 *
 */
public class H1bMatcherMerger extends BasicMatcherMerger implements
        MatcherMerger {

    TitleMatcher titleMatcher;

    public H1bMatcherMerger(Properties props)
    {
        _factory = new SimpleRecordFactory();
        String tt = props.getProperty("TitleThreshold");

        float tf = tt == null ? 0.9F : Float.parseFloat(tt);

        titleMatcher = new TitleMatcher(tf);

    }

    protected double calculateConfidence(double c1, double c2)
    {
        return 1.0;
    }

    public H1bMatcherMerger(RecordFactory factory)
    {
        _factory = factory;
        titleMatcher = new TitleMatcher(0.9F);
    }

    public H1bMatcherMerger(RecordFactory factory, float tt) {
        _factory = factory;
        titleMatcher = new TitleMatcher(tt);
    }

    protected boolean matchInternal(Record r1, Record r2) {

        Attribute t0 = r1.getAttribute("lca_case_employer_name");
        Attribute t1 = r2.getAttribute("lca_case_employer_name");

        Attribute t2 = r1.getAttribute("lca_case_employer_address");
        Attribute t3 = r2.getAttribute("lca_case_employer_address");

        Attribute t4 = r1.getAttribute("lca_case_employer_postal_code");
        Attribute t5 = r2.getAttribute("lca_case_employer_postal_code");

        Attribute t6 = r1.getAttribute("lca_case_employer_city");
        Attribute t7 = r2.getAttribute("lca_case_employer_city");

        Attribute t8 = r1.getAttribute("lca_case_employer_state");
        Attribute t9 = r2.getAttribute("lca_case_employer_state");


//        if (!ExistentialBooleanComparator.attributesMatch(t0, t1, titleMatcher))
//            return false;
//
//        if (!ExistentialBooleanComparator.attributesMatch(t2, t3, titleMatcher))
//            return false;
//
//        if (!ExistentialBooleanComparator.attributesMatch(t4, t5, titleMatcher))
//            return false;
//
//        if (!ExistentialBooleanComparator.attributesMatch(t6, t7, titleMatcher))
//            return false;

        if (ExistentialBooleanComparator.attributesMatch(t0, t1, titleMatcher)) {
            return true;
        }

        return false;
    }
}
