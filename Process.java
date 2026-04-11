import java.util.ArrayList;

public class Process {
    PCB pcb;
    ArrayList<String> instructions;
    int arrivalTime;
    int instructionCount;

    public Process(int processID, int arrivalTime, ArrayList<String> instructions) {
        this.arrivalTime = arrivalTime;
        this.instructions = new ArrayList<>(instructions);
        this.instructionCount = instructions.size();
        this.pcb = new PCB(processID, ProcessState.NEW, 0, -1, -1);
    }
      public PCB getPcb() {
        return pcb;
    }

    public void setPcb(PCB pcb) {
        this.pcb = pcb;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
        this.instructionCount = instructions.size();
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getInstructionCount() {
        return instructionCount;
    }

    public void setInstructionCount(int instructionCount) {
        this.instructionCount = instructionCount;
    }

    
    public String toString() {
        return "Process{" +
                "pcb=" + pcb +
                ", arrivalTime=" + arrivalTime +
                ", instructionCount=" + instructionCount +
                ", instructions=" + instructions +
                '}';
    }
    //public String getNextInstruction() {
    //    if (pcb.programCounter < instructions.size()) {
      //      String instruction = instructions.get(pcb.programCounter);
        //    pcb.programCounter++;
         //   return instruction;
        //} else {
         //   return null; 
        //}
    //}
}
