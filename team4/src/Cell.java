public class Cell {
    private int row;
    private int column;
    private String value;
    private boolean isLocked;

    public Cell() {
        this.value = "_";
        this.isLocked = false;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
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
