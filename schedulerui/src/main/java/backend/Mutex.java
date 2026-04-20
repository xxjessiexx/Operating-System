package backend;

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
            System.out.println("Process " + owner + " has locked " + resourceName);
            return true;

        } else {
            process.pcb.processState = ProcessState.BLOCKED;
            // set the process state to blocked and add it to the blocked queue for this mutex
            if (!blockedQueue.contains(process)) {
                blockedQueue.add(process);
            }
            System.out.println("mutex's blocked queue: " + blockedQueue);
            return false;

        }

    }

    public Process semSignal(Process process) {
        //////return process that will accquire resource else null if no process is locking resource
    if (owner != process) {
            System.out.println("Process " + process.pcb.processID
                    + " cannot release resource " + resourceName + " because it is not the owner.");
            return null;
        }

        if (blockedQueue.isEmpty()) {
            locked = false;
            owner = null;
            System.out.println(resourceName + " released");
            return null;
        } else {
            //////change state here or in scheduler????????????????????????????????????????????????????????/
        Process nextProcess = blockedQueue.poll();
            owner = nextProcess;
            System.out.println(resourceName + " released" + " new owner " + owner);
            return nextProcess;
        }
    }

    

}
