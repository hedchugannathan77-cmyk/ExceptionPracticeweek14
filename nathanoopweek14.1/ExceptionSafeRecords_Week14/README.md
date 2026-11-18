# ExceptionSafeRecords – Week 14

## Purpose
This is a Week 14 extension of the Week 13 exception-safe record application.
It adds UTF-8 text-file persistence using a repository interface.

## Open in Apache NetBeans
1. Extract `ExceptionSafeRecords_Week14.zip`.
2. In NetBeans, choose File > Open Project.
3. Select the extracted `ExceptionSafeRecords_Week14` folder (the folder containing `pom.xml`).
4. Run the project. The main class is `app.Main`.

## Relative data location
The application reads and writes `data/records.txt`, relative to the project working directory.
The included file contains synthetic sample records only.

## Record format
One record per line:
`RECORD_ID|NAME|QUANTITY`
Encoding: UTF-8. Delimiter: pipe (`|`). Exactly three fields are required.
IDs are compared without regard to letter case for duplicate detection.

## Malformed-record policy
- Missing file: treated as a first run; the application starts with an empty list.
- Blank line: permitted and skipped.
- Wrong number of fields: line is skipped and a warning is displayed.
- Invalid integer or invalid record values: line is skipped and a warning is displayed.
- Duplicate identifier: later duplicate line is skipped and reported.
- Valid lines after malformed lines are still loaded.
- Saving writes the current valid in-memory collection. It does not copy malformed lines.

## Resource management
Reading uses `Files.newBufferedReader` in try-with-resources.
Writing uses `Files.newBufferedWriter` in try-with-resources.
Both use UTF-8.

## Important
The module guide does not prescribe a specific delimiter or malformed-line policy.
This implementation uses `|` and skip-with-warning as documented assumptions.
Confirm that these match your instructor's announcement before submission.
The test sheet is initially marked NOT YET RUN. Execute each case in NetBeans and fill in the actual result/status using your own evidence.
