package org.jetbrains.kotlin.Math

import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextInputDialog
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.stage.Stage

/**
 * Класс GraphEditor предоставляет функциональность для редактирования графа.
 *
 * @property create_vertex Метод для создания новой вершины в графе.
 * @property choose_new_name Метод для выбора имени новой вершины.
 * @property getClickedLine Метод для получения ребра, на которое было произведено нажатие.
 * @property getClickedCircle Метод для получения вершины, на которую было произведено нажатие.
 * @property create_edge Метод для создания нового ребра в графе.
 */
class GraphEditor() {

    /**
     * Метод create_vertex создает новую вершину в графе.
     *
     * @param stage сцена, на которой отображается граф
     * @param graph_visual визуализация графа
     * @param step_information информация о текущем шаге
     * @param button1 кнопка 1
     * @param button2 кнопка 2
     * @param button3 кнопка 3
     * @param button4 кнопка 4
     */
    fun create_vertex(stage: Stage, graph_visual: GraphVizualisation, step_information: Label,button1 : Button, button2 : Button, button3 : Button, button4 : Button) {
        if (graph_visual.get_step() == -1) {
            able_disable_buttons(button1,button2,button3,button4,true)
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
                    graph_visual.graph.name_vertex.add(new_name)
                    able_disable_buttons(button1,button2,button3,button4,false)
                    step_information.text = "Чтобы выбрать новую начальную вершину нажмите на нее дважды." +
                            " Редактирование графа доступно только на данном шаге."
                    action(stage, graph_visual)
                }
            }
        }
    }

    /**
     * Метод choose_new_name позволяет выбрать имя для новой вершины.
     *
     * @param graph_visual визуализация графа
     *
     * @return имя для новой вершины
     */
    fun choose_new_name(graph_visual: GraphVizualisation): String {
        val dialog = TextInputDialog()
        dialog.title = "Ввод имени для новой вершины."
        dialog.headerText = "Оставьте поле пустным или закройте окно, если хотите, чтобы имя было задано автоматически."
        dialog.contentText = "Пожалуйста, введите имя для новой вершины:"
        val result = dialog.showAndWait()
        var name: String
        if (result.isPresent) {
            name = result.get()
            if (name == "") name = graph_visual.graph.default_name(graph_visual.graph.name_vertex.size + 1)
        } else name = graph_visual.graph.default_name(graph_visual.graph.name_vertex.size + 1)
        return name
    }

    /**
     * Метод getClickedLine возвращает ребро, на которое было произведено нажатие.
     *
     * @param x координата x нажатия
     * @param y координата y нажатия
     * @param graph_visual визуализация графа
     *
     * @return ребро, на которое было произведено нажатие или null, если ребра не было
     */
    fun getClickedLine(x: Double, y: Double, graph_visual: GraphVizualisation): Edge?{
        for(edgeline in graph_visual.get_edges()){
            for (edge in edgeline){
                if (edge.get_line().contains(x,y)) return edge
            }
        }
        return null
    }

    /**
     * Метод getClickedCircle возвращает вершину, на которую было произведено нажатие.
     *
     * @param x координата x нажатия
     * @param y координата y нажатия
     * @param graph_visual визуализация графа
     *
     * @return вершина, на которую было произведено нажатие или null, если вершины не было
     */
    fun getClickedCircle(x: Double, y: Double, graph_visual: GraphVizualisation): VertexVizualisation? {
        for (circle in graph_visual.get_vertexes()) {
            if (circle.get_circle().contains(x, y)) return circle
        }
        return null
    }

    /**
     * Метод create_edge создает новое ребро в графе.
     *
     * @param stage сцена, на которой отображается граф
     * @param graph_visual визуализация графа
     * @param step_information информация о текущем шаге
     * @param button1 кнопка 1
     * @param button2 кнопка 2
     * @param button3 кнопка 3
     * @param button4 кнопка 4
     */
    fun create_edge(stage: Stage, graph_visual: GraphVizualisation, step_information: Label,button1 : Button, button2 : Button, button3 : Button, button4 : Button) {
        if (graph_visual.get_step() == -1) {
            able_disable_buttons(button1,button2,button3,button4,true)
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
                            able_disable_buttons(button1,button2,button3,button4,false)
                            action(stage, graph_visual)
                        } else startCircle = clickedCircle
                    }
                }
            }
        }
    }

    /**
     * Метод choose_weight() открывает диалоговое окно для ввода веса нового ребра и возвращает введенное значение.
     * Если поле ввода оставлено пустым или окно закрыто, вес ребра будет равен "0".
     *
     * @return введенное пользователем значение веса ребра
     */
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

    /**
     * Метод createLine создает новое ребро между двумя заданными вершинами на графической сцене.
     * Вес ребра запрашивается с помощью метода choose_weight().
     *
     * @param startCircle начальная вершина ребра
     * @param endCircle конечная вершина ребра
     * @param graph_visual визуализация графа
     * @param step_information информация о текущем шаге
     */
    fun createLine(startCircle: VertexVizualisation, endCircle: VertexVizualisation, graph_visual: GraphVizualisation, step_information: Label) {
        val line = Line()
        line.startX = startCircle.get_circle().centerX
        line.startY = startCircle.get_circle().centerY
        line.endX = endCircle.get_circle().centerX
        line.endY = endCircle.get_circle().centerY
        line.stroke = Color.BLACK
        line.strokeWidth = 2.0
        val weight = choose_weight()
        if (startCircle.get_number() > endCircle.get_number()) {
            val new_edge = Edge(endCircle, startCircle, weight)
            graph_visual.add_edge(new_edge)
        } else {
            val new_edge = Edge(startCircle, endCircle, weight)
            graph_visual.add_edge(new_edge)
        }
        step_information.text = "Чтобы выбрать новую начальную вершину нажмите на нее дважды." +
                " Редактирование графа доступно только на данном шаге."
    }

    /**
     * Метод able_disable_buttons позволяет включать или отключать кнопки на графической сцене.
     *
     * @param button1 первая кнопка
     * @param button2 вторая кнопка
     * @param button3 третья кнопка
     * @param button4 четвертая кнопка
     * @param action значение true или false, определяющее, включить или отключить кнопки
     */
    fun able_disable_buttons(button1 : Button, button2 : Button, button3 : Button, button4 : Button, action : Boolean){
        button1.isDisable = action
        button2.isDisable = action
        button3.isDisable = action
        button4.isDisable = action
    }

    /**
     * Метод delete_element позволяет пользователю удалить выбранный элемент (вершину или ребро) на графической сцене.
     *
     * @param stage сцена, на которой отображается граф
     * @param graph_visual визуализация графа
     * @param step_information информация о текущем шаге
     * @param button1 первая кнопка
     * @param button2 вторая кнопка
     * @param button3 третья кнопка
     * @param button4 четвертая кнопка
     */
    fun delete_element(stage: Stage,graph_visual: GraphVizualisation, step_information: Label,button1 : Button, button2 : Button, button3 : Button, button4 : Button) {
        if (graph_visual.get_step() == -1){
            able_disable_buttons(button1,button2,button3,button4,true)
            step_information.text = "Чтобы удалить элемент, нажмите на него."
            stage.scene.setOnMouseClicked { event ->
                val clicked_circle : Circle?
                val clicked_line : Line?

                clicked_circle = getClickedCircle(event.x,event.y,graph_visual)
                if (clicked_circle != null) {
                    graph_visual.delete_vertex(clicked_circle)

                }
                clicked_line = getClickedLine(event.x,event.y,graph_visual)
                if (clicked_line != null){
                    graph_visual.delete_edge(clicked_line)
                }
                able_disable_buttons(button1,button2,button3,button4,false)
                stage.scene.setOnMouseClicked(null)
                action(stage, graph_visual)
                step_information.text = "Чтобы выбрать новую начальную вершину нажмите на нее дважды." +
                        " Редактирование графа доступно только на данном шаге."
            }
        }

    }

    /**
     * Метод isInsideCircle проверяет, находятся ли указанные координаты внутри заданного круга.
     *
     * @param x координата по оси X
     * @param y координата по оси Y
     * @param circle круг для проверки
     * @return true, если координаты находятся внутри круга, иначе false
     */
    fun isInsideCircle(x: Double, y: Double, circle: Circle): Boolean {
        val distance = Math.sqrt(Math.pow(x - circle.centerX, 2.0) + Math.pow(y - circle.centerY, 2.0))
        return distance <= circle.radius
    }

    /**
     * Метод action позволяет пользователю выполнять действия с элементами графа.
     *
     * @param stage сцена, на которой отображается граф
     * @param graph_visual визуализация графа
     */
    fun action(stage: Stage, graph_visual: GraphVizualisation) {
        stage.scene.setOnMouseClicked { event ->
            if (event.button == MouseButton.PRIMARY && event.clickCount == 2 && graph_visual.get_step() == -1) {
                for (i in 0 until graph_visual.get_vertexes().size) {
                    graph_visual.get_vertexes()[i].get_circle().stroke = Color.BLACK
                    if (isInsideCircle(event.sceneX, event.sceneY, graph_visual.get_vertexes()[i].get_circle())) {
                        graph_visual.graph.set_start_vertex(graph_visual.get_vertexes()[i].get_number() - 1)
                        graph_visual.get_vertexes()[graph_visual.graph.start_vertex].get_circle().stroke = Color.RED
                    }
                }
            }
        }
        val deltaX = DoubleArray(graph_visual.get_vertexes().size)
        val deltaY = DoubleArray(graph_visual.get_vertexes().size)
        stage.scene.setOnMousePressed { event ->
            if (event.button == MouseButton.PRIMARY) {
                for (vertex in graph_visual.get_vertexes()) {
                    if (isInsideCircle(event.sceneX, event.sceneY, vertex.get_circle())) {
                        deltaX[vertex.get_number() - 1] = event.sceneX - vertex.get_circle().centerX
                        deltaY[vertex.get_number() - 1] = event.sceneY - vertex.get_circle().centerY
                    }
                }
            }
        }
        stage.scene.setOnMouseDragged { event ->
            if (event.button == MouseButton.PRIMARY) {
                for (vertex in graph_visual.get_vertexes()) {
                    if (isInsideCircle(event.sceneX, event.sceneY, vertex.get_circle())) {
                        vertex.get_circle().centerX = event.sceneX - deltaX[vertex.get_number() - 1]
                        vertex.get_circle().centerY = event.sceneY - deltaY[vertex.get_number() - 1]
                        // Обновляем положение метки в соответствии с новым положением круга
                        vertex.get_name().x = vertex.get_circle().centerX - vertex.get_name().layoutBounds.width / 2.0
                        vertex.get_name().y = vertex.get_circle().centerY + vertex.get_name().layoutBounds.height / 4.0
                        for (edge_list in graph_visual.get_edges()) {
                            for (edge in edge_list) {
                                if (vertex.get_number() - 1 == edge.get_positions().first || vertex.get_number() - 1 == edge.get_positions().second) {
                                    edge.get_label().layoutX = (edge.get_line().startX + edge.get_line().endX) / 2.0
                                    edge.get_label().layoutY = (edge.get_line().startY + edge.get_line().endY) / 2.0
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}