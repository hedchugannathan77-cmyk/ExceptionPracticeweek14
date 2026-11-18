package app;

import exception.DuplicateRecordException;
import model.ItemRecord;
import repository.LoadResult;
import repository.RecordRepository;
import repository.TextFileRecordRepository;
import service.RecordManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Path DATA_FILE = Path.of("data", "records.txt");

    public static void main(String[] args) {
        RecordRepository repository = new TextFileRecordRepository(DATA_FILE);
        RecordManager manager = new RecordManager();

        System.out.println("=== EXCEPTION-SAFE RECORDS: WEEK 14 ===");
        System.out.println("Data file: " + DATA_FILE.toAbsolutePath().normalize());

        try {
            LoadResult result = repository.loadAll();
            for (ItemRecord record : result.getRecords()) {
                manager.addRecord(record);
            }

            System.out.println("Loaded " + result.getRecords().size()
                    + " valid record(s).");
            if (result.getWarnings().isEmpty()) {
                System.out.println("No malformed-line warnings.");
            } else {
                System.out.println("File warnings:");
                for (String warning : result.getWarnings()) {
                    System.out.println(" - " + warning);
                }
            }
        } catch (IOException | DuplicateRecordException ex) {
            System.out.println("Could not load records: " + ex.getMessage());
            System.out.println("The application will continue with an empty list.");
        }

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("1 - Add record");
                System.out.println("2 - Display records");
                System.out.println("3 - Save records");
                System.out.println("0 - Save and exit");
                System.out.print("Choose: ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        addRecord(scanner, manager, repository);
                        break;
                    case "2":
                        displayRecords(manager.getAllRecords());
                        break;
                    case "3":
                        saveRecords(manager, repository);
                        break;
                    case "0":
                        saveRecords(manager, repository);
                        running = false;
                        System.out.println("Application closed.");
                        break;
                    default:
                        System.out.println("Invalid menu choice. Please select 0, 1, 2, or 3.");
                }
            }
        }
    }

    private static void addRecord(
            Scanner scanner, RecordManager manager, RecordRepository repository) {
        try {
            System.out.print("Record ID: ");
            String id = scanner.nextLine();

            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Quantity: ");
            String quantityText = scanner.nextLine().trim();

            int quantity;
            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid quantity. Enter a whole number such as 5.");
                return;
            }

            ItemRecord record = new ItemRecord(id, name, quantity);
            manager.addRecord(record);
            System.out.println("Record added successfully.");

            // Save after each successful addition so the record is persisted promptly.
            saveRecords(manager, repository);
        } catch (DuplicateRecordException | IllegalArgumentException ex) {
            System.out.println("Record not added: " + ex.getMessage());
        }
    }

    private static void displayRecords(List<ItemRecord> records) {
        System.out.println("--- SAVED/LOADED RECORDS ---");
        if (records.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (ItemRecord record : records) {
            System.out.println(record);
        }
        System.out.println("Total records: " + records.size());
    }

    private static void saveRecords(
            RecordManager manager, RecordRepository repository) {
        try {
            repository.saveAll(manager.getAllRecords());
            System.out.println("Saved " + manager.getAllRecords().size()
                    + " record(s) successfully.");
        } catch (IOException | IllegalArgumentException ex) {
            System.out.println("Save failed: " + ex.getMessage());
            System.out.println("Your current in-memory records remain available until exit.");
        }
    }
}
