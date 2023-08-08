package com.anubhav.textviewplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anubhav.textviewplayground.databinding.ActivityBadgeBinding

class BadgeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBadgeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBadgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var count = 1
        binding.apply {
            badgeView.text = count.toString()

            incrementButton.setOnClickListener {
                count += 1
                badgeView.text = count.toString()
            }
        }
    }
}