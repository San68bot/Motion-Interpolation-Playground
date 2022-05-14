import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class App: Application() {
    private val WIDTH = 800.0
    private val HEIGHT = 800.0

    lateinit var background: Image
    lateinit var stage: Stage
    lateinit var canvas: Canvas
    lateinit var gc: GraphicsContext

    override fun start(stage: Stage?) {
        this.stage = stage!!

        setupStage()
        drawBackground()

        val root = Group()
        stage.scene = Scene(StackPane(root))

        root.children.addAll(canvas)

        stage.show()
    }

    private fun setupStage() {
        stage.title = "Motion Interpolation Playground"
        stage.isResizable = false
    }

    private fun drawBackground() {
        background = Image("/white.jpg")
        canvas = Canvas(WIDTH, HEIGHT)
        gc = canvas.graphicsContext2D
        gc.drawImage(background, 0.0, 0.0)
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}