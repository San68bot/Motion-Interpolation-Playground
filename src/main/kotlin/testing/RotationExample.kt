package testing

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.transform.Rotate
import javafx.stage.Stage

class RotationExample: Application() {
    override fun start(stage: Stage) {
        val rectangle2 = Rectangle(150.0, 75.0, 200.0, 150.0)
        rectangle2.fill = Color.BURLYWOOD
        rectangle2.stroke = Color.BLACK

        val rotate = Rotate()
        rotate.angle = 45.0
        rotate.pivotX = 300.0
        rotate.pivotY = 150.0
        rectangle2.transforms.addAll(rotate)

        val root = Group(rectangle2)
        val scene = Scene(root, 600.0, 300.0)
        stage.title = "Rotation transformation example"
        stage.scene = scene
        stage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(RotationExample::class.java, *args)
}