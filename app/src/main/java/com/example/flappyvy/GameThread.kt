package com.example.flappyvy

import android.graphics.Canvas
import android.os.SystemClock
import android.util.Log
import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder) : Thread() {

    private var isRunning: Boolean = true
    private val delay: Long = 33

    override fun run() {
        while (isRunning) {
            val startTime = SystemClock.uptimeMillis()
            var canvas: Canvas? = null

            try {
                canvas = surfaceHolder.lockCanvas(null)
                if (canvas != null) {
                    synchronized(surfaceHolder) {
                        AppConstants.getGameEngine().updateAndDrawableBackgroundImage(canvas)
                        AppConstants.getGameEngine().updateAndDrawBird(canvas)
                    }
                }
            } finally {
                canvas?.let {
                    surfaceHolder.unlockCanvasAndPost(it)
                }
            }

            val loopTime = SystemClock.uptimeMillis() - startTime
            if (loopTime < delay) {
                try {
                    sleep(delay - loopTime)
                } catch (e: InterruptedException) {
                    Log.e("Interrupted", "Interrupted while sleeping", e)
                }
            }
        }
    }

    fun isRunning(): Boolean {
        return isRunning
    }

    fun setRunning(state: Boolean) {
        isRunning = state
    }
}