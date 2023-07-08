package com.anubhav.textviewplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.anubhav.textviewplayground.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var num = 0
    var repeatCount = 0
    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.incrementButton.setOnClickListener {
            num+=1
            binding.rollingNumberView.setNumber(num)
        }

        binding.decrementButton.setOnClickListener {
            num-=1
            binding.rollingNumberView.setNumber(num)
        }


        handler.post(object : Runnable {
            override fun run() {
                repeatCount++

                if (repeatCount > 100) {
                    handler.removeCallbacks(this)
                } else {
                    binding.incrementButton.performClick()
                    handler.postDelayed(this, 500)
                }
            }
        })
    }
}