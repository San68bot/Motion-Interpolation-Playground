package geometry

import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle

class MCircle(val center: Point, val radius: Double, val fill: Paint, val xTF: TextField? = null, val yTF: TextField? = null) {
    val circle = Circle(center.x, center.y, radius, fill)

    private var orgSceneX = 0.0
    private var orgSceneY = 0.0
    private var orgTranslateX = 0.0
    private var orgTranslateY = 0.0

    private var newTranslateX = 0.0
    private var newTranslateY = 0.0

    fun setup(): MCircle {
        circle.cursor = Cursor.HAND
        circle.onMouseDragged = circleOnMouseDraggedEventHandler
        xTF?.text = center.x.toString()
        yTF?.text = center.y.toString()
        return this
    }

    private var circleOnMouseDraggedEventHandler = EventHandler<MouseEvent> { t ->
        newTranslateX = orgTranslateX + (t.sceneX - orgSceneX)
        newTranslateY = orgTranslateY + (t.sceneY - orgSceneY)
        if (newTranslateX in 0.0 + radius..Sim.scene - radius && newTranslateY in 0.0 + radius..(Sim.scene-75.0) - radius) {
            (t.source as Circle).translateX = newTranslateX - circle.centerX
            (t.source as Circle).translateY = newTranslateY - circle.centerY
            xTF?.text = newTranslateX.toString()
            yTF?.text = newTranslateY.toString()
        }
    }

    fun resetPosition(p: Point) {
        resetX(p.x)
        resetY(p.y)
    }

    fun resetX(x: Double) {
        newTranslateX = x
        circle.translateX = newTranslateX - circle.centerX
        xTF?.text = newTranslateX.toString()
    }

    fun resetY(y: Double) {
        newTranslateY = y
        circle.translateY = newTranslateY - circle.centerY
        yTF?.text = newTranslateY.toString()
    }
}