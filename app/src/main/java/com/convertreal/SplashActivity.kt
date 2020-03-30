package com.convertreal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep((3 * 1000).toLong())

                    val show_screen = Intent(baseContext, MainActivity::class.java)
                    startActivity(show_screen)

                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        background.start()
    }
}