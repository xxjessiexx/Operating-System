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
            case "assign":     //types of 2nd field: input, readFile, anything else??????????
            //parts[1] is the variable name we are trying to assign a value to in memory
                if (parts[2].equals("input")) {
                String userInput = systemCalls.input("Please enter a value: ");
                memory.setVariableValue(p, parts[1], userInput);
                } 
                else if (parts[2].equals("readFile")) {

                String fileNameVariable = parts[3];
                Object fileNameValue = memory.getVariableValue(p, fileNameVariable);

                if (fileNameValue != null) {
                    String fileContent = systemCalls.readFile((String)fileNameValue);
                    memory.setVariableValue(p, parts[1], fileContent);
                } else {
                    System.out.println("File name variable " + fileNameVariable + " not found.");
                    }
                } 
            /*else {
            String value = parts[2];

            try {
                int intValue = Integer.parseInt(value);
                memory.setVariableValue(p, variableName, intValue);
            } catch (NumberFormatException e) {
                memory.setVariableValue(p, variableName, value);
            }*/           
                break;

            case "print":
                String varvalue= (String) memory.getVariableValue(p, parts[1]);
                systemCalls.print(varvalue);
                break;

            case "printFromTo":
                int xFirst = (int) memory.getVariableValue(p, parts[1]);
                int ySecond = (int) memory.getVariableValue(p, parts[2]);

                printFromTo(xFirst,ySecond);
                break;

            case "writeFile":
                String fileName = (String) memory.getVariableValue(p, parts[1]);
                String dataToWrite = (String)memory.getVariableValue(p, parts[2]);


                systemCalls.writeFile(fileName, dataToWrite);

                break;
            case "readFile":
                //n3mha tany wala only in assign???????
                break;
            case "semWait":
                String resource = parts[1];
                OS.semWait(p, resource);
                break;
            case "semSignal":
                String resource2= parts[1];
                OS.semSignal(p, resource2);
                break;

        }

        
        ///p.pcb.pc++;
    }

    public void printFromTo(int start, int end) {
        for (int i = start; i <= end; i++) {
        systemCalls.print(i+" ");
        }
    }
    public void assign(){}

    
    public ArrayList<String> readProgramFile(String fileName) {
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
