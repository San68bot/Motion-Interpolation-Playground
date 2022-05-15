package testing

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.stage.Stage


class rotateAboutPoint: Application() {
    override fun start(primaryStage: Stage) {
        val canvas = Canvas(400.0, 400.0)
        val x = 50.0
        val y = 100.0
        val width = 50.0
        val height = 100.0

        val gc: GraphicsContext = canvas.graphicsContext2D
        val rotationCenterX = 200.0
        val rotationCenterY = 200.0

        gc.save()
        gc.translate(rotationCenterX, rotationCenterY)
        gc.rotate(90.0)
        gc.translate(-rotationCenterX, -rotationCenterY)

        gc.fillRect(200.0 - width/2.0, 30.0, width, height)
        gc.restore()

        val scene = Scene(Group(canvas))
        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(rotateAboutPoint::class.java, *args)
}