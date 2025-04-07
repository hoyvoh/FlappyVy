package com.example.flappyvy

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect

class BitmapBank(resources: Resources) {

    private val originalBackgroundGame: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.background_game)
        ?: throw IllegalArgumentException("Background image not found")

    // Tạo bitmap đã được điều chỉnh kích thước
    private val backgroundGame: Bitmap = scaleBackground(originalBackgroundGame, AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT)

    private val bird: Array<Bitmap?> = arrayOfNulls(4) // Array to hold bird frames
    private val pipeBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pipe_green)
    private val pipeFlippedBitmap: Bitmap = getFlippedBitmap(pipeBitmap)
    val gameOverBitmap = BitmapFactory.decodeResource(resources, R.drawable.gameover)

    init {
        // Initialize the bird array with bitmap frames
        bird[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_frame1)
        bird[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_frame2)
        bird[2] = BitmapFactory.decodeResource(resources, R.drawable.bird_frame3)
        bird[3] = BitmapFactory.decodeResource(resources, R.drawable.bird_frame4)
    }

    private fun getFlippedBitmap(originalBitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(-1f, 1f) // Lật ngược theo chiều ngang
        return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, false)
    }

    private fun scaleBackground(originalBitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        // Tính tỷ lệ mới để giữ nguyên tỷ lệ
        val scaleWidth = targetWidth.toFloat() / originalBitmap.width
        val scaleHeight = targetHeight.toFloat() / originalBitmap.height

        // Sử dụng tỷ lệ nhỏ hơn để tránh biến dạng
        val scale = Math.min(scaleWidth, scaleHeight)

        // Tính kích thước mới
        val scaledWidth = (originalBitmap.width * scale).toInt()
        val scaledHeight = (originalBitmap.height * scale).toInt()

        // Tạo bitmap đã được điều chỉnh kích thước
        return Bitmap.createScaledBitmap(originalBitmap, scaledWidth, targetHeight, true)
    }

    fun drawObstacles(canvas: Canvas, obstacles: List<Obstacle>) {
        val scaleFactor = 2f

        for (obstacle in obstacles) {
            val scaledWidth = (pipeBitmap.width * scaleFactor).toInt()
            val scaledHeight = (pipeBitmap.height * scaleFactor).toInt()

            // Vẽ ống trên
            val topRect: Rect = Rect(
                obstacle.getX().toInt(),
                obstacle.gapPosition - scaledHeight,
                (obstacle.getX() + scaledWidth).toInt(),
                obstacle.gapPosition
            )
            canvas.drawBitmap(pipeBitmap, null, topRect, null)

            // Vẽ ống dưới
            val bottomRect: Rect = Rect(
                obstacle.getX().toInt(),
                obstacle.gapPosition + obstacle.gap,
                (obstacle.getX() + scaledWidth).toInt(),
                obstacle.gapPosition + obstacle.gap + scaledHeight
            )
            canvas.drawBitmap(pipeFlippedBitmap, null, bottomRect, null)
        }
    }



    fun drawGameOver(canvas: Canvas) {

        val bitmapWidth = gameOverBitmap.width
        val bitmapHeight = gameOverBitmap.height
        val x = (AppConstants.SCREEN_WIDTH - bitmapWidth) / 2f
        val y = (AppConstants.SCREEN_HEIGHT - bitmapHeight) / 2f

        canvas.drawBitmap(gameOverBitmap, x, y, null)
    }



    fun getBackgroundGame(): Bitmap {
        return backgroundGame
    }

    fun getBackgroundWidth(): Int {
        return backgroundGame.width
    }

    fun getBackgroundHeight(): Int {
        return backgroundGame.height
    }

    fun getBird(frame: Int): Bitmap? {
        return bird[frame]
    }

    fun getBirdWidth(): Int {
        return bird[0]?.width ?: 0 // Safe call and return 0 if null
    }

    fun getBirdHeight(): Int {
        return bird[0]?.height ?: 0 // Safe call and return 0 if null
    }

    // Phương thức trả về pipeBitmap nếu cần
    fun getPipeBitmap(): Bitmap {
        return pipeBitmap
    }

    fun getPipeFlippedBitmap(): Bitmap {
        return pipeFlippedBitmap
    }
}
