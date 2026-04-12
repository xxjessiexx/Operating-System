import java.util.LinkedList;
import java.util.Queue;

public class Mutex {
    String resourceName;
    boolean locked;
    Process owner;
    Queue<Process> blockedQueue;

    public Mutex(String resourceName) {
        this.resourceName = resourceName;
        this.locked = false;
        this.owner = null;
        this.blockedQueue = new LinkedList<>();
    }

    public boolean semWait(Process process) {
        if (!locked) {
            locked = true;
            owner = process;
            return true;
        } else {
            process.pcb.processState=ProcessState.BLOCKED;
            blockedQueue.add(process);
            return false;
        }
    }

    public Process semSignal(Process process) {
        if (owner != process) {
            System.out.println("Process " + process.pcb.processID +
                    " cannot release resource " + resourceName + " because it is not the owner.");
            return null;
        }

        if (blockedQueue.isEmpty()) {
            locked = false;
            owner = null;
            return null;
        } else {
            Process nextProcess = blockedQueue.poll();
            owner = nextProcess;
            nextProcess.pcb.processState=ProcessState.READY;
            return nextProcess;
        }
    }

}