package src;
import java.util.LinkedList;
import java.util.Queue;

public class Mutex {
    String resourceName;
    boolean locked;
    Process owner;    //process who is currently holding the rescource
    Queue<Process> blockedQueue;

    public Mutex(String resourceName) {
        this.resourceName = resourceName;
        this.locked = false;
        this.owner = null;
        this.blockedQueue = new LinkedList<>();
    }

   
    public boolean semWait(Process process) {   //i need to lock resource return true if i accquired rescource else false (resource already locked)
        if (!locked) {
            locked = true;
            owner = process;
            return true;
        } else {
             // set the process state to blocked and add it to the blocked queue for this mutex
            blockedQueue.add(process);
            return false;
        }
    }

    
    public Process semSignal(Process process) {       //////return process that will accquire resource else null if no process is locking resource
    if (owner != process) {
        System.out.println("Process " + process.pcb.processID +
                " cannot release resource " + resourceName + " because it is not the owner.");
        return null;
    }

    if (blockedQueue.isEmpty()) {
        locked = false;
        owner = null;
        return null;
    } else {       //////change state here or in scheduler????????????????????????????????????????????????????????/
        Process nextProcess = blockedQueue.poll();
        owner = nextProcess;
        return nextProcess;
    }
}

}