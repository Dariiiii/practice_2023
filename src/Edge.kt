package org.jetbrains.kotlin.Math
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.text.Font


class Edge(Vertex1 : VertexVizualisation, Vertex2: VertexVizualisation,val weight : Int) : Line(){
    var line : Line
    var edgegroup : Group
    val line_label : Label
    var position_1 : Int = Vertex1.number - 1
    var position_2 : Int = Vertex2.number - 1
    init{
        val lineWidth = 4.0
        line = Line()
        line.stroke = Color.DARKGRAY
        line.strokeWidth = lineWidth
        line_label = Label("$weight")
        line_label.font = Font.font("Times New Roman",18.0)
        // Привязываем начало линии к окружности circle1
        line.startXProperty().bind(Vertex1.circle.centerXProperty())
        line.startYProperty().bind(Vertex1.circle.centerYProperty())
        line.endXProperty().bind(Vertex2.circle.centerXProperty())
        line.endYProperty().bind(Vertex2.circle.centerYProperty())
        line_label.textFill = Color.BLACK
        line_label.layoutX = (line.startX + line.endX) / 2.0
        line_label.layoutY =(line.startY + line.endY) / 2.0
        // Привязываем конец линии к окружности circle2
        edgegroup = Group(line,line_label)
    }
}