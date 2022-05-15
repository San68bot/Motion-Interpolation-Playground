package applicationBase

import javafx.scene.shape.Circle

class MC: Circle(){
    init {
        this.radius = 7.0
        this.centerX = 100.0
        this.centerY = 100.0
    }

    fun setPosition(x: Double, y: Double){
        this.centerX = x
        this.centerY = y
    }
}