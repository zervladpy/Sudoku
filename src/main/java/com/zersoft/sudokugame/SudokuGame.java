package com.zersoft.sudokugame;

import java.util.Scanner;

public class SudokuGame {

    public static void main (String[] args) throws Exception {

        /*
         *  1. Lea desde línea de órdenes un sudoku con el siguiente formato (cada línea es una fila del sudoku):
         *  513876492
         *  789243651
         *  246915783
         *  328654917
         *  974182536
         *  165397248
         *  697438125
         *  851729364
         *  432561879
         */

        Scanner sc = new Scanner(System.in);

        String line = sc.nextLine();

        int size = line.length();

        char[][] table = new char[size][size];

        table[0] = line.toCharArray();

        for (int i = 1; i < size; i++) {
            table[i] = sc.nextLine().toCharArray();
        }

        Sudoku sudoku = new Sudoku(table);

        /*
         * 2. Indique si es válido, en cuyo caso debe guardarlo en un archivo binario, cuyo nombre será pedido desde línea de órdenes.
         * */

        /*
         *  ¿Cuándo es válido? No entendi muy bien.
         *  El ejercicio fue completado si el sudoku tiene que estar completo y no repetirse ningun número donde no puede repetirse
         *  Un Sudoku que tiene celdas vacias no es considerado como válido. En un futuro puede ser que se resuelva.
         * */

        // FIXME: Lineas comentadas para comprobar el ejercicio 3


        if (sudoku.isValid()) {
            System.out.println("Sudoku válido.");
            System.out.print("Nombre del archivo donde guardar el sudoku: ");
            String fileName = sc.nextLine() + ".dat";
            sudoku.saveSudoku(fileName);
        } else {
            System.out.println("Sudoku no válido...");
        }


        /*
         * 3. Muestre los sudokus hijo (tenga en cuenta que los sudokus no necesariamente deben estar completos, con espacios donde no hay valor).
         *  Por ejemplo, otra posible entrada con celdas vacías sería:
         * 5 3876492
         * 789243 5
         * 246 15783
         * 328654917
         * 974182536
         * 1 5397 48
         * 697438125
         * 851729364
         * 4325 1879
         * */

        System.out.println("Posibles Sudokus para la primera posición vacia: ");
        var posibleSudokus = sudoku.getChildren();

        for (Sudoku sd : posibleSudokus) {
            System.out.println(sd);
        }

    }
}
