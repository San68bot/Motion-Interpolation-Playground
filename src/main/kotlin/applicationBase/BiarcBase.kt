package applicationBase

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.stage.Stage

abstract class BiarcBase: Base() {
    val simSettings = HBox(5.0)

    var p1 = Label("Position1:  (")
    var comma1 = Label(", ")
    var comma1_2 = Label(", ")
    var p2 = Label("),  Position2:  (")
    var comma2 = Label(", ")
    var comma2_2 = Label(", ")
    var d1 = Label("Distance1:  (")
    var d2 = Label("),  Distance2:  (")
    var close1 = Label(")")
    var close2 = Label(")")

    val p1_x = TextField("0")
    val p1_y = TextField("0")
    val p1_h = TextField("0")

    val p2_x = TextField("0")
    val p2_y = TextField("0")
    val p2_h = TextField("0")

    override fun initiate(stage: Stage) {
        super.initiate(stage)
        setFont(close1, 15.0)

        p1_x.prefWidth = 50.0
        p1_y.prefWidth = 50.0
        p1_h.prefWidth = 50.0

        p2_x.prefWidth = 50.0
        p2_y.prefWidth = 50.0
        p2_h.prefWidth = 50.0

        simSettings.alignment = Pos.CENTER
        simSettings.children.addAll(
            p1, p1_x, comma1, p1_y, comma1_2, p1_h,
            p2, p2_x, comma2, p2_y, comma2_2, p2_h,
            close1
        )
    }
}