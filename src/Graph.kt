<<<<<<< HEAD
package org.jetbrains.kotlin.Math

import java.io.File
import java.util.*

open class Graph(var start_vertex: Int = 0) {
    var name_vertex: MutableList<String> = mutableListOf()
    var data = mutableListOf<MutableList<Int>>()

    fun default_name(quotient: Int): String {
        val base = 26
        val sb = StringBuilder()
        var coefficient = quotient
        while (coefficient > 0) {
            coefficient--
            val remainder = coefficient % base
            sb.append(('A'.code + remainder).toChar())
            coefficient /= base
        }
        return (sb.reverse().toString())
    }

    fun read_from_file(filename: String) {
        try {
            val file = File(filename)
            val scanner = Scanner(file)
            println("Если вы желаете задать имена вершин в файле, введите \"1\". Введите \"2\", чтобы имена вершин были заданы автоматически.")
            val flag = readln().toInt()
            if (flag == 1) this.name_vertex = scanner.nextLine().split(" ").toMutableList()
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                val row = line.split(" ").map { it.toInt() }.toMutableList()
                data.add(row)
            }
            if (flag == 2) {
                for (i in 0 until data.size) {
                    this.name_vertex.add(default_name(i + 1))
                }
            }
            modificate()
        }
        catch (e: Exception) { println("Файл не найден: $filename") }
    }

    fun read_from_console() {
        val scan = Scanner(System.`in`)
        println("Сколько вершин должно быть в графе?")
        val size = readln().toInt()
        println("Если вы желаете задать имена вершин, введите \"1\". Введите \"2\", чтобы имена вершин были заданы автоматически.")
        val flag = readln().toInt()
        when (flag) {
            1 -> {
                println("Введите имена вершин через пробел: ")
                this.name_vertex = scan.nextLine().split(" ").toMutableList()
            }

            2 -> {
                for (i in 0 until size) {
                    this.name_vertex.add(default_name(i + 1))
                }
            }
        }
        println("Введите матрицу смежности: ")
        for (i in 0 until size) {
            val line = scan.nextLine()
            val row = line.split(" ").map { it.toInt() }.toMutableList()
            data.add(row)
        }
        modificate()
    }


    override fun toString(): String {
        var string: String = ""
        data.forEach() {
            string += it.toString() + "\n"
        }
        return string
    }

    fun modificate() {
        for (i in 0 until data.size) {
            for (j in 0 until data[i].size) {
                if (data[i][j] == -1) data[i][j] = Int.MAX_VALUE
            }
        }
    }

    fun set_start_vertex(new_start_vertex: Int) { this.start_vertex = new_start_vertex }

    fun PrimAlgorithm(): Pair<Pair<MutableList<Pair<Int, Int>>, MutableList<MutableList<Pair<Int, Int>>>>, MutableList<Int>> {
        val edges_considered_at_the_step: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()
        val result_edges: MutableList<Pair<Int, Int>> = mutableListOf()
        val added_vertexes: MutableList<Int> = mutableListOf()
        added_vertexes.add(this.start_vertex)
        result_edges.add(Pair(0, 0))
        var index = 0
        while (added_vertexes.size < data.size) {
            edges_considered_at_the_step.add(mutableListOf())
            iterated_PrimAlgorithm(result_edges, added_vertexes, edges_considered_at_the_step[index])
            index++
        }
        edges_considered_at_the_step.add(mutableListOf())
        return Pair(Pair(result_edges, edges_considered_at_the_step), added_vertexes)
    }

    fun correct_graph(): Boolean {
        for (i in 0 until data.size) {
            var flag = false
            for (j in 0 until data[i].size) {
                if (data[i][j] != Int.MAX_VALUE) flag = true
            }
            if (!flag) {
                println("Созданный граф является несвязным или задано не более двух вершин.")
                return false
            }
        }
        return true
    }

    fun iterated_PrimAlgorithm(result_edges: MutableList<Pair<Int, Int>>, added_vertexes: MutableList<Int>,
                               edges_considered_at_the_step: MutableList<Pair<Int, Int>> ) {
        var min = Int.MAX_VALUE
        var new_edge = 0 to 0
        for (i in 0 until added_vertexes.size) {
            for (j in 0 until data[added_vertexes[i]].size) {
                if (j !in added_vertexes) {
                    if (data[added_vertexes[i]][j] < Int.MAX_VALUE) edges_considered_at_the_step.add(Pair(added_vertexes[i], j))
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

    fun create_new_vertex(name : String) {
        for (i in 0 until data.size) data[i].add(Int.MAX_VALUE)
        data.add(MutableList(data.size + 1) { Int.MAX_VALUE })
        name_vertex.add(name)
    }

    fun delete_vertex(number : Int){
        data.removeAt(number - 1)
        for (string in data){
            string.removeAt(number - 1)
        }
    }
}
=======
package org.jetbrains.kotlin.Math

import java.io.File
import java.util.*

// класс для графа. Граф представлен матрицей смежности, если ребра между вершинами нет, то в соответстсвующей ячейке будет значение Int.MAXVALUE
open class Graph(var start_vertex: Int = 0) {
    //mutableListOf - представляет собой динамически расширяемый список элементов.
    var name_vertex: MutableList<String> = mutableListOf()
    var data = mutableListOf<MutableList<Int>>()
//    var start_vertex: Int = 0

    // метод для чтения матрицы из файла
    fun read_from_file(filename: String) {
        try {
            val file = File(filename)
            val scanner = Scanner(file)
            println("Если вы желаете задать имена вершин в файле, введите \"1\". Введите \"2\", чтобы имена вершин были заданы автоматически.")
            val flag = readln().toInt()
            if (flag == 1) {
                this.name_vertex = scanner.nextLine().split(" ").toMutableList()
            }
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                val row = line.split(" ").map { it.toInt() }.toMutableList()
                data.add(row)
            }
            if (flag == 2) {
                val base = 26
                var quotient: Int
                for (i in 0 until data.size) {
                    quotient = i + 1
                    val sb = StringBuilder()
                    while (quotient > 0) {
                        quotient--
                        val remainder = quotient % base
                        sb.append(('A'.code + remainder).toChar())
                        quotient /= base
                    }
                    this.name_vertex.add(sb.reverse().toString())
                }
            }
            modificate()

        } catch (e: Exception) {
            println("Файл не найден: $filename")

        }
    }

    // метод для чтения матрицы из консоли
    fun read_from_console() {
        val scan = Scanner(System.`in`)
        println("Сколько вершин должно быть в графе?")
        val size = readln().toInt()
        println("Если вы желаете задать имена вершин, введите \"1\". Введите \"2\", чтобы имена вершин были заданы автоматически.")
        val flag = readln().toInt()
        when (flag) {
            1 -> {
                println("Введите имена вершин через пробел: ")
                this.name_vertex = scan.nextLine().split(" ").toMutableList()
            }

            2 -> {
                val base = 26
                var quotient: Int
                for (i in 0 until size) {
                    quotient = i + 1
                    val sb = StringBuilder()
                    while (quotient > 0) {
                        quotient--
                        val remainder = quotient % base
                        sb.append(('A'.code + remainder).toChar())
                        quotient /= base
                    }
                    this.name_vertex.add(sb.reverse().toString())
                }
            }
        }
        println("Введите матрицу смежности: ")
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

    fun set_start_vertex(new_start_vertex: Int) {
        this.start_vertex = new_start_vertex
    }

    // реализация алгоритма Прима для графа, возвращает массив пар, соответствующих началу и концу ребра
    fun PrimAlgorithm(): Pair<Pair<MutableList<Pair<Int, Int>>, MutableList<MutableList<Pair<Int, Int>>>>, MutableList<Int>> {
        val edges_considered_at_the_step: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()
        val result_edges: MutableList<Pair<Int, Int>> = mutableListOf()
        val added_vertexes: MutableList<Int> = mutableListOf()
        added_vertexes.add(this.start_vertex)
        result_edges.add(Pair(0, 0))
        var index = 0
        while (added_vertexes.size < data.size) {
            edges_considered_at_the_step.add(mutableListOf())
            iterated_PrimAlgorithm(result_edges, added_vertexes, edges_considered_at_the_step[index])
            index++
        }
        edges_considered_at_the_step.add(mutableListOf())
        return Pair(Pair(result_edges, edges_considered_at_the_step), added_vertexes)
    }
    fun correct_graph() : Boolean{
        for (i in 0 until data.size){
            var flag = false
            for (j in 0 until data[i].size ){
                if (data[i][j] != Int.MAX_VALUE) flag = true
            }
            if (!flag) return false
        }
        return true
    }
    fun iterated_PrimAlgorithm(result_edges: MutableList<Pair<Int, Int>>, added_vertexes: MutableList<Int>,
                               edges_considered_at_the_step: MutableList<Pair<Int, Int>>, ) {
        var min = Int.MAX_VALUE
        var new_edge = 0 to 0
        for (i in 0 until added_vertexes.size) {
            for (j in 0 until data[added_vertexes[i]].size) {
                if (j !in added_vertexes) {
                    if (data[added_vertexes[i]][j] < Int.MAX_VALUE) edges_considered_at_the_step.add(
                        Pair(
                            added_vertexes[i],
                            j
                        )
                    )
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
    fun create_new_vertex(){
        for (i in 0 until data.size) data[i].add(Int.MAX_VALUE)
        data.add(MutableList(data.size+1){Int.MAX_VALUE})
        print(this)
    }
}
>>>>>>> ac6fbe4cfc37591309b2261d097abdf70e8b676a
