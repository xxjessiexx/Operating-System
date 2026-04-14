package src;
import java.util.ArrayList;
import java.util.Scanner;
public class OS {

    int globalTime = 0;

    Mutex userInput;
    Mutex userOutput;
    Mutex file;

    Memory memory;
    Scheduler scheduler;
    SystemCalls sys;
    Interpreter interpreter;

    public OS() {
        memory = new Memory();
        scheduler = new Scheduler();
        sys = new SystemCalls();

        userInput = new Mutex("userInput");
        userOutput = new Mutex("userOutput");
        file = new Mutex("file");

        interpreter = new Interpreter(sys, memory, this);
    }

    
    public static void main(String[] args) {
        OS os = new OS();
        os.run();

    }
public void run() {
    Process p1 = new Process(0);
    Process p2 = new Process(1);
    //Process p3 = new Process(4);

    ArrayList<Process> processes = new ArrayList<>();
    processes.add(p1);
    processes.add(p2);
    //processes.add(p3);
    String SchedulerAlgorithm = "";
    Scanner sc = new Scanner(System.in); //////remove , through gui!!!!!!!!!!!!!!!!!!!!!
    System.out.println("Enter the scheduling algorithm (RoundRobin, HRRN, MultilevelFeedbackQueue): ");//MIGHT NEED TO REMOVE //////remove , through gui!!!!!!!!!!!!!!!!!!!!!
    SchedulerAlgorithm = sc.nextLine();//////remove , through gui!!!!!!!!!!!!!!!!!!!!!

    int pid = 1;

    while (!processes.isEmpty()) {       //////runs as long as there are processes still not created or not terminated
        ArrayList<Process> toRemove = new ArrayList<>();

        for (Process p : processes) {

            if (p.arrivalTime == globalTime && p.pcb == null) {
                PCB pcb1 = new PCB(pid);
                p.pcb = pcb1;

                p.instructions = interpreter.readProgramFile("test programs/program"+pid+".txt"); //i just want to try it so i chose program 1 !! TEMPORARY!!
                // later replace "null" with actual program file name
                pid++;
                if(!memory.allocateProcess(p)) {
                    // swapping
                    while(!memory.swap(p));
                }

                scheduler.addProcess(p, globalTime);
            }
        }

        Process currentProcess = scheduler.SchedulingAlgorithm(SchedulerAlgorithm , globalTime);  ///returns the process that should run

        if (currentProcess != null) {
            if(!currentProcess.inMemory){      ///if process not in memory , ie in disk
                memory.d.removeProcess(currentProcess);   //remove it from disk 
                if(!memory.allocateProcess(currentProcess))     //try to allocate in memeory if no space
                    while(!memory.swap(currentProcess));          //swap and keep swaping until a free space is found
            }
            currentProcess.inMemory=true;
            String instruction = memory.getInstruction(currentProcess); //get next instruction to execute
            interpreter.ExecuteInstruction(currentProcess, instruction); //execute this instruction 
            currentProcess.pcb.programCounter++;

            if(currentProcess.isCompleted()) { // if the process has completed all its instructions, deallocate its memory, remove it from the scheduler and from the list of processes
            
               // memory.deallocateProcess(p); when it is done it should be removed from memory but we don't have a deallocate process method yet
                scheduler.removeTerminatedProcess(currentProcess);  //remove from all queues + chnage state to terminated
                toRemove.add(currentProcess);
            }
            else if (SchedulerAlgorithm.equals("MultilevelFeedbackQueue")) { // if we are using MLFQ, after the process is done executing, we need to update its position in the correct queue according to how much time it has used in the current queue level and whether it is blocked or not
                scheduler.updateMLFQ(currentProcess, globalTime);
        }
        }

        processes.removeAll(toRemove); // remove completed processes from the list of processes to check
        globalTime++;
    }
}

///////////semwait
public void semWait(Process p, String resourceName) {
    Mutex mutex = getMutexByName(resourceName);

    if (mutex == null) {
        System.out.println("Invalid resource name: " + resourceName);
        return;
    }

    boolean acquired = mutex.semWait(p); // tries to acquire the resource, if it is not available, it blocks the process and adds it to the mutex's blocked queue

    if (!acquired) {
        ///the proces state is already set to blocked in mutex's semwait
        scheduler.removeBlockedProcess(p); // if the process is blocked, remove it from the scheduler so it won't be scheduled to run until it is unblocked
    }   //////THIS removes the process from the ready queues + add it to the blocked queue + change its state to blocked
}
    
///////////semsignal
public void semSignal(Process p, String resourceName) {
    Mutex mutex = getMutexByName(resourceName);

    if (mutex == null) {
        System.out.println("Invalid resource name: " + resourceName);
        return;
    }

    Process unblockedProcess = mutex.semSignal(p); // unblocks the next process waiting for the resource, if there is one, and returns it

    if (unblockedProcess != null) {
        scheduler.addUnblockedProcess(unblockedProcess, globalTime); // adds the unblocked process back to the scheduler so it can be scheduled to run
        //+ remove from blocked queue + change state to ready
    }
}
///////////getting mutex for semwait and semsignal
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



}