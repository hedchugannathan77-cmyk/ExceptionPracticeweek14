package model;

/**
 * One validated synthetic record.
 */
public class ItemRecord {
    private final String recordId;
    private final String name;
    private final int quantity;

    public ItemRecord(String recordId, String name, int quantity) {
        if (recordId == null || recordId.isBlank()) {
            throw new IllegalArgumentException("Record ID cannot be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Record name cannot be blank.");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be a positive whole number.");
        }
        if (recordId.contains("|") || name.contains("|")
                || recordId.contains("\n") || recordId.contains("\r")
                || name.contains("\n") || name.contains("\r")) {
            throw new IllegalArgumentException(
                    "Record ID and name cannot contain the pipe delimiter or a line break.");
        }

        this.recordId = recordId.trim();
        this.name = name.trim();
        this.quantity = quantity;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "ID: " + recordId + " | Name: " + name + " | Quantity: " + quantity;
    }
}
