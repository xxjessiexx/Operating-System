import java.util.Queue;
import java.util.LinkedList;
public class Scheduler {
    Queue<Process> readyQueue = new LinkedList<>(); //does the queue hold the pcb or the process
    
    public Process roundRobin(int timeQuantum) {
        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();
                if (currentProcess.pcb.programCounter < currentProcess.InstructionCounter) {
                readyQueue.add(currentProcess);
            }
            
        }
        return Process;
    }

   // public void addProcess(PCB pcb) {
        //readyQueue.add(pcb);
   // }

    //public PCB getNextProcess() {
        //return readyQueue.poll();
   // }
}