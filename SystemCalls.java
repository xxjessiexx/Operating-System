import java.util.Scanner;
import java.io.*;

public class SystemCalls {
    public static void print(String message) {
        System.out.println(message);

    }
    public static String input(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        String userInput = scanner.nextLine();
        return userInput;
    }
      public void writeFile(String fileName, String data) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(String fileName) {
        StringBuilder content = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
