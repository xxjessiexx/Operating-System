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
            process.getPcb().setProcessState(ProcessState.BLOCKED);
            blockedQueue.add(process);
            return false;
        }
    }

    public Process semSignal(Process process) {
        if (owner != process) {
            System.out.println("Process " + process.getPcb().getProcessID() +
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
            nextProcess.getPcb().setProcessState(ProcessState.READY);
            return nextProcess;
        }
    }

}
