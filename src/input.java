import java.util.*;
import java.io.*;

/* 
 Algoritma UCS (Uniform cost search) dan A* (atau A star) dapat digunakan untuk menentukan 
 lintasan terpendek dari suatu titik ke titik lain. Pada tugas kecil 3 ini, anda diminta menentukan lintasan terpendek berdasarkan peta Google Map jalan-jalan di kota Bandung. Dari ruas-ruas jalan di peta dibentuk graf. Simpul menyatakan persilangan jalan (simpang 3, 4 atau 5) atau ujung jalan. Asumsikan jalan dapat dilalui dari dua arah. Bobot graf menyatakan jarak (m atau km) antar simpul. Jarak antar dua simpul dapat dihitung dari koordinat kedua simpul menggunakan rumus jarak Euclidean (berdasarkan koordinat) atau dapat menggunakan ruler di Google Map, atau cara lainnya yang disediakan oleh Google Map.
*/

/*
 Langkah pertama di dalam program ini adalah membuat graf yang merepresentasikan peta (di area 
tertentu, misalnya di sekitar Bandung Utara/Dago). Berdasarkan graf yang dibentuk, lalu program 
menerima input simpul asal dan simpul tujuan, lalu menentukan lintasan terpendek antara 
keduanya menggunakan algoritma UCS dan A*. Lintasan terpendek dapat ditampilkan pada 
peta/graf (misalnya jalan-jalan yang menyatakan lintasan terpendek diberi warna merah). Nilai 
heuristik yang dipakai adalah jarak garis lurus dari suatu titik ke tujuan.
 */

// Spesifikasi program:
// 1. Program menerima input file graf (direpresentasikan sebagai matriks ketetanggaan 
// berbobot), jumlah simpul minimal 8 buah.
// 2. Program dapat menampilkan peta/graf
// 3. Program menerima input simpul asal dan simpul tujuan.
// 4. Program dapat menampilkan lintasan terpendek beserta jaraknya antara simpul asal dan 
// simpul tujuan.
// 5. Antarmuka program bebas, apakah pakai GUI atau command line saja.

/* 
 Bonus: Bonus nilai diberikan jika dapat menggunakan Google Map API untuk menampilkan peta, 
membentuk graf dari peta, dan menampilkan lintasan terpendek di peta (berupa jalan yang diberi 
warna). Simpul graf diperoleh dari peta (menggunakan API Google Map) dengan mengklik ujung 
jalan atau persimpangan jalan, lalu jarak antara kedua simpul dihitung langsung dengan rumus 
Euclidean.
*/

class input {
    // input file from google maps api and convert it to adjacency matrix
    private List<List<Integer>> adjacencyMatrix;
    private List<List<Double>> coordinates;
    private List<List<Double>> euclideanDistance;
    private List<List<Double>> heuristic;
    private Integer start;
    private Integer goal;
    // constructor
    public input(){
        // initialize all the variables with empty
        adjacencyMatrix = new ArrayList<List<Integer>>();
        coordinates = new ArrayList<List<Double>>();
        euclideanDistance = new ArrayList<List<Double>>();
        heuristic = new ArrayList<List<Double>>();
        start = 0;
        goal = 0;
    }
    // getter
    public List<List<Integer>> getAdjacencyMatrix(){
        return adjacencyMatrix;
    }
    public List<List<Double>> getCoordinates(){
        return coordinates;
    }
    public List<List<Double>> getEuclideanDistance(){
        return euclideanDistance;
    }
    public List<List<Double>> getHeuristic(){
        return heuristic;
    }
    public Integer getStart(){
        return start;
    }
    public Integer getGoal(){
        return goal;
    }
    // setter for reading file
    public void setter(){
        var inp = new Scanner(System.in);
        System.out.print("Masukkan nama file: ");
        String filename = inp.nextLine();
        // Membaca file dari folder tests
        File file = new File("../tests/" + filename + ".txt");
        while (!file.exists()){
            System.out.println("File " + filename + " tidak ditemukan");
            System.out.print("Masukkan nama file: ");
            filename = inp.nextLine();
            file = new File("../tests/" + filename + ".txt");
        }
        
        // Membaca isi file dan memasukkannya ke dalam adjacency matrix
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] dataSplit = data.split(" ");
                List<Integer> row = new ArrayList<Integer>();
                for (int i = 0; i < dataSplit.length; i++){
                    row.add(Integer.parseInt(dataSplit[i]));
                    // row2.add(Integer.parseInt(dataSplit[i]));
                    // row3.add(Integer.parseInt(dataSplit[i]));
                    // row4.add(Integer.parseInt(dataSplit[i]));
                }
                adjacencyMatrix.add(row);
            }
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

        // input start and goal index
        System.out.print("Masukkan simpul awal: ");
        start = inp.nextInt();
        System.out.print("Masukkan simpul tujuan: ");
        goal = inp.nextInt();

        // print adjacency matrix
        System.out.println("\nAdjacency Matrix:");
        for (int i = 0; i < adjacencyMatrix.size(); i++) {
            for (int j = 0; j < adjacencyMatrix.get(i).size(); j++) {
                System.out.print(adjacencyMatrix.get(i).get(j) + " ");
            }
            System.out.println();
        }
        // print start and goal index
        System.out.println("\nSimpul awal: " + start);
        System.out.println("Simpul tujuan: " + goal);
        
        // Membaca file dari coordinates dalam google maps dan memasukkannya ke dalam coordinates
        

        inp.close();
    }
}