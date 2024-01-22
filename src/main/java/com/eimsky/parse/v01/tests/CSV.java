package com.eimsky.parse.v01.tests;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSV {
    private static final String CSV_FILE = "file.csv";
    public static void main(String[] args) {
        CSV csv = new CSV();
        csv.readCSV(CSV_FILE);
    }

    public void readCSV(String filePath){
        try {
            // Read CSV file
            CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(0).build(); // Skip header
            List<String[]> rows = reader.readAll();

            // Extract and display headers
            String[] headers = rows.get(0);
            for (int i = 0; i < headers.length; i++) {
                System.out.println(i + ": " + headers[i]);
            }

            // Choose a header (e.g., headerIndex)
            int headerIndex = 4;

            // Read data based on the chosen header
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                System.out.println(row[headerIndex]);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}
