package org.jetbrains.kotlin.Math
import java.io.File
import java.util.*
// класс для графа. Граф представлен матрицей смежности, если ребра между вершинами нет, то в соответстсвующей ячейке будет значение Int.MAXVALUE
open class Graph() {

    var data = mutableListOf<MutableList<Int>>()
// метод для чтения матрицы из файла
    fun read_from_file(file : File){
        val scanner = Scanner(file)
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine()
            var row = line.split(" ").map{ it.toInt() }.toMutableList()
            data.add(row)
        }
        modificate()
    }
// метод для чтения матрицы из консоли
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
// метод, который возвращает строку для вывода информации о графе в консоль
    override fun toString(): String {
        var string : String = ""
        data.forEach(){
            string += it.toString() + "\n"
        }
        return string
    }
// метод для переопределения весов ребер(если в файле было -1, значит ребра нет)
    fun modificate(){
        for(i in 0 until data.size){
            for(j in 0 until data[i].size){
                if (data[i][j] == -1){
                    data[i][j] = Int.MAX_VALUE
                }
            }
        }
    }
// реализация алгоритма Прима для графа, возвращает массив пар, соответствующих началу и концу ребра
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