import java.util.ArrayList;

public class Process {
    PCB pcb;
    ArrayList<String> instructions;
    int ArrivalTime;


    public Process(int ArrivalTime) {
        this.ArrivalTime = ArrivalTime;
        this.instructions = new ArrayList<>(instructions);
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
