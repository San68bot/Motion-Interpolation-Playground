package testing

import javafx.application.Application
import javafx.scene.shape.Arc
import javafx.scene.shape.ArcType
import javafx.stage.Stage

class drawArcTest: Application() {
    override fun start(primaryStage: Stage?) {
        //draw arc
        val arc = Arc(100.0, 100.0, 50.0, 50.0, 0.0, 90.0)
        arc.stroke = javafx.scene.paint.Color.BLACK
        arc.fill = javafx.scene.paint.Color.WHITE
        arc.strokeWidth = 2.0
        arc.type = ArcType.OPEN

        //add arc to group
        val group = javafx.scene.Group()
        group.children.add(arc)
        //add group to scene
        val scene = javafx.scene.Scene(group)
        primaryStage!!.scene = scene
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(drawArcTest::class.java, *args)
}