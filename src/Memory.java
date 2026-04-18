package src;

import java.util.ArrayList;

public class Memory {
    private static final int SIZE = 40;
    private MemoryWord[] memory;
    Process lastaddedProcess;
    disk d;

    public Memory() {
        memory = new MemoryWord[SIZE];
        d = new disk();
    }

    public void writeWord(int index, MemoryWord word) { // write a single word
        if (index >= 0 && index < SIZE) { // check within bound of memory
            memory[index] = word;
        } else {
            System.out.println("Invalid memory index.");
        }
    }

    public MemoryWord readWord(int index) {
        if (index >= 0 && index < SIZE) {
            return memory[index]; /// read a specific word with an index
        }
        System.out.println("Invalid memory index.");
        return null;
    }

    public void printMemory() {
        System.out.println("= Memory Contents =");
        for (int i = 0; i < SIZE; i++) {
            if (memory[i] == null) {
                System.out.println("address: " + i + ": Empty");
            } else {
                System.out.println("address " + i + ": " + memory[i]);
            }
        }
        System.out.println("-------------");
    }

    public int getRequiredSize(Process process) {
        return 5 + process.getInstructions().size() + 3; // get size inst + 5 for pcb + 3 for variables
    }

    public boolean isFreeBlock(int start, int size) {
        if (start < 0 || start + size > SIZE) {
            return false;
        }

        for (int i = start; i < start + size; i++) {
            if (memory[i] != null) { //// if anything in block not free return false
                return false;
            }
        }
        return true;
    }

    public int findFreeBlock(int size) { // takes size + return free block in memory
        for (int start = 0; start <= SIZE - size; start++) {
            if (isFreeBlock(start, size)) {
                return start;
            }
        }
        return -1;
    }

    public boolean allocateProcess(Process process) { // return false if no space true if space found
        int requiredSize = getRequiredSize(process); // get required size for process
        int start = findFreeBlock(requiredSize); // find starting index of free block to accomadte process

        if (start == -1) {
            return false;
        }
        lastaddedProcess = process; /// the last process added to the memory

        int end = start + requiredSize - 1;

        process.pcb.memStart = start;
        process.pcb.memEnd = end;

        memory[start] = new MemoryWord("PID", process.pcb.processID);
        memory[start + 1] = new MemoryWord("State", process.pcb.processState);
        memory[start + 2] = new MemoryWord("PC", process.pcb.programCounter);
        memory[start + 3] = new MemoryWord("MemStart", start);
        memory[start + 4] = new MemoryWord("MemEnd", end);

        int currentIndex = start + 5;
        for (int i = 0; i < process.getInstructions().size(); i++) {
            memory[currentIndex] = new MemoryWord(
                    "Instruction_" + i,
                    process.getInstructions().get(i));
            currentIndex++;
        }

        for (int i = 0; i < 3; i++) { //updated i get variable values from process and dont set it to null directly
            memory[currentIndex] = new MemoryWord(
                    process.variableNames[i],
                    process.variableValues[i]);
            currentIndex++;
        }

        return true;
    }

    public int getVariablesStart(Process process) { /// return the index of the first variable of the 3
        return process.pcb.memEnd - 2;
    }

     
                                                                           
    public Object getVariableValue(Process process, String variableName) { // returns null if variable mot found or if
    if (process != null && process.inMemory) {                              // already the value is null
        int start = getVariablesStart(process);

        for (int i = start; i <= process.pcb.memEnd; i++) {
            MemoryWord word = memory[i];
            if (word != null && word.getName().equals(variableName)) {
                return word.getValue();
            }
        }
    }

    return process.getStoredVariableValue(variableName);
}

    public String getInstruction(Process process) {
        int instructionIndex = process.pcb.programCounter; // Might need to get the pcb from memory
        int instructionMemoryIndex = process.pcb.memStart + 5 + instructionIndex;

        if (instructionMemoryIndex >= process.pcb.memStart + 5 && instructionMemoryIndex <= process.pcb.memEnd - 3) {
            MemoryWord word = memory[instructionMemoryIndex];
            if (word != null) {
                return (String) word.getValue();
            }
        }
        return null;
    }

    public void setVariableValue(Process process, String variableName, Object value) { //i save variables in process too not only mem
        int start = getVariablesStart(process);

        for (int i = start; i <= process.pcb.memEnd; i++) {
            MemoryWord word = memory[i];

            if (word != null && variableName.equals(word.getName())) {
                word.setValue(value);
                process.setStoredVariableValue(variableName, value);
                return;
            }
        }

        for (int i = start; i <= process.pcb.memEnd; i++) {
            MemoryWord word = memory[i];

            if (word != null && word.getValue() == null) {
                word.setName(variableName);
                word.setValue(value);
                process.setStoredVariableValue(variableName, value);
                return;
            }
        }

        System.out.println("No space available for more variables in process " + process.pcb.processID);
    }

    public void saveProcessVariablesToProcess(Process p) { //added to save the variables content so 
                                                            // they can be restored after swapping
        if (p == null || p.pcb == null || !p.inMemory) {
            return;
        }

        int start = getVariablesStart(p);

        for (int i = 0; i < 3; i++) {
            MemoryWord word = memory[start + i];
            if (word != null) {
                p.variableNames[i] = word.getName();
                p.variableValues[i] = word.getValue();
            }
        }
    }

    public boolean swap(Process process) {
        saveProcessVariablesToProcess(lastaddedProcess); //save variables before deallocating
        deallocate(lastaddedProcess);
        lastaddedProcess.inMemory = false;
        d.addtoDisk(lastaddedProcess);
        return allocateProcess(process);
    }

    public void deallocate(Process p) {
        if (p == null || p.pcb == null) {
            return;
        }

        saveProcessVariablesToProcess(p);

        int lowbound = p.pcb.memStart;
        int highbound = p.pcb.memEnd;

        for (int i = lowbound; i <= highbound; i++) {
            memory[i] = null;
        }

        p.inMemory = false;
    }

    

    public void updateMemory(Process p) {
        if (p == null || p.pcb == null) {
            return;
        }
        if (!p.pcb.processState.equals(ProcessState.TERMINATED)) {
            int memStart = p.pcb.memStart;
            memory[memStart + 1].value = p.pcb.processState; // updateing process state
            memory[memStart + 2].value = p.pcb.programCounter;
        }
    }

}