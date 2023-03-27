package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.stopwatch.databinding.ActivityMainBinding
import com.example.stopwatch.fragments.SettingFragment
import com.example.stopwatch.fragments.StopwatchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initScreen()
    }

    private fun initScreen () {
        val stopwatchFragment = StopwatchFragment()
        val settingFragment = SettingFragment()

        supportFragmentManager.beginTransaction().add(R.id.fragment_layout, StopwatchFragment()).commit()

        binding.stopwatchBtn.setOnClickListener {
            changeFragment(stopwatchFragment, R.id.fragment_layout)
        }
        binding.settingBtn.setOnClickListener {
            changeFragment(settingFragment, R.id.fragment_layout)
        }
    }

    private fun changeFragment (fragment: Fragment, layout: Int) {
        supportFragmentManager.beginTransaction().replace(layout, fragment).commit()
    }

}