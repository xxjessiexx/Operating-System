package backend;
import java.util.ArrayList;

public class Process {
    PCB pcb;
    ArrayList<String> instructions;
    int arrivalTime;
    boolean inMemory;
    int waitingTime; // for HRRN scheduling to calculate the total time a process has been in the ready state
    int queueLevel; // for MLFQ to track which queue level a process is currently in
    int timeUsedInLevel; // for MLFQ to track how much time a process has used in its current queue level
    int orderNo; 
    String[] variableNames; //to store variable name
    Object[] variableValues; //variable values stored here so they can be restored

    public Process(int arrivalTime, int orderNo) {
        this.arrivalTime = arrivalTime;
        this.waitingTime = 0;
        this.inMemory = true;       ///assuming at first keda keda it must be loaded even if no space smth else will be swapped
        this.queueLevel=0;
        this.timeUsedInLevel=0;
        variableNames = new String[3];
        variableValues = new Object[3];
        this.orderNo=orderNo;
        for (int i = 0; i < 3; i++) { //var initialized with null
            variableNames[i] = "Var_" + (i + 1);
            variableValues[i] = null;
        }
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

    public Object getStoredVariableValue(String variableName) {
        for (int i = 0; i < 3; i++) {
            if (variableNames[i] != null && variableNames[i].equals(variableName)) {
                return variableValues[i];
            }
        }
        return null;
    }

    public void setStoredVariableValue(String variableName, Object value) {
        for (int i = 0; i < 3; i++) {
            if (variableNames[i] != null && variableNames[i].equals(variableName)) {
                variableValues[i] = value;
                return;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (variableValues[i] == null && variableNames[i].startsWith("Var_")) {
                variableNames[i] = variableName;
                variableValues[i] = value;
                return;
            }
        }

        System.out.println("No space available for more variables in process " + pcb.processID);
    }
    
    @Override
    public String toString() {
        return "Process{" +
                "pcb=" + pcb +
                ", arrivalTime=" + arrivalTime +
                ", waitingTime=" + waitingTime + 
                '}';
    }
    
    
}
