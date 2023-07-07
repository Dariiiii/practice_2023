package org.jetbrains.kotlin.Math

import java.io.File
import java.util.*

// класс для графа. Граф представлен матрицей смежности, если ребра между вершинами нет, то в соответстсвующей ячейке будет значение Int.MAXVALUE
open class Graph() {
    //mutableListOf - представляет собой динамически расширяемый список элементов.
    var data = mutableListOf<MutableList<Int>>()

    // метод для чтения матрицы из файла
    fun read_from_file(file: File) {
        val scanner = Scanner(file)
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine()
            var row = line.split(" ").map { it.toInt() }.toMutableList()
            data.add(row)
        }
        modificate()
    }

    // метод для чтения матрицы из консоли
    fun read_from_console() {
        val scan = Scanner(System.`in`)
        val size = scan.nextInt()
        println()
        for (i in 0 until size) {
            val line = scan.nextLine()
            val row = line.split(" ").map { it.toInt() }.toMutableList()
            data.add(row)
        }
        modificate()
    }

    // метод, который возвращает строку для вывода информации о графе в консоль
    //????????????
    override fun toString(): String {
        var string: String = ""
        data.forEach() {
            string += it.toString() + "\n"
        }
        return string
    }

    // метод для переопределения весов ребер(если в файле было -1, значит ребра нет)
    fun modificate() {
        for (i in 0 until data.size) {
            for (j in 0 until data[i].size) {
                if (data[i][j] == -1) {
                    data[i][j] = Int.MAX_VALUE
                }
            }
        }
    }

    // реализация алгоритма Прима для графа, возвращает массив пар, соответствующих началу и концу ребра
    fun PrimAlgorithm(): Pair<Pair<MutableList<Pair<Int, Int>>, MutableList<MutableList<Pair<Int, Int>>>>, MutableList<Int>> {
        val edges_considered_at_the_step: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()
        val result_edges: MutableList<Pair<Int, Int>> = mutableListOf()
        val added_vertexes: MutableList<Int> = mutableListOf()
        added_vertexes.add(0)
        result_edges.add(Pair(0,0))
        var index = 0
        while (added_vertexes.size < data.size) {
            edges_considered_at_the_step.add(mutableListOf())
            iterated_PrimAlgorithm(result_edges, added_vertexes, edges_considered_at_the_step[index])
            index++
        }
        edges_considered_at_the_step.add(mutableListOf())
//        println(edges_considered_at_the_step)
        return Pair(Pair(result_edges, edges_considered_at_the_step), added_vertexes)
    }

    fun iterated_PrimAlgorithm(
        result_edges: MutableList<Pair<Int, Int>>,
        added_vertexes: MutableList<Int>,
        edges_considered_at_the_step: MutableList<Pair<Int, Int>>,
    ) {
        var min = Int.MAX_VALUE
        var new_edge = 0 to 0
        for (i in 0 until added_vertexes.size) {
            for (j in i until data[added_vertexes[i]].size) {
                if (j !in added_vertexes) {
                    edges_considered_at_the_step.add(Pair(added_vertexes[i], j))
                    if (data[added_vertexes[i]][j] < min) {
                        min = data[added_vertexes[i]][j]
                        new_edge = Pair(added_vertexes[i], j)
                    }
                }
            }
        }
        added_vertexes.add(new_edge.second)
        result_edges.add(new_edge)
    }
}