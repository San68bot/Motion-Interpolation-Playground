import applications.BiarcWindow
import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.Stage

class Sim: Application() {
    companion object {
        const val scene = 800.0
        const val background = "/grid800px.jpg"
    }

    private val optionsRect = Rectangle(260.0, 225.0, 250.0, 250.0)

    private val homeBox = VBox(15.0)

    private val title = Text("Motion Interpolation")
    private val biarc = Button("Biarc Interpolation")
    private val alphago = Button("AlphaGo Path Planning")

    private val startPane = Pane()
    private val mainPane = BorderPane()

    override fun start(stage: Stage) {
        optionsRect.fill = Color.rgb(69, 69, 69, 0.75)
        optionsRect.x = 400.0 - optionsRect.width / 2

        homeBox.padding = Insets(5.0, 5.0, 5.0, 5.0)
        homeBox.alignment = Pos.CENTER
        homeBox.layoutX = 290.0
        homeBox.layoutY = 230.0

        title.font = Font.font(Font.getDefault().toString() + "", FontWeight.BOLD, 25.0)
        title.fill = Color.WHITE
        biarc.font = Font.font(Font.getDefault().toString() + "", 15.0)
        alphago.font = Font.font(Font.getDefault().toString() + "", 15.0)

        homeBox.children.addAll(title, biarc, alphago)
        startPane.children.addAll(optionsRect, homeBox)

        biarc.setOnAction { BiarcWindow().initiate(stage) }
        alphago.setOnAction { println("AlphaGo") }

        startPane.background = Background(BackgroundImage(Image(background), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, null))
        mainPane.center = startPane

        val scene = Scene(mainPane, scene, scene)
        stage.title = "Motion Interpolation Playground"
        stage.scene = scene
        stage.icons.add(Image(background))
        stage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(Sim::class.java, *args)
}