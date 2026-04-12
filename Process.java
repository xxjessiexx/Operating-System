import java.util.ArrayList;

public class Process {
    PCB pcb;
    ArrayList<String> instructions;
    int arrivalTime;
    boolean isBlocked;
    boolean isTerminated;
    boolean inMemory;

    public Process(int arrivalTime) {
        this.arrivalTime = arrivalTime;
        this.isBlocked = false;
        this.isTerminated = false;
        this.inMemory = false;
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
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "Process{" +
                "pcb=" + pcb +
                ", arrivalTime=" + arrivalTime +
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
