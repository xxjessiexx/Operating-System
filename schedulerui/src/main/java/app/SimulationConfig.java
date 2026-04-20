package app;

public class SimulationConfig {
    private int p1Arrival;
    private int p2Arrival;
    private int p3Arrival;
    private String algorithm;
    private int quantum;

    public int getP1Arrival() {
        return p1Arrival;
    }

    public void setP1Arrival(int p1Arrival) {
        this.p1Arrival = p1Arrival;
    }

    public int getP2Arrival() {
        return p2Arrival;
    }

    public void setP2Arrival(int p2Arrival) {
        this.p2Arrival = p2Arrival;
    }

    public int getP3Arrival() {
        return p3Arrival;
    }

    public void setP3Arrival(int p3Arrival) {
        this.p3Arrival = p3Arrival;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }
}