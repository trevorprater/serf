package serf.data.storage;

import java.util.Iterator;
import java.util.Set;

import serf.data.Record;

public interface DataSource {

	public abstract Set<Record> getAllRecords();

	public abstract Iterator iterator();

}