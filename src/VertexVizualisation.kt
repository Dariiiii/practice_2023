package org.jetbrains.kotlin.Math

import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import javafx.scene.text.Text
/**
 * Класс, представляющий визуализацию вершины графа.
 *
 * Этот класс используется для создания и визуализации вершины графа на графической сцене.
 * Визуализация вершины состоит из окружности и текстовой метки с названием вершины.
 * Класс позволяет получить номер вершины, уменьшить его на единицу, получить группу, содержащую окружность и текстовую метку,
 * а также получить окружность и метку вершины отдельно.
 *
 * @param scene_size Размер сцены, на которой будет располагаться вершина.
 * @param x Координата x вершины на сцене.
 * @param y Координата y вершины на сцене.
 * @param name_vertex Название вершины.
 * @param number Номер вершины.
 *
 * @property name Текстовая метка с названием вершины.
 * @property circle Окружность, представляющая вершину графа.
 * @property data Группа, содержащая окружность и метку вершины.
 */class VertexVizualisation(scene_size : Double, x : Double, y : Double, name_vertex: String,private var number: Int ) : Circle(){
    /**
     * Текстовая метка с названием вершины.
     */
    private var name: Text

    /**
     * Окружность, представляющая вершину графа.
     */
    private var circle: Circle

    /**
     * Группа, содержащая окружность и метку вершины.
     */
    private var data: Group

    /**
     * Инициализирует визуализацию вершины графа.
     *
     * Создает окружность и текстовую метку с названием вершины, задает им внешний вид и привязывает к окружности.
     */
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

    /**
     * Возвращает номер вершины.
     *
     * @return Номер вершины.
     */
    fun get_number(): Int {
        return number
    }

    /**
     * Уменьшает номер вершины на единицу.
     */
    fun decrease_number() {
        number--
    }

/**
 * Возвращает группу, содержащую окружность и метку вершины.
 *
 * @return Группа, содержащая окружность и метку вершины.
 **/
    fun group() : Group { return data }

    /**
     * Возвращает окружность вершины.
     *
     * @return Окружность вершины.
     */
    fun get_circle() : Circle { return circle }

    /**
     * Возвращает текстовую метку вершины.
     *
     * @return Текстовая метка вершины.
     */
    fun get_name() : Text { return name }
}