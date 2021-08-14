package com.example.multiplayerdotandbox.game.GameModelView;

import com.example.multiplayerdotandbox.game.controllerss.Player;

public class Grid {
    private final int rows;
    private final int columns;

    private enum Line {
        LEFT, RIGHT, TOP, BOTTOM
    }

    private final byte[][] boxes;

    public Grid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        boxes = new byte[rows][];
        for (int i = 0; i < rows; i++)
            boxes[i] = new byte[columns];
    }

    public Box getBoxAt(int row, int column) {
        return new Box(boxes[row][column]);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int setLineForDots(int dotStart, int dotEnd, int player) {
        int rowStart = dotStart / (columns + 1);
        int rowEnd = dotEnd / (columns + 1);
        int columnStart = dotStart % (columns + 1);
        int columnEnd = dotEnd % (columns + 1);

        return toggleLine(true, rowStart, columnStart, rowEnd, columnEnd, player);
    }

    private int setLineAtBox(int row, int column, Line line, int player) {
        switch (line) {
            case TOP:
                boxes[row][column] |= 1;
                break;
            case RIGHT:
                boxes[row][column] |= 2;
                break;
            case BOTTOM:
                boxes[row][column] |= 4;
                break;
            case LEFT:
                boxes[row][column] |= 8;
                break;
        }

        if ((boxes[row][column] & 15) == 15) {
            if (player == Player.PLAYER1) {
                boxes[row][column] |= 16;
                boxes[row][column] &= ~32;
            } else if (player == Player.PLAYER2) {
                boxes[row][column] |= 32;
                boxes[row][column] &= ~16;
            }
            return 1;
        }

        return 0;
    }

    private void unsetLineAtBox(int row, int column, Line line) {
        switch (line) {
            case TOP:
                boxes[row][column] &= (1) ^ 0xFF;
                break;
            case RIGHT:
                boxes[row][column] &= (1 << 1) ^ 0xFF;
                break;
            case BOTTOM:
                boxes[row][column] &= (1 << 2) ^ 0xFF;
                break;
            case LEFT:
                boxes[row][column] &= (1 << 3) ^ 0xFF;
                break;
        }

        boxes[row][column] &= (1 << 4) ^ 0xFF;
        boxes[row][column] &= (1 << 5) ^ 0xFF;
    }

    private int toggleLine(boolean set, int rowStart, int columnStart, int rowEnd, int columnEnd, int player) {
        if (columnStart < 0 || columnStart > columns ||
                columnEnd < 0 || columnEnd > columns ||
                rowStart < 0 || rowStart > rows ||
                rowEnd < 0 || rowEnd > rows)
            return 0;

        int row = rowStart < rowEnd ? rowStart : rowEnd;
        int column = columnStart < columnEnd ? columnStart : columnEnd;

        if (rowStart == rowEnd && columnStart != columnEnd) {
            if (row == 0) {
                if (set)
                    return setLineAtBox(row, column, Line.TOP, player);
                else
                    unsetLineAtBox(row, column, Line.TOP);
            } else if (row == rows) {
                if (set)
                    return setLineAtBox(row - 1, column, Line.BOTTOM, player);
                else
                    unsetLineAtBox(row - 1, column, Line.BOTTOM);
            } else {
                if (set) {
                    return setLineAtBox(row, column, Line.TOP, player) +
                            setLineAtBox(row - 1, column, Line.BOTTOM, player);
                } else {
                    unsetLineAtBox(row, column, Line.TOP);
                    unsetLineAtBox(row - 1, column, Line.BOTTOM);
                }
            }
        } else if (columnStart == columnEnd && rowStart != rowEnd) {
            if (column == 0) {
                if (set)
                    return setLineAtBox(row, column, Line.LEFT, player);
                else
                    unsetLineAtBox(row, column, Line.LEFT);
            } else if (column == columns) {
                if (set)
                    return setLineAtBox(row, column - 1, Line.RIGHT, player);
                else
                    unsetLineAtBox(row, column - 1, Line.RIGHT);
            } else {
                if (set) {
                    return setLineAtBox(row, column - 1, Line.RIGHT, player) +
                            setLineAtBox(row, column, Line.LEFT, player);
                } else {
                    unsetLineAtBox(row, column - 1, Line.RIGHT);
                    unsetLineAtBox(row, column, Line.LEFT);
                }
            }
        }

        return 0;
    }

    public int getScore(int player) {
        int score = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (player == Player.PLAYER1 && (boxes[i][j] & 16) == 16)
                    score++;
                else if (player == Player.PLAYER2 && (boxes[i][j] & 32) == 32)
                    score++;

        return score;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                s.append(Integer.toString(boxes[i][j]));
                s.append(",");
            }
        }
        return s.toString();
    }

    public void loadBoard(String boardValues) {

        String[] values = boardValues.split(",");
        int countValues = 0;
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                boxes[i][j] = Byte.valueOf(values[countValues]);
                countValues++;
            }
        }
    }

    public Grid getCopy() {
        Grid result = new Grid(getRows(), getColumns());
        result.loadBoard(this.toString());
        return result;
    }

}
