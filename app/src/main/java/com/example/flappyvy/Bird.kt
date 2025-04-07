package com.example.flappyvy

class Bird {

    private var birdX: Int
    private var birdY: Int
    private var velocity: Int = 0
    private var currentFrame: Int
    companion object {
        var maxFrame: Int = 3
    }

    init {
        birdX = AppConstants.SCREEN_WIDTH / 2 - AppConstants.getBitmapBank().getBirdWidth() / 2
        birdY = AppConstants.SCREEN_HEIGHT / 2 - AppConstants.getBitmapBank().getBirdHeight() / 2
        currentFrame = 0 // Initialized to 0, assuming it starts from frame 0
        velocity = 0
    }

    fun getCurrentFrame(): Int {
        return currentFrame
    }

    fun setCurrentFrame(currentFrame: Int) {
        this.currentFrame = currentFrame
    }

    fun getVelocity(): Int {
        return velocity
    }

    fun setVelocity(velocity: Int) {
        this.velocity = velocity
    }

    fun getX(): Int {
        return birdX
    }

    fun getY(): Int {
        return birdY
    }

    fun setX(birdX: Int) {
        this.birdX = birdX
    }

    fun setY(birdY: Int) {
        this.birdY = birdY
    }
}
