package com.eagletech.tableled

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.widget.TextView
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.eagletech.tableled.databinding.ActivityMainBinding
import com.eagletech.tableled.datashare.MySharedPreferences


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mySharedPreferences: MySharedPreferences
    private var mText: String = "Welcome!!!"
    private var isAnimationRunning = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        mySharedPreferences = MySharedPreferences.getInstance(this)
        funUI()
        fucClick()
    }

    private fun funUI() {
        if (mySharedPreferences.isPremium == true){
            mainBinding.bottomBar.addIcon.visibility = View.VISIBLE

        }else{
            if ((mySharedPreferences.getTimes() > 0)) {
                mainBinding.bottomBar.addIcon.visibility = View.VISIBLE

            } else {
                mainBinding.bottomBar.addIcon.visibility = View.GONE
            }
        }
    }

    private fun fucClick() {
        mainBinding.bottomBar.addIcon.setOnClickListener {
            showInputDialog()
        }
        mainBinding.topBar.infoIcon.setOnClickListener {
            Log.d("click", "infoIcon")
            showInfoDialog()
        }

        mainBinding.topBar.menuIcon.setOnClickListener {
            Log.d("click", "menuIcon")
            val intent = Intent(this, PaymentTimesActivity::class.java)
            startActivity(intent)

        }

        mainBinding.bottomBar.layoutPlayPause.setOnClickListener {
            if (isAnimationRunning) {
                stopAnimation()
                mainBinding.bottomBar.playIcon.visibility = View.GONE
                mainBinding.bottomBar.pauseIcon.visibility = View.VISIBLE
            } else {
                startAnimation()
                mainBinding.bottomBar.playIcon.visibility = View.VISIBLE
                mainBinding.bottomBar.pauseIcon.visibility = View.GONE
            }
        }

    }

    // Hiển thị dialog nhập thông tin
    private fun showInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter text")
        val input = EditText(this)
        builder.setView(input)
        builder.setPositiveButton("Confirm") { dialog, which ->
            if (mySharedPreferences.getTimes() > 0 || mySharedPreferences.isPremium == true){
                val enteredText = input.text.toString()
                if (enteredText.isNotBlank()) {
                    mText = enteredText
                    loadUI(mText)
                }
                mySharedPreferences.removeTime()
                Toast.makeText(this, "Add Text Successfully", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Please buy more uses", Toast.LENGTH_LONG).show()
                val intent = Intent(this, PaymentTimesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }

        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // Khởi tạo hoặc khôi phục trạng thái của animation
    private fun startAnimation() {
        isAnimationRunning = true
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        animation.duration = 5000 // Đặt thời gian di chuyển là 5 giây
        animation.repeatCount = Animation.INFINITE // Lặp vô hạn
        animation.interpolator = LinearInterpolator() // Đặt kiểu di chuyển
        mainBinding.movingTextView.startAnimation(animation)
    }

    // Dừng animation
    private fun stopAnimation() {
        isAnimationRunning = false
        mainBinding.movingTextView.clearAnimation()
    }

    private fun loadUI(s: String) {
        val screenWidth = getScreenWidth()
        val textSize = screenWidth / 30
        mainBinding.movingTextView.textSize = textSize
        mainBinding.movingTextView.text = s
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        animation.duration = 5000 // Đặt thời gian di chuyển là 5 giây
        animation.repeatCount = Animation.INFINITE // Lặp vô hạn
        animation.interpolator = LinearInterpolator() // Đặt kiểu di chuyển
        mainBinding.movingTextView.startAnimation(animation)
    }

    // Phương thức để lấy chiều rộng màn hình thực
    private fun getScreenWidth(): Float {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels.toFloat() // Trả về chiều rộng của màn hình
    }


    // Show dialog cho dữ liệu SharePreferences
    private fun showInfoDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Information")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        if (mySharedPreferences.isPremium == true) {
            dialog.setMessage("You have successfully registered")
        } else {
            dialog.setMessage("You have ${mySharedPreferences.getTimes()} turns use")
        }
        dialog.show()
    }

    override fun onRestart() {
        super.onRestart()
        funUI()
    }
}