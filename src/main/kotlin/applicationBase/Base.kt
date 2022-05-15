package applicationBase

import Sim
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.*
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.Stage

abstract class Base {
    var mainPane = BorderPane()
    var simPane = Pane()
    private var back = Button("Back")

    val canvas = Canvas(Sim.scene, Sim.scene)
    val gc get() = canvas.graphicsContext2D

    open fun initiate(stage: Stage) {
        back.layoutX = 10.0
        back.layoutY = 10.0
        simPane.children.addAll(back)

        backButton(stage)

        stage.setOnCloseRequest { endTasks() }
        simBackground()
        mainPane.center = simPane
    }

    private fun backButton(stage: Stage) {
        back.setOnAction {
            Sim().start(stage)
            endTasks()
        }
        back.onKeyPressed = EventHandler { e: KeyEvent ->
            if (e.code == KeyCode.ENTER) {
                Sim().start(stage)
                endTasks()
            }
        }
    }

    private fun simBackground() {
        simPane.background = Background(
            BackgroundImage(
                Image(Sim.background), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                null, null
            )
        )
    }

    open fun endTasks() {}

    open infix fun Label.fontSize(fontSize: Double): Label {
        this.font = Font.font(fontSize)
        return this
    }

    open fun setFont(label: Label, fontSize: Double) {
        label.font = Font.font(fontSize)
    }

    open fun setFontBold(label: Label, fontSize: Double) {
        label.font = Font.font(Font.getDefault().toString() + "", FontWeight.BOLD, fontSize)
    }

    open fun setFont(text: Text, fontSize: Double) {
        text.font = Font.font(fontSize)
    }

    open fun setFontBold(text: Text, fontSize: Double) {
        text.font = Font.font(Font.getDefault().toString() + "", FontWeight.BOLD, fontSize)
    }
}