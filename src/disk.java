package src;
public class disk {
    Process[] Disk;
    int index;


    public disk(){
        Disk = new Process[50];
        index=0;
    }

    public void addtoDisk(Process p){
        Disk[index]=p;
        index++;
    }

    public void removeProcess(Process p){
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

    public String toString(){
        
        String res="----DISK-----"+ "\n";
        for(int i=0; i<index;i++){
            res+= Disk[i] +"\n";
        }
        res+= "-------------" +"\n";
        return res;
    }
}
