package com.zersoft.sudokugame;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Sudoku implements Serializable {

    final private Set<Character> DEFAULT_ALPHABET;
    private Set<Character> alphabet;
    private char[][] table;

    {
        DEFAULT_ALPHABET = Set.of((char) 1, (char) 2, (char) 3, (char) 4, (char) 5, (char) 6, (char) 7, (char) 8, (char) 9);
    }

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
     * @param alphabet chars that will compose Sudoku Alphabet instead of default
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
     * @param table char[][] array that will compose the sudoku field
     */
    public Sudoku (char[][] table) throws Exception {
        setTable(table);
    }

    /**
     * @param size Create a new Sudoku from an int Size. alphabet setted as (char) integers
     * @return new created table poblated with empty characters
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

        for (int row = 0; row < table.length; row++) {
            for (int col = 0; col < table.length; col++) {
                char field = getField(row, col);
                if (Character.isWhitespace(field)) {
                    this.table[row][col] = (char) 0;
                }
            }
        }

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
    private int getSubgridSize () {
        return (int) Math.sqrt(table.length);
    }

    /**
     * @return If void cell return true
     */
    public boolean isComplete () {
        Point voidCell = getNextCellVoid();

        return voidCell.x == table.length + 1;

    }

    /**
     * @return if Sudoku was completed correctly
     */
    public boolean isValid () {

        if (!checkColRowValid()) {
            return false;
        }

        var subgridPoints = getSubgridStartPositions();

        for (Point subgridPoint : subgridPoints) {
            if (!checkSubgrid(subgridPoint)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return List of Point for all Subgrid start positions
     */
    private List<Point> getSubgridStartPositions () {
        List<Point> startPositions = new ArrayList<>();
        for (int row = 0; row < getSize(); row = row + getSubgridSize()) {
            for (int col = 0; col < getSize(); col = col + getSubgridSize()) {
                startPositions.add(new Point(row, col));
            }
        }
        return startPositions;

    }


    /**
     * @param startPos Subgrid start position
     * @return true if is valid
     */
    private boolean checkSubgrid (Point startPos) {

        var subGrind = new HashSet<>(alphabet);

        for (int row = startPos.x; row < startPos.x + getSubgridSize(); row++) {
            for (int col = startPos.y; col < startPos.y + getSubgridSize(); col++) {
                subGrind.remove(getField(row, col));
            }
        }

        return subGrind.isEmpty();

    }


    private boolean checkColRowValid () {
        for (int row = 0; row < table.length; row++) {

            Set<Character> rowSet = new HashSet<>(alphabet);
            Set<Character> colSet = new HashSet<>(alphabet);

            for (int col = 0; col < table.length; col++) {
                rowSet.remove(getField(row, col));
                colSet.remove(getField(col, row));
            }

            if (!rowSet.isEmpty() || !colSet.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return An empty Point, if point position is +1 of table length is done
     */
    private Point getNextCellVoid () {
        for (int row = 0; row < table.length; row++) {
            for (int col = 0; col < table.length; col++) {
                if (table[row][col] == (char) 0) {
                    return new Point(row, col);
                }
            }
        }
        return new Point(table.length + 1, table.length + 1);
    }

    /**
     * @param cell empty cell
     * @return List of Integer [x, y] where starts the miniSudoku
     */
    private Point getStartChildPos (Point cell) {

        int childSize = getSubgridSize();
        int startX = (cell.x < childSize) ? 0 : (cell.x / childSize) * childSize;
        int startY = (cell.y < childSize) ? 0 : (cell.y / childSize) * childSize;

        return new Point(startX, startY);
    }

    /**
     * @param cell empty cell
     * @return child with contained cell
     */
    private Sudoku getChild (Point cell) throws Exception {
        char[][] child = new char[getSubgridSize()][getSubgridSize()];

        Point startPositions = getStartChildPos(cell);
        int startX = startPositions.x;
        int startY = startPositions.y;

        for (int row = 0; row < getSubgridSize(); row++) {
            for (int col = 0; col < getSubgridSize(); col++) {
                child[row][col] = table[startX + row][startY + col];
            }
        }

        return new Sudoku(child);
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

        Point startPositions = getStartChildPos(cell);
        int startX = startPositions.x;
        int startY = startPositions.y;

        for (int i = startX; i < startX + getSubgridSize(); i++) {
            for (int j = startY; j < startY + getSubgridSize(); j++) {
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

        none.retainAll(emptyRowValues);
        none.retainAll(emptyColValues);
        none.retainAll(emptyChildValues);

        Sudoku child = getChild(emptyCell);
        for (char c : none) {
            child.setField(emptyCell.x, emptyCell.y, c);
            posibleChilds.add(child);
        }

        return posibleChilds;

    }

    /**
     * @param pathName String path name to save Sudoku
     * @return (true / false) depending on if the game was saved or not
     */

    public boolean saveSudoku (String pathName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(pathName))) {
            outputStream.writeObject(this);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString () {

        StringBuilder sb = new StringBuilder();

        for (char[] chars : table) {
            for (int col = 0; col < table.length; col++) {

                if (chars[col] == (char) 0) {
                    sb.append(" ");
                } else {
                    sb.append(chars[col]);
                }
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

}
