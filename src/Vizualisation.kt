package org.jetbrains.kotlin.Math

import javafx.application.Application//
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.stage.Stage
import java.io.File

class Vizualisation : Application() {
    lateinit var graph_visual: GraphVizualisation
    lateinit var full_group: Group
    lateinit var step_information : Label
    // начальный метод, в котором создаются кнопки, окно приложения и выбирается формат ввода данных
    override fun start(stage: Stage) {//окно

        val graph = Graph()
        val file = File("Graph1.txt")//создание файла
        stage.title = "Визуализация алгоритма Прима"//заголовок окна
        stage.width = 1000.0//высота окна
        stage.height = 800.0//ширина
        //Button - создание кнопки
        // "setOnAction" - это метод, используемый для назначения действия (Action) на определенное событие
        val new_vertex = Button("add vertex")
        new_vertex.setOnAction { create_vertex(stage, graph) }
        val new_edge = Button("add edge")
        new_edge.setOnAction { create_edge(stage, graph) }
        val delete = Button("delete element")
        delete.setOnAction { delete_element(stage, graph) }
        val next_step = Button("next step")
        next_step.setOnAction { step_by_step_algorythm(stage,graph_visual.next_step()) }
        val previous_step = Button("previous step")
        previous_step.setOnAction { step_by_step_algorythm(stage,graph_visual.previous_step()) }
        val final = Button("move to result")
        final.setOnAction { step_by_step_algorythm(stage,graph_visual.set_get_step(graph_visual.graph.data.size - 1)) }
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
        if(operations.width == 0.0) {
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

    fun step_by_step_algorythm(stage: Stage,step: Int) {
        val step_data = graph_visual.graph.PrimAlgorithm()
        val result_edges = step_data.first.first
        val edges_considered_at_the_step = step_data.first.second
        val considered_vertexes = step_data.second
        for (edge_list in graph_visual.edges) {
            for (edge in edge_list) {
                edge.line.stroke = Color.DARKGRAY
                if(step != - 1) {
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

        for (i in 0 until step + 1){
            graph_visual.vertexes[i].circle.stroke = Color.RED
        }
        update_step_information(stage,step,considered_vertexes,edges_considered_at_the_step)
    }

    fun update_step_information(stage: Stage,step: Int,considered_vertexes : MutableList<Int>,added_edges : MutableList<MutableList<Pair<Int, Int>>>){
        var weights = ""
//        println(graph_visual.graph.data[added_edges[0][step].first][added_edges[0][step].second].toString())
        if (step >=0) {
            for (edge in added_edges[step])
                weights += " ${graph_visual.graph.data[edge.first][edge.second]}"
        }
        if (step<graph_visual.graph.data.size - 1 && step > 0){
            step_information.text =
                "На шаге $step была добавлена вершина ${graph_visual.vertexes[considered_vertexes[step]].name.text}. Ребра, рассматриваемые на данном шаге, имеют веса $weights"
        }
        else if( step ==0)
            step_information.text = "Вершина  ${graph_visual.vertexes[considered_vertexes[0]].name.text} была выбрана в качестве начальной. Ребра, рассматриваемые на данном шаге, имеют веса $weights"
        else if (step == graph_visual.graph.data.size - 1) step_information.text ="На шаге $step была добавлена вершина ${graph_visual.vertexes[considered_vertexes.last()].name.text}. Построение минимального остовного дерева окончено"
        else step_information.text = "Здесь будет отображена информация"
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
        step_information = Label("Здесь будет отображена информация")
        step_information.layoutX = 30.0
        step_information.layoutY = stage.height - 100.0
        full_group = Group(graph_visual.full_graph, buttons1, buttons2,step_information)
        val scene = Scene(full_group, 800.0, 600.0)
        stage.setScene(scene)
        action(stage)
    }

}