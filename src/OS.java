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
    Process p3 = new Process(4);

    ArrayList<Process> processes = new ArrayList<>();
    processes.add(p1);
    processes.add(p2);
    processes.add(p3);
    String SchedulerAlgorithm = ""; 
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the scheduling algorithm (RoundRobin, HRRN, MultilevelFeedbackQueue): ");//MIGHT NEED TO REMOVE
    SchedulerAlgorithm = sc.nextLine();

    int pid = 1;

    while (!processes.isEmpty()) {
        ArrayList<Process> toRemove = new ArrayList<>();

        for (Process p : processes) {

            if (p.arrivalTime == globalTime && p.pcb == null) {
                PCB pcb1 = new PCB(pid++);
                p.pcb = pcb1;

                p.instructions = interpreter.readProgramFile("null"); 
                // later replace "null" with actual program file name

                if (!memory.allocateProcess(p)) {
                    // swapping
                }

                scheduler.addProcess(p, globalTime);
            }
        }

        Process currentProcess = scheduler.SchedulingAlgorithm(SchedulerAlgorithm , globalTime);
        if (currentProcess != null) {
            String instruction = memory.getInstruction(currentProcess);
            interpreter.ExecuteInstruction(currentProcess, instruction);
            currentProcess.pcb.programCounter++;

            if(currentProcess.isCompleted()) { // if the process has completed all its instructions, deallocate its memory, remove it from the scheduler and from the list of processes
                currentProcess.pcb.processState = ProcessState.TERMINATED;
               // memory.deallocateProcess(p); when it is done it should be removed from memory but we don't have a deallocate process method yet
                scheduler.removeProcess(currentProcess);
                toRemove.add(currentProcess);
            }
            else if (SchedulerAlgorithm.equals("MultilevelFeedbackQueue")) {
                scheduler.updateMLFQ(currentProcess, globalTime);
         }
        }

        processes.removeAll(toRemove);
        globalTime++;
    }
}
    
    public int getGlobalTime() {
        return globalTime;
    }

    public void incrementGlobalTime() {
        globalTime++;
    }

   
public void semWait(Process p, String resourceName) {
    Mutex mutex = getMutexByName(resourceName);

    if (mutex == null) {
        System.out.println("Invalid resource name: " + resourceName);
        return;
    }

    boolean acquired = mutex.semWait(p);

    if (!acquired) {
        scheduler.removeProcess(p);
    }
}
    
 public void semSignal(Process p, String resourceName) {
    Mutex mutex = getMutexByName(resourceName);

    if (mutex == null) {
        System.out.println("Invalid resource name: " + resourceName);
        return;
    }

    Process unblockedProcess = mutex.semSignal(p);

    if (unblockedProcess != null) {
        scheduler.addProcess(unblockedProcess, globalTime);
    }
}
  
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
}