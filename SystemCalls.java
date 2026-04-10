import java.util.Scanner;
import java.io.*;
import java.lang.reflect.Member;

public class SystemCalls {
    public static void print(String message) {  // prints the message to the console
        System.out.println(message);

    }
    public static String input(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        String userInput = scanner.nextLine();
        return userInput;
    }
      public void writeFile(String fileName, String data) { // writes data to a file
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(String fileName) {  // reads data from a file and returns it as a string
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
    public void ReadFromMem( Memory memory, int address){ // reads data from a specific memory address and prints it to the console
        MemoryWord word = memory.memory[address];
        if (word != null) {
            System.out.println(word.value);
        }
    }
    public void WriteToMem(Memory memory, int address,String label, Object value){ // writes data to a specific memory address
        MemoryWord word = new MemoryWord();
        word.value = value;
        word.label = label;
        memory.memory[address] = word;
    }

   
}
