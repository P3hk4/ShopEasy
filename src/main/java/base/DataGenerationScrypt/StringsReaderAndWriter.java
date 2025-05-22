package base.DataGenerationScrypt;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class StringsReaderAndWriter {
    public static ArrayList<String> readStringsFromFile(String path) {
        ArrayList<String> strings = new ArrayList<>();

        // Удаляем "classpath:" если есть и добавляем ведущий слеш
        String normalizedPath = path.startsWith("classpath:")
                ? path.substring("classpath:".length())
                : path;

        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }

        try (InputStream is = StringsReaderAndWriter.class.getResourceAsStream(normalizedPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                throw new FileNotFoundException("Файл не найден: " + normalizedPath);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    strings.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла " + path + ": " + e.getMessage());
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
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
