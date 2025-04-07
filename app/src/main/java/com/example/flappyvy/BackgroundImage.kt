package com.example.flappyvy


class BackgroundImage {

    private var backgroundImageX = 0
    private var backgroundImageY = 0
    private val backgroundVelocity = 3

    fun getX(): Int {
        return backgroundImageX
    }

    fun getY(): Int {
        return backgroundImageY
    }

    fun setX(x: Int) {
        backgroundImageX = x
    }

    fun setY(y: Int) {
        backgroundImageY = y
    }

    fun getVelocity(): Int {
        return backgroundVelocity
    }
}