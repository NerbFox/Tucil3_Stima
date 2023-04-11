package stima.tucil3.algo

import java.util.*

class Algo {
    private var prioQueue: PriorityQueue<Pair<List<Int>, Double>> = PriorityQueue()
    private var distance: Int = 0

    var data: Input = Input()
    var path: List<Int> = ArrayList()
    var distanceD: Double = 0.0

    private fun initialize(d: Input){
        path = ArrayList()
        d.changeEuclideanToMeters()
        data = d
        prioQueue = PriorityQueue<Pair<List<Int>, Double>>{
            t1, t2 -> t1.second.compareTo(t2.second)
        }
    }

    private fun reset(){
        prioQueue.clear()
        data.reset()
        path = ArrayList()
        distance = 0
        distanceD = 0.0
    }

    private fun templateFunctionPath(mode: Int){
        val n = data.adjacencyMatrix[0].size
        val start = data.start
        val goal = data.goal
        val hn = data.euclideanDistToGoal
        val startList: MutableList<Int> = ArrayList()
        startList.add(start)

        var fnS = 0.0

        if(mode == 2){
            fnS = hn[start]
        }
        prioQueue.add(Pair<List<Int>, Double>(startList, fnS))

        while (prioQueue.peek()?.first?.last()?.equals(goal) == false){
            val temp = prioQueue.poll()
            println((temp?.first ?: 0).toString() + " " + (temp?.second ?: 0))
            val tempInt = temp!!.first.last()

            for (i in 0 until n){
                if (data.adjacencyMatrix[tempInt][i] != 0){
                    val cost = data.adjacencyMatrix[tempInt][i].toDouble()
                    val tempList = ArrayList<Int>()

                    tempList.addAll(temp.first)
                    tempList.add(i)

                    var gn = temp.second + cost
                    var fn: Double

                    if (mode == 1){
                        fn = gn
                    }
                    else{
                        gn -= hn[tempInt]
                        fn = gn + hn[i]
                    }

                    println("fn = $fn")
                    prioQueue.add(Pair<List<Int>, Double>(tempList, fn))
                }
            }
        }
        val tempPath = prioQueue.poll()

        distanceD = tempPath?.second ?: 0.0
        path = tempPath?.first ?: ArrayList()
    }

    fun runAlgo(selectedMatrix: String, selectedCoor: String, algoType: Int, startIdx: Int, goalIdx: Int){
        reset()

        val reader = Input()
        reader.readMatrix(selectedMatrix)
        reader.readCoor(selectedCoor)

        if (reader.coordinates.size != reader.adjacencyMatrix[0].size){
            throw Exception("Coordinate count and node count does not match")
        }

        reader.start = startIdx
        reader.goal = goalIdx
        reader.readDist()

        initialize(reader)

        if(algoType == 1){
            println("A*")
            templateFunctionPath(1)
        } else{
            println("UCS")
            templateFunctionPath(2)
        }

        //println("Hasil:")
        for (i in path.indices) {
            print(path[i].toString() + " ")
        }
        println()
        println("Jarak: $distanceD")
    }
}