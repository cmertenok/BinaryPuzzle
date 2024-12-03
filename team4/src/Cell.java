public class Cell {
    private int row;
    private int column;
    private String value;

    public Cell() {
        this.value = "";
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

    @Override
    public String toString() {
        return String.format("Cell(%d,%d)\nValue: %s", getRow(), getColumn(), getValue());
    }
}
