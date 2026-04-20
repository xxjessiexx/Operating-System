package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.InputStream;

public class Interpreter {

    private SystemCalls systemCalls;
    private Memory memory;
    OS operatingSystem;

    public Interpreter(SystemCalls systemCalls, Memory memory, OS os) {
        this.systemCalls = systemCalls;
        this.memory = memory;
        operatingSystem = os;
    }

    public void ExecuteInstruction(Process p, String instruction) {
        String[] parts = instruction.split(" ");
        String command = parts[0];

       switch (command) {
            case "assign":     //types of 2nd field: input, readFile, anything else??????????
            //parts[1] is the variable name we are trying to assign a value to in memory
                if (parts[2].equals("input")) {
                    String userInput = systemCalls.input(
                            p.pcb.processID,
                            instruction,
                            parts[1]
                    );
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
                else {
                    String value = parts[2];
                    memory.setVariableValue(p, parts[1], value);
                }        
                break;

            case "print":
                String varvalue = String.valueOf(memory.getVariableValue(p, parts[1]));
                systemCalls.showOutput(p.pcb.processID, instruction, varvalue);
                break;


           case "printFromTo":
               int xFirst = Integer.parseInt(String.valueOf(memory.getVariableValue(p, parts[1])));
               int ySecond = Integer.parseInt(String.valueOf(memory.getVariableValue(p, parts[2])));

               String output = printFromTo(xFirst, ySecond);
               systemCalls.showOutput(p.pcb.processID, instruction, output);
               break;

            case "writeFile":
                String fileName = (String) memory.getVariableValue(p, parts[1]);
                String dataToWrite = (String)memory.getVariableValue(p, parts[2]);


                systemCalls.writeFile(fileName, dataToWrite);

                break;
            case "readFile":
                String filenamevariable = parts[1];
                String filename = (String)memory.getVariableValue(p, filenamevariable);
                systemCalls.readFile(filename);
                break;
            case "semWait":
                String resource = parts[1];
                operatingSystem.semWait(p, resource);
                break;
            case "semSignal":
                String resource2= parts[1];
                operatingSystem.semSignal(p, resource2);
                break;


       }
        
    }

    public String printFromTo(int start, int end) {
        StringBuilder output = new StringBuilder();

        for (int i = start; i <= end; i++) {
            output.append(i);
            if (i < end) {
                output.append("\n");
            }
        }

        return output.toString();
    }

    public ArrayList<String> readProgramFile(String fileName) {
        ArrayList<String> instructions = new ArrayList<>();

        try {
            InputStream stream = getClass().getResourceAsStream("/programs/" + fileName);

            if (stream == null) {
                throw new RuntimeException("Program file not found in resources: " + fileName);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String line;

            while ((line = br.readLine()) != null) {
                instructions.add(line);
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error reading program file: " + fileName);
            e.printStackTrace();
        }

        return instructions;
    }

    

    

}
