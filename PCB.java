

import java.io.Serializable;

public class PCB {

    int processID;
    ProcessState processState;
    int programCounter;
    int memStart;
    int memEnd;

    public PCB(int pid) {   //change add pid , it was always setting it to zero
        this.processID = pid;
        this.processState = ProcessState.NEW;
        this.programCounter = 0;
        this.memStart = 0;
        this.memEnd = 0;
    }

    
    public PCB(int processID, ProcessState processState, int programCounter, int memStart, int memEnd) {
        this.processID = processID;
        this.processState = processState;
        this.programCounter = programCounter;
        this.memStart = memStart;
        this.memEnd = memEnd;
    }

   
    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public ProcessState getProcessState() {
        return processState;
    }

    public void setProcessState(ProcessState processState) {
        this.processState = processState;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int getMemStart() {
        return memStart;
    }

    public void setMemStart(int memStart) {
        this.memStart = memStart;
    }

    public int getMemEnd() {
        return memEnd;
    }

    public void setMemEnd(int memEnd) {
        this.memEnd = memEnd;
    }

    @Override
    public String toString() {
        return "PCB{" +
                "PID=" + processID +
                ", State=" + processState +
                ", PC=" + programCounter +
                ", MemStart=" + memStart +
                ", MemEnd=" + memEnd +
                '}';
    }
}

