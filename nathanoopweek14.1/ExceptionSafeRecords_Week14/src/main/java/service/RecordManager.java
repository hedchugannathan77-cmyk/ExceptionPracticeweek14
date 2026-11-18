package service;

import exception.DuplicateRecordException;
import model.ItemRecord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Keeps records in a typed collection and prevents duplicate identifiers.
 */
public class RecordManager {
    private final List<ItemRecord> records = new ArrayList<>();

    public RecordManager() {
    }

    public RecordManager(List<ItemRecord> initialRecords) throws DuplicateRecordException {
        for (ItemRecord record : initialRecords) {
            addRecord(record);
        }
    }

    public void addRecord(ItemRecord record) throws DuplicateRecordException {
        if (record == null) {
            throw new IllegalArgumentException("Record cannot be null.");
        }

        for (ItemRecord existing : records) {
            if (existing.getRecordId().equalsIgnoreCase(record.getRecordId())) {
                throw new DuplicateRecordException(
                        "Duplicate record ID: " + record.getRecordId());
            }
        }
        records.add(record);
    }

    public List<ItemRecord> getAllRecords() {
        return Collections.unmodifiableList(records);
    }

    public boolean hasRecords() {
        return !records.isEmpty();
    }
}
