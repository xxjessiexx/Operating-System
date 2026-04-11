public class MemoryWord {
    String name;
    Object value;

    public MemoryWord(String key, Object value) {     //was empty, takes now the key and the value
        this.name = key;
        this.value = value;
    }

    public MemoryWord(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        return name + " = " + value;
    }
}
