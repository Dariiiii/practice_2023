package org.jetbrains.kotlin.Math
import java.io.File
import java.util.*
open class Graph() {

    var data = mutableListOf<MutableList<Int>>()

    fun read_from_file(file : File){
        val scanner = Scanner(file)
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine()
            var row = line.split(" ").map{ it.toInt() }.toMutableList()
            data.add(row)
        }
        modificate()
    }

    fun read_from_console(){
        val size = scan.nextInt()
        val scan = java.util.Scanner(System.`in`)

        println()
        for(i in 0 until size){
            val line = scan.nextLine()
            val row = line.split(" ").map { it.toInt() }.toMutableList()
            data.add(row)
        }
        modificate()
    }

    override fun toString(): String {
        var string : String = ""
        data.forEach(){
            string += it.toString() + "\n"
        }
        return string
    }

    fun modificate(){
        for(i in 0 until data.size){
            for(j in 0 until data[i].size){
                if (data[i][j] == -1){
                    data[i][j] = Int.MAX_VALUE
                }
            }
        }
    }

    fun PrimAlgorithm() : Array<Pair<Int,Int>>{
        var result_edges : Array<Pair<Int,Int>> = arrayOf()
        var added_vertexes : Array<Int> = arrayOf()
        added_vertexes = added_vertexes.plusElement(0)
        while (added_vertexes.size < data.size){
            var min = Int.MAX_VALUE
            var min_edge : Pair<Int,Int> = (0 to 0)
            for (i in 0 until added_vertexes.size){
                for (j in 0 until data[added_vertexes[i]].size){
                        if (data[added_vertexes[i]][j] < min && !(j in added_vertexes)){
                            min = data[added_vertexes[i]][j]
                            min_edge = Pair(added_vertexes[i], j)
                        }
                }
            }
            added_vertexes = added_vertexes.plusElement(min_edge.second)
            result_edges = result_edges.plusElement(min_edge)
        }
        return result_edges
    }
}