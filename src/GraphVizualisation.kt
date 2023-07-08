<<<<<<< HEAD
package org.jetbrains.kotlin.Math

import javafx.scene.Group
import kotlin.math.cos
import kotlin.math.sin

//класс для визуализации графа(множество ребер и множество вершин)
class GraphVizualisation(scene_size: Double, val graph: Graph) {
    var vertexes = mutableListOf<VertexVizualisation>()
    val radius = (3.0 / 8.0) * scene_size
    var edges = mutableListOf<MutableList<Edge>>()
    var full_graph = Group()
    var step = -1

    init {
        var previous_y = (3.0 / 4.0) * scene_size
        var previous_x = scene_size / 2.0
        val alpha0 = 2.0 * Math.PI / graph.data.size.toDouble()
        print(graph.data.size)
        for (i in 0 until graph.data.size) {

            val alpha = alpha0 * i
            previous_x += radius * cos(alpha)
            previous_y -= radius * sin(alpha)
            vertexes.add(VertexVizualisation(scene_size, previous_x, previous_y, graph.name_vertex[i], i + 1))
        }
        for (i in 0 until graph.data.size) {
            edges.add(mutableListOf<Edge>())
            for (j in 0 until i) {
                if (graph.data[i][j] != Int.MAX_VALUE) {
                    edges[i].add(Edge(vertexes[i], vertexes[j], graph.data[i][j]))
                }
            }
        }

        for (edge_array in edges) {
            for (edge in edge_array)
                full_graph.children.add(edge.edgegroup)
        }
        for (vertex in vertexes) {
            full_graph.children.add(vertex.data)
        }
    }

    fun set_get_step(step: Int): Int {
        this.step = step
        return this.step
    }

    fun next_step(): Int {
        if (step < graph.data.size - 1) step += 1
        else step = graph.data.size - 1
        return step
    }

    fun previous_step(): Int {
        if (step > -1) step -= 1
        else step = -1
        return step
    }
    fun add_edge(new_edge : Edge, index1 : Int, index2 : Int) {
        if (index1 != index2) {
            graph.data[index1][index2] = new_edge.weight
            graph.data[index2][index1] = new_edge.weight
            if (index1 < index2) {
                edges[index2].add(index1, new_edge)
            } else {
                edges[index1].add(index2, new_edge)
            }
            full_graph.children.add(new_edge.edgegroup)
        }
    }
    fun add_vertex(new_vertex: VertexVizualisation){
        graph.create_new_vertex()
        vertexes.add(new_vertex)
        full_graph.children.add(new_vertex.data)
    }
=======
package org.jetbrains.kotlin.Math

import javafx.scene.Group
import kotlin.math.cos
import kotlin.math.sin

//класс для визуализации графа(множество ребер и множество вершин)
class GraphVizualisation(scene_size: Double, val graph: Graph) {
    var vertexes = mutableListOf<VertexVizualisation>()
    val radius = (3.0 / 8.0) * scene_size
    var edges = mutableListOf<MutableList<Edge>>()
    var full_graph = Group()
    var step = -1

    init {
        var previous_y = (3.0 / 4.0) * scene_size
        var previous_x = scene_size / 2.0
        val alpha0 = 2.0 * Math.PI / graph.data.size.toDouble()
        for (i in 0 until graph.data.size) {
            val alpha = alpha0 * i
            previous_x += radius * cos(alpha)
            previous_y -= radius * sin(alpha)
            vertexes.add(VertexVizualisation(scene_size, previous_x, previous_y, graph.name_vertex[i], i + 1))
        }
        for (i in 0 until graph.data.size) {
            edges.add(mutableListOf<Edge>())
            for (j in 0 until i) {
                if (graph.data[i][j] != Int.MAX_VALUE) {
                    edges[i].add(Edge(vertexes[i], vertexes[j], graph.data[i][j]))
                }
            }
        }

        for (edge_array in edges) {
            for (edge in edge_array)
                full_graph.children.add(edge.edgegroup)
        }
        for (vertex in vertexes) {
            full_graph.children.add(vertex.data)
        }
    }

    fun set_get_step(step: Int): Int {
        this.step = step
        return this.step
    }

    fun next_step(): Int {
        if (step < graph.data.size - 1) step += 1
        else step = graph.data.size - 1
        return step
    }

    fun previous_step(): Int {
        if (step > -1) step -= 1
        else step = -1
        return step
    }
>>>>>>> 5fa09004ea99ccf1b3b5822615b52576546331cc
}