package geometry

import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.transform.Rotate
import utils.round
import utils.toDegrees
import kotlin.math.atan2

class MCircle(val center: Position, val radius: Double, val fill: Paint, val xTF: TextField? = null, val yTF: TextField? = null, val tTF: TextField? = null) {
    val circle = Circle(center.x, center.y, radius, fill)
    val rectangle = Rectangle(center.x, center.y, 28.0, 6.0)

    private var x = 0.0
    private var y = 0.0
    private val rotate = Rotate(center.deg, center.x, center.y)

    fun setup(): MCircle {
        circle.cursor = Cursor.HAND
        circle.onMouseDragged = circleOnMouseDraggedEventHandler
        x = center.x
        y = center.y

        rectangle.fill = fill
        rectangle.y -= (rectangle.height / 2)
        rectangle.cursor = Cursor.HAND
        rectangle.onMouseDragged = rectangleOnMouseDraggedEventHandler

        xTF?.text = center.x.round().toString()
        yTF?.text = center.y.round().toString()
        tTF?.text = center.deg.round().toString()
        return this
    }

    private var circleOnMouseDraggedEventHandler = EventHandler<MouseEvent> { t ->
        x = t.sceneX
        y = t.sceneY
        if (x in 0.0 + radius..Sim.scene - radius && y in 0.0 + radius..(Sim.scene-75.0) - radius) {
            (t.source as Circle).translateX = x - circle.centerX
            (t.source as Circle).translateY = y - circle.centerY
            xTF?.text = x.round().toString()
            yTF?.text = y.round().toString()
            updateRect()
        }
    }

    private var rectangleOnMouseDraggedEventHandler = EventHandler<MouseEvent> { t ->
        val angle = atan2(t.sceneY - y, t.sceneX - x).toDegrees()
        rotate.angle = angle
        tTF?.text = angle.round().toString()
        updateRect()
    }

    private fun updateRect() {
        rectangle.x = x
        rectangle.y = y - (rectangle.height / 2)

        rotate.pivotX = x
        rotate.pivotY = y

        rectangle.transforms.clear()
        rectangle.transforms.addAll(rotate)
    }

    fun resetPosition(p: Position) {
        resetX(p.x)
        resetY(p.y)
        resetAngle(p.deg)
    }

    fun resetX(newX: Double) {
        x = newX
        circle.translateX = x - circle.centerX
        xTF?.text = x.round().toString()
        rectangle.x = newX
    }

    fun resetY(newY: Double) {
        y = newY
        circle.translateY = y - circle.centerY
        yTF?.text = y.round().toString()
        rectangle.y = newY - (rectangle.height / 2)
    }

    fun resetAngle(newAngle: Double) {
        rotate.angle = newAngle
        tTF?.text = newAngle.round().toString()
        updateRect()
    }
}