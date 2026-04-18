package src;

import java.util.ArrayDeque;
import java.util.Deque;

public class Scheduler {

    Deque<Process> readyQueue = new ArrayDeque<>();
    Deque<Process> blockedQueue = new ArrayDeque<>();
    Deque<Process> PQ0 = new ArrayDeque<>(); // highest priority queue for processes that have been in the ready state
    // the longest --MLFQ
    Deque<Process> PQ1 = new ArrayDeque<>();
    Deque<Process> PQ2 = new ArrayDeque<>();
    Deque<Process> PQ3 = new ArrayDeque<>(); // lowest priority queue for processes that have been in the ready state
    // the shortest -- MLFQ
    int usedTime = 0;
    Process HRRNprocess = null;
    Process MLFQprocess = null;
    Process RRprocess = null;

    // helper fns
    // calculate the response ratio for a process in the ready queue
    public double calculateResponseRatio(Process process, int globalTime) {
        int burstTime = process.getInstructionCounter();
        return (double) ((process.waitingTime + burstTime) / burstTime);
    }

    // add a process to the ready queue and set its state to ready
    public void addProcess(Process process, int globalTime) {
        process.pcb.processState = ProcessState.READY;
        readyQueue.add(process);
        process.queueLevel = 0;
        process.timeUsedInLevel = 0;
        PQ0.add(process);
    }

    public void addUnblockedProcess(Process process, int globalTime) { // same implementation as addProcess for now
        process.pcb.processState = ProcessState.READY;
        blockedQueue.remove(process);
        readyQueue.add(process);

        process.timeUsedInLevel = 0;
        mlfqQueue(process.queueLevel).add(process);
    }

    // remove a process from all relevant queues if terminated 
    public void removeTerminatedProcess(Process process) {
        process.pcb.processState = ProcessState.TERMINATED;
        readyQueue.remove(process);
        PQ0.remove(process);
        PQ1.remove(process);
        PQ2.remove(process);
        PQ3.remove(process);
        if (MLFQprocess == process) {
            MLFQprocess = null;
        }
        usedTime = 0;
    }

    public void removeBlockedProcess(Process process) {
        process.pcb.processState = ProcessState.BLOCKED;
        readyQueue.remove(process);
        if (!blockedQueue.contains(process)) {
            blockedQueue.add(process);
        }
        PQ0.remove(process);
        PQ1.remove(process);
        PQ2.remove(process);
        PQ3.remove(process);
        if (MLFQprocess == process) {
            MLFQprocess = null;
        }
        usedTime = 0;

    }

    // scheduling algorithms
    public Process roundRobin(int timeQuantum, int globalTime) {

        if (RRprocess == null && readyQueue.isEmpty()) { // no current process and no ready processes
            return null;
        }

        if (readyQueue.isEmpty() && RRprocess != null) { // only one process currently running
            RRprocess.pcb.processState = ProcessState.RUNNING;
            return RRprocess;
        }

        if (RRprocess == null) { // if i have no process running and i have processes in the ready queue i need to start one
            RRprocess = readyQueue.poll();
            if (RRprocess != null) {
                RRprocess.pcb.processState = ProcessState.RUNNING;
            }
            return RRprocess;
        }

        if (usedTime < timeQuantum
                && !RRprocess.isCompleted()
                && RRprocess.pcb.processState != ProcessState.BLOCKED) { // if the process still has time left in
            // its quantum and is not blocked or
            // completed, let it continue running

            RRprocess.pcb.processState = ProcessState.RUNNING;
            return RRprocess;
        }

        if (RRprocess.isCompleted()) { // if the process is completed, remove it from the ready queue and reset used time
            usedTime = 0;
            readyQueue.remove(RRprocess);
            RRprocess = readyQueue.poll(); // get the next process to run, if there is one, and set its state to running
        } else if (RRprocess.pcb.processState == ProcessState.BLOCKED) { // if it's completed or blocked don't add it back, get another process to run
            removeBlockedProcess(RRprocess);
            usedTime = 0;
            RRprocess = readyQueue.poll(); // if the process has used up its quantum or is blocked or completed, remove
        } // it from the ready queue and add it back if it's not completed or blocked
        // to let other processes run
        else if (usedTime >= timeQuantum) { // if ur not blocked or completed but u used up ur quantum, add it back to the ready queue and get another process to run
            usedTime = 0;
            RRprocess.pcb.processState = ProcessState.READY;
            readyQueue.addLast(RRprocess);
            RRprocess = readyQueue.poll(); // if the process has used up its quantum, remove it from the ready queue and add it back to let other processes run
        }

        if (RRprocess != null) {
            RRprocess.pcb.processState = ProcessState.RUNNING;
        }

        return RRprocess;
    }

    public Process HRRN(int globalTime) {   //check lw fe process and running and not finished return it else lw finished set hrrnprocess=null to fund a new
        //process else blocked but not finished add to blocked queue + find another
        double highestResponseRatio = -1;

        if (HRRNprocess != null
                && HRRNprocess.pcb.processState.equals(ProcessState.RUNNING)
                && !HRRNprocess.isCompleted()) { // if the process is still running and has instructions left to
            // execute, return it to continue running
            return HRRNprocess;
        }

        if (HRRNprocess != null
                && HRRNprocess.isCompleted()) { // if the process doesn't have any more instructions to
            // execute,terminate it and set it to null so that a new process can be
            // selected
            HRRNprocess = null;
        }
        if (HRRNprocess != null
                && !HRRNprocess.pcb.processState.equals(ProcessState.RUNNING)
                && !HRRNprocess.isCompleted()) { // if the selected process is no longer running (blocked or preempted)
            // but still has instructions to execute, set it to null so that a new                            // process can be selected
            HRRNprocess = null;
        }

        if (HRRNprocess == null) {
            ///if null i need to find a process calculate rr + set state running + remove from ready queue
            for (Process process : readyQueue) { // get the HRRN ratio for each process still in the ready queue

                double responseRatio = calculateResponseRatio(process, globalTime);

                if (responseRatio > highestResponseRatio) { // set the new highest response ratio and the process with
                    // it
                    highestResponseRatio = responseRatio;
                    HRRNprocess = process;
                }
            }
            if (HRRNprocess != null && readyQueue.contains(HRRNprocess)) { // if the running process is still in the
                // ready queue, remove it to avoid duplication                                                    // duplication
                readyQueue.remove(HRRNprocess);
            }
            if (HRRNprocess != null) { // as long as the process is selected it is running
                HRRNprocess.pcb.processState = ProcessState.RUNNING;
            }
        }

        return HRRNprocess;
    }

    public Process MultilevelFeedbackQueue(int globalTime) { // chooses according to the highest priority non-empty
        System.out.println("START OF MLFQ");                                                  // queue
        System.out.println(PQ0);
        System.out.println(PQ1);
        System.out.println(PQ2);
        System.out.println(PQ3);
        if (MLFQprocess != null
                && MLFQprocess.pcb.processState == ProcessState.RUNNING
                && !MLFQprocess.isCompleted()
                && MLFQprocess.pcb.processState != ProcessState.BLOCKED) { // if the selected process is still running,
            // not completed, and not blocked, return it
            // to continue using its quantum before
            // preemption
            int quantum = (int) Math.pow(2, MLFQprocess.queueLevel);

            if (MLFQprocess.timeUsedInLevel < quantum) { // if the process has not used up the full quantum of its
                // current queue level yet, return it to continue running
                return MLFQprocess;
            }
        }

        if (PQ0.isEmpty() && PQ1.isEmpty() && PQ2.isEmpty() && PQ3.isEmpty()) { // if all priority queues are empty,
            // return null because there is no
            // process to schedule
            return null;
        }

        Process process = null;

        if (!PQ0.isEmpty()) { // if the highest priority queue is not empty, select from it
            process = PQ0.poll();
        } else if (!PQ1.isEmpty()) {
            process = PQ1.poll();
        } else if (!PQ2.isEmpty()) {
            process = PQ2.poll();
        } else if (!PQ3.isEmpty()) {
            process = PQ3.poll();
        }

        if (process != null) { // if a process is selected, set its state to running
            process.pcb.processState = ProcessState.RUNNING;
            MLFQprocess = process;
        }

        return process;
    }

    public void updateMLFQ(Process process, int globalTime) { // called right after a process is done executing to set
        // it back into the correct queue
        if (process == null) {
            return;
        }

        if (process.isCompleted()) { // if the process is completed remove it from all queues
            removeTerminatedProcess(process);
            if (MLFQprocess == process) {
                MLFQprocess = null;
            }
            return;
        }

        if (process.pcb.processState == ProcessState.BLOCKED) { // if the process is blocked remove it from all queues
            removeBlockedProcess(process);
            if (MLFQprocess == process) {
                MLFQprocess = null;
            }
            return;
        }

        process.timeUsedInLevel++;

        int quantum = (int) Math.pow(2, process.queueLevel); // gets the quantum for the process's current queue level
        // (1 for PQ0, 2 for PQ1, 4 for PQ2)

        if (process.timeUsedInLevel < quantum) { // if the process has not used up the full quantum of its current level
            // yet, keep it running and do not return it to a queue yet
            process.pcb.processState = ProcessState.RUNNING;
            MLFQprocess = process;
            return;
        }

        process.pcb.processState = ProcessState.READY; // if the process has used up its full quantum and is not
        // completed or blocked, set it back to ready and add it to the
        // correct queue

        if (process.queueLevel < 3) {
            process.queueLevel++;
        }

        process.timeUsedInLevel = 0;
        MLFQprocess = null;

        switch (process.queueLevel) {
            case 0:
                PQ0.addLast(process);
                break;
            case 1:
                PQ1.addLast(process);
                break;
            case 2:
                PQ2.addLast(process);
                break;
            case 3:
                PQ3.addLast(process);
                break;
        }
    }

    public Process SchedulingAlgorithm(String algorithm, int globalTime) {
        printQueues();
        switch (algorithm) {
            case "RoundRobin":
                return roundRobin(4, globalTime); // Example time quantum of 2
            case "HRRN":
                return HRRN(globalTime);
            case "MultilevelFeedbackQueue":
                return MultilevelFeedbackQueue(globalTime);
            default:
                System.out.println("Invalid scheduling algorithm: " + algorithm);
                return null;
        }
    }

    public Deque mlfqQueue(int i) {
        switch (i) {
            case 0:
                return PQ0;

            case 1:
                return PQ1;

            case 2:
                return PQ2;

            case 3:
                return PQ3;
            default:
                return PQ0;  //keda keda mesh hnwslha

        }
    }

    public void printQueues() {
        System.out.println("AFTER UPDATEQueues");
        System.out.print("Ready queue: ");
        System.out.println(readyQueue);
        System.out.print("blocked queue: ");
        System.out.println(blockedQueue);
        System.out.print("MLFQ 0 to 3: ");
        System.out.println(PQ0);
        System.out.println(PQ1);
        System.out.println(PQ2);
        System.out.println(PQ3);
    }

    public static void main(String args[]) {

    }

}
