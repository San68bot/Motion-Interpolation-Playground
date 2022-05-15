package applications

import Sim
import ApplicationBase
import geometry.MCircle
import geometry.Point
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage


class BiarcWindow: ApplicationBase() {
    private val simSettings = HBox(5.0)
    private val simSettings2 = HBox(5.0)
    private val simSettingsMain = VBox(2.5)

    //labels
    private var p1 = Label("Position1:  (")
    private var p2 = Label("Position2:  (")
    private var d1 = Label("Distance1:  (")
    private var d2 = Label("Distance2:  (")

    //textfields
    private val p1_x = TextField("0")
    private val p1_y = TextField("0")
    private val p1_h = TextField("0")
    private val p2_x = TextField("0")
    private val p2_y = TextField("0")
    private val p2_h = TextField("0")
    private val d1_tf = TextField("0")
    private val d2_tf = TextField("0")

    //constants
    private val fontSize = 15.0
    private val tfWidth = 60.0

    //center: (400.0, 357.5)
    private val mc1 = MCircle(Point(200.0, 307.5), 8.0, Color.RED, p1_x, p1_y).setup()
    private val mc2 = MCircle(Point(600.0, 407.5), 8.0, Color.BLUE, p2_x, p2_y).setup()

    override fun initiate(stage: Stage) {
        super.initiate(stage)
        //padding alignment for the simSettings
        simSettings.padding = Insets(2.5, 5.0, 5.0, 5.0)
        simSettings2.padding = Insets(0.0, 5.0, 10.0, 5.0)
        simSettingsMain.children.addAll(simSettings, simSettings2)

        //setting up labels and textfields
        setFonts(p1, p2, d1, d2)
        setTextFieldsFormat(p1_x, p1_y, p1_h, p2_x, p2_y, p2_h, d1_tf, d2_tf)

        //setting alignment for the simSettings
        simSettings.alignment = Pos.CENTER
        simSettings2.alignment = Pos.CENTER

        //adding labels and textfields to the simSettings
        simSettings.children.addAll(
            p1 colorFill Color.RED, p1_x, Label(", ") fontSize fontSize colorFill Color.RED, p1_y, Label(", ") fontSize fontSize  colorFill Color.RED, p1_h, Label("),  ") fontSize fontSize colorFill Color.RED,
            p2 colorFill Color.BLUE, p2_x, Label(", ") fontSize fontSize colorFill Color.BLUE, p2_y, Label(", ") fontSize fontSize colorFill Color.BLUE, p2_h, Label(")") fontSize fontSize colorFill Color.BLUE
        )
        simSettings2.children.addAll(
            d1 colorFill Color.RED, d1_tf, Label("),  ") fontSize fontSize colorFill Color.RED,
            d2 colorFill Color.BLUE, d2_tf, Label(")") fontSize fontSize colorFill Color.BLUE
        )

        //textfield listeners
        p1_x.setOnKeyPressed { event -> if (event.code == KeyCode.ENTER) mc1.resetX(p1_x.text.toDouble()) }
        p1_y.setOnKeyPressed { event -> if (event.code == KeyCode.ENTER) mc1.resetY(p1_y.text.toDouble()) }
        p2_x.setOnKeyPressed { event -> if (event.code == KeyCode.ENTER) mc2.resetX(p2_x.text.toDouble()) }
        p2_y.setOnKeyPressed { event -> if (event.code == KeyCode.ENTER) mc2.resetY(p2_y.text.toDouble()) }

        //adding mcircles to the simulation pane
        simPane.children.addAll(mc1.circle, mc2.circle)

        //setup the scene and display the stage
        mainPane.bottom = simSettingsMain
        val scene = Scene(mainPane, Sim.scene, Sim.scene)
        stage.title = "Biarc Interpolation"
        stage.scene = scene
    }

    private fun setFonts(vararg labels: Label) {
        labels.forEach { setFont(it, fontSize) }
    }

    private fun setTextFieldsFormat(vararg textFields: TextField) {
        textFields.forEach {
            it.prefWidth = tfWidth
            it.alignment = Pos.CENTER
        }
    }
}