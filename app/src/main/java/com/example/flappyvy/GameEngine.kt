package com.example.flappyvy

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class GameEngine {

    private val bitmapBank: BitmapBank = AppConstants.getBitmapBank()
    private val backgroundImage = BackgroundImage()
    val bird: Bird = Bird()
    private var gameState: Int = 0 // 0 -> not started, 1 -> playing, 2 -> game over
    private val paint = Paint()
    private val obstacles: MutableList<Obstacle> = mutableListOf()
    private val maxObstacles: Int = 5 // Giới hạn số lượng chướng ngại vật tối đa trên màn hình

    // Thay đổi ở đây
    private var distanceSinceLastObstacle: Int = 0
    private val baseDistanceThreshold: Int = 500 // pixel - có thể chỉnh tăng giảm cho phù hợp

    init {
        // Tạo chướng ngại vật ban đầu
        createObstacle()
    }

    fun updateAndDrawableBackgroundImage(canvas: Canvas) {
        val bgBitmap = bitmapBank.getBackgroundGame()
        val bgWidth = bgBitmap.width

        // Di chuyển nền
        backgroundImage.setX(backgroundImage.getX() - backgroundImage.getVelocity())

        // Nếu nền đầu ra khỏi màn hình thì reset lại
        if (backgroundImage.getX() <= -bgWidth) {
            backgroundImage.setX(0)
        }

        // Vẽ 2 tấm nền ghép nhau để tạo cảm giác cuộn vô hạn
        val destRect1 = Rect(
            backgroundImage.getX(), 0,
            backgroundImage.getX() + bgWidth,
            AppConstants.SCREEN_HEIGHT
        )
        val destRect2 = Rect(
            backgroundImage.getX() + bgWidth, 0,
            backgroundImage.getX() + 2 * bgWidth,
            AppConstants.SCREEN_HEIGHT
        )

        canvas.drawBitmap(bgBitmap, null, destRect1, null)
        canvas.drawBitmap(bgBitmap, null, destRect2, null)

        // Cập nhật và vẽ chướng ngại vật
        updateObstacles()
        bitmapBank.drawObstacles(canvas, obstacles)
    }


    fun updateAndDrawBird(canvas: Canvas) {
        if (gameState == 1) { // Kiểm tra nếu trò chơi đang chơi
            // Cập nhật vị trí của chim
            if (bird.getY() < (AppConstants.SCREEN_HEIGHT - bitmapBank.getBirdHeight()) || bird.getVelocity() < 0) {
                bird.setVelocity(bird.getVelocity() + AppConstants.gravity) // Áp dụng trọng lực
                bird.setY(bird.getY() + bird.getVelocity()) // Cập nhật vị trí của chim
            }

            // Kiểm tra va chạm với chướng ngại vật
            for (obstacle in obstacles) {
                if (checkCollision(bird, obstacle)) {
                    setGameState(2) // Đặt trạng thái trò chơi thành game over
                    break
                }
            }

            // Kiểm tra nếu chim đã chạm đất
            if (bird.getY() >= (AppConstants.SCREEN_HEIGHT - bitmapBank.getBirdHeight())) {
                setGameState(2) // Đặt trạng thái trò chơi thành game over
            }

            // Kiểm tra nếu chim đã bay lên quá cao
            if (bird.getY() < 1) {
                setGameState(2) // Đặt trạng thái trò chơi thành game over
            }
        }

        // Vẽ chim
        val currentFrame = bird.getCurrentFrame()
        val birdBitmap: Bitmap = AppConstants.getBitmapBank().getBird(currentFrame)
            ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Bitmap placeholder

        canvas.drawBitmap(birdBitmap, bird.getX().toFloat(), bird.getY().toFloat(), null)

        // Cập nhật khung hình của chim cho hoạt hình
        var newFrame = currentFrame + 1
        if (newFrame > Bird.maxFrame) {
            newFrame = 0
        }
        bird.setCurrentFrame(newFrame)

        // Vẽ chữ "Game Over" nếu trò chơi đã kết thúc
        if (gameState == 2) {
            bitmapBank.drawGameOver(canvas)
        }
    }

    private fun updateObstacles() {
        // Cập nhật vị trí của các chướng ngại vật
        for (obstacle in obstacles) {
            obstacle.update()
        }

        // Tăng khoảng cách đã cuộn
        distanceSinceLastObstacle += backgroundImage.getVelocity()

        // Scale ngưỡng tạo obstacle theo tốc độ chim
        val speedFactor = Math.max(1f, bird.getVelocity() / 10f)
        val dynamicThreshold = (baseDistanceThreshold * speedFactor).toInt()

        // Tạo chướng ngại vật mới khi đủ khoảng cách và số lượng chưa vượt giới hạn
        if (distanceSinceLastObstacle > dynamicThreshold && obstacles.size < maxObstacles) {
            createObstacle()
            distanceSinceLastObstacle = 0
        }

        // Xóa các chướng ngại vật đã ra ngoài màn hình
        obstacles.removeAll { it.isOffScreen() }
    }

    private fun createObstacle() {
        val newObstacle = Obstacle(
            bitmapBank.getPipeBitmap(), // Lấy bitmap từ BitmapBank
            250, // Khoảng cách giữa các chướng ngại vật
            AppConstants.SCREEN_WIDTH,
            AppConstants.SCREEN_HEIGHT
        )
        obstacles.add(newObstacle)
    }

    private fun checkCollision(bird: Bird, obstacle: Obstacle): Boolean {
        val birdRect = Rect(
            bird.getX().toInt(),
            bird.getY().toInt(),
            bird.getX().toInt() + bitmapBank.getBirdWidth(),
            bird.getY().toInt() + bitmapBank.getBirdHeight()
        )

        val (topPipe, bottomPipe) = obstacle.getCollisionRects()
        return birdRect.intersects(topPipe.left, topPipe.top, topPipe.right, topPipe.bottom) ||
                birdRect.intersects(bottomPipe.left, bottomPipe.top, bottomPipe.right, bottomPipe.bottom)
    }

    fun setGameState(state: Int) {
        gameState = state
    }

    fun getGameState(): Int {
        return gameState
    }

    fun resetGame() {
        bird.setY(AppConstants.SCREEN_HEIGHT / 2) // Đặt chim vào giữa màn hình
        bird.setVelocity(0) // Đặt lại vận tốc
        backgroundImage.setX(0) // Đặt lại vị trí hình nền
        gameState = 0 // Đặt lại trạng thái trò chơi
        obstacles.clear() // Xóa chướng ngại vật
        distanceSinceLastObstacle = 0 // Reset khoảng cách cuộn
    }
}
