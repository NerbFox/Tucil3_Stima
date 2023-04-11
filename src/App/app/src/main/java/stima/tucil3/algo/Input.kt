package stima.tucil3.algo

import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class Input {
    var start: Int = 0
    var goal: Int = 0

    var adjacencyMatrix: MutableList<MutableList<Int>> = mutableListOf()
    var coordinates: MutableList<Pair<Double, Double>> = mutableListOf()
    var names: MutableList<String> = mutableListOf()
    var euclideanDistToGoal: MutableList<Double> = mutableListOf()

    fun readMatrix(matrixContent: String){
        val reader = Scanner(matrixContent)
        while(reader.hasNextLine()){
            val data = reader.nextLine()
            val dataSplit = data.split(" ")
            val row = ArrayList<Int>()
            for (i in dataSplit.indices) {
                row.add(dataSplit[i].toInt())
            }
            adjacencyMatrix.add(row)
        }
    }

    fun reset(){
        adjacencyMatrix.clear()
        coordinates.clear()
        names.clear()
        euclideanDistToGoal.clear()
    }

    fun readCoor(coorContent: String){
        val reader = Scanner(coorContent)

        while(reader.hasNextLine()){
            val data = reader.nextLine()
            val dataSplit = data.split(" ")
            if(dataSplit.size != 3){
                throw Exception("Column count in coordinate file is invalid")
            }
            coordinates.add(Pair(dataSplit[0].toDouble(), dataSplit[1].toDouble()))
            names.add(dataSplit[2])
        }
        reader.close()
    }

    fun readDist(){
        val goalX = coordinates[goal].first
        val goalY = coordinates[goal].second

        for (i in 0 until coordinates.size) {
            val x = coordinates[i].first
            val y = coordinates[i].second
            euclideanDistToGoal.add(euclideanDistance(x, y, goalX, goalY))
        }
    }

    private fun euclideanDistance(x1: Double, y1: Double, x2: Double, y2: Double): Double{
        return sqrt((x1 - x2).pow(2.0) + (y1 - y2).pow(2.0))
    }
    private fun lattitudeToMeters(lattitude: Double): Double {
//        val EarthRadius = 6371000.0
//        val lat = Math.toRadians(lattitude!!)
//        return EarthRadius * Math.log(Math.tan(Math.PI / 4 + lat / 2))
        return lattitude * 111319
    }

    private fun longitudeToMeters(longitude: Double): Double {
//        val EarthRadius = 6371000.0
//        return Math.toRadians(longitude!!) * EarthRadius
        return longitude * 111319
    }

    // change euclidean distance to meters
    fun changeEuclideanToMeters() {
        // clear euclideanDistToGoal
        euclideanDistToGoal.clear()
        var goalX: Double = coordinates[goal].first
        var goalY: Double = coordinates[goal].second
        goalX = lattitudeToMeters(goalX)
        goalY = lattitudeToMeters(goalY)
        var x: Double
        var y: Double
        for (i in coordinates.indices) {
            x = coordinates[i].first // lattitude
            y = coordinates[i].second // longitude
            x = lattitudeToMeters(x)
            y = longitudeToMeters(y)
            euclideanDistToGoal.add(euclideanDistance(x, y, goalX, goalY))
        }
    }

}