package org.jetbrains.kotlin.Math
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.stage.Stage
import java.io.File

class Vizualisation : Application(){
    //var vizual_graph = GraphVizualisation()
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
        val previous_step = Button("next element")
        previous_step.setOnAction { println("previous step") }
        val final = Button("move to result")
        final.setOnAction { println("final") }
        val first_step = Button("move to start")
        first_step.setOnAction { println("back") }
        val variant = scan.nextInt()
        val movements = HBox(next_step,previous_step)
        val big_movements = HBox(first_step,final)
        val operations = HBox(new_edge,new_vertex,delete)
        operations.spacing = 10.0
        movements.spacing = 10.0
        big_movements.spacing = 10.0
        val operation_group = Group()
        //vbox.children.addAll()
        when (variant){
            1 -> {
                graph.read_from_file(file)
                draw_graph(stage, graph)
            }
            2 -> {
                graph.read_from_console()
                draw_graph(stage, graph)
            }
            3 ->{
                create_graph(stage, graph)
            }
        }
        stage.show()

    }
    fun create_graph(stage: Stage,graph: Graph){
        TODO()
    }
    fun create_vertex(stage: Stage,graph: Graph){
        TODO()
    }
    fun create_edge(stage: Stage,graph: Graph){
        TODO()
    }
    fun delete_element(stage: Stage, graph: Graph){
        TODO()
    }
    fun action(stage: Stage, graph: Graph,graph_visual : GraphVizualisation){

    }
    fun draw_graph(stage: Stage,graph: Graph, buttons : Group){
        println(graph)
        var graph_visual = GraphVizualisation(stage.height,graph)
        val full_group = Group(graph_visual.full_graph, buttons)
        var scene = Scene(buttons,800.0,600.0)
        stage.setScene(scene)
        action(stage, graph,graph_visual)
    }

}