import javafx.scene.control.Label
import javafx.stage.Stage
import org.jetbrains.kotlin.Math.GraphVizualisation

class step_information(stage: Stage) : Label() {
    init {
        layoutX = 30.0
        layoutY = stage.height - 100.0
    }
    fun update_information_before_algorithm(isConstucting : Boolean){
        text = if (isConstucting) "Вы в режиме редактирования графа, нажмите Escape, чтобы выйти"
        else "Если не хотите редактировать граф, выберите двойным нажатием ЛКМ стартовую вершину, иначе нажмите Q"
    }

    fun update_information_during_algorithm(graph_visual : GraphVizualisation, step: Int, considered_vertexes: MutableList<Int>,
                                            added_edges: MutableList<MutableList<Pair<Int, Int>>>, ) {
        var weights = ""
        print(step)
        if (step >= 0) {
            for (edge in added_edges[step])
                weights += " ${graph_visual.graph.data[edge.first][edge.second]}"
        }
        if (step < graph_visual.graph.data.size - 1 && step > 0) {
            text =
                "На шаге №$step была добавлена вершина \"${graph_visual.vertexes[considered_vertexes[step]].name.text}\". Ребра, рассматриваемые на данном шаге, имеют веса $weights"
        } else if (step == 0)
            text =
                "Вершина  \"${graph_visual.vertexes[considered_vertexes[0]].name.text}\" была выбрана в качестве начальной. Ребра, рассматриваемые на данном шаге, имеют веса $weights"
        else if (step == graph_visual.graph.data.size - 1) text =
            "На шаге №$step была добавлена вершина \"${graph_visual.vertexes[considered_vertexes.last()].name.text}\". Построение минимального остовного дерева окончено"
        else text = "Чтобы выбрать выбрать новую начальную вершину нажмите на нее дважды."
        print(step)
    }
}