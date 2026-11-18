package repository;

import model.ItemRecord;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * UTF-8 text-file implementation using ID|NAME|QUANTITY on each line.
 */
public class TextFileRecordRepository implements RecordRepository {
    private final Path filePath;

    public TextFileRecordRepository(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null.");
        }
        this.filePath = filePath;
    }

    @Override
    public LoadResult loadAll() throws IOException {
        List<ItemRecord> validRecords = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        Set<String> seenIds = new HashSet<>();

        // A missing file is a normal first-run condition, not a fatal error.
        if (!Files.exists(filePath)) {
            return new LoadResult(validRecords, warnings);
        }

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Blank lines are permitted and skipped.
                if (line.isBlank()) {
                    continue;
                }

                try {
                    ItemRecord record = decode(line);
                    String normalizedId = record.getRecordId().toLowerCase(Locale.ROOT);

                    if (!seenIds.add(normalizedId)) {
                        warnings.add("Line " + lineNumber + ": duplicate ID '"
                                + record.getRecordId() + "'; line skipped.");
                        continue;
                    }
                    validRecords.add(record);
                } catch (NumberFormatException ex) {
                    warnings.add("Line " + lineNumber
                            + ": quantity is not a valid whole number; line skipped.");
                } catch (IllegalArgumentException ex) {
                    warnings.add("Line " + lineNumber + ": "
                            + ex.getMessage() + " Line skipped.");
                }
            }
        }

        return new LoadResult(validRecords, warnings);
    }

    @Override
    public void saveAll(List<ItemRecord> records) throws IOException {
        if (records == null) {
            throw new IllegalArgumentException("Records list cannot be null.");
        }

        // Validate the complete list before opening/truncating the destination.
        Set<String> seenIds = new HashSet<>();
        for (ItemRecord record : records) {
            if (record == null) {
                throw new IllegalArgumentException("Cannot save a null record.");
            }
            String normalizedId = record.getRecordId().toLowerCase(Locale.ROOT);
            if (!seenIds.add(normalizedId)) {
                throw new IllegalArgumentException(
                        "Cannot save duplicate record ID: " + record.getRecordId());
            }
        }

        Path parent = filePath.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {
            for (ItemRecord record : records) {
                writer.write(encode(record));
                writer.newLine();
            }
        }
    }

    /** Converts one validated record into the approved line format. */
    private String encode(ItemRecord record) {
        return record.getRecordId() + "|" + record.getName() + "|" + record.getQuantity();
    }

    /** Converts one text line into a fully validated record. */
    private ItemRecord decode(String line) {
        String[] fields = line.split("\\|", -1);
        if (fields.length != 3) {
            throw new IllegalArgumentException(
                    "Expected exactly 3 fields (ID|NAME|QUANTITY).");
        }

        String id = fields[0].trim();
        String name = fields[1].trim();
        int quantity = Integer.parseInt(fields[2].trim());
        return new ItemRecord(id, name, quantity);
    }

    public Path getFilePath() {
        return filePath;
    }
}
