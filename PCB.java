

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

