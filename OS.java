import java.util.ArrayList;

public class OS {
     public Integer globalTime = 0;

    public static void main(String[] args) {
        Process p1 = new  Process (0);
        Process p2 = new  Process (1);
        Process p3 = new  Process (4);
        Memory memory = new Memory();

        Scheduler scheduler = new Scheduler();

        Mutex mutex = new Mutex();

        Interpreter interpreter = new Interpreter();
        ArrayList<Process> processes = new ArrayList<>();
        int i=1;
        processes.add(p1);
        processes.add(p2);
        processes.add(p3);
        
        while(!processes.isEmpty()) {
            for (Process p : processes) {
                if (p.arrivalTime == globalTime) { {
                    PCB p1 = new PCB(i,"ready",)
            }
           
        }

        
    }
    public int getGlobalTime() {
        return 0;
    }
    public void incrementGlobalTime() {
        this.globalTime++;
    }
}
