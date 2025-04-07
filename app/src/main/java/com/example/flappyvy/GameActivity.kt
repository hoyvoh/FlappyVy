package com.example.flappyvy

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView = GameView(this)
        setContentView(gameView)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            // Nếu trò chơi đang ở trạng thái game over, reset trò chơi
            if (AppConstants.getGameEngine().getGameState() == 2) {
                AppConstants.getGameEngine().resetGame() // Khởi động lại trò chơi
            } else {
                AppConstants.getGameEngine().setGameState(1) // Đặt trạng thái trò chơi thành playing
                AppConstants.getGameEngine().bird.setVelocity(AppConstants.VELOCITY_WHEN_JUMPED) // Đặt vận tốc của chim để nhảy
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        // Resume any necessary game resources if needed
    }

    override fun onPause() {
        super.onPause()
        // Pause the game or release resources if necessary
    }
}
