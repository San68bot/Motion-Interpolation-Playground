package applicationBase

import geometry.Position
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.shape.Circle
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

    val c1 = Position(40.0, 40.0, 0.0)
    val circle: Circle get() = Circle(c1.x, c1.y, 5.0)

    override fun initiate(stage: Stage) {
        super.initiate(stage)
        setFont(close1, 15.0)

        p1_x.prefWidth = 50.0
        p1_y.prefWidth = 50.0
        p1_h.prefWidth = 50.0

        p2_x.prefWidth = 50.0
        p2_y.prefWidth = 50.0
        p2_h.prefWidth = 50.0

        mainPane.setOnMouseClicked { e ->
            if (e.x <= 800 && e.y <= 800) {
                c1.x = e.x
                c1.y = e.y
                updatePos()
            }
        }
        mainPane.setOnMouseDragged { e ->
            if (e.x <= 800 && e.y <= 800) {
                c1.x = e.x
                c1.y = e.y
                updatePos()
            }
        }
        mainPane.setOnKeyPressed { e ->
            if (e.code === KeyCode.ENTER) {
                c1.x = p1_x.text.toDouble()
                c1.y = p1_y.text.toDouble()
                updatePos()
            }
        }

        simSettings.alignment = Pos.CENTER
        simSettings.children.addAll(
            p1, p1_x, comma1, p1_y, comma1_2, p1_h,
            p2, p2_x, comma2, p2_y, comma2_2, p2_h,
            close1
        )
    }

    fun updatePos() {
        p1_x.text = c1.x.toString()
        p1_y.text = c1.y.toString()
        mc.setPosition(c1.x, c1.y)
    }
}