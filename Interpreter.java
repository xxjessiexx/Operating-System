import java.util.*;
import java.io.*;
public class Interpreter {

    private SystemCalls systemCalls;
    private Memory memory;

    public Interpreter(SystemCalls systemCalls, Memory memory) {
        this.systemCalls = systemCalls;
        this.memory = memory;
    }
    public ArrayList<String> loadProgram(String fileName) {
        ArrayList<String> instructions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                instructions.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading program file: " + fileName);
        }

        return instructions;
    }

    public void printFromTo(int start, int end) {
        for (int i = start; i <= end; i++) {
            System.out.println(i);
        }
    }
    public void assign(){}

    
    public ArrayList<String> readFile(String fileName) {
        ArrayList<String> instructions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                instructions.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instructions;
    }

        ///p.pcb.pc++;
    

}
