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
    private lateinit var graph_visual : GraphVizualisation
    private lateinit var full_group : Group
    private lateinit var step_information : Label
    private val graph_editor = GraphEditor()

    override fun start(stage: Stage) {
        val graph = Graph()
        val filename = "Graph1.txt"
        stage.title = "Визуализация алгоритма Прима"
        stage.width = 1000.0
        stage.height = 800.0
        val new_vertex = Button("добавить вершину")
        val new_edge = Button("добавить/изменить ребро")
        val delete = Button("удалить элемент графа")
        val next_step = Button("следующий шаг")
        val previous_step = Button("предыдущий шаг")
        val final = Button("перейти к результату")
        val first_step = Button("перейти к началу/форматирование графа")
        val movements = HBox(first_step, previous_step, next_step, final)
        val operations = HBox(new_edge, new_vertex, delete)
        operations.spacing = 10.0
        movements.spacing = 10.0
        new_vertex.setOnAction { graph_editor.create_vertex(stage, graph_visual, step_information,final,first_step,previous_step,next_step) }
        new_edge.setOnAction { graph_editor.create_edge(stage, graph_visual, step_information,final,first_step,previous_step,next_step) }
        delete.setOnAction { graph_editor.delete_element(stage, graph_visual, step_information,final,first_step,previous_step,next_step) }
        next_step.setOnAction {
            if (graph_visual.get_step() == -1) {
                new_vertex.isDisable = true
                new_edge.isDisable = true
                delete.isDisable = true
                if (graph_visual.graph.correct_graph()) step_by_step_algorythm(graph_visual.next_step(),new_edge,new_vertex,delete)
            }
            else step_by_step_algorythm(graph_visual.next_step(),new_edge,new_vertex,delete)
        }
        previous_step.setOnAction { step_by_step_algorythm(graph_visual.previous_step(),new_edge,new_vertex,delete) }
        final.setOnAction {
            if (graph_visual.get_step() == -1) {
                new_vertex.isDisable = true
                new_edge.isDisable = true
                delete.isDisable = true
                if (graph_visual.graph.correct_graph()) step_by_step_algorythm(graph_visual.set_get_step(graph_visual.graph.data.size - 1), new_edge, new_vertex,delete)
            }
                else step_by_step_algorythm(graph_visual.set_get_step(graph_visual.graph.data.size - 1),new_edge,new_vertex,delete)
        }
        first_step.setOnAction {
                new_vertex.isDisable = false
                new_edge.isDisable = false
                delete.isDisable = false
                draw_graph(stage, graph, operations, movements)
        }
        println("Как вы хотите ввести граф : \n\"1\" - из файла(матрица смежности), \n\"2\" - из консоли(матрица смежности), \n\"3\" - из файла(нижнетреугольная матрица)," +
                "\n\"4\" - из консоли(нижнетреугольная матрица), \n\"5\" - в режиме реального времени самому нарисовать граф.")
        val variant = scan.nextInt()
        when (variant) {
            1 -> {
                println("Матрица смежности должна быть представлена в формате : симметричная матрица с \"-1\" на главной диагонали, где \"-1\" означает отсутствие ребра между двумя вершинами.")
                graph.read_from_file(filename)
                graph.check_symmetry()
            }

            2 -> {
                println("Матрица смежности должна быть представлена в формате : симметричная матрица с \"-1\" на главной диагонали, где \"-1\" означает отсутствие ребра между двумя вершинами.")
                graph.read_from_console()
                graph.check_symmetry()
            }
            3 -> {
                println("Матрица должна быть представлена в формате : нижнетреугольная матрица с \"-1\" на главной диагонали, где \"-1\" означает отсутствие ребра между двумя вершинами.")
                graph.read_from_file(filename)
                graph.reflect_matrix()
            }
            4 -> {
                println("Матрица должна быть представлена в формате : нижнетреугольная матрица с \"-1\" на главной диагонали, где \"-1\" означает отсутствие ребра между двумя вершинами.")
                graph.read_from_console()
                graph.reflect_matrix()
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

    fun step_by_step_algorythm(step: Int, new_edge: Button, new_vertex: Button, delete: Button) {
        val step_data = graph_visual.graph.PrimAlgorithm()
        val result_edges = step_data.first.first
        val edges_considered_at_the_step = step_data.first.second
        val considered_vertexes = step_data.second
        for (edge_list in graph_visual.get_edges()) {
            for (edge in edge_list) {
                edge.get_line().stroke = Color.DARKGRAY
                if (step != -1) {
                    if (Pair(edge.get_positions().first, edge.get_positions().second) in
                        result_edges.subList(0, step + 1) || Pair(edge.get_positions().second,
                            edge.get_positions().first) in result_edges.subList(0, step + 1)) {
                        edge.get_line().stroke = Color.RED
                    } else if (Pair(edge.get_positions().first, edge.get_positions().second) in
                        edges_considered_at_the_step[step] || Pair(edge.get_positions().second,
                            edge.get_positions().first) in edges_considered_at_the_step[step]) {
                        edge.get_line().stroke = Color.BLUE
                    }
                }
            }
        }
        for (vertex in graph_visual.get_vertexes())
            vertex.get_circle().stroke = Color.BLACK
        if (step == -1) {
            new_vertex.isDisable = false
            new_edge.isDisable = false
            delete.isDisable = false
            graph_visual.get_vertexes()[graph_visual.graph.start_vertex].get_circle().stroke = Color.RED
        }
        for (i in 0 until step + 1)
            graph_visual.get_vertexes()[considered_vertexes[i]].get_circle().stroke = Color.RED
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
                "добавлена вершина \"${graph_visual.get_vertexes()[considered_vertexes[step]].get_name().text}\". Ребра, " +
                "рассматриваемые на данном шаге, имеют веса $weights"
        else if (step == 0) step_information.text = "Вершина  \"" +
                "${graph_visual.get_vertexes()[considered_vertexes[0]].get_name().text}\" была выбрана в качестве начальной. " +
                "Ребра, рассматриваемые на данном шаге, имеют веса $weights"
        else if (step == graph_visual.graph.data.size - 1) step_information.text = "На шаге №$step была добавлена " +
                "вершина \"${graph_visual.get_vertexes()[considered_vertexes.last()].get_name().text}\". Построение минимального " +
                "остовного дерева окончено"
        else step_information.text = "Чтобы выбрать выбрать новую начальную вершину нажмите на нее дважды." +
                " Редактирование графа доступно только на данном шаге."
    }

    fun draw_graph(stage: Stage, graph: Graph, buttons1: HBox, buttons2: HBox) {
        graph_visual = GraphVizualisation(stage.height, graph)
        step_information = Label("Чтобы выбрать выбрать новую начальную вершину нажмите на нее дважды." +
                    " Редактирование графа доступно только на данном шаге.")
        step_information.layoutX = 30.0
        step_information.layoutY = stage.height - 100.0
        full_group = Group(graph_visual.group(), buttons1, buttons2, step_information)
        val scene = Scene(full_group, 1000.0, 800.0)
        stage.setScene(scene)
        graph_editor.action(stage, graph_visual)
    }

}