package com.zersoft.sudokugame;

import java.io.Serializable;
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

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                char cellValue = table[i][j];
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

    public boolean isComplete() {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++) {
                if (getField(i, j) == (char) 0) {
                    return false;
                }

            }

        }

        return true;
    }

    public boolean isValid() {

        for (int i = 0; i < table.length; i++) {

            Set<Character> row = new HashSet<>(getSize());
            Set<Character> col = new HashSet<>(getSize());

            for (int j = 0; j < table.length; j++) {
                row.add((char) getField(i, j));
                col.add((char) getField(j, i));
            }

            if (row.size() != getSize() || col.size() != getSize()) {
                return false;
            }

        }

        return true;
    }

    public List<Sudoku> getChildren() throws NoSuchMethodException {
        // TODO: crete method
        throw new NoSuchMethodException();
    }

    public boolean saveSudoku(String pathName) {

        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
