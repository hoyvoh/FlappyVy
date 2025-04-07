package com.example.flappyvy


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Obstacle(
    private val bitmap: Bitmap,
    val gap: Int,
    private val screenWidth: Int,
    private val screenHeight: Int
) {
    private var x: Float = screenWidth.toFloat()
    private val topRect: Rect = Rect()
    private val bottomRect: Rect = Rect()

    // Randomly determine the height of the gap
    var gapPosition: Int = (100..screenHeight - gap - 100).random()

    init {
        // Initialize the rectangle positions
        updateRectangles()
    }

    private fun updateRectangles() {
        topRect.set(
            x.toInt(),
            gapPosition - bitmap.height,
            x.toInt() + bitmap.width,
            gapPosition
        )

        bottomRect.set(
            x.toInt(),
            gapPosition + gap,
            x.toInt() + bitmap.width,
            screenHeight
        )
    }

    fun draw(canvas: Canvas) {
        // Draw the top and bottom pipes
        canvas.drawBitmap(bitmap, x, gapPosition - bitmap.height.toFloat(), null)
        canvas.drawBitmap(bitmap, x, gapPosition + gap.toFloat(), null)
    }

    fun update() {
        // Move the obstacle to the left
        x -= 5 // Adjust speed here
        updateRectangles()
    }

    fun isOffScreen(): Boolean {
        return x < -bitmap.width
    }

    fun getCollisionRects(): Pair<Rect, Rect> {
        return Pair(topRect, bottomRect)
    }

    fun getX(): Float {
        return x
    }

    fun reset() {
        x = screenWidth.toFloat()
        gapPosition = (100..screenHeight - gap - 100).random()
        updateRectangles()
    }
}
