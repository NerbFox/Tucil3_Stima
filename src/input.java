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
    private List<Tuple<Double, Double>> coordinates;
    private List<String> names;
    private List<Double> euclideanDistToGoal;
    private Integer start;
    private Integer goal;
    // constructor
    public input(){
        // initialize all the variables with empty
        adjacencyMatrix = new ArrayList<List<Integer>>();
        coordinates = new ArrayList<Tuple<Double, Double>>();
        names = new ArrayList<String>();
        euclideanDistToGoal = new ArrayList<Double>();
        start = 0;
        goal = 0;
    }
    // getter
    public List<List<Integer>> getAdjacencyMatrix(){
        return adjacencyMatrix;
    }
    public List<Tuple<Double, Double>> getCoordinates(){
        return coordinates;
    }
    public List<String> getNames(){
        return names;
    }
    public List<Double> getEuclideanDistToGoal(){
        return euclideanDistToGoal;
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

        // Membaca file adjacency matrix
        System.out.print("Masukkan nama file adjacency matrix: ");
        String filename = inp.nextLine();
        File file = new File("../tests/" + filename + ".txt");
        while (!file.exists()){
            System.out.println("File " + filename + " tidak ditemukan");
            System.out.print("Masukkan nama file adjacency matrix: ");
            filename = inp.nextLine();
            file = new File("../tests/" + filename + ".txt");
        }
        
        // Membaca isi file dan memasukkannya ke dalam adjacency matrix
        boolean isValidFile = false;
        while (!isValidFile){
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] dataSplit = data.split(" ");
                    List<Integer> row = new ArrayList<Integer>();
                    for (int i = 0; i < dataSplit.length; i++){
                        row.add(Integer.parseInt(dataSplit[i]));
                    }
                    adjacencyMatrix.add(row);
                }
                myReader.close();
                isValidFile = true;
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file.");
                e.printStackTrace();
            }
        }

        
        
        // ------------------------------------------------------------ //
        // baca file koordinat
        System.out.print("Masukkan nama file koordinat: ");
        filename = inp.nextLine();
        // Membaca file dari folder tests
        file = new File("../tests/" + filename + ".txt");
        // contoh isi file  
        // 1.2 3.4 jalan1
        // 2.3 4.5 jalan2
        // baca file koordinat dan masukkan ke dalam coordinates dan names
        while (!file.exists()){
            System.out.println("File " + filename + " tidak ditemukan");
            System.out.print("Masukkan nama file koordinat: ");
            filename = inp.nextLine();
            file = new File("../tests/" + filename + ".txt");
        }
        isValidFile = false;
        while (!isValidFile){
            try {
                boolean colomnOutOfBound = false;
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] dataSplit = data.split(" ");
                    if (dataSplit.length != 3){
                        System.out.println("Jumlah kolom pada file koordinat tidak sesuai");
                        colomnOutOfBound = true;
                        break;
                    }
                    coordinates.add(new Tuple<Double, Double>(Double.parseDouble(dataSplit[0]), Double.parseDouble(dataSplit[1])));
                    names.add(dataSplit[2]);
                }
                myReader.close();
                // cek apakah jumlah koordinat sama dengan jumlah simpul
                isValidFile = true;
                if (coordinates.size() != adjacencyMatrix.get(0).size()){
                    isValidFile = false;
                    System.out.println("Jumlah koordinat tidak sama dengan jumlah simpul");
                }
                if (colomnOutOfBound){
                    isValidFile = false;
                }
                // baca ulang file jika tidak valid
                if (!isValidFile){
                    System.out.print("Masukkan nama file koordinat: ");
                    filename = inp.nextLine();
                    file = new File("../tests/" + filename + ".txt");
                }
                while (!file.exists()){
                    System.out.println("File " + filename + " tidak ditemukan");
                    System.out.print("Masukkan nama file koordinat: ");
                    filename = inp.nextLine();
                    file = new File("../tests/" + filename + ".txt");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file.");
                e.printStackTrace();
            }
        }



        // Membaca file dari coordinates dalam google maps dan memasukkannya ke dalam coordinates
        
        
        // input start and goal index
        // System.out.print("Masukkan simpul awal: ");
        // start = inp.nextInt();
        // System.out.print("Masukkan simpul tujuan: ");
        // goal = inp.nextInt();

        // input berupa nama simpul
        System.out.print("Masukkan nama posisi awal: ");
        String startName = inp.nextLine();
        System.out.print("Masukkan nama posisi tujuan: ");
        String goalName = inp.nextLine();
        start = -1; goal = -1;
        for (int i = 0; i < names.size(); i++){
            if (names.get(i).equals(startName)){
                start = i;
            }
            if (names.get(i).equals(goalName)){
                goal = i;
            }
        }
        while(start == -1 || goal == -1){
            System.out.println("Nama simpul tidak ditemukan");
            System.out.print("Masukkan nama posisi awal: ");
            startName = inp.nextLine();
            System.out.print("Masukkan nama posisi tujuan: ");
            goalName = inp.nextLine();
            start = -1; goal = -1;
            for (int i = 0; i < names.size(); i++){
                if (names.get(i).equals(startName)){
                    start = i;
                }
                if (names.get(i).equals(goalName)){
                    goal = i;
                }
            }
        }
        
        // menghitung jarak dari simpul ke goal dan memasukkannya ke dalam euclideanDistToGoal
        Double goalX = coordinates.get(goal).getItem1();
        Double goalY = coordinates.get(goal).getItem2();
        Double x, y;
        for (int i = 0; i < coordinates.size(); i++){
            x = coordinates.get(i).getItem1();
            y = coordinates.get(i).getItem2();
            euclideanDistToGoal.add(Math.sqrt(Math.pow(goalX - x, 2) + Math.pow(goalY - y, 2)));
        }

        // print adjacency matrix
        System.out.println("\nAdjacency Matrix:");
        for (int i = 0; i < adjacencyMatrix.size(); i++) {
            for (int j = 0; j < adjacencyMatrix.get(i).size(); j++) {
                System.out.print(adjacencyMatrix.get(i).get(j) + " ");
            }
            System.out.println();
        }
        // print coordinates and names
        System.out.println("\nKoordinat dan nama simpul:");
        for (int i = 0; i < coordinates.size(); i++) {
            System.out.println(coordinates.get(i).getItem1() + " " + coordinates.get(i).getItem2() + " " + names.get(i));
        }
        // print euclidean distance
        System.out.println("\nJarak Euclidean dari simpul ke simpul tujuan:");
        for (int i = 0; i < euclideanDistToGoal.size(); i++) {
            System.out.println(euclideanDistToGoal.get(i));
        }
        // print start and goal index
        System.out.println("\nSimpul awal: " + startName + " (" + start + ")");
        System.out.println("Simpul tujuan: " + goalName + " (" + goal + ")");
        inp.close();
    }
    public Double euclideanDistance(Double x1, Double y1, Double x2, Double y2){
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}