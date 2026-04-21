package backend;

import java.util.ArrayList;

public class OS {

    int globalTime = 0;

    Mutex userInput;
    Mutex userOutput;
    Mutex file;

    Memory memory;
    Scheduler scheduler;
    SystemCalls sys;
    Interpreter interpreter;

    ArrayList<Process> processes;
    String schedulerAlgorithm;
    int quantum;
    boolean finished;
    Process currentProcess;
    int nextPid;
    String lastExecutedInstruction = "-";
    String readyQueueSnapshot = "[]";
    String blockedQueueSnapshot = "[]";
    String pq0Snapshot = "[]";
    String pq1Snapshot = "[]";
    String pq2Snapshot = "[]";
    String pq3Snapshot = "[]";

    public OS(String schedulerAlgorithm, int quantum) {
        this.schedulerAlgorithm = schedulerAlgorithm;
        this.quantum = quantum;

        this.globalTime = 0;
        this.finished = false;
        this.nextPid = 1;

        this.userInput = new Mutex("userInput");
        this.userOutput = new Mutex("userOutput");
        this.file = new Mutex("file");

        this.memory = new Memory();
        this.scheduler = new Scheduler();
        this.scheduler.setQuantum(quantum); // very important
        this.sys = new SystemCalls();
        this.interpreter = new Interpreter(sys, memory, this);

        this.processes = new ArrayList<>();
    }

    public int getQuantum() {
        return scheduler.getQuantum();
    }

    public int getUsedTime() {
        return scheduler.getUsedTime();
    }

    public String getSchedulerAlgorithm() {
        return schedulerAlgorithm;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public String getCurrentInstructionSnapshot() {
        return lastExecutedInstruction == null ? "-" : lastExecutedInstruction;

    }

    private void captureQueueSnapshots() {
        readyQueueSnapshot = scheduler.readyQueueAsString();
        blockedQueueSnapshot = scheduler.blockedQueueAsString();
        pq0Snapshot = scheduler.pq0AsString();
        pq1Snapshot = scheduler.pq1AsString();
        pq2Snapshot = scheduler.pq2AsString();
        pq3Snapshot = scheduler.pq3AsString();
    }

    public void setupSimulation(int p1Arrival, int p2Arrival, int p3Arrival, String algorithm, int quantum) {
        globalTime = 0;
        finished = false;
        nextPid = 1;
        currentProcess = null;

        memory = new Memory();
        scheduler = new Scheduler();
        scheduler.setQuantum(quantum); // IMPORTANT
        sys = new SystemCalls();

        userInput = new Mutex("userInput");
        userOutput = new Mutex("userOutput");
        file = new Mutex("file");
        readyQueueSnapshot = "[]";
        blockedQueueSnapshot = "[]";
        pq0Snapshot = "[]";
        pq1Snapshot = "[]";
        pq2Snapshot = "[]";
        pq3Snapshot = "[]";

        interpreter = new Interpreter(sys, memory, this);
        lastExecutedInstruction = "-";
        processes = new ArrayList<>();
        processes.add(new Process(p1Arrival, 1));
        processes.add(new Process(p2Arrival, 2));
        processes.add(new Process(p3Arrival, 3));
        System.out.println("setupSimulation received:");
        System.out.println("p1Arrival = " + p1Arrival);
        System.out.println("p2Arrival = " + p2Arrival);
        System.out.println("p3Arrival = " + p3Arrival);
        schedulerAlgorithm = algorithm;
        this.quantum = quantum;
    }

    public void updateAllProcessesInMemory() {
        for (Process p : processes) {
            if (p != null && p.pcb != null && p.inMemory) {
                memory.updateMemory(p);
            }
        }
    }

    public void runAll() {
        while (!finished) {
            runOneStep();

            try {
                Thread.sleep(1000); // optional delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void runOneStep() {
        if (finished) {
            return;
        }

        ArrayList<Process> toRemove = new ArrayList<>();

        System.out.println("===================================================");
        System.out.println();
        System.out.println("Global time: " + globalTime);

        for (Process p : processes) {

            if (p.arrivalTime == globalTime && p.pcb == null) {
                System.out.println(p + " arrived");

                PCB pcb1 = new PCB(p.orderNo);
                p.pcb = pcb1;

                p.instructions = interpreter.readProgramFile("program" + p.orderNo + ".txt");

                if (!memory.allocateProcess(p)) {
                    while (!memory.swap(p)) {
                        // keep swapping until space is found
                    }
                }

                scheduler.addProcess(p, globalTime);

                if (p.pcb != null && p.inMemory) {
                    memory.updateMemory(p);
                }
            }

            if (p.pcb != null
                    && p.arrivalTime != globalTime
                    && (p.pcb.processState.equals(ProcessState.BLOCKED)
                            || p.pcb.processState.equals(ProcessState.READY))) {

                p.waitingTime++;
                System.out.println("at process: " + p + " global time incremented: " + globalTime);
            }
        }

        /*
         * IMPORTANT: capture queue state here, before scheduler may remove a new
         * process from PQ0
         */
        captureQueueSnapshots();

        currentProcess = scheduler.SchedulingAlgorithm(schedulerAlgorithm, globalTime);
        System.out.println("Process returned from scheduler: " + currentProcess);

        if (currentProcess != null) {
            if (!currentProcess.inMemory) {
                memory.d.removeProcess(currentProcess);

                if (!memory.allocateProcess(currentProcess)) {
                    while (!memory.swap(currentProcess)) {
                        // keep swapping until space is found
                    }
                }
            }

            currentProcess.inMemory = true;

            String instruction = memory.getInstruction(currentProcess);
            lastExecutedInstruction = (instruction == null) ? "-" : instruction;
            interpreter.ExecuteInstruction(currentProcess, instruction);
            currentProcess.pcb.programCounter++;

            if (currentProcess.isCompleted()) {
                System.out.println(currentProcess + " terminated");
                memory.deallocate(currentProcess);
                scheduler.removeTerminatedProcess(currentProcess);
                toRemove.add(currentProcess);
            } else if (schedulerAlgorithm.equals("MultilevelFeedbackQueue")) {
                scheduler.updateMLFQ(currentProcess, globalTime);
            } else if (schedulerAlgorithm.equals("RoundRobin")) {
                scheduler.usedTime++;
            }
        }

        processes.removeAll(toRemove);

        updateAllProcessesInMemory();

        if (processes.isEmpty()) {
            finished = true;
        }

        globalTime++;
    }

    public void updateAllProcessesInMemory(ArrayList<Process> processes) { // new updates all porcesses not just current
                                                                           // one
        for (Process p : processes) {
            if (p != null && p.pcb != null && p.inMemory) {
                memory.updateMemory(p);
            }
        }
    }
    // public void run() {
    // Process p1 = new Process(0,1);
    // Process p2 = new Process(1,2);
    // Process p3 = new Process(0,3);
    //
    // ArrayList<Process> processes = new ArrayList<>();
    // processes.add(p1);
    // processes.add(p2);
    // processes.add(p3);
    // String SchedulerAlgorithm = "";
    // Scanner sc = new Scanner(System.in); //////remove , through
    // gui!!!!!!!!!!!!!!!!!!!!!
    // System.out.println("Enter the scheduling algorithm (RoundRobin, HRRN,
    // MultilevelFeedbackQueue): ");//MIGHT NEED TO REMOVE //////remove , through
    // gui!!!!!!!!!!!!!!!!!!!!!
    // SchedulerAlgorithm = sc.nextLine();//////remove , through
    // gui!!!!!!!!!!!!!!!!!!!!!
    //
    //
    // while (!processes.isEmpty()) { //////runs as long as there are processes
    // still not created or not terminated
    // ArrayList<Process> toRemove = new ArrayList<>();
    // System.out.println("===================================================");
    // System.out.println();
    // System.out.println("Global time: " +globalTime);
    //
    // for (Process p : processes) {
    //
    // if (p.arrivalTime == globalTime && p.pcb == null) {
    // System.out.println(p +" arrived");
    //
    // PCB pcb1 = new PCB(p.orderNo);
    // p.pcb = pcb1;
    // System.out.println(p.orderNo);
    // p.instructions = interpreter.readProgramFile("test
    // programs/program"+p.pcb.processID+".txt"); //i just want to try it so i chose
    // program 1 !! TEMPORARY!!
    // // later replace "null" with actual program file name
    //
    // if(!memory.allocateProcess(p)) {
    // // swapping
    // while(!memory.swap(p));
    // }
    //
    //
    //
    // scheduler.addProcess(p, globalTime);
    // if(p.pcb!=null && p.inMemory){
    // memory.updateMemory(p);
    // }
    // }
    //
    // if(p.pcb!=null&&p.arrivalTime!=globalTime &&(
    // p.pcb.processState.equals(ProcessState.BLOCKED)||p.pcb.processState.equals(ProcessState.READY))){
    // p.waitingTime++;
    // System.out.println("at process: "+p + "global time incremented : "+
    // globalTime);
    // }
    // }
    // memory.printMemory();
    // System.out.println(memory.d);
    // System.out.println("-----------------");
    //
    //
    //
    // Process currentProcess = scheduler.SchedulingAlgorithm(SchedulerAlgorithm ,
    // globalTime); ///returns the process that should run
    // System.err.println("Process returned from scheduler: " + currentProcess);
    // if (currentProcess != null) {
    // if(!currentProcess.inMemory){ ///if process not in memory , ie in disk
    // memory.d.removeProcess(currentProcess); //remove it from disk
    // if(!memory.allocateProcess(currentProcess)) //try to allocate in memeory if
    // no space
    // while(!memory.swap(currentProcess)); //swap and keep swaping until a free
    // space is found
    // }
    // currentProcess.inMemory=true;
    // String instruction = memory.getInstruction(currentProcess); //get next
    // instruction to execute
    // System.out.println("Instruction executing: " + instruction);
    // interpreter.ExecuteInstruction(currentProcess, instruction); //execute this
    // instruction
    // currentProcess.pcb.programCounter++;
    //
    // if(currentProcess.isCompleted()) { // if the process has completed all its
    // instructions, deallocate its memory, remove it from the scheduler and from
    // the list of processes
    // System.out.println(currentProcess+" terminated");
    // memory.deallocate(currentProcess); //when it is done it should be removed
    // from memory but we don't have a deallocate process method yet
    // scheduler.removeTerminatedProcess(currentProcess); //remove from all queues +
    // chnage state to terminated
    // toRemove.add(currentProcess);
    // }
    // else if (SchedulerAlgorithm.equals("MultilevelFeedbackQueue")) { // if we are
    // using MLFQ, after the process is done executing, we need to update its
    // position in the correct queue according to how much time it has used in the
    // current queue level and whether it is blocked or not
    // scheduler.updateMLFQ(currentProcess, globalTime);
    // }
    // else if (SchedulerAlgorithm.equals("RoundRobin")){
    // scheduler.usedTime++;
    // }
    // }
    //
    // processes.removeAll(toRemove); // remove completed processes from the list of
    // processes to check
    // globalTime++;
    // updateAllProcessesInMemory(processes); //update all porcesses not only
    // current process
    // try {
    // Thread.sleep(1000); // 1000 ms = 1 second delay
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }
    // }

    /////////// semwait
    public void semWait(Process p, String resourceName) {
        Mutex mutex = getMutexByName(resourceName);

        if (mutex == null) {
            System.out.println("Invalid resource name: " + resourceName);
            return;
        }

        boolean acquired = mutex.semWait(p); // tries to acquire the resource, if it is not available, it blocks the
                                             // process and adds it to the mutex's blocked queue

        if (!acquired) {
            /// the proces state is already set to blocked in mutex's semwait
            scheduler.removeBlockedProcess(p); // if the process is blocked, remove it from the scheduler so it won't be
                                               // scheduled to run until it is unblocked
        } ////// THIS removes the process from the ready queues + add it to the blocked
          ////// queue + change its state to blocked
    }

    /////////// semsignal
    public void semSignal(Process p, String resourceName) {
        Mutex mutex = getMutexByName(resourceName);

        if (mutex == null) {
            System.out.println("Invalid resource name: " + resourceName);
            return;
        }

        Process unblockedProcess = mutex.semSignal(p); // unblocks the next process waiting for the resource, if there
                                                       // is one, and returns it

        if (unblockedProcess != null) {
            scheduler.addUnblockedProcess(unblockedProcess, globalTime); // adds the unblocked process back to the
                                                                         // scheduler so it can be scheduled to run
            // + remove from blocked queue + change state to ready
        }
    }

    /////////// getting mutex for semwait and semsignal
    private Mutex getMutexByName(String resourceName) {
        switch (resourceName) {
            case "userInput":
                return userInput;
            case "userOutput":
                return userOutput;
            case "file":
                return file;
            default:
                return null;
        }
    }

    public int getGlobalTime() {
        return globalTime;
    }

    public void incrementGlobalTime() {
        globalTime++;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getMemorySnapshot() {
        return memory.memoryAsString();
    }

    public String getDiskSnapshot() {
        return memory.d.toString();
    }

    public String getReadyQueueSnapshot() {
    return readyQueueSnapshot;
    }

    public String getBlockedQueueSnapshot() {
        return blockedQueueSnapshot;
    }

    public String getPQ0Snapshot() {
        return pq0Snapshot;
    }

    public String getPQ1Snapshot() {
        return pq1Snapshot;
    }

    public String getPQ2Snapshot() {
        return pq2Snapshot;
    }

    public String getPQ3Snapshot() {
        return pq3Snapshot;
    }

    public String getRunningProcessSnapshot() {
        if (currentProcess == null || currentProcess.pcb == null) {
            return "-";
        }
        return "P" + currentProcess.pcb.processID;
    }

    public String getProcessStateSnapshot(int pid) {
        for (Process p : processes) {
            if (p.pcb != null && p.pcb.processID == pid) {
                return p.pcb.processState.toString();
            }
        }
        return "NOT CREATED / FINISHED";
    }

    public String getAlgorithm() {
        return schedulerAlgorithm;
    }

}
