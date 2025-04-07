package com.example.flappyvy

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView

class CircleImageView(context: Context, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmapShader: BitmapShader? = null

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable ?: return
        val bitmap = drawableToBitmap(drawable)

        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader

        val radius = width.coerceAtMost(height) / 2f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }

    private fun drawableToBitmap(drawable: android.graphics.drawable.Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}