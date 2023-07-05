package org.jetbrains.kotlin.Math
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import javafx.scene.text.Font
import javafx.scene.text.Text

class Edge(Vertex1 : VertexVizualisation, Vertex2: VertexVizualisation,val weight : Int) : Shape(){
    var line : Line
    var edgegroup : Group
    init{
        val lineWidth = 2.0
        line = Line()
        line.stroke = Color.BLACK
        line.strokeWidth = lineWidth
        val line_label = Label("$weight")
        line_label.font = Font.font("Times New Roman",20.0)
        // Привязываем начало линии к окружности circle1
        line.startXProperty().bind(Vertex1.circle.centerXProperty())
        line.startYProperty().bind(Vertex1.circle.centerYProperty())
        line.endXProperty().bind(Vertex2.circle.centerXProperty())
        line.endYProperty().bind(Vertex2.circle.centerYProperty())
        line_label.layoutX = (line.startX.toDouble() + line.endX.toDouble()) / 2.0 - line_label.width
        line_label.layoutY =(line.startY.toDouble() + line.endY.toDouble()) / 2.0 - line_label.height
        // Привязываем конец линии к окружности circle2

        edgegroup = Group(line,line_label)
    }
}