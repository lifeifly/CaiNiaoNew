package com.example.thumbupsample

/**
 * 坐标点
 */
class CvPoint() {
    private var x = 0F
    private var y = 0F

    constructor(x: Float, y: Float) : this() {
        this.x = x
        this.y = y
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CvPoint

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String {
        return "CvPoint(x=$x, y=$y)"
    }

    fun setX(x: Float) {
        this.x = x
    }

    fun setY(y: Float) {
        this.y = y
    }

    fun getX():Float {
        return x
    }

    fun getY():Float {
        return y
    }

}