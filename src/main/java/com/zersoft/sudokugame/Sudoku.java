package com.zersoft.sudokugame;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Sudoku implements Serializable {

    final private Set<Character> DEFAULT_ALPHABET = Set.of((char) 1, (char) 2, (char) 3, (char) 4, (char) 5, (char) 6, (char) 7, (char) 8, (char) 9);

    private Set<Character> alphabet;
    private char[][] table;

    /**
     * Default constructor<br>
     * Sets DEFAULT_ALPHABET as alphabet<br>
     * Creates table with DEFAULT_ALPHABET
     */
    public Sudoku () {
        alphabet = DEFAULT_ALPHABET;
        table = new char[alphabet.size()][alphabet.size()];

        table = defaultTableSetup(alphabet.size());
    }

    /**
     * @param alphabet
     * @throws Exception
     */
    public Sudoku (char[] alphabet) throws Exception {

        this.alphabet = new HashSet<>();

        for (char c : alphabet) {
            this.alphabet.add(c);
        }

        int sqrt = (int) Math.sqrt(this.alphabet.size());

        if ((sqrt * sqrt) != this.alphabet.size()) {
            throw new Exception("Tamaño de sudoku no válido");
        }

        table = defaultTableSetup(this.alphabet.size());
    }

    /**
     * @param table
     * @throws Exception
     */
    public Sudoku (char[][] table) throws Exception {
        setTable(table);
    }

    /**
     * @param size
     * @return
     */
    private char[][] defaultTableSetup (int size) {
        char[][] newTable = new char[size][size];

        for (int i = 0; i < alphabet.size(); i++) {
            for (int j = 0; j < alphabet.size(); j++) {
                newTable[i][j] = (char) 0;
            }
        }

        return newTable;
    }

    /**
     * @param table char[][] from where extract the alphabet
     * @return a Set of Characteres with complete alphabet
     */
    private Set<Character> extractAlphabet (char[][] table) {
        Set<Character> alphabetSet = new HashSet<>();

        for (char[] chars : table) {
            for (char cellValue : chars) {
                if (cellValue != (char) 0) {
                    alphabetSet.add(cellValue);
                }
            }
        }

        return alphabetSet;
    }

    /**
     * @return in Size of Sudoku
     */
    public int getSize () {
        return alphabet.size();
    }

    /**
     * @return char[][] table
     */
    public char[][] getTable () {
        return table;
    }

    /**
     * @param table Sets a char[][] table as new table
     */
    public void setTable (char[][] table) throws Exception {
        int len = table.length;
        int wid = table[0].length;

        if ((len * wid) != (len * len)) {
            throw new Exception("Tamaño de sudoku no válido");
        }

        this.table = table;

        alphabet = extractAlphabet(table);
    }

    /**
     * @param row       Position x on table
     * @param col       Position y on table
     * @param character Character to place on Position
     */
    public void setField (int row, int col, char character) {
        table[row][col] = character;
    }

    /**
     * @param row Position x on table
     * @param col Position y on table
     * @return Character on the determiend position
     */
    public char getField (int row, int col) {
        return table[row][col];
    }

    /**
     * @return int miniSudoku length
     */
    private int getChildSize () {
        return (int) Math.sqrt(table.length);
    }

    public boolean isComplete () {
        Point voidCell = getNextCellVoid();

        return voidCell.x == table.length + 1;

    }

    public boolean isValid () {

        // TODO: add isValid method

        return false;
    }

    private Point getNextCellVoid () {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++) {
                if (table[i][j] == (char) 0) {
                    return new Point(i, j);
                }
            }
        }
        return new Point(table.length + 1, table.length + 1);
    }

    /**
     * @param cell empty cell
     * @return List of Integer [x, y] where starts the miniSudoku
     */
    private List<Integer> getStartChildPos (Point cell) {

        List<Integer> startPos = new ArrayList<>();

        int childSize = getChildSize();
        int startX = (cell.x < childSize) ? 0 : (cell.x / childSize) * childSize;
        int startY = (cell.y < childSize) ? 0 : (cell.y / childSize) * childSize;

        startPos.add(startX);
        startPos.add(startY);

        return startPos;
    }

    /**
     * @param cell empty cell
     * @return child with contained cell
     */
    private char[][] getChild (Point cell) {
        char[][] child = new char[getChildSize()][getChildSize()];

        List<Integer> startPositions = getStartChildPos(cell);
        int startX = startPositions.get(0);
        int startY = startPositions.get(1);

        for (int i = 0; i < getChildSize(); i++) {
            for (int j = 0; j < getChildSize(); j++) {
                child[i][j] = table[startX + i][startY + j];
            }
        }

        return child;
    }

    /**
     * @param cell An empty cell Point on the table
     * @return Set of none placed characters in the row
     */
    private Set<Character> getRowPossibleChars (Point cell) {
        Set<Character> none = new HashSet<>(alphabet);

        for (char[] chars : table) {
            none.remove(chars[cell.y]);
        }

        return none;
    }

    /**
     * @param cell An empty cell point on the table
     * @return Set of none placed characters in the column
     */
    private Set<Character> getColPossibleChars (Point cell) {
        Set<Character> none = new HashSet<>(alphabet);

        for (char ch : table[cell.x]) {
            none.remove(ch);
        }

        return none;
    }

    /**
     * @param cell An empty cell point on the table
     * @return Set of none placed characters in the mini Sudoku
     */
    private Set<Character> getMiniPosibleChars (Point cell) {
        Set<Character> none = new HashSet<>(alphabet);

        List<Integer> startPositions = getStartChildPos(cell);
        int startX = startPositions.get(0);
        int startY = startPositions.get(1);

        for (int i = startX; i < startX + getChildSize(); i++) {
            for (int j = startY; j < startY + getChildSize(); j++) {
                none.remove(getField(i, j));
            }
        }

        return none;
    }

    /**
     * @return Posible Solutions for the first Empty Cell
     */
    public List<Sudoku> getChildren () throws Exception {

        List<Sudoku> posibleChilds = new ArrayList<>();

        if (isComplete()) {
            return posibleChilds;
        }

        var emptyCell = getNextCellVoid();

        var emptyRowValues = getRowPossibleChars(emptyCell);
        var emptyColValues = getColPossibleChars(emptyCell);
        var emptyChildValues = getMiniPosibleChars(emptyCell);

        Set<Character> none = new HashSet<>(alphabet);

        none.removeAll(emptyChildValues);
        none.removeAll(emptyRowValues);
        none.removeAll(emptyColValues);

        char[][] child = getChild(emptyCell);

        for (char c : none) {
            child[emptyCell.x][emptyCell.y] = c;
            posibleChilds.add(new Sudoku(child));
        }

        return posibleChilds;

    }

    /**
     * @param pathName String path name to save Sudoku
     * @return (true / false) depending on if the game was saved or not
     */
    public boolean saveSudoku (String pathName) {
        return false;
    }

    @Override
    public String toString () {

        StringBuilder sb = new StringBuilder();

        for (char[] chars : table) {
            for (int j = 0; j < table.length; j++) {

                if (chars[j] == (char) 0) {
                    sb.append(" ");
                } else {
                    sb.append(chars[j]);
                }
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

}
