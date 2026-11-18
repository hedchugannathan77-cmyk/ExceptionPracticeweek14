package repository;

import model.ItemRecord;
import java.io.IOException;
import java.util.List;

/**
 * Contract for loading and saving records.
 */
public interface RecordRepository {
    LoadResult loadAll() throws IOException;

    void saveAll(List<ItemRecord> records) throws IOException;
}
