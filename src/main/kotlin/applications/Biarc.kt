package applications

import applicationBase.BiarcBase
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage

class Biarc: BiarcBase() {
    private val simSettings2 = HBox(5.0)
    private val simSettingsMain = VBox(2.5)

    val d1_tf = TextField("0")
    val d2_tf = TextField("0")

    override fun initiate(stage: Stage) {
        super.initiate(stage)

        simSettings.padding = Insets(2.5, 5.0, 5.0, 5.0)
        simSettings2.padding = Insets(0.0, 5.0, 10.0, 5.0)
        simSettings2.alignment = Pos.CENTER
        simSettingsMain.children.addAll(simSettings, simSettings2)

        setFont(p1, 15.0)
        setFont(p2, 15.0)
        setFont(d1, 15.0)
        setFont(d2, 15.0)
        setFont(close2, 15.0)

        d1_tf.prefWidth = 50.0
        d2_tf.prefWidth = 50.0

        simSettings2.children.addAll(
            d1, d1_tf,
            d2, d2_tf,
            close2
        )

        mainPane.bottom = simSettingsMain
        val scene = Scene(mainPane, Sim.scene, Sim.scene)
        stage.title = "Biarc Interpolation"
        stage.scene = scene
    }
}