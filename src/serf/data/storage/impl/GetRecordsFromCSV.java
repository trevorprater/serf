package serf.data.storage.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import serf.data.Attribute;

import serf.data.Record;
import serf.data.storage.DataSource;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import org.apache.commons.csv.CSVParser;

/**
 * Created by trevorprater on 5/28/15.
 */

public class GetRecordsFromCSV implements DataSource {

    private static final String [] FILE_HEADER_MAPPING = {"year","status","lca_case_wage_rate_from","part_time_pos","lca_case_employer_state","yr_source_pub_2","pw_2","pw_1","lca_case_submit","pw_unit_2","lca_case_workloc1_state","total_workers","lca_case_soc_code","lca_case_workloc2_city","decision_date","yr_source_pub_1","serialid","pw_unit_1","lca_case_workloc1_city","lca_case_employer_city","lca_case_workloc2_state","lca_case_job_title","other_wage_source_1","lca_case_employment_start_date","lca_case_employer_name","lca_case_employer_address","lca_case_employer_postal_code","lca_case_wage_rate_unit", "other_wage_source_2", "full_time_pos", "lca_case_wage_rate_to", "lca_case_number", "lca_case_employment_end_date"};
    CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader(FILE_HEADER_MAPPING);

    CSVParser csvParser;
    File csvData;
    List<CSVRecord> csvRecords = new ArrayList<CSVRecord>();
    List<Record> records = new ArrayList<Record>();


    public GetRecordsFromCSV(String fileSrc) throws IOException
    {
        //FileReader fr = new FileReader(fileSrc);
        //input = new InputSource(fr);
        csvData = new File(fileSrc);
        init();
    }

    public void parseCSV() throws IOException
    {
        for(CSVRecord record : csvParser){
            csvRecords.add(record);
        }
    }

    /* (non-Javadoc)
     * @see serf.data.DataSource#getAllRecords()
     */
    public List<Record> getAllRecords() throws IOException {

        for (int i = 1; i < csvRecords.size(); i++) {
            Set<Attribute> attrSet = new HashSet<Attribute>();
            CSVRecord csvRecord = csvRecords.get(i);

            attrSet.add(new Attribute("lca_case_employer_name", csvRecord.get("lca_case_employer_name")));
            attrSet.add(new Attribute("lca_case_employer_city", csvRecord.get("lca_case_employer_city")));
            attrSet.add(new Attribute("lca_case_employer_state", csvRecord.get("lca_case_employer_state")));
            attrSet.add(new Attribute("lca_case_employer_postal_code", csvRecord.get("lca_case_employer_postal_code")));
            attrSet.add(new Attribute("lca_case_employer_address", csvRecord.get("lca_case_employer_address")));

            records.add(new Record(1.0, attrSet));
        }

        return records;

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
        return records.iterator();
    }

    protected void init() throws IOException
    {
        csvParser = CSVParser.parse(csvData, Charset.forName("UTF-8"), csvFileFormat);
    }
}


