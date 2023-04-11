import java.util.*;
// import java.io.*;
public class algo {
    // // PriorityQueue untuk UCS tuple String, Integer
    // private PriorityQueue< Tuple<List<Integer>, Integer> > UCSQueue;
    // PriorityQueue untuk A* tuple String, Double
    private PriorityQueue< Tuple<List<Integer>, Double> > PrioQueue;
    // path result
    private List<Integer> path;
    // input data
    private input data;
    // distance
    private Integer distance;
    private Double distanceD;

    // constructor
    // 0 10 9 7 8 13 14 15 20 21 23 
    public algo(input d) {
        path = new ArrayList<Integer>();
        d.changeEuclideanToMeters();
        data = d;
        // change 
        PrioQueue = new PriorityQueue<>(new Comparator<Tuple<List<Integer>, Double>>() {
            @Override
            public int compare(Tuple<List<Integer>, Double> t1, Tuple<List<Integer>, Double> t2) {
                // System.out.println("compare");
                return (t1.getItem2()).compareTo(t2.getItem2());
            }
        });
        // UCSQueue = new PriorityQueue<>(new Comparator<Tuple<List<Integer>, Integer>>() {
        //     @Override
        //     public int compare(Tuple<List<Integer>, Integer> t1, Tuple<List<Integer>, Integer> t2) {
        //         // System.out.println("compare");
        //         return (t1.getItem2()).compareTo(t2.getItem2());
        //     }
        // });
    }
    // getter path
    public List<Integer> getPath() {
        return path;
    }
    // getter distance
    public Integer getDistance() {
        return distance;
    }
    // getter distanceD
    public Double getDistanceD() {
        return distanceD;
    }
    public void reset() {
        PrioQueue.clear();
        PrioQueue.clear();
        path.clear();
        distance = 0;
    }
    
    public void temp(){
        // isi random to PrioQueue
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(0);
        temp.add(1);
        for (int i = 0; i < 10; i++) {
            PrioQueue.add(new Tuple<List<Integer>, Double>(temp, Math.random()));
        }
        printPrioQueue();
    }
    
    public void printPrioQueue() {
        System.out.println("PrioQueue");
        for (int i = 0; i < PrioQueue.size(); i++) {
            Tuple<List<Integer>, Double> t = PrioQueue.poll();
            System.out.println(t.getItem1() + " " + t.getItem2());
            PrioQueue.add(t);
        }
        // while (!UCSQueue.isEmpty()) {
            //     Tuple<List<Integer>, Integer> t = UCSQueue.poll();
            //     System.out.println(t.getItem1() + " " + t.getItem2());
            // }
        }
    
    // Method untuk algoritma UCS dan A*
    public void templateFunctionPath(int mode){
        // mode 1 = UCS
        // mode 2 = A*
        reset();
        int n = data.getAdjacencyMatrix().get(0).size();
        Integer start = data.getStart();
        Integer goal = data.getGoal();
        List<Double> hn = data.getEuclideanDistToGoal();
        // add start node to queue
        // initialize Tuple start list with one element start and cost 0
        List<Integer> startList = new ArrayList<Integer>();
        startList.add(start);
        Double fnS = 0.0;
        if (mode == 2) {
            fnS = hn.get(start);
        }
        PrioQueue.add(new Tuple<List<Integer>, Double>(startList, fnS));

        // // Cara di ppt
        // // selama index 0 dari tuple pertama dari queue tidak sama dengan goal
        // while (! PrioQueue.peek().getItem1().get(0).equals(goal)) {
        //     // ambil elemen pertama dari queue
        //     Tuple<List<Integer>, Double> temp = PrioQueue.poll();
        //     // print tuple yang diambil
        //     System.out.println(temp.getItem1() + " " + temp.getItem2());
        //     Integer tempInt = temp.getItem1().get(0);
        //     // add semua node adjacent ke queue 
        //     for (int i = 0; i < n; i++) {
        //         if (! data.getAdjacencyMatrix().get(tempInt).get(i).equals(0)) {
        //             Double cost = data.getAdjacencyMatrix().get(tempInt).get(i).doubleValue();
        //             List<Integer> tempList = new ArrayList<Integer>();
        //             // copy temp.getItem1() to tempList
        //             tempList.addAll(temp.getItem1());
        //             // remove first element from tempList
        //             tempList.remove(0);
        //             tempList.add(tempInt); // add parent node
        //             // add i to tempList to first element
        //             tempList.add(0, i);

        // instead of peek first element (must remove first element, add to last element), peek last element (no need to remove and add the first element)
        // sebelumnya kita harus mengambil elemen pertama dari list, kemudian menambahkan elemen pertama dari list ke elemen terakhir dari list
        // dengan cara ini, kita hanya perlu mengambil elemen terakhir dari list 
        // selama indeks terakhir tidak sama dengan goal
        while (! PrioQueue.peek().getItem1().get(PrioQueue.peek().getItem1().size() - 1).equals(goal)) { 
            // ambil elemen pertama dari queue
            Tuple<List<Integer>, Double> temp = PrioQueue.poll();
            // print tuple yang diambil
            System.out.println(temp.getItem1() + " " + temp.getItem2());
            Integer tempInt = temp.getItem1().get(temp.getItem1().size() - 1);
            // add semua node adjacent ke queue 
            for (int i = 0; i < n; i++) {
                if (! data.getAdjacencyMatrix().get(tempInt).get(i).equals(0)) {
                    Double cost = data.getAdjacencyMatrix().get(tempInt).get(i).doubleValue();
                    List<Integer> tempList = new ArrayList<Integer>();
                    // copy temp.getItem1() to tempList
                    tempList.addAll(temp.getItem1());
                    tempList.add(i);


                    // increase cost and make new tuple with cost + recent cost + heuristic cost
                    Double gn = temp.getItem2() + cost;
                    Double fn = 0.0;
                    if (mode == 1) {
                        // UCS -> fn = gn
                        fn = gn;
                    } else if (mode == 2) {
                        // A* -> fn = gn + hn
                        // increase cost and make new tuple with cost + recent cost + heuristic cost - heuristic cost of parent node
                        gn -= hn.get(tempInt);
                        // System.out.println("hn.get(temp) = " + hn.get(tempInt));
                        fn = gn + hn.get(i);
                    }
                    // print fn
                    // System.out.println("fn = " + fn);
                    Tuple<List<Integer>, Double> tempTuple = new Tuple<List<Integer>, Double>(tempList, fn);
                    // add tempTuple to PrioQueue with priority from lowest Integer to highest Integer from second element of tuple
                    PrioQueue.add(tempTuple);
                }
            }
        }
        Tuple<List<Integer>, Double> tempPath = PrioQueue.poll();
        // ambil elemen kedua dari queue dan masukkan ke distance
        this.distanceD = tempPath.getItem2();
        // ambil elemen pertama dari queue dan masukkan ke path
        path = tempPath.getItem1();
        // // pindahkan elemen pertama dari path ke elemen terakhir
        // path.add(path.get(0));
        // path.remove(0);

    }

}

 // public void algoUCS(){
    //     reset();
    //     // inisiasi jumlah kolom atau baris dari adjacency matrix
    //     int n = data.getAdjacencyMatrix().get(0).size();
    //     Integer start = data.getStart();
    //     Integer goal = data.getGoal();
    //     // add all adjacenct node to queue from start node to UCSQueue priority from lowest Integer to highest Integer
    //     // selama elemen pertama dari queue bukan goal maka lakukan perulangan
    //     // ambil elemen pertama dari queue
    //     // get first element from queue
    //     // add start node to queue
    //     // initialize Tuple start list with one element start and cost 0
    //     List<Integer> startList = new ArrayList<Integer>();
    //     startList.add(start);
    //     UCSQueue.add(new Tuple<List<Integer>, Integer>(startList, 0));

    //     while (! UCSQueue.peek().getItem1().get(0).equals(goal)) {
    //         // printUCSQueue();
    //         // ambil elemen pertama dari queue
    //         Tuple<List<Integer>, Integer> temp = UCSQueue.poll();
    //         Integer tempInt = temp.getItem1().get(0);
    //         // add semua node adjacent ke queue 
    //         for (int i = 0; i < n; i++) {
    //             if (! data.getAdjacencyMatrix().get(tempInt).get(i).equals(0)) {
    //                 Integer cost = data.getAdjacencyMatrix().get(tempInt).get(i);
    //                 System.out.println("i = " + i + " cost = " + cost);
    //                 List<Integer> tempList = new ArrayList<Integer>();
    //                 // copy temp.getItem1() to tempList
    //                 tempList.addAll(temp.getItem1());
    //                 // remove first element from tempList
    //                 tempList.remove(0);
    //                 tempList.add(tempInt); // add parent node
    //                 // add i to tempList to first element
    //                 tempList.add(0, i);
    //                 // increase cost and make new tuple
    //                 Tuple<List<Integer>, Integer> tempTuple = new Tuple<List<Integer>, Integer>(tempList, cost + temp.getItem2());
    //                 // add tempTuple to UCSQueue with priority from lowest Integer to highest Integer from second element of tuple
    //                 UCSQueue.add(tempTuple);
    //             }
    //         }
    //     }
    //     Tuple<List<Integer>, Integer> tempPath = UCSQueue.poll();
    //     // ambil elemen pertama dari queue dan masukkan ke path
    //     path = tempPath.getItem1();
    //     // pindahkan elemen pertama dari path ke elemen terakhir
    //     path.add(path.get(0));
    //     path.remove(0);
    //     // ambil elemen kedua dari queue dan masukkan ke distance
    //     distance = tempPath.getItem2();

    //     // // ambil elemen pertama dari queue
    //         // Tuple<List<Integer>, Integer> temp = UCSQueue.poll();
            
    //         // for (int i = 0; i < n; i++) {
    //         //     if (data.getAdjacencyMatrix().get(UCSQueue.peek().getItem1().get(UCSQueue.peek().getItem1().size() - 1)).get(i) != 0) {
    //         //         List<Integer> path = new ArrayList<Integer>();
    //         //         path = UCSQueue.peek().getItem1();
    //         //         path.add(i);
    //         //         int tempCost = UCSQueue.peek().getItem2() + data.getAdjacencyMatrix().get(UCSQueue.peek().getItem1().get(UCSQueue.peek().getItem1().size() - 1)).get(i);
    //         //         UCSQueue.add(new Tuple<List<Integer>, Integer>(path, tempCost));
    //         //     }
    //         // }
    // }

    // public void printUCSQueue() {
    //     System.out.println("UCSQueue");
    //     // print UCSQueue ascending
    //     for (Tuple<List<Integer>, Integer> t : UCSQueue) {
    //         System.out.println(t.getItem1() + " " + t.getItem2());
    //     }
        
    // }

    // public void algoAstar(){
    //     reset();
    //     // inisiasi jumlah kolom atau baris dari adjacency matrix
    //     int n = data.getAdjacencyMatrix().get(0).size();
    //     Integer start = data.getStart();
    //     Integer goal = data.getGoal();
    //     List<Double> hn = data.getEuclideanDistToGoal();
    //     // algoritma A*
    //     // menghitung fungsi f(n) = g(n) + h(n)
    //     // g(n) = jarak dari start ke n
    //     // h(n) = jarak dari n ke goal
    //     // A * uses a heuristic function h(n) to estimate the cost of the cheapest path from n to the goal node.
    //     // add start node to queue
    //     // initialize Tuple start list with one element start and cost 0
    //     List<Integer> startList = new ArrayList<Integer>();
    //     startList.add(start);
    //     PrioQueue.add(new Tuple<List<Integer>, Double>(startList, 0.0));
    //     while (! PrioQueue.peek().getItem1().get(0).equals(goal)) {
    //         // ambil elemen pertama dari queue
    //         Tuple<List<Integer>, Double> temp = PrioQueue.poll();
    //         Integer tempInt = temp.getItem1().get(0);
    //         // add semua node adjacent ke queue 
    //         for (int i = 0; i < n; i++) {
    //             if (! data.getAdjacencyMatrix().get(tempInt).get(i).equals(0)) {
    //                 Double cost = data.getAdjacencyMatrix().get(tempInt).get(i).doubleValue();
    //                 List<Integer> tempList = new ArrayList<Integer>();
    //                 // copy temp.getItem1() to tempList
    //                 tempList.addAll(temp.getItem1());
    //                 // remove first element from tempList
    //                 tempList.remove(0);
    //                 tempList.add(tempInt); // add parent node
    //                 // add i to tempList to first element
    //                 tempList.add(0, i);
    //                 // increase cost and make new tuple with cost + recent cost + heuristic cost
    //                 Tuple<List<Integer>, Double> tempTuple = new Tuple<List<Integer>, Double>(tempList, cost + temp.getItem2() + hn.get(i));
    //                 // add tempTuple to PrioQueue with priority from lowest Integer to highest Integer from second element of tuple
    //                 PrioQueue.add(tempTuple);
    //             }
    //         }
    //     }
    //     Tuple<List<Integer>, Double> tempPath = PrioQueue.poll();
    //     // ambil elemen pertama dari queue dan masukkan ke path
    //     path = tempPath.getItem1();
    //     // pindahkan elemen pertama dari path ke elemen terakhir
    //     path.add(path.get(0));
    //     path.remove(0);
    //     // ambil elemen kedua dari queue dan masukkan ke distance
    //     this.distanceD = tempPath.getItem2();
    // }
    // change yg ucs to Double aja biar bisa dijadikan template
    // make A* and UCS a template method 

// masukkan start node ke queue
// ambil elemen pertama dari queue
// selama elemen pertama dari queue bukan goal
// add semua node adjacent dari node pertama queue ke dalam queue 
// salin isi dari queue pertama ke list baru dan tambahkan node yang baru di add ke list baru
// hitung nilai f(n) tergantung mode yang dipilih
// UCS -> fn = gn (gn = jarak dari start ke n)
// A* -> fn = gn + hn (hn = jarak dari n ke goal, untuk gn pada A* kurangi dengan hn node parent)
// tambahkan list baru ke queue dengan nilai f(n) sebagai prioritas