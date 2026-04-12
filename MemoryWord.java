public class MemoryWord {
    String name;
    Object value;


    public MemoryWord(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    
    public String toString() {
        return name + " = " + value;
    }
}
