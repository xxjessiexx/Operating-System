import java.util.ArrayList;

public class OS {

    public static void main(String[] args) {

        int globalTime = 0;

        Process p1 = new  Process (0);
        Process p2 = new  Process (1);
        Process p3 = new  Process (4);

        Memory memory = new Memory();

        Scheduler scheduler = new Scheduler();
        SystemCalls sys = new SystemCalls();

        Mutex mutex1 = new Mutex("userInput");
        Mutex mutex2 = new Mutex("userOutput");
        Mutex mutex3 = new Mutex("file");

        Interpreter interpreter = new Interpreter(sys,memory);

        ArrayList<Process> processes = new ArrayList<>();

        int pid=1;
        processes.add(p1);
        processes.add(p2);
        processes.add(p3);
        
        while(!processes.isEmpty()) {
            for (Process p : processes) {
                if (p.arrivalTime == globalTime) {
                    PCB pcb1 = new PCB(pid);
                    p.pcb = pcb1;
                    p.instructions = interpreter.readFile("null");   //add att of file name to each process?
                    
                    if(!memory.allocateProcess(p)){
                        //swapping
                    }
            }
           
        }
    }
        //p1 new process with arrival time 0
        // process p1 = new Process(0);
        //p1.pcb = new PCB(i,"ready",0,0,0);
        //p1.instructions = interpreter.readFile("file.txt"); -- read the instructions from a file and store them in the process
        //
        //scheduler.addProcess(p1); -- add the process to the scheduler's ready queue
        //--LOOP--
        //px = scheduler.getNextProcess(); -- gets the next process from the scheduler
        //interpreter.ExecuteInstruction(px.getNextInstruction()); -- execute the next instruction of the process using the interpreter
    }
    public int getGlobalTime() {
        return 0;
    }
    public void incrementGlobalTime() {
        this.globalTime++;
    }
}
