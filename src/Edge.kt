package org.jetbrains.kotlin.Math

import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.text.Font
/*
* Класс, представляющий ребро графа.
*
* @property Vertex1 Первая вершина ребра [VertexVizualisation].
* @property Vertex2 Вторая вершина ребра [VertexVizualisation].
* @property weight Вес ребра.
* @property line Линия, представляющая ребро.
* @property edgegroup Группа, содержащая линию и метку ребра.
* @property line_label Метка, отображающая вес ребра.
* @property position_1 Позиция первой вершины в графе.
* @property position_2 Позиция второй вершины в графе.
*/

class Edge(Vertex1 : VertexVizualisation, Vertex2: VertexVizualisation,private val weight : Int) : Line(){
    private var line : Line
    private var edgegroup : Group
    private val line_label : Label
    private var position_1 : Int = Vertex1.get_number() - 1
    private var position_2 : Int = Vertex2.get_number() - 1
    /**
     * Инициализация ребра.
     *
     * Создает линию, метку ребра и привязывает их к вершинам графа.
     */
    init{
        val lineWidth = 4.0
        line = Line()
        line.stroke = Color.DARKGRAY
        line.strokeWidth = lineWidth
        line_label = Label("$weight")
        line_label.font = Font.font("Times New Roman",18.0)
        // Привязываем начало линии к окружности circle1
        line.startXProperty().bind(Vertex1.get_circle().centerXProperty())
        line.startYProperty().bind(Vertex1.get_circle().centerYProperty())
        line.endXProperty().bind(Vertex2.get_circle().centerXProperty())
        line.endYProperty().bind(Vertex2.get_circle().centerYProperty())
        line_label.textFill = Color.BLACK
        line_label.layoutX = (line.startX + line.endX) / 2.0
        line_label.layoutY =(line.startY + line.endY) / 2.0
        // Привязываем конец линии к окружности circle2
        edgegroup = Group(line,line_label)
    }
    /**
     * Возвращает группу, содержащую линию и метку ребра.
     */
    fun group() : Group { return edgegroup }

    /**
     * Возвращает вес ребра.
     */
    fun get_weight() : Int { return weight}

    /**
     * Возвращает линию, представляющую ребро.
     */
    fun get_line() : Line { return line}

    /**
     * Возвращает метку ребра.
     */
    fun get_label() : Label { return line_label}

    /**
     * Уменьшает позиции вершин ребра на указанное значение индекса.
     *
     * @param index Индекс вершины (1 или 2).
     */
    fun decrease_positions(index : Int) {
        if (index == 1) position_1--
        else position_2--
    }
    /**
     * Возвращает позиции вершин ребра.
     *
     * @return Пара значений: первая вершина, вторая вершина.
     */
    fun get_positions() : Pair<Int, Int> { return Pair(position_1,position_2)}
}