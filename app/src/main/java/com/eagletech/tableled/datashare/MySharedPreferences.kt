package com.eagletech.tableled.datashare

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences constructor(context: Context) {
    private val sharedPreferences: SharedPreferences


    init {
        sharedPreferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
    }

    companion object {
        @Volatile
        private var instance: MySharedPreferences? = null

        fun getInstance(context: Context): MySharedPreferences {
            return instance ?: synchronized(this) {
                instance ?: MySharedPreferences(context).also { instance = it }
            }
        }
    }

    // Lấy ra thông tin mua theo lượt
    fun getTimes(): Int {
        return sharedPreferences.getInt("times", 0)
    }

    fun setTimes(lives: Int) {
        sharedPreferences.edit().putInt("times", lives).apply()
    }

    fun addTimes(amount: Int) {
        val current = getTimes()
        setTimes(current + amount)
    }

    fun removeTime() {
        val current = getTimes()
        if (current > 0) {
            setTimes(current - 1)
        }
    }


    // Lấy thông tin mua premium
    var isPremium: Boolean?
        get() {
            val userId = sharedPreferences.getString("UserId", "")
            return sharedPreferences.getBoolean("PremiumPlan_\$userId$userId", false)
        }
        set(state) {
            val userId = sharedPreferences.getString("UserId", "")
            sharedPreferences.edit().putBoolean("PremiumPlan_\$userId$userId", state!!).apply()
//            sharedPreferences.edit().apply()
        }

    // Lưu thông tin người dùng
    fun currentUserId(userid: String?) {
        sharedPreferences.edit().putString("UserId", userid).apply()
//        sharedPreferences.edit().apply()
    }

    // Lấy ra thông tin id người dùng
    fun getCurrentUserId(): String? {
        return sharedPreferences.getString("UserId", null)
    }

}