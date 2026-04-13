package src;
import java.util.ArrayList;

public class Process {
    PCB pcb;
    ArrayList<String> instructions;
    int arrivalTime;
    boolean isBlocked;
    boolean inMemory;
   

    public Process(int arrivalTime) {
        this.arrivalTime = arrivalTime;
        this.isBlocked = false;
        
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
