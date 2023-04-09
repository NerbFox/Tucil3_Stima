package stima.tucil3.algo

import java.lang.Exception

class Algo {
    fun runAlgo(selectedMatrix: String, selectedCoor: String, algoType: Int, startIdx: Int, goalIdx: Int){
        var reader = Input()
        reader.readMatrix(selectedMatrix)
        reader.readCoor(selectedCoor)

        if (reader.coordinates.size != reader.adjacencyMatrix.get(0).size){
            throw Exception("Coordinate count and node count does not match")
        }

        reader.start = startIdx
        reader.goal = goalIdx
        reader.readDist()

        //TODO: Implement
    }
}