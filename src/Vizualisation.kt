package org.jetbrains.kotlin.Math

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage

class Vizualisation : Application() {
    lateinit var graph_visual: GraphVizualisation
    lateinit var full_group: Group
    lateinit var step_information: Label
    val graph_editor = GraphEditor()

    override fun start(stage: Stage) {
        val graph = Graph()
        val filename = "Graph1.txt"
        stage.title = "Визуализация алгоритма Прима"
        stage.width = 1000.0
        stage.height = 800.0
        val new_vertex = Button("добавить вершину")
        new_vertex.setOnAction { graph_editor.create_vertex(stage, graph_visual, step_information) }
        val new_edge = Button("добавить/изменить ребро")
        new_edge.setOnAction { graph_editor.create_edge(stage, graph_visual, step_information) }
        val delete = Button("удалить элемент графа")
        delete.setOnAction { graph_editor.delete_element(stage, graph_visual,step_information) }
        val next_step = Button("следующий шаг")
        next_step.setOnAction {
            if (graph_visual.step == -1) {
                if (graph_visual.graph.correct_graph()) step_by_step_algorythm(graph_visual.next_step())
            } else step_by_step_algorythm(graph_visual.next_step())
        }
        val previous_step = Button("предыдущий шаг")
        previous_step.setOnAction { step_by_step_algorythm(graph_visual.previous_step()) }
        val final = Button("перейти к результату")
        final.setOnAction { step_by_step_algorythm(graph_visual.set_get_step(graph_visual.graph.data.size - 1)) }
        val first_step = Button("перейти к началу/форматирование графа")
        val movements = HBox(first_step, previous_step, next_step, final)
        val operations = HBox(new_edge, new_vertex, delete)
        operations.spacing = 10.0
        movements.spacing = 10.0
        first_step.setOnAction { draw_graph(stage, graph, operations, movements) }
        println("Как вы хотите ввести граф : \"1\" - из файла(матрица смежности), \"2\" - из консоли(матрица смежности), \"3\" - в режиме реального времени самому нарисовать граф.")
        val variant = scan.nextInt()
        when (variant) {
            1 -> {
                println("Матрица смежности должна быть представлена в формате : симметричная матрица с \"-1\" на главной диагонали, где \"-1\" означает отсутствие ребра между двумя вершинами.")
                graph.read_from_file(filename)
            }

            2 -> {
                println("Матрица смежности должна быть представлена в формате : симметричная матрица с \"-1\" на главной диагонали, где \"-1\" означает отсутствие ребра между двумя вершинами.")
                graph.read_from_console()
            }
        }
        draw_graph(stage, graph, operations, movements)
        if (operations.width == 0.0) {
            Platform.runLater {
                val width1 = operations.width
                val width2 = movements.width
                val height2 = movements.height
                operations.layoutX = stage.width / 2.0 - width1 / 2.0
                operations.layoutY = 5.0
                movements.layoutX = stage.width / 2.0 - width2 / 2.0
                movements.layoutY = stage.height - 3.0 * height2
            }
        }
        stage.show()
    }

    fun step_by_step_algorythm(step: Int) {
        val step_data = graph_visual.graph.PrimAlgorithm()
        val result_edges = step_data.first.first
        val edges_considered_at_the_step = step_data.first.second
        val considered_vertexes = step_data.second
        for (edge_list in graph_visual.edges) {
            for (edge in edge_list) {
                edge.line.stroke = Color.DARKGRAY
                if (step != -1) {
                    if (Pair(edge.position_1, edge.position_2) in result_edges.subList(0, step + 1) || Pair(
                            edge.position_2, edge.position_1
                        ) in result_edges.subList(0, step + 1)
                    ) {
                        edge.line.stroke = Color.RED
                    } else if (Pair(edge.position_1, edge.position_2) in edges_considered_at_the_step[step] || Pair(
                            edge.position_2, edge.position_1
                        ) in edges_considered_at_the_step[step]
                    ) {
                        edge.line.stroke = Color.BLUE
                    }
                }
            }
        }

        for (vertex in graph_visual.vertexes) vertex.circle.stroke = Color.BLACK
        if (step == -1) graph_visual.vertexes[graph_visual.graph.start_vertex].circle.stroke = Color.RED
        for (i in 0 until step + 1) {
            graph_visual.vertexes[considered_vertexes[i]].circle.stroke = Color.RED
        }
        update_step_information(step, considered_vertexes, edges_considered_at_the_step)
    }

    fun update_step_information(step: Int, considered_vertexes: MutableList<Int>,
        added_edges: MutableList<MutableList<Pair<Int, Int>>>, ) {
        var weights = ""
        if (step >= 0) {
            for (edge in added_edges[step])
                weights += " ${graph_visual.graph.data[edge.first][edge.second]}"
        }
        if (step < graph_visual.graph.data.size - 1 && step > 0) step_information.text = "На шаге №$step была " +
                "добавлена вершина \"${graph_visual.vertexes[considered_vertexes[step]].name.text}\". Ребра, " +
                "рассматриваемые на данном шаге, имеют веса $weights"
        else if (step == 0) step_information.text = "Вершина  \"" +
                "${graph_visual.vertexes[considered_vertexes[0]].name.text}\" была выбрана в качестве начальной. " +
                "Ребра, рассматриваемые на данном шаге, имеют веса $weights"
        else if (step == graph_visual.graph.data.size - 1) step_information.text = "На шаге №$step была добавлена " +
                "вершина \"${graph_visual.vertexes[considered_vertexes.last()].name.text}\". Построение минимального " +
                "остовного дерева окончено"
        else step_information.text = "Чтобы выбрать выбрать новую начальную вершину нажмите на нее дважды." +
                " Редактирование графа доступно только на данном шаге."
    }

    fun draw_graph(stage: Stage, graph: Graph, buttons1: HBox, buttons2: HBox) {
        graph_visual = GraphVizualisation(stage.height, graph)
        step_information = Label(
            "Чтобы выбрать выбрать новую начальную вершину нажмите на нее дважды." +
                    " Редактирование графа доступно только на данном шаге.")
        step_information.layoutX = 30.0
        step_information.layoutY = stage.height - 100.0
        full_group = Group(graph_visual.full_graph, buttons1, buttons2, step_information)
        val scene = Scene(full_group, 1000.0, 800.0)
        stage.setScene(scene)
        graph_editor.action(stage, graph_visual)
    }

}