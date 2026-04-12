
import java.util.Queue;
import java.util.LinkedList;
public class Scheduler {
    Queue<Process> readyQueue = new LinkedList<>(); //does the queue hold the pcb or the process
    
    public Process roundRobin(int timeQuantum , int globalTime) {
        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();
                if (currentProcess.pcb.programCounter < currentProcess.InstructionCounter) {
                readyQueue.add(currentProcess);
            }
            
        }
        return Process;
    }
    public Process HRRN (int globalTime){
        Process highestResponseRatioProcess = null;
        double highestResponseRatio = -1;

        for (Process process : readyQueue) {
            double responseRatio = (double) (OS.globalTime - process.arrivalTime) / process.getInstructionCounter();
            if (responseRatio > highestResponseRatio) {
                highestResponseRatio = responseRatio;
                highestResponseRatioProcess = process;
            }
        }

        if (highestResponseRatioProcess != null) {
            readyQueue.remove(highestResponseRatioProcess);
        }

        return highestResponseRatioProcess;
    }
    public Process MultilevelFeedbackQueue(int globalTime) {
        // Implement multilevel feedback queue scheduling logic here
        return null; // Placeholder return statement
    }



    public Process SchedulingAlgorithm(String algorithm , int globalTime) {
        switch (algorithm) {
            case "RoundRobin":
                return roundRobin(2 , globalTime); // Example time quantum of 2
            case "HRRN":
                return HRRN(globalTime);
            case "MultilevelFeedbackQueue":
                return MultilevelFeedbackQueue(globalTime);
            default:
                System.out.println("Invalid scheduling algorithm: " + algorithm);
                return null;
        }
    }




   public void addProcess(Process process) {
        readyQueue.add(process);
    }



    //public PCB getNextProcess() {
        //return readyQueue.poll();
   // }
}