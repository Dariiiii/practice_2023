package org.jetbrains.kotlin.Math
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import javafx.scene.text.Font
import javafx.scene.text.Text

class VertexVizualisation(scene_size : Double, x : Double, y : Double, val number: Int) : Shape(){
    var name: Text
    var circle: Circle
    var data : Group
    init {
        circle = Circle(x,y, scene_size / 20.0)
        circle.fill = Color.PINK
        val base = 26
        var quotient = number
        val sb = StringBuilder()
        while (quotient > 0) {
            quotient--
            val remainder = quotient % base
            sb.append(('A'.code + remainder).toChar())
            quotient /= base
        }
        println(number)
        val text = Text("${sb.reverse()}")
        println(text.text)
        text.font = Font.font("Arial",20.0)
        this.name = text
        circle.stroke = Color.BLACK
        text.fill = Color.BLACK
        text.x = circle.centerX - text.layoutBounds.width / 2
        text.y = circle.centerY
        data = Group(circle,text)

    }

}