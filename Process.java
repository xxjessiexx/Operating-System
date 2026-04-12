import java.util.ArrayList;

public class Process {
    PCB pcb;
    ArrayList<String> instructions;
    int ArrivalTime;     //removed instruction counter , we can get size arraylist 


    public Process(int ArrivalTime) {
        this.ArrivalTime = ArrivalTime;
        this.instructions = new ArrayList<>();    //not sure like this or we give it the instructions list as a parameter

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
