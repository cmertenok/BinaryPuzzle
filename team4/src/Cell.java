public class Cell {
    private int row;
    private int column;
    private String value;

    public Cell() {
        this.value = "_";
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
        this.value = value;
    }

    public boolean isEmpty() {
        return this.value.equals("_");
    }

    @Override
    public String toString() {
        return String.format("Value: %s", getValue());
    }
}
