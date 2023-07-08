package org.jetbrains.kotlin.Math

import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Shape
import javafx.scene.text.Font
import javafx.scene.text.Text
// класс для визуализации вершины, состоящей из круга и имени(символ латинского алфавита)pr
class VertexVizualisation(scene_size : Double, x : Double, y : Double, name_vertex: String, val number: Int ) : Shape(){
    var name: Text
    var circle: Circle
    var data : Group
    init {
        circle = Circle(x,y, scene_size / 25.0)
        circle.fill = Color.PINK
        val text = Text(name_vertex)
        text.font = Font.font("Arial",15.0)
        this.name = text
        circle.strokeWidth = 3.0
        circle.stroke = Color.BLACK
        text.fill = Color.BLACK
        text.x = circle.centerX - text.layoutBounds.width / 2.0
        text.y = circle.centerY + text.layoutBounds.height / 4.0
        data = Group(circle,text)

    }

}