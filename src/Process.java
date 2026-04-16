package src;
import java.util.ArrayList;

public class Process {
    PCB pcb;
    ArrayList<String> instructions;
    int arrivalTime;
    boolean inMemory;
    int waitingTime; // for HRRN scheduling to calculate the total time a process has been in the ready state
    int queueLevel; // for MLFQ to track which queue level a process is currently in
    int timeUsedInLevel; // for MLFQ to track how much time a process has used in its current queue level
    

    public Process(int arrivalTime) {
        this.arrivalTime = arrivalTime;
        this.waitingTime = 0;
        this.inMemory = true;       ///assuming at first keda keda it must be loaded even if no space smth else will be swapped
        this.queueLevel=0;
        this.timeUsedInLevel=0;
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
   
    public int getInstructionCounter() {
        return instructions.size();
    }
    
    public boolean isCompleted() {
       return pcb.programCounter >= instructions.size();
    }

    
    @Override
    public String toString() {
        return "Process{" +
                "pcb=" + pcb +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
    
    
}
