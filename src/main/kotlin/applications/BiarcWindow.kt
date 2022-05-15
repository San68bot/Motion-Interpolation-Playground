package applications

import applicationBase.Base
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage

class BiarcWindow: Base() {
    private val simSettings = HBox(5.0)
    private val simSettings2 = HBox(5.0)
    private val simSettingsMain = VBox(2.5)

    private var p1 = Label("Position1:  (")
    private var p2 = Label("),  Position2:  (")
    private var d1 = Label("Distance1:  (")
    private var d2 = Label("),  Distance2:  (")

    private val p1_x = TextField("0")
    private val p1_y = TextField("0")
    private val p1_h = TextField("0")
    private val p2_x = TextField("0")
    private val p2_y = TextField("0")
    private val p2_h = TextField("0")
    private val d1_tf = TextField("0")
    private val d2_tf = TextField("0")

    private val fontSize = 15.0
    private val tfWidth = 50.0

    override fun initiate(stage: Stage) {
        super.initiate(stage)
        simSettings.padding = Insets(2.5, 5.0, 5.0, 5.0)
        simSettings2.padding = Insets(0.0, 5.0, 10.0, 5.0)
        simSettingsMain.children.addAll(simSettings, simSettings2)

        setFonts(p1, p2, d1, d2)
        setPrefWidths(p1_x, p1_y, p1_h, p2_x, p2_y, p2_h, d1_tf, d2_tf)

        simSettings.alignment = Pos.CENTER
        simSettings2.alignment = Pos.CENTER

        simSettings.children.addAll(
            p1, p1_x, Label(", ") fontSize fontSize, p1_y, Label(", ") fontSize fontSize, p1_h,
            p2, p2_x, Label(", ") fontSize fontSize, p2_y, Label(", ") fontSize fontSize, p2_h,
            Label(")") fontSize fontSize
        )
        simSettings2.children.addAll(
            d1, d1_tf,
            d2, d2_tf,
            Label(")") fontSize fontSize
        )

        mainPane.bottom = simSettingsMain
        val scene = Scene(mainPane, Sim.scene, Sim.scene)
        stage.title = "Biarc Interpolation"
        stage.scene = scene
    }

    private fun setFonts(vararg labels: Label) {
        labels.forEach { setFont(it, fontSize) }
    }

    private fun setPrefWidths(vararg textFields: TextField) {
        textFields.forEach { it.prefWidth = tfWidth }
    }
}