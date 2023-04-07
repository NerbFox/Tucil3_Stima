import java.util.*;
// import java.io.*;
public class algo {
    // PriorityQueue untuk UCS tuple String, Integer
    private PriorityQueue< Tuple<List<Integer>, Integer> > UCSQueue;
    // path result
    private List<Integer> path;
    // input data
    private input data;
    // distance
    private Integer distance;

    // constructor
    public algo(input d) {
        // UCSQueue = new PriorityQueue< Tuple<List<Integer>, Integer> >();
        path = new ArrayList<Integer>();
        data = d;
        UCSQueue = new PriorityQueue<>(new Comparator<Tuple<List<Integer>, Integer>>() {
            @Override
            public int compare(Tuple<List<Integer>, Integer> t1, Tuple<List<Integer>, Integer> t2) {
                // System.out.println("compare");
                return (t1.getItem2()).compareTo(t2.getItem2());
            }
        });
    }
    // getter path
    public List<Integer> getPath() {
        return path;
    }
    // getter distance
    public Integer getDistance() {
        return distance;
    }
    public void algoUCS(){
        // inisialisasi clear all attribute
        UCSQueue.clear();
        path.clear();
        distance = 0;

        // inisiasi jumlah kolom atau baris dari adjacency matrix
        int n = data.getAdjacencyMatrix().get(0).size();
        Integer start = data.getStart();
        Integer goal = data.getGoal();
        // add all adjacenct node to queue from start node to UCSQueue priority from lowest Integer to highest Integer
        // selama elemen pertama dari queue bukan goal maka lakukan perulangan
        // ambil elemen pertama dari queue
        // get first element from queue
        // add start node to queue
        // initialize Tuple start list with one element start and cost 0
        List<Integer> startList = new ArrayList<Integer>();
        startList.add(start);
        UCSQueue.add(new Tuple<List<Integer>, Integer>(startList, 0));
        while (! UCSQueue.peek().getItem1().get(0).equals(goal)) {
            printUCSQueue();
            // ambil elemen pertama dari queue
            Tuple<List<Integer>, Integer> temp = UCSQueue.poll();
            Integer tempInt = temp.getItem1().get(0);
            // add semua node adjacent ke queue 
            for (int i = 0; i < n; i++) {
                if (! data.getAdjacencyMatrix().get(tempInt).get(i).equals(0)) {
                    Integer cost = data.getAdjacencyMatrix().get(tempInt).get(i);
                    System.out.println("i = " + i + " cost = " + cost);
                    List<Integer> tempList = new ArrayList<Integer>();
                    // copy temp.getItem1() to tempList
                    tempList.addAll(temp.getItem1());
                    // remove first element from tempList
                    tempList.remove(0);
                    tempList.add(tempInt); // add parent node
                    // add i to tempList to first element
                    tempList.add(0, i);
                    // increase cost and make new tuple
                    Tuple<List<Integer>, Integer> tempTuple = new Tuple<List<Integer>, Integer>(tempList, cost + temp.getItem2());
                    // add tempTuple to UCSQueue with priority from lowest Integer to highest Integer from second element of tuple
                    UCSQueue.add(tempTuple);
                }
            }
        }
        Tuple<List<Integer>, Integer> tempPath = UCSQueue.poll();
        // ambil elemen pertama dari queue dan masukkan ke path
        path = tempPath.getItem1();
        // pindahkan elemen pertama dari path ke elemen terakhir
        path.add(path.get(0));
        path.remove(0);
        // ambil elemen kedua dari queue dan masukkan ke distance
        distance = tempPath.getItem2();

        // // ambil elemen pertama dari queue
            // Tuple<List<Integer>, Integer> temp = UCSQueue.poll();
            
            // for (int i = 0; i < n; i++) {
            //     if (data.getAdjacencyMatrix().get(UCSQueue.peek().getItem1().get(UCSQueue.peek().getItem1().size() - 1)).get(i) != 0) {
            //         List<Integer> path = new ArrayList<Integer>();
            //         path = UCSQueue.peek().getItem1();
            //         path.add(i);
            //         int tempCost = UCSQueue.peek().getItem2() + data.getAdjacencyMatrix().get(UCSQueue.peek().getItem1().get(UCSQueue.peek().getItem1().size() - 1)).get(i);
            //         UCSQueue.add(new Tuple<List<Integer>, Integer>(path, tempCost));
            //     }
            // }
    }
    public void temp(){
        // isi random to USCQueue
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(0);
        temp.add(1);
        for (int i = 10; i > 0; i--) {
            // printUCSQueue();
            UCSQueue.add(new Tuple<List<Integer>, Integer>(temp, i));
        }
        printUCSQueue();
    }
    // public void printUCSQueue() {
    //     System.out.println("UCSQueue");
    //     // print UCSQueue ascending
    //     for (Tuple<List<Integer>, Integer> t : UCSQueue) {
    //         System.out.println(t.getItem1() + " " + t.getItem2());
    //     }
        
    // }

    public void printUCSQueue() {
        System.out.println("UCSQueue");
        while (!UCSQueue.isEmpty()) {
            Tuple<List<Integer>, Integer> t = UCSQueue.poll();
            System.out.println(t.getItem1() + " " + t.getItem2());
        }
    }
        
}

 
//  // ambil elemen pertama dari queue
//  Tuple<List<Integer>, Integer> temp = UCSQueue.poll();
//  // cek apakah sudah pernah dikunjungi
//  if (temp.getItem1().size() > 0) {
//      List<Integer> tempVisited = temp.getItem1();
//      tempVisited.remove(tempVisited.size() - 1);
//      if (tempVisited.containsAll(UCSQueue.peek().getItem1())) {
//          continue;
//      }
//  }
//  // tambahkan ke visited
//  UCSQueue.peek().getItem1().add(UCSQueue.peek().getItem1().get(UCSQueue.peek().getItem1().size() - 1));
//  // tambahkan ke queue
//  for (int i = 0; i < n; i++) {
//      if (data.getAdjacencyMatrix().get(UCSQueue.peek().getItem1().get(UCSQueue.peek().getItem1().size() - 1)).get(i) != 0) {
//          List<Integer> path = new ArrayList<Integer>();
//          path = UCSQueue.peek().getItem1();
//          path.add(i);
//          int tempCost = UCSQueue.peek().getItem2() + data.getAdjacencyMatrix().get(UCSQueue.peek().getItem1().get(UCSQueue.peek().getItem1().size() - 1)).get(i);
//          UCSQueue.add(new Tuple<List<Integer>, Integer>(path, tempCost));
//      }
//  }
// }



// List<Integer> visited = new ArrayList<Integer>();
// List<Integer> path = new ArrayList<Integer>();
// List<Integer> tempPath = new ArrayList<Integer>();
// List<Integer> tempVisited = new ArrayList<Integer>();
// int tempCost = 0;
// // inisialisasi queue
// UCSQueue.add(new Tuple<List<Integer>, Integer>(path, tempCost));
// // loop
// while (!UCSQueue.isEmpty()) {
//     // ambil elemen terdepan
//     Tuple<List<Integer>, Integer> temp = UCSQueue.poll();
//     tempPath = temp.getItem1();
//     tempCost = temp.getItem2();
//     // cek apakah sudah sampai
//     if (tempPath.size() > 0 && tempPath.get(tempPath.size() - 1) == goal) {
//         // print path
//         System.out.println("Path: " + tempPath);
//         System.out.println("Cost: " + tempCost);
//         break;
//     }
//     // cek apakah sudah pernah dikunjungi
//     if (tempPath.size() > 0) {
//         tempVisited = tempPath;
//         tempVisited.remove(tempVisited.size() - 1);
//     }
//     if (tempVisited.containsAll(visited)) {
//         continue;
//     }
//     // tambahkan ke visited
//     visited.add(tempPath.get(tempPath.size() - 1));
//     // tambahkan ke queue
//     for (int i = 0; i < n; i++) {
//         if (data.getAdjacencyMatrix().get(tempPath.get(tempPath.size() - 1)).get(i) != 0) {
//             path = new ArrayList<Integer>(tempPath);
//             path.add(i);
//             UCSQueue.add(new Tuple<List<Integer>, Integer>(path, tempCost + data.getAdjacencyMatrix().get(tempPath.get(tempPath.size() - 1)).get(i)));
//         }
//     }
// }