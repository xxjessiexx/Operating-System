package src;
import java.util.Scanner;
import java.io.*;
import java.lang.reflect.Member;

public class SystemCalls {
    private Scanner scanner;

    public SystemCalls() {
        scanner = new Scanner(System.in);
    }


    public void print(String message) {
        System.out.println(message);
    }

    
    public String input(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    
    public void writeFile(String fileName, String data) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + fileName);
        }
    }

   
    public String readFile(String fileName) {
        StringBuilder content = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
        }

        return content.toString().trim();
    }

   
    public MemoryWord readFromMemory(Memory memory, int address) {
        return memory.readWord(address);
    }

  
    public void writeToMemory(Memory memory, int address, String name, Object value) {
        MemoryWord word = new MemoryWord(name, value);
        memory.writeWord(address, word);
    }

   
}
