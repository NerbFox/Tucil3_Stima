import java.util.*;
// import java.io.*;

public class Main {
    public static void main(String[] args) {
        // pilihan algoritma
        System.out.println("Pilih algoritma yang ingin digunakan:");
        System.out.println("1. UCS");
        System.out.println("2. A*");
        System.out.print("Masukkan pilihan: ");
        Scanner scanner = new Scanner(System.in);
        String pilihan = scanner.nextLine();
        while (!pilihan.equals("1") && !pilihan.equals("2")) {
            System.out.println("Pilihan tidak valid");
            System.out.print("Masukkan pilihan: ");
            pilihan = scanner.nextLine();
        }
        // input
        var data = new input();
        data.setter();
        // pilihan heuristik
        if (pilihan.equals("2")) {
            System.out.println("Pilih heuristik yang ingin digunakan:");
            System.out.println("1. Manhattan");
            System.out.println("2. Euclidean");
            System.out.print("Masukkan pilihan: ");
            int pilihan2 = scanner.nextInt();
            while (pilihan2 != 1 && pilihan2 != 2) {
                System.out.println("Pilihan tidak valid");
                System.out.print("Masukkan pilihan: ");
                pilihan2 = scanner.nextInt();
            }
            if (pilihan2 == 1) {
                // algoritma A* dengan heuristik Manhattan
                // var algo = new Algo(data.getAdjacencyMatrix(), data.getCoordinates(),
                // data.getManhattanDistance());
                // algo.AStar();
            } else {
                // algoritma A* dengan heuristik Euclidean
                // var algo = new Algo(data.getAdjacencyMatrix(), data.getCoordinates(),
                // data.getEuclideanDistance());
                // algo.AStar();
            }
        } else {
            // algoritma UCS
            System.out.println("UCS");

            var algo = new algo(data);
            algo.temp();
            // algo.algoUCS();
            // // print hasil
            // System.out.println("Hasil:");
            // for (int i = 0; i < algo.getPath().size(); i++) {
            //     System.out.print(algo.getPath().get(i) + " ");
            // }
            // System.out.println();
            // System.out.println("Jarak: " + algo.getDistance());
        }
        scanner.close();
    }
}
