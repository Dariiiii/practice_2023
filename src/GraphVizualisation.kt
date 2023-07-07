package org.jetbrains.kotlin.Math

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.text.Font
import javafx.scene.text.Text
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

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
            vertexes.add(VertexVizualisation(scene_size, previous_x, previous_y, i + 1))
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

    fun set_get_step(step: Int) : Int{
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
}