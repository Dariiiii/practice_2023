package org.jetbrains.kotlin.Math

import javafx.application.Application//
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.stage.Stage//
import java.io.File

class Vizualisation : Application() {
    lateinit var graph_visual: GraphVizualisation
    lateinit var full_group: Group

    // начальный метод, в котором создаются кнопки, окно приложения и выбирается формат ввода данных
    override fun start(stage: Stage) {//окно
        val graph = Graph()
        val file = File("Graph1.txt")//создание файла
        stage.title = "Визуализация алгоритма Прима"//заголовок окна
        stage.width = 800.0//высота окна
        stage.height = 600.0//ширина

        //Button - создание кнопки
        // "setOnAction" - это метод, используемый для назначения действия (Action) на определенное событие
        val new_vertex = Button("add vertex")
        new_vertex.setOnAction { create_vertex(stage, graph) }
        val new_edge = Button("add edge")
        new_edge.setOnAction { create_edge(stage, graph) }
        val delete = Button("delete element")
        delete.setOnAction { delete_element(stage, graph) }
        val next_step = Button("next step")
        next_step.setOnAction { step_by_step_algorythm(graph_visual.next_step()) }
        val previous_step = Button("previous step")
        previous_step.setOnAction { step_by_step_algorythm(graph_visual.previous_step()) }
        val final = Button("move to result")
        final.setOnAction { algorithm_vizualisation() }
        val first_step = Button("move to start")

        //HBox - размещение элементов в горизонтальной линии
        //Spacing в контексте HBox относится к расстоянию между элементами внутри контейнера.
        //Он задает величину отступа между каждой парой соседних элементов.
        val movements = HBox(first_step, previous_step, next_step, final)
        val operations = HBox(new_edge, new_vertex, delete)
        operations.spacing = 10.0
        movements.spacing = 10.0
        first_step.setOnAction { draw_graph(stage, graph, operations, movements) }



        println("Как вы хотите ввести граф : 1 - из файла(матрица смежности), 2 - из консоли(матрица смежности), 3 - в режиме реального времени самому нарисовать граф")
        val variant = scan.nextInt()
        when (variant) {
            1 -> {
                graph.read_from_file(file)
                //окно, граф, функции, перемещение
                draw_graph(stage, graph, operations, movements)
            }

            2 -> {
                graph.read_from_console()
                draw_graph(stage, graph, operations, movements)
            }

            3 -> {
                create_graph(stage, graph)
            }
        }
        //установка местоположения кнопок
        Platform.runLater {
            val width1 = operations.width
            val height1 = operations.height
            val width2 = movements.width
            val height2 = movements.height
            operations.layoutX = stage.width / 2.0 - width1 / 2.0
            operations.layoutY = 5.0
            movements.layoutX = stage.width / 2.0 - width2 / 2.0
            movements.layoutY = stage.height - 3.0 * height2
        }

        stage.show()
    }

    fun create_graph(stage: Stage, graph: Graph) {
        println("Не работает пока(")
    }

    fun create_vertex(stage: Stage, graph: Graph) {
        println("вершина создается")
    }

    fun create_edge(stage: Stage, graph: Graph) {
        println("ребро создается")
    }

    fun delete_element(stage: Stage, graph: Graph) {
        println("штучка удаляется")
    }

    fun step_by_step_algorythm(step: Int) {
        val result_edges = graph_visual.graph.PrimAlgorithm().first
        val edges_considered_at_the_step = graph_visual.graph.PrimAlgorithm().second
//        println(step)
        for (edge_list in graph_visual.edges) {
            for (edge in edge_list) {
                edge.line.stroke = Color.DARKGRAY
                if (Pair(edge.position_1, edge.position_2) in result_edges.subList(0, step+1) || Pair(
                        edge.position_2, edge.position_1
                    ) in result_edges.subList(0, step+1)
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


    // метод для отображения результатов алгоритма
    fun algorithm_vizualisation() {
        val result_edges = graph_visual.graph.PrimAlgorithm().first
        for (edge_list in graph_visual.edges) {
            for (edge in edge_list) {
                if (Pair(edge.position_1, edge.position_2) in result_edges || Pair(
                        edge.position_2,
                        edge.position_1
                    ) in result_edges
                ) {
                    edge.line.stroke = Color.RED
                }
            }
        }
    }

    // метод для проверки местоположения курсора внутри круга
    fun isInsideCircle(x: Double, y: Double, circle: Circle): Boolean {
        val distance = Math.sqrt(Math.pow(x - circle.centerX, 2.0) + Math.pow(y - circle.centerY, 2.0))
        return distance <= circle.radius
    }

    // метод для обработки каких-либо действий пользователя, не относящихся к нажатию кнопок
    fun action(stage: Stage) {

        val deltaX = DoubleArray(graph_visual.vertexes.size)
        val deltaY = DoubleArray(graph_visual.vertexes.size)
        stage.scene.setOnMousePressed { event ->
            if (event.button == MouseButton.PRIMARY) {
                // Если нажата левая кнопка мыши
                for (vertex in graph_visual.vertexes) {
                    if (isInsideCircle(event.sceneX, event.sceneY, vertex.circle)) {
                        deltaX[vertex.number - 1] = event.sceneX - vertex.circle.centerX
                        deltaY[vertex.number - 1] = event.sceneY - vertex.circle.centerY
                    }
                }
            }
        }

        stage.scene.setOnMouseDragged { event ->
            if (event.button == MouseButton.PRIMARY) {
                // Если нажата левая кнопка мыши
                for (vertex in graph_visual.vertexes) {
                    if (isInsideCircle(event.sceneX, event.sceneY, vertex.circle)) {
                        vertex.circle.centerX = event.sceneX - deltaX[vertex.number - 1]
                        vertex.circle.centerY = event.sceneY - deltaY[vertex.number - 1]

                        // Обновляем положение метки в соответствии с новым положением круга
                        vertex.name.x = vertex.circle.centerX - vertex.name.layoutBounds.width / 2.0
                        vertex.name.y = vertex.circle.centerY + vertex.name.layoutBounds.height / 4.0
                        for (edge_list in graph_visual.edges) {
                            for (edge in edge_list) {
                                if (vertex.number - 1 == edge.position_1 || vertex.number - 1 == edge.position_2) {
                                    edge.line_label.layoutX =
                                        (edge.line.startX.toDouble() + edge.line.endX.toDouble()) / 2.0
                                    edge.line_label.layoutY =
                                        (edge.line.startY.toDouble() + edge.line.endY.toDouble()) / 2.0
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // создание графа из уже заданной матрицы из файла или из консоли
    fun draw_graph(stage: Stage, graph: Graph, buttons1: HBox, buttons2: HBox) {
        graph_visual = GraphVizualisation(stage.height, graph)
        full_group = Group(graph_visual.full_graph, buttons1, buttons2)
        val scene = Scene(full_group, 800.0, 600.0)
        stage.setScene(scene)

        action(stage)
    }

}