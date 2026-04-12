import java.util.*;
import java.io.*;
public class Interpreter {

    private SystemCalls systemCalls;
    private Memory memory;

    public Interpreter(SystemCalls systemCalls, Memory memory) {
        this.systemCalls = systemCalls;
        this.memory = memory;
    }
     public void ExecuteInstruction(Process p, String instruction) {
        String[] parts = instruction.split(" ");
        String command = parts[0];

       switch (command) {
            case "assign":               
                break;

            case "print":
                String varvalue= (String) memory.getVariableValue(p, parts[1]);
                systemCalls.print(varvalue);
                break;

            case "printFromTo":
                int xFirst = (int) memory.getVariableValue(p, parts[1]);
                int ySecond = (int) memory.getVariableValue(p, parts[1]);

                for (int i = xFirst; i <= ySecond; i++) {
                    System.out.print(i + " ");
                }
                System.out.println();
                break;
        }

        
        ///p.pcb.pc++;
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
