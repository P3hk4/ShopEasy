package base.DataGenerationScrypts;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class RemoveEmptyLines {

    public static void main(String[] args) {
        String inputFileName = "Main\\resources\\Data\\IN.txt"; // Имя файла для чтения
        String outputFileName = "Main\\resources\\Data\\OUT.txt"; // Имя файла для записи

        // Чтение строк из файла
        List<String> lines = readLinesFromFile(inputFileName);

        // Удаление пустых строк
        lines.removeIf(String::isEmpty);

        // Сортировка строк
        Collections.sort(lines);

        // Запись отсортированных строк в другой файл
        writeLinesToFile(lines, outputFileName);
    }

    // Метод для чтения строк из файла
    private static List<String> readLinesFromFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return lines;
    }

    // Метод для записи строк в файл
    private static void writeLinesToFile(List<String> lines, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Переход на новую строку
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
