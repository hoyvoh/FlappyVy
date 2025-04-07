package com.example.flappyvy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var playBtn: ImageView
    private lateinit var infoBtn: Button
    private var popupWindow: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppConstants.initialization(applicationContext)

        playBtn = findViewById(R.id.playBtn)
        infoBtn = findViewById(R.id.infoBtn)

        playBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }

        infoBtn.setOnClickListener {
            if (popupWindow == null || !popupWindow!!.isShowing) {
                showInfoPopup(it)
            } else {
                popupWindow?.dismiss() // Tắt popup nếu đang mở
                popupWindow = null
            }
        }
    }

    private fun showInfoPopup(view: View) {
        // Tạo popup window
        val popupView = layoutInflater.inflate(R.layout.popup_info, null)
        popupWindow = PopupWindow(popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        // Hiển thị popup
        popupWindow?.showAsDropDown(view, 0, 0)
    }
}
