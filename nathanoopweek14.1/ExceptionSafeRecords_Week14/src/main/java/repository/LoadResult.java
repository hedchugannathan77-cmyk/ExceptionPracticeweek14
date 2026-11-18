package repository;

import model.ItemRecord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds valid records and warnings discovered while reading a text file.
 */
public class LoadResult {
    private final List<ItemRecord> records;
    private final List<String> warnings;

    public LoadResult(List<ItemRecord> records, List<String> warnings) {
        this.records = new ArrayList<>(records);
        this.warnings = new ArrayList<>(warnings);
    }

    public List<ItemRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }
}
