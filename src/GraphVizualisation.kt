package org.jetbrains.kotlin.Math

import javafx.scene.Group
import kotlin.math.cos
import kotlin.math.sin

/**
 * Класс для визуализации графа (множество ребер и множество вершин).
 *
 * @param scene_size размер сцены для визуализации
 * @property graph граф, который нужно визуализировать
 * @property vertexes список вершин графа
 * @property radius радиус окружности, на которой расположены вершины графа
 * @property edges список ребер графа
 * @property full_graph группа, содержащая все вершины и ребра графа
 * @property step шаг визуализации графа
 */
class GraphVizualisation(scene_size: Double, val graph: Graph) {
    private var vertexes = mutableListOf<VertexVizualisation>()
    private val radius = (3.0 / 8.0) * scene_size
    private var edges = mutableListOf<MutableList<Edge>>()
    private var full_graph = Group()
    private var step = -1

    init {
        var previous_y : Double
        var previous_x : Double
        val alpha0 = 2.0 * Math.PI / graph.data.size.toDouble()
        for (i in 0 until graph.data.size) {
            val alpha = alpha0 * i
            previous_x = radius * cos(alpha) + 500.0
            previous_y = radius * sin(alpha) + scene_size / 2.0
            vertexes.add(VertexVizualisation(scene_size, previous_x, previous_y, graph.name_vertex[i], i + 1))
        }
        for (i in 0 until graph.data.size) {
            edges.add(mutableListOf<Edge>())
            for (j in 0 until i + 1) {
                if (graph.data[i][j] != Int.MAX_VALUE) {
                    edges[i].add(Edge(vertexes[j], vertexes[i], graph.data[i][j]))
                }
            }
        }
        for (edge_array in edges) {
            for (edge in edge_array)
                full_graph.children.add(edge.group())
        }
        for (vertex in vertexes) {
            full_graph.children.add(vertex.group())
        }
    }

    /**
     * Устанавливает и возвращает текущий шаг при визуализации.
     *
     * @param step: Номер шага.
     * @return: Новый номер шага.
     */
    fun set_get_step(step: Int): Int {
        this.step = step
        return this.step
    }

    /**
     * Возвращает список ребер графа.
     *
     * @return: Список ребер графа.
     */
    fun get_edges() : MutableList<MutableList<Edge>> { return edges }

    /**
     * Возвращает список вершин графа.
     *
     * @return: Список вершин графа.
     */
    fun get_vertexes() : MutableList<VertexVizualisation> { return vertexes }

    /**
     * Возвращает объект Group, содержащий все ребра и вершины графа.
     *
     * @return: Объект Group.
     */
    fun group() : Group { return full_graph }

    /**
     * Возвращает текущий шаг при визуализации.
     *
     * @return: Номер текущего шага.
     */
    fun get_step() : Int { return step }

    /**
     * Переходит к следующему шагу при визуализации и возвращает новый номер шага.
     *
     * @return: Новый номер шага.
     */
    fun next_step(): Int {
        if (step < graph.data.size - 1) step += 1
        else step = graph.data.size - 1
        return step
    }

    /**
     * Переходит к предыдущему шагу при визуализации и возвращает новый номер шага.
     *
     * @return: Новый номер шага.
     */
    fun previous_step(): Int {
        if (step > -1) step -= 1
        else step = -1
        return step
    }

    /**
     * Добавляет новое ребро в граф.
     *
     * @param new_edge: Новое ребро для добавления.
     */
    fun add_edge(new_edge : Edge) {
        if (new_edge.get_positions().second != new_edge.get_positions().first) {
            graph.data[new_edge.get_positions().first][new_edge.get_positions().second] = new_edge.get_weight()
            graph.data[new_edge.get_positions().second][new_edge.get_positions().first] = new_edge.get_weight()
            for (edge in edges[new_edge.get_positions().second]) {
                if (edge.get_positions().first == new_edge.get_positions().first) {
                    edges[new_edge.get_positions().second].remove(edge)
                    full_graph.children.remove(edge.group())
                    break
                }
            }
            edges[new_edge.get_positions().second].add(new_edge)
            full_graph.children.add(0,new_edge.group())
        }
    }

    /**
     * Метод для добавления новой вершины в граф.
     *
     * @param new_vertex новая вершина, которую нужно добавить в граф
     */
    fun add_vertex(new_vertex: VertexVizualisation){
        graph.create_new_vertex(new_vertex.get_name().text)
        vertexes.add(new_vertex)
        edges.add(mutableListOf<Edge>())
        full_graph.children.add(new_vertex.group())
    }

    /**
     * Метод для удаления вершины из графа.
     *
     * @param vertex_to_delete вершина, которую нужно удалить из графа
     */
    fun delete_vertex(vertex_to_delete : VertexVizualisation) {
        for (edge in edges[vertex_to_delete.get_number()-1]) {
            if (edge.get_positions().first == vertex_to_delete.get_number() - 1 || edge.get_positions().second == vertex_to_delete.get_number() - 1 ) {
                full_graph.children.remove(edge.group())
            }
        }
        edges.removeAt(vertex_to_delete.get_number() - 1)
        for (i in (vertex_to_delete.get_number() - 1)until edges.size) {
            var size = 0
            while (size < edges[i].size) {
                val edge = edges[i][size]
                if (edge.get_positions().first == (vertex_to_delete.get_number() - 1)) {
                    full_graph.children.remove(edge.group())
                    edges[i].remove(edge)
                }
                else size++
            }
        }
        graph.delete_vertex(vertex_to_delete.get_number())
        full_graph.children.remove(vertex_to_delete.group())
        vertexes.remove(vertex_to_delete)
        for (i in vertex_to_delete.get_number() - 1 until vertexes.size) {
            vertexes[i].decrease_number()
        }
        for (i in 0 until edges.size){
            for(edge in edges[i]){
                if (edge.get_positions().first > vertex_to_delete.get_number() - 1)
                    edge.decrease_positions(1)
                if (edge.get_positions().second > vertex_to_delete.get_number() - 1)
                    edge.decrease_positions(2)
            }
        }
    }

    /**
     * Метод для удаления ребра из графа.
     *
     * @param edge ребро, которое нужно удалить из графа
     */
    fun delete_edge(edge : Edge){
        graph.data[edge.get_positions().first][edge.get_positions().second] = Int.MAX_VALUE
        graph.data[edge.get_positions().second][edge.get_positions().first] = Int.MAX_VALUE
        edges[edge.get_positions().second].remove(edge)
        full_graph.children.remove(edge.group())
    }

}