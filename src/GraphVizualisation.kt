package org.jetbrains.kotlin.Math

import javafx.scene.Group
import kotlin.math.cos
import kotlin.math.sin

//класс для визуализации графа(множество ребер и множество вершин)

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

    fun add_vertex(new_vertex: VertexVizualisation){
        graph.create_new_vertex(new_vertex.get_name().text)
        vertexes.add(new_vertex)
        edges.add(mutableListOf<Edge>())
        full_graph.children.add(new_vertex.group())
    }

    fun delete_vertex(vertex_to_delete : VertexVizualisation) {
        graph.delete_vertex(vertex_to_delete.get_number())
        full_graph.children.remove(vertex_to_delete.group())
        for (edge_list in edges) {
            for (edge in edge_list) {
                if (edge.get_positions().second == vertex_to_delete.get_number() - 1 || edge.get_positions().first == vertex_to_delete.get_number() - 1) {
                    full_graph.children.remove(edge.group())
                }
            }
        }
        vertexes.remove(vertex_to_delete)
        for (i in vertex_to_delete.get_number() - 1 until vertexes.size) {
            vertexes[i].decrease_number()
        }
        for (edgeline in edges){
            for(edge in edgeline){
                if (edge.get_positions().first > vertex_to_delete.get_number() - 1) edge.decrease_positions(1)
                if (edge.get_positions().second > vertex_to_delete.get_number() - 1) edge.decrease_positions(2)
            }
        }
    }

    fun delete_edge(edge : Edge){
        graph.data[edge.get_positions().first][edge.get_positions().second] = Int.MAX_VALUE
        graph.data[edge.get_positions().second][edge.get_positions().first] = Int.MAX_VALUE
        edges[edge.get_positions().second].remove(edge)
        full_graph.children.remove(edge.group())
    }

    fun get_edges() : MutableList<MutableList<Edge>> { return edges }

    fun get_vertexes() : MutableList<VertexVizualisation> { return vertexes }

    fun group() : Group { return full_graph }

    fun get_step() : Int { return step }
}