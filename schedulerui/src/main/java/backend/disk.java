package backend;

public class disk {

    Process[] Disk;
    int index;

    public disk() {
        Disk = new Process[50];
        index = 0;
    }

    public void addtoDisk(Process p) {
        Disk[index] = p;
        index++;
    }

    public void removeProcess(Process p) {
        for (int i = 0; i < index; i++) {
            if (Disk[i].equals(p)) {

                // Shift elements
                for (int j = i; j < index - 1; j++) {
                    Disk[j] = Disk[j + 1];
                }

                // Remove last duplicate (now shifted)
                Disk[index - 1] = null;

                // Decrease size
                index--;

                return; // stop after removing
            }
        }
    }

    public String toString() {

        StringBuilder res = new StringBuilder("----DISK-----\n");
        for (int i = 0; i < index; i++) {
            Process p = Disk[i];
            if (p == null || p.pcb == null) {
                continue;
            }

            res.append("PID = ").append(p.pcb.processID).append("\n");
            res.append("State = ").append(p.pcb.processState).append("\n");
            res.append("PC = ").append(p.pcb.programCounter).append("\n");
            res.append("MemStart = ").append(p.pcb.memStart).append("\n");
            res.append("MemEnd = ").append(p.pcb.memEnd).append("\n");
            res.append("arrivalTime = ").append(p.arrivalTime).append("\n");
            res.append("waitingTime = ").append(p.waitingTime).append("\n");

            if (p.instructions != null) {
                for (int j = 0; j < p.instructions.size(); j++) {
                    res.append("Instruction_").append(j).append(" = ").append(p.instructions.get(j)).append("\n");
                }
            }

            for (int j = 0; j < 3; j++) {
                String variableName = p.variableNames[j] != null ? p.variableNames[j] : "Var_" + (j + 1);
                Object variableValue = p.variableValues[j];
                res.append(variableName).append(" = ").append(variableValue).append("\n");
            }

            res.append("-------------\n");
        }
        return res.toString();
    }
}
