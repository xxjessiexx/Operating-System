package src;

import java.util.*;
import java.io.*;
import java.lang.classfile.Instruction;

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
                systemCalls.print("Please enter a value: ");
                String userInput = systemCalls.input();
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
                String varvalue= (String) memory.getVariableValue(p, parts[1]);     //typecast int???
                systemCalls.print(varvalue);
                break;

            case "printFromTo":
                int xFirst = Integer.parseInt((String)memory.getVariableValue(p, parts[1]));
                int ySecond = Integer.parseInt((String)memory.getVariableValue(p, parts[2]));

                printFromTo(xFirst,ySecond);
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

    public void printFromTo(int start, int end) {
        for (int i = start; i <= end; i++) {
            systemCalls.print(i + " ");
        }
    }

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

    public static void main (String args[]){
        Memory m = new Memory();
        SystemCalls s = new SystemCalls();
        OS os = new OS();

        Interpreter i = new Interpreter(s, m, os);
        
        
        String instruction = "printFromTo x y";
        ArrayList inst = new ArrayList<>();
        inst.add(instruction);
        inst.add("assign b readFile x");
        inst.add("assign b readFile x");
        
       

        Process p = new Process(2);
        p.pcb = new PCB(1);
        p.instructions=inst;
        m.allocateProcess(p);
        i.ExecuteInstruction(p, "assign x input");
        i.ExecuteInstruction(p, "assign y input");
        i.ExecuteInstruction(p, instruction);

        


        m.printMemory();

        

    }

}
