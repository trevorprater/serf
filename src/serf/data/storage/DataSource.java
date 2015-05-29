package serf.data.storage;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import serf.data.Record;

public interface DataSource {

	public abstract List<Record> getAllRecords() throws IOException;

	public abstract Iterator iterator();

}