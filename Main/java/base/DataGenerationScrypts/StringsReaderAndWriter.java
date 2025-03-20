package base.DataGenerationScrypts;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class StringsReaderAndWriter {
    public static ArrayList<String> readStringsFromFile(String filePath) {
        ArrayList<String> strings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    strings.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return null;
        }
        Collections.sort(strings);
        return strings;
    }

    public static void writeStringsToFile(ArrayList<String> list, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String item : list) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
