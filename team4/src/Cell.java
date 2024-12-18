public class Cell {
    private String value;
    private boolean isLocked;

    public Cell() {
        this.value = "_";
        this.isLocked = false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (isLocked) {
            return;
        }
        this.value = value;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isEmpty() {
        return this.value.equals("_");
    }

    @Override
    public String toString() {
        return String.format("%s", getValue());
    }
}
