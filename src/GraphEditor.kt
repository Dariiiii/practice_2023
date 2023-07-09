package org.jetbrains.kotlin.Math

import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.TextInputDialog
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.stage.Stage

class GraphEditor() {

    fun create_vertex(stage: Stage, graph_visual: GraphVizualisation, step_information: Label) {
        if (graph_visual.step == -1) {
            step_information.text = "Выберете место для вставки вершины. "
            stage.scene.setOnMouseClicked { event ->
                if (event.button == MouseButton.PRIMARY && getClickedCircle(event.x, event.y, graph_visual) == null) {
                    val new_name = choose_new_name(graph_visual)
                    val new_vertex = VertexVizualisation(
                        stage.height, event.x, event.y, new_name,
                        graph_visual.graph.data.size + 1
                    )
                    graph_visual.add_vertex(new_vertex)
                    stage.scene.setOnMouseClicked(null)
                    step_information.text = "Чтобы выбрать выбрать новую начальную вершину нажмите на нее дважды." +
                            " Редактирование графа доступно только на данном шаге."
                    action(stage, graph_visual)
                }
            }
        }
    }

    fun choose_new_name(graph_visual: GraphVizualisation): String {
        val dialog = TextInputDialog()
        dialog.title = "Ввод имени для новой вершины."
        dialog.headerText = "Оставьте поле пустным или закройте окно, если хотите, чтобы имя было задано автоматически."
        dialog.contentText = "Пожалуйста, введите имя для новой вершины:"
        val result = dialog.showAndWait()
        var name: String
        if (result.isPresent) {
            name = result.get()
            if (name == "") name = graph_visual.graph.default_name(graph_visual.graph.data.size)
        } else name = graph_visual.graph.default_name(graph_visual.graph.data.size)
        return name
    }

    fun getClickedLine(x: Double, y: Double, graph_visual: GraphVizualisation): Edge?{
        for(edgeline in graph_visual.edges){
            for (edge in edgeline){
                if (edge.line.contains(x,y)) return edge
            }
        }
        return null
    }

    fun getClickedCircle(x: Double, y: Double, graph_visual: GraphVizualisation): VertexVizualisation? {
        for (circle in graph_visual.vertexes) {
            if (circle.circle.contains(x, y)) return circle
        }
        return null
    }

    fun create_edge(stage: Stage, graph_visual: GraphVizualisation, step_information: Label) {
        if (graph_visual.step == -1) {
            step_information.text = "Чтобы создать ребро, необходимо выбрать две вершины и последоавтельно нажать на них."
            var startCircle: VertexVizualisation? = null
            stage.scene.setOnMouseClicked { event ->
                if (event.button == MouseButton.PRIMARY && graph_visual.graph.data.size >= 2) {
                    val clickedCircle = getClickedCircle(event.x, event.y, graph_visual)
                    if (clickedCircle != null) {
                        if (startCircle != null) {
                            createLine(startCircle!!, clickedCircle, graph_visual, step_information)
                            startCircle = null
                            stage.scene.setOnMouseClicked(null)
                            action(stage, graph_visual)
                        } else startCircle = clickedCircle
                    }
                }
            }
        }
    }

    fun choose_weight(): Int {
        val dialog = TextInputDialog()
        dialog.title = "Ввод веса для нового ребра."
        dialog.headerText = "Если вы оставите поле пустным или закроете окно, вес ребра будет равен \"0\"."
        dialog.contentText = "Пожалуйста, введите целое число:"
        var weight = 0
        val result = dialog.showAndWait()
        if (result.isPresent) {
            val input = result.get()
            try {
                weight = input.toInt()
            } catch (e: NumberFormatException) {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Ошибка!"
                alert.headerText = "Неверный формат числа."
                alert.contentText = "Пожалуйста, введите целое число."
                alert.showAndWait()
            }
        }
        return weight
    }

    fun createLine(startCircle: VertexVizualisation, endCircle: VertexVizualisation,
                   graph_visual: GraphVizualisation, step_information: Label) {
        val line = Line()
        line.startX = startCircle.circle.centerX
        line.startY = startCircle.circle.centerY
        line.endX = endCircle.circle.centerX
        line.endY = endCircle.circle.centerY
        line.stroke = Color.BLACK
        line.strokeWidth = 2.0
        val weight = choose_weight()
        if (startCircle.number > endCircle.number) {
            val new_edge = Edge(endCircle, startCircle, weight)
            graph_visual.add_edge(new_edge)
        } else {
            val new_edge = Edge(startCircle, endCircle, weight)
            graph_visual.add_edge(new_edge)
        }
        step_information.text = "Чтобы выбрать выбрать новую начальную вершину нажмите на нее дважды." +
                " Редактирование графа доступно только на данном шаге."
    }

    fun delete_element(stage: Stage,graph_visual: GraphVizualisation, step_information: Label) {
        if (graph_visual.step == -1){
            step_information.text = "Чтобы удалить элемент, нажмите на него."
            stage.scene.setOnMouseClicked { event ->
                var clicked_circle : Circle?
                var clicked_line : Line?
                clicked_circle = getClickedCircle(event.x,event.y,graph_visual)
                if (clicked_circle != null) {
                    graph_visual.delete_vertex(clicked_circle)
                }
                clicked_line = getClickedLine(event.x,event.y,graph_visual)
                if (clicked_line != null){
                    graph_visual.delete_edge(clicked_line)
                }
                stage.scene.setOnMouseClicked(null)
                action(stage, graph_visual)
                step_information.text = "Чтобы выбрать выбрать новую начальную вершину нажмите на нее дважды." +
                        " Редактирование графа доступно только на данном шаге."
            }
        }

    }

    fun isInsideCircle(x: Double, y: Double, circle: Circle): Boolean {
        val distance = Math.sqrt(Math.pow(x - circle.centerX, 2.0) + Math.pow(y - circle.centerY, 2.0))
        return distance <= circle.radius
    }

    fun action(stage: Stage, graph_visual: GraphVizualisation) {
        stage.scene.setOnMouseClicked { event ->
            if (event.button == MouseButton.PRIMARY && event.clickCount == 2 && graph_visual.step == -1) {
                for (i in 0 until graph_visual.vertexes.size) {
                    graph_visual.vertexes[i].circle.stroke = Color.BLACK
                    if (isInsideCircle(event.sceneX, event.sceneY, graph_visual.vertexes[i].circle)) {
                        graph_visual.graph.set_start_vertex(graph_visual.vertexes[i].number - 1)
                        graph_visual.vertexes[graph_visual.graph.start_vertex].circle.stroke = Color.RED
                    }
                }
            }
        }
        val deltaX = DoubleArray(graph_visual.vertexes.size)
        val deltaY = DoubleArray(graph_visual.vertexes.size)
        stage.scene.setOnMousePressed { event ->
            if (event.button == MouseButton.PRIMARY) {
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
                                    edge.line_label.layoutX = (edge.line.startX + edge.line.endX) / 2.0
                                    edge.line_label.layoutY = (edge.line.startY + edge.line.endY) / 2.0
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}