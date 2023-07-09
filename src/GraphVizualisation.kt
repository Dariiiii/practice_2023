package org.jetbrains.kotlin.Math

import javafx.scene.Group
import kotlin.math.cos
import kotlin.math.sin

class GraphVizualisation(scene_size: Double, val graph: Graph) {
    var vertexes = mutableListOf<VertexVizualisation>()
    val radius = (3.0 / 8.0) * scene_size
    var edges = mutableListOf<MutableList<Edge>>()
    var full_graph = Group()
    var step = -1
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

    fun add_edge(new_edge : Edge) {
        if (new_edge.position_2 != new_edge.position_1) {
            graph.data[new_edge.position_1][new_edge.position_2] = new_edge.weight
            graph.data[new_edge.position_2][new_edge.position_1] = new_edge.weight
            for (edge in edges[new_edge.position_2]) {
                if (edge.position_1 == new_edge.position_1) {
                    edges[new_edge.position_2].remove(edge)
                    full_graph.children.remove(edge.edgegroup)
                    break
                }
            }
            edges[new_edge.position_2].add(new_edge)
            full_graph.children.add(0,new_edge.edgegroup)
        }
    }

    fun add_vertex(new_vertex: VertexVizualisation){
        graph.create_new_vertex(new_vertex.name.text)
        vertexes.add(new_vertex)
        edges.add(mutableListOf<Edge>())
        full_graph.children.add(new_vertex.data)
    }

    fun delete_vertex(vertex_to_delete : VertexVizualisation) {
        graph.delete_vertex(vertex_to_delete.number)
        full_graph.children.remove(vertex_to_delete.data)
        for (edge_list in edges) {
            for (edge in edge_list) {
                if (edge.position_2 == vertex_to_delete.number - 1 || edge.position_1 == vertex_to_delete.number - 1) {
                    full_graph.children.remove(edge.edgegroup)
                }
            }
        }
        vertexes.remove(vertex_to_delete)
        for (i in vertex_to_delete.number - 1 until vertexes.size) {
            vertexes[i].number -= 1
        }
        for (edgeline in edges){
            for(edge in edgeline){
                if (edge.position_1 > vertex_to_delete.number - 1) edge.position_1 --
                if (edge.position_2 > vertex_to_delete.number - 1) edge.position_2 --
            }
        }
    }

    fun delete_edge(edge : Edge){
        graph.data[edge.position_1][edge.position_2] = Int.MAX_VALUE
        graph.data[edge.position_2][edge.position_1] = Int.MAX_VALUE
        edges[edge.position_2].remove(edge)
        full_graph.children.remove(edge.edgegroup)
    }
}