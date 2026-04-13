
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

public class Scheduler {
    Deque<Process> readyQueue = new ArrayDeque<>();
    int usedTime = 0;
    Process HRRNprocess = null;

    public void addProcess(Process process) {
        readyQueue.add(process);
        process.pcb.processState = ProcessState.READY;
    }


    public Process roundRobin(int timeQuantum) {
    Process currentProcess = readyQueue.peek();

    if (currentProcess == null) {
        return null;
    }

    if (usedTime < timeQuantum
            && currentProcess.pcb.programCounter < currentProcess.getInstructionCounter()
            && currentProcess.pcb.processState != ProcessState.BLOCKED) {

        usedTime++;
        currentProcess.pcb.processState = ProcessState.RUNNING;
        return currentProcess;
    }

    currentProcess = readyQueue.poll();
    usedTime = 0;

    if (currentProcess.pcb.programCounter < currentProcess.getInstructionCounter()
            && currentProcess.pcb.processState != ProcessState.BLOCKED) {

        currentProcess.pcb.processState = ProcessState.READY;
        readyQueue.addLast(currentProcess);
    }
    
    else if (currentProcess.pcb.programCounter >= currentProcess.getInstructionCounter()) {
        currentProcess.pcb.processState = ProcessState.TERMINATED;
    }
    Process newProcess = readyQueue.peek();

    if (newProcess != null) {
        usedTime++;
        newProcess.pcb.processState = ProcessState.RUNNING;
    }

    return newProcess;
}

    public Process HRRN(int globalTime) {

    double highestResponseRatio = -1;

    if (HRRNprocess != null
            && HRRNprocess.pcb.processState.equals(ProcessState.RUNNING)
            && HRRNprocess.pcb.programCounter < HRRNprocess.getInstructionCounter()) { //if the process is still running and has instructions left to execute, return it to continue running
        return HRRNprocess;
    }

    if (HRRNprocess != null
            && HRRNprocess.pcb.programCounter >= HRRNprocess.getInstructionCounter()) { //if the process doesn't have any more instructions to execute, terminate it and set it to null so that a new process can be selected
        HRRNprocess.pcb.processState = ProcessState.TERMINATED;
        HRRNprocess = null;
    }
    if (HRRNprocess != null
            && !HRRNprocess.pcb.processState.equals(ProcessState.RUNNING)
            && HRRNprocess.pcb.programCounter < HRRNprocess.getInstructionCounter()) { // if the selected process is no longer running (blocked or preempted) but still has instructions to execute, set it to null so that a new process can be selected
        HRRNprocess = null;
    }

    if (HRRNprocess == null) {
        for (Process process : readyQueue) { //get the HRRN ratio for each process still in the ready queue

            double responseRatio =
                    (double) ((globalTime - process.arrivalTime) + process.getInstructionCounter()) / process.getInstructionCounter(); 
            if (responseRatio > highestResponseRatio) {  //set the new highest response ratio and the process with it
                highestResponseRatio = responseRatio;
                HRRNprocess = process;
            }
        }
        if (HRRNprocess != null && readyQueue.contains(HRRNprocess)) { //if the running process is still in the ready queue, remove it to avoid duplication
            readyQueue.remove(HRRNprocess);
        }
        if (HRRNprocess != null) {  //as long as the process is selected it is running
            HRRNprocess.pcb.processState = ProcessState.RUNNING;
        }
    }

    return HRRNprocess;
}

    public Process MultilevelFeedbackQueue(int globalTime) {
        // Implement multilevel feedback queue scheduling logic here
        return null; // Placeholder return statement
    }

    public Process SchedulingAlgorithm(String algorithm, int globalTime) {
        switch (algorithm) {
            case "RoundRobin":
                return roundRobin(2); // Example time quantum of 2
            case "HRRN":
                return HRRN(globalTime);
            case "MultilevelFeedbackQueue":
                return MultilevelFeedbackQueue(globalTime);
            default:
                System.out.println("Invalid scheduling algorithm: " + algorithm);
                return null;
        }
    }

    

}