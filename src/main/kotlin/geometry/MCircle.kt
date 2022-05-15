package geometry

import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle

class MCircle(val center: Point, val radius: Double, val fill: Paint, val xTF: TextField? = null, val yTF: TextField? = null) {
    val circle = Circle(center.x, center.y, radius, fill)

    private var x = 0.0
    private var y = 0.0

    fun setup(): MCircle {
        circle.cursor = Cursor.HAND
        circle.onMouseDragged = circleOnMouseDraggedEventHandler
        xTF?.text = center.x.toString()
        yTF?.text = center.y.toString()
        return this
    }

    private var circleOnMouseDraggedEventHandler = EventHandler<MouseEvent> { t ->
        x = t.sceneX
        y = t.sceneY
        if (x in 0.0 + radius..Sim.scene - radius && y in 0.0 + radius..(Sim.scene-75.0) - radius) {
            (t.source as Circle).translateX = x - circle.centerX
            (t.source as Circle).translateY = y - circle.centerY
            xTF?.text = x.toString()
            yTF?.text = y.toString()
        }
    }

    fun resetPosition(p: Point) {
        resetX(p.x)
        resetY(p.y)
    }

    fun resetX(newX: Double) {
        x = newX
        circle.translateX = x - circle.centerX
        xTF?.text = x.toString()
    }

    fun resetY(newY: Double) {
        y = newY
        circle.translateY = y - circle.centerY
        yTF?.text = y.toString()
    }
}