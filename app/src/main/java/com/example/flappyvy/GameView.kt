package com.example.flappyvy

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.NonNull
import com.example.flappyvy.GameThread

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private lateinit var gameThread: GameThread

    init {
        initView()
    }

    override fun surfaceCreated(@NonNull holder: SurfaceHolder) {
        if (!gameThread.isRunning()) {
            gameThread = GameThread(holder)
            gameThread.start()
        } else {
            gameThread.start()
        }
    }

    override fun surfaceChanged(@NonNull holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // No-op
    }

    override fun surfaceDestroyed(@NonNull holder: SurfaceHolder) {
        if (gameThread.isRunning()) {
            gameThread.setRunning(false)
            var retry = true
            while (retry) {
                try {
                    gameThread.join()
                    retry = false
                } catch (e: InterruptedException) {
                    // Optionally log error
                }
            }
        }
    }

    private fun initView() {
        val holder = holder
        holder.addCallback(this)
        isFocusable = true

        gameThread = GameThread(holder)
    }
}