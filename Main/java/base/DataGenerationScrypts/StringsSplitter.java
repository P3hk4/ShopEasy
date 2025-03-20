package base.DataGenerationScrypts;

import java.util.ArrayList;
import java.util.List;

public class StringsSplitter {

    public static List<List<String>> splitStrings(List<String> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return null;
        }

        List<List<String>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());

        for (String str : inputList) {
            String[] parts = str.split(" – ");
            if (parts.length == 2) {
                result.get(0).add(parts[0].trim());
                result.get(1).add(parts[1].trim());
            } else {
                System.out.println("Ошибка при разбиении строки: " + str);
            }
        }
        return result;
    }


}
