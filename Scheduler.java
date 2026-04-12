
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

public class Scheduler {
    Deque<Process> readyQueue = new ArrayDeque<>();
    int usedTime = 0;
    Process HRRNprocess = null;

    public Process roundRobin(int timeQuantum) {
        Process currentProcess = readyQueue.poll();

        if (timeQuantum > usedTime && currentProcess != null
                && currentProcess.pcb.programCounter < currentProcess.getInstructionCounter()) {

            usedTime++;
            readyQueue.addFirst(currentProcess);
            currentProcess.pcb.processState = ProcessState.RUNNING;
            return currentProcess;
        } else {
            usedTime = 0;
            if (currentProcess.pcb.programCounter < currentProcess.getInstructionCounter()) {
                readyQueue.addLast(currentProcess);
                currentProcess.pcb.processState = ProcessState.READY;

            } else {
                currentProcess.pcb.processState = ProcessState.TERMINATED;
            }
            Process newProcess = readyQueue.peek();

            if (newProcess != null) {
                usedTime++;
                newProcess.pcb.processState = ProcessState.RUNNING;
            }

            return newProcess;
        }

    }

    public Process HRRN(int globalTime) {
        Process highestResponseRatioProcess = null;
        double highestResponseRatio = -1;

        for (Process process : readyQueue) {
            double responseRatio = (double) (globalTime - process.arrivalTime) / process.getInstructionCounter();
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

    public void addProcess(Process process) {
        readyQueue.add(process);
        process.pcb.processState = ProcessState.READY;
    }

}