package stima.tucil3.algo

import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class Input {
    var start: Int = 0
    var goal: Int = 0

    var adjacencyMatrix: MutableList<MutableList<Int>> = mutableListOf()
    var coordinates: MutableList<Pair<Double, Double>> = mutableListOf()
    var names: MutableList<String> = mutableListOf()
    var euclideanDistToGoal: MutableList<Double> = mutableListOf()
    var heuristic: MutableList<MutableList<Double>> = mutableListOf()

    fun readMatrix(matrixContent: String){
        val reader = Scanner(matrixContent)
        while(reader.hasNextLine()){
            var data = reader.nextLine()
            var dataSplit = data.split(" ")
            var row = ArrayList<Int>()
            for (i in 0 until dataSplit.size) {
                row.add(dataSplit.get(i).toInt())
            }
            adjacencyMatrix.add(row)
        }
    }

    fun reset(){
        adjacencyMatrix.clear()
        coordinates.clear()
        names.clear()
        euclideanDistToGoal.clear()
        heuristic.clear()
    }

    fun readCoor(coorContent: String){
        val reader = Scanner(coorContent)

        while(reader.hasNextLine()){
            var data = reader.nextLine()
            var dataSplit = data.split(" ")
            if(dataSplit.size != 3){
                throw Exception("Column count in coordinate file is invalid")
            }
            coordinates.add(Pair<Double, Double>(dataSplit[0].toDouble(), dataSplit[1].toDouble()))
            names.add(dataSplit[2])
        }
        reader.close()
    }

    fun readDist(){
        var goalX = coordinates.get(goal).first
        var goalY = coordinates.get(goal).second

        for (i in 0 until coordinates.size) {
            var x = coordinates.get(i).first
            var y = coordinates.get(i).second
            euclideanDistToGoal.add(Math.sqrt(Math.pow(goalX - x, 2.0) + Math.pow(goalY - y, 2.0)))
        }
    }

    fun euclideanDistance(x1: Double, y1: Double, x2: Double, y2: Double): Double{
        return Math.sqrt(Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0))
    }

}