package org.jetbrains.kotlin.Math

import java.io.File
import java.util.*
import kotlin.system.exitProcess


/**
 * Ккласс для графа. Граф представлен матрицей смежности, если ребра между вершинами нет, то в соответстсвующей ячейке будет значение Int.MAXVALUE.
 *
 * @property start_vertex начальная вершина
 * @property name_vertex список имен вершин
 * @property data матрица смежности графа
 */
open class Graph(var start_vertex: Int = 0) {
    var name_vertex: MutableList<String> = mutableListOf()
    var data = mutableListOf<MutableList<Int>>()
    /**
     * Метод для генерации имени вершины по умолчанию.
     *
     * @param quotient порядок вершины для генерации имени вершины
     * @return сгенерированное имя вершины
     */
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

    /**
     * Метод для чтения матрицы смежности из файла.
     *
     * @param filename имя файла, из которого будет производиться чтение
     */
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
        } catch (e: Exception) {
            println("Файл не найден: $filename")
        }
    }

    /**
     * Метод для чтения матрицы смежности из консоли.
     */
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
        println("Введите матрицу: ")
        for (i in 0 until size) {
            val line = scan.nextLine()
            val row = line.split(" ").map { it.toInt() }.toMutableList()
            data.add(row)
        }
        modificate()
    }
    /**
     * Метод для отражения матрицы смежности (симметричность графа).
     */
    fun reflect_matrix(){
        if (check_correct_matrix()) {
            for (i in 0 until data.size) {
                for (j in (i + 1) until data.size) {
                    data[i].add(data[j][i])
                }
            }
        }
        else {
            println("Матрица введена неверно. Невозможно создать граф. Попробуйте снова.")
            exitProcess(0)
        }
    }
    /**
     * Метод для проверки корректности матрицы смежности.
     *
     * @return true, если матрица корректна, иначе false
     */
    fun check_correct_matrix(): Boolean {
        var flag = true
        for (i in 0 until data.size) {
            if (data[i].size != (i + 1)) flag = false
        }
        return flag
    }
    /**
     * Метод для проверки симметричности матрицы смежности.
     */
    fun check_symmetry(){
        var check = true
        for (i in 0 until data.size) {
            if (check) {
                for (j in 0 until data.size) {
                    if (data[i][j] != data[j][i]) {
                        println("Матрица смежности несимметрична.")
                        check = false
                    }
                }
            }
        }
    }

    // метод для переопределения весов ребер(если в файле было -1, значит ребра нет)
    fun modificate() {
        for (i in 0 until data.size) {
            for (j in 0 until data[i].size) {
                if (data[i][j] == -1 || i == j) data[i][j] = Int.MAX_VALUE
            }
        }
    }

    // метод, который возвращает строку для вывода информации о графе в консоль
    override fun toString(): String {
        var string = ""
        data.forEach() {
            string += it.toString() + "\n"
        }
        return string
    }
    /**
     * Метод для установки вершины начала.
     *
     * @param new_start_vertex новая вершина начала
     */
    fun set_start_vertex(new_start_vertex: Int) {
        this.start_vertex = new_start_vertex
    }

    /**
     * Реализация алгоритма Прима для графа, возвращающая массив пар, соответствующих началу и концу ребра.
     *
     * @return пара, содержащая массив пар ребер и массив массивов пар ребер, а также список добавленных вершин
     */
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
    /**
     * Метод для проверки корректности графа.
     *
     * @return true, если граф корректен, иначе false
     */
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
    /**
     * Итерационная реализация алгоритма Прима для графа.
     *
     * @param result_edges список ребер
     * @param added_vertexes список добавленных вершин
     * @param edges_considered_at_the_step список ребер, рассмотренных на текущем шаге
     */
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
    /**
     * Метод для создания новой вершины в графе.
     *
     * @param name имя новой вершины
     */
    fun create_new_vertex(name: String) {
        for (i in 0 until data.size) data[i].add(Int.MAX_VALUE)
        data.add(MutableList(data.size + 1) { Int.MAX_VALUE })
        name_vertex.add(name)
    }
    /**
     * Метод для удаления вершины из графа.
     *
     * @param number номер удаляемой вершины
     */
    fun delete_vertex(number: Int) {
        data.removeAt(number - 1)
        for (string in data) {
            string.removeAt(number - 1)
        }
    }
    /**
     * Метод для получения матрицы смежности графа.
     *
     * @return матрица смежности графа
     */
    fun get_matrix(): MutableList<MutableList<Int>> { return data }
    /**
     * Метод для получения списка имен вершин графа.
     *
     * @return список имен вершин графа
     */
    fun get_names(): MutableList<String> { return name_vertex }

}