package com.example.androidgames;

public class Position {
    private int row;
    private int column;
    String type;

    public Position(int row, int column, String type) {
        this.row = row;
        this.column = column;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Row: " + row + " Column: " + column + " Type: " + type;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getType() {
        return type;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setType(String type) {
        this.type = type;
    }
}
