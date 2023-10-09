package com.zersoft.sudokugame;

import java.awt.*;
import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Sudoku implements Serializable {

    final private Set<Character> DEFAULT_ALPHABET = Set.of((char) 1, (char) 2, (char) 3, (char) 4, (char) 5, (char) 6, (char) 7, (char) 8, (char) 9);

    private Set<Character> alphabet;
    private char[][] table;

    public Sudoku() {
        alphabet = DEFAULT_ALPHABET;
        table = new char[alphabet.size()][alphabet.size()];

        table = defaultTableSetup(alphabet.size());
    }

    public Sudoku(char[] alphabet) throws Exception {

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

    public Sudoku(char[][] table) throws Exception {
        setTable(table);
    }

    private char[][] defaultTableSetup(int size) {
        char[][] newTable = new char[size][size];

        for (int i = 0; i < alphabet.size(); i++) {
            for (int j = 0; j < alphabet.size(); j++) {
                newTable[i][j] = (char) 0;
            }
        }

        return newTable;
    }

    private Set<Character> extractAlphabet(char[][] table) {
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

    public int getSize() {
        return alphabet.size();
    }

    public char[][] getTable() {
        return table;
    }

    public void setField(int i, int j, char c) {
        table[i][j] = c;
    }

    public void setTable(char[][] table) throws Exception {
        int len = table.length;
        int wid = table[0].length;

        if ((len * wid) != (len * len)) {
            throw new Exception("Tamaño de sudoku no válido");
        }

        this.table = table;

        alphabet = extractAlphabet(table);
    }

    public char getField(int i, int j) {
        return table[i][j];
    }

    private int getChildSize() {
        return (int) Math.sqrt(table.length);
    }

    public boolean isComplete() {
        Point voidCell = getNextCellVoid();

        return voidCell.x == table.length + 1;

    }

    public boolean isValid() {

        // TODO: add isValid method

        return false;
    }

    public Point getNextCellVoid() {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++) {
                if (table[i][j] == (char) 0) {
                    return new Point(i, j);
                }
            }
        }
        return new Point(table.length +1, table.length +1);
     }

     private List<Integer> getStartChildPos() {

        List<Integer> startPos = new ArrayList<>();

        int size = getChildSize();

         for (int i = 0; i < table.length + 1 ; i = i + size) {
            startPos.add(i);
         }

        return startPos;
     }

    public List<Sudoku> getChildren() throws NoSuchMethodException {
        List<Sudoku> sudokuChild = new ArrayList<>();

        if (isComplete()) {
            return sudokuChild;
        }


        Point voidCell = getNextCellVoid();

        Set<Character> row = new HashSet<>(alphabet.size());

        for (int rowChar = 0; rowChar < table.length ; rowChar++) {
            row.add(table[rowChar][voidCell.y]);
        }

        Set<Character> col = new HashSet<>(alphabet.size());

        for (int colChar = 0; colChar < table.length; colChar++) {
            col.add(table[voidCell.y][colChar]);
        }


        Set<Character> child = new HashSet<>(alphabet.size());
        Point startPoint = new Point(-1, -1);
        List<Integer> listChildStartPos = getStartChildPos();

        for (int startPos : listChildStartPos) {

            if (startPos >= voidCell.x && voidCell.x < startPos + getChildSize()) {
                startPoint.setLocation(startPos, startPoint.y);
            }

            if (startPos >= voidCell.y && voidCell.y < startPos + getChildSize()) {
                startPoint.setLocation(startPoint.x, startPos);
            }

        }

        for (int i = startPoint.x; i < startPoint.x + getChildSize(); i++) {

            for (int j = startPoint.y; j < startPoint.y + getChildSize(); j++) {

                child.add(table[i][j]);

            }
        }


        return sudokuChild;

    }

    public boolean saveSudoku(String pathName) {

        return false;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (char[] chars : table) {
            for (int j = 0; j < table.length; j++) {

                if (chars[j] == (char) 0) {
                    sb.append(" ");
                }else {
                    sb.append(chars[j]);
                }
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

}
