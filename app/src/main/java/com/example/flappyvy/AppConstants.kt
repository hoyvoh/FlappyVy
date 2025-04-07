package com.example.flappyvy

import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

object AppConstants {

    private lateinit var bitmapBank: BitmapBank
    private lateinit var gameEngine: GameEngine
    var gravity: Int = 3
    var VELOCITY_WHEN_JUMPED: Int = -40

    fun initialization(context: Context) {
        setScreenSize(context)
        bitmapBank = BitmapBank(context.resources)
        gameEngine = GameEngine()
        gravity = 3
        VELOCITY_WHEN_JUMPED = -40
    }

    fun getBitmapBank(): BitmapBank {
        return bitmapBank
    }

    fun getGameEngine(): GameEngine {
        return gameEngine
    }

    var SCREEN_WIDTH: Int = 0
    var SCREEN_HEIGHT: Int = 0

    private fun setScreenSize(context: Context) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()

        // Check the API level and use the appropriate method to get the display metrics
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = wm.currentWindowMetrics
            display?.let {
                val bounds = it.bounds
                SCREEN_WIDTH = bounds.width()
                SCREEN_HEIGHT = bounds.height()
            }
        } else {
            @Suppress("DEPRECATION")
            val display = wm.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(metrics)
            SCREEN_WIDTH = metrics.widthPixels
            SCREEN_HEIGHT = metrics.heightPixels
        }
    }
}
