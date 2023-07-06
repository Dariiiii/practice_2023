package org.jetbrains.kotlin.Math
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.stage.Stage
import java.io.File

class Vizualisation : Application(){
    lateinit var graph_visual: GraphVizualisation
    override fun start(stage: Stage) {
        var graph = Graph()
        val file = File("Graph1.txt")
        println("Как вы хотите ввести граф : 1 - из файла(матрица смежности), 2 - из консоли(матрица смежности), в режиме реального времени самому нарисовать граф")
        stage.title = "Визуализация алгоритма Прима"
        stage.width = 800.0
        stage.height = 600.0
        val new_vertex = Button("add vertex")
        new_vertex.setOnAction { create_vertex(stage, graph) }
        val new_edge = Button("add edge")
        new_edge.setOnAction { create_edge(stage, graph) }
        val delete = Button("delete element")
        delete.setOnAction { delete_element(stage, graph) }
        val next_step = Button("next step")
        next_step.setOnAction { println("next step") }
        val previous_step = Button("previous_step")
        previous_step.setOnAction { println("previous step") }
        val final = Button("move to result")
        final.setOnAction { algorithm_vizualisation(stage,graph, graph_visual) }
        val first_step = Button("move to start")
        first_step.setOnAction { println("back") }
        val variant = scan.nextInt()
        val movements = HBox(previous_step,next_step)
        val big_movements = HBox(first_step,final)
        val operations = HBox(new_edge,new_vertex,delete)
        operations.spacing = 10.0
        movements.spacing = 10.0
        big_movements.spacing = 10.0
        val all_movements = VBox(movements,big_movements)
        val all_buttons = BorderPane()
        all_buttons.top = operations
        all_buttons.bottom = all_movements
        when (variant){
            1 -> {
                graph.read_from_file(file)
                draw_graph(stage, graph,all_buttons)
            }
            2 -> {
                graph.read_from_console()
                draw_graph(stage, graph,all_buttons)
            }
            3 ->{
                create_graph(stage, graph)
            }
        }
        stage.show()

    }

    fun create_graph(stage: Stage,graph: Graph){
        println("Не работает пока(")
    }

    fun create_vertex(stage: Stage,graph: Graph){
        println("вершина создается")
    }

    fun create_edge(stage: Stage,graph: Graph){
        println("ребро создается")
    }

    fun delete_element(stage: Stage, graph: Graph){
        println("штучка удаляется создается")
    }
    fun algorithm_vizualisation(stage: Stage, graph: Graph,graph_visual : GraphVizualisation){
        val result = graph.PrimAlgorithm()
        for (edge_list in graph_visual.edges) {
            for (edge in edge_list) {
                if (Pair(edge.position_1,edge.position_2) in result || Pair(edge.position_2,edge.position_1) in result)      {
                    edge.line.stroke = Color.RED
                }
            }
        }
    }

    fun isInsideCircle(x: Double, y: Double, circle: Circle): Boolean {
        val distance = Math.sqrt(Math.pow(x - circle.centerX, 2.0) + Math.pow(y - circle.centerY, 2.0))
        return distance <= circle.radius
    }

    fun action(stage: Stage, graph: Graph,graph_visual : GraphVizualisation) {
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
                        vertex.name.x = vertex.circle.centerX
                        vertex.name.y = vertex.circle.centerY - vertex.name.layoutBounds.height / 2.0
                        for (edge_list in graph_visual.edges) {
                            for (edge in edge_list) {
                                if (vertex.number - 1 == edge.position_1 || vertex.number - 1 == edge.position_2){
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

    fun draw_graph(stage: Stage,graph: Graph, buttons : BorderPane){
        println(graph)
        graph_visual = GraphVizualisation(stage.height,graph)
        val full_group = Group(graph_visual.full_graph, buttons)
        var scene = Scene(full_group,800.0,600.0)
        stage.setScene(scene)
        action(stage, graph,graph_visual)
    }

}