public class PCB {

    int processID;
    String processState;
    int programCounter;
    int MemInt;
    int MemFinal;
    public PCB() {
        this.processID = 0;
        this.processState = "New";
        this.programCounter = 0;
        this.MemInt = 0;
        this.MemFinal = 0;
    }

    public PCB(int processID, String processState, int programCounter, int MemInt, int MemFinal) {
        this.processID = processID;
        this.processState = processState;
        this.programCounter = programCounter;
        this.MemInt = MemInt;
        this.MemFinal = MemFinal;
    }

}