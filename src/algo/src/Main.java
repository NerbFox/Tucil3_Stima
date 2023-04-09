import java.util.*;
// import java.io.*;
// javac Main.java algo.java input.java Tuple.java 
// java Main
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
            // algoritma A* dengan heuristik Euclidean
            System.out.println("A*");
            var algo = new algo(data);
            // algo.algoAstar();
            algo.templateFunctionPath(2);
            // print hasil
            System.out.println("Hasil:");
            for (int i = 0; i < algo.getPath().size(); i++) {
                System.out.print(algo.getPath().get(i) + " ");
            }
            System.out.println();
            System.out.println("Jarak: " + algo.getDistanceD());

            // System.out.println("Pilih heuristik yang ingin digunakan:");
            // System.out.println("1. Manhattan");
            // System.out.println("2. Euclidean");
            // System.out.print("Masukkan pilihan: ");
            // String Pilihan2 = scanner.nextLine();
            // while (!Pilihan2.equals("1") && !Pilihan2.equals("2")) {
            //     System.out.println("Pilihan tidak valid");
            //     System.out.print("Masukkan pilihan: ");
            //     Pilihan2 = scanner.nextLine();
            // }
            // if (Pilihan2 == "1") {
            //     // algoritma A* dengan heuristik Manhattan
            //     // var algo = new Algo(data.getAdjacencyMatrix(), data.getCoordinates(),
            //     // data.getManhattanDistance());
            //     // algo.AStar();
            // } else {
            //     // algoritma A* dengan heuristik Euclidean
            //     // var algo = new Algo(data.getAdjacencyMatrix(), data.getCoordinates(),
            //     // data.getEuclideanDistance());
            //     // algo.AStar();
            // }
        } else {
            // algoritma UCS
            System.out.println("UCS");
            var algo = new algo(data);
            // algo.temp();
            // algo.algoUCS();
            algo.templateFunctionPath(1);
            // print hasil
            System.out.println("Hasil:");
            for (int i = 0; i < algo.getPath().size(); i++) {
                System.out.print(algo.getPath().get(i) + " ");
            }
            System.out.println();
            System.out.println("Jarak: " + algo.getDistanceD());
        }
        scanner.close();
    }
}
