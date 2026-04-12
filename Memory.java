public class Memory {
    private static final int SIZE = 40;
    private MemoryWord[] memory;

    public Memory() {
        memory = new MemoryWord[SIZE];
    }

    public void writeWord(int index, MemoryWord word) {
        if (index >= 0 && index < SIZE) {
            memory[index] = word;
        } else {
            System.out.println("Invalid memory index.");
        }
    }

    public MemoryWord readWord(int index) {
        if (index >= 0 && index < SIZE) {
            return memory[index];
        }
        System.out.println("Invalid memory index.");
        return null;
    }

    public void printMemory() {
        System.out.println("===== Memory Contents =====");
        for (int i = 0; i < SIZE; i++) {
            if (memory[i] == null) {
                System.out.println(i + ": Empty");
            } else {
                System.out.println(i + ": " + memory[i]);
            }
        }
        System.out.println("===========================");
    }

    public int getRequiredSize(Process process) {
        return 5 + process.getInstructions().size() + 3;
    }

    public boolean isFreeBlock(int start, int size) {
        if (start < 0 || start + size > SIZE) {
            return false;
        }

        for (int i = start; i < start + size; i++) {
            if (memory[i] != null) {
                return false;
            }
        }
        return true;
    }

    public int findFreeBlock(int size) {
        for (int start = 0; start <= SIZE - size; start++) {
            if (isFreeBlock(start, size)) {
                return start;
            }
        }
        return -1;
    }

    public boolean allocateProcess(Process process) {
        int requiredSize = getRequiredSize(process);
        int start = findFreeBlock(requiredSize);

        if (start == -1) {
            return false;
        }

        int end = start + requiredSize - 1;

        process.pcb.memStart=start;
        process.pcb.memEnd=end;

        memory[start] = new MemoryWord("P" + process.pcb.processID + "_PID", process.pcb.processID);
        memory[start + 1] = new MemoryWord("P" + process.pcb.processID + "_State", process.pcb.processState);
        memory[start + 2] = new MemoryWord("P" + process.pcb.processID + "_PC", process.pcb.programCounter);
        memory[start + 3] = new MemoryWord("P" + process.pcb.processID + "_MemStart", start);
        memory[start + 4] = new MemoryWord("P" + process.pcb.processID + "_MemEnd", end);

        int currentIndex = start + 5;
        for (int i = 0; i < process.getInstructions().size(); i++) {
            memory[currentIndex] = new MemoryWord(
                    "P" + process.pcb.processID + "_Instruction_" + i,
                    process.getInstructions().get(i)
            );
            currentIndex++;
        }

        for (int i = 1; i <= 3; i++) {
            memory[currentIndex] = new MemoryWord(
                    "Var_" + i,
                    null
            );
            currentIndex++;
        }

        return true;
    }
    public int getVariablesStart(Process process) {
    return process.pcb.memEnd - 2;
}

public Object getVariableValue(Process process, String variableName) {
    int start = getVariablesStart(process);

    for (int i = start; i <= process.pcb.memEnd; i++) {
        MemoryWord word = memory[i];
        if (word != null && word.getName().equals(variableName)) {
            return word.getValue();
        }
    }
    return null;
}
public String getInstruction(Process process) {
    int instructionIndex = process.pcb.programCounter; //Might need to get  the pcb from memory
    int instructionMemoryIndex = process.pcb.memStart + 5 + instructionIndex;

    if (instructionMemoryIndex >= process.pcb.memStart + 5 && instructionMemoryIndex <= process.pcb.memEnd - 3) {
        MemoryWord word = memory[instructionMemoryIndex];
        if (word != null) {
            return (String) word.getValue();
        }
    }
    return null;
}

public void setVariableValue(Process process, String variableName, Object value) {
    int start = getVariablesStart(process);

    for (int i = start; i <= process.pcb.memEnd; i++) {
        MemoryWord word = memory[i];

        if (word != null && variableName.equals(word.getName())) {
            word.setValue(value);
            return;
        }
    }

    for (int i = start; i <= process.pcb.memEnd; i++) {
        MemoryWord word = memory[i];

        if (word != null && word.getValue() == null) {
            word.setName(variableName);
            word.setValue(value);
            return;
        }
    }

    System.out.println("No space available for more variables in process " + process.pcb.processID);
}
}