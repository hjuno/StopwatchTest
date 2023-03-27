package com.example.stopwatch.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stopwatch.databinding.FragmentStopwatchBinding
import org.w3c.dom.Text
import java.lang.String

class StopwatchFragment : Fragment() {

    private var repsArray = mutableListOf<kotlin.String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStopwatchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stopwatchButtonControl()
    }

    override fun onResume() {
        super.onResume()
        binding.timeTxt.text = "" + MinTime + ":"+ String.format("%02d", SecTime) + ":" + String.format("%03d", MilTime)
        when (state) {
            INIT_STATE -> {
                binding.resetBtn.visibility = View.GONE
                binding.startBtn.text = "시작";
            }
            START_STATE -> {
                binding.resetBtn.visibility = View.VISIBLE
                binding.startBtn.text = "멈춰";
                binding.resetBtn.text = "랩스";
            }
            STOP_STATE -> {
                binding.resetBtn.visibility = View.VISIBLE
                binding.startBtn.text = "다시시작";
                binding.resetBtn.text = "리셋";
            }
        }
        repsArray.forEach {i ->
            binding.reps.append(i)
        }
    }
    private fun stopwatchButtonControl() {
        binding.startBtn.setOnClickListener {
            when (state) {
                INIT_STATE -> {
                    binding.resetBtn.visibility = View.VISIBLE
                    binding.startBtn.text = "멈춰";
                    binding.resetBtn.text = "랩스";
                    state = START_STATE
                    startStopwatch()
                }
                START_STATE -> {
                    binding.startBtn.text = "다시시작";
                    binding.resetBtn.text = "리셋";
                    state = STOP_STATE
                    pausedStopwatch()
                }
                STOP_STATE -> {
                    binding.startBtn.text = "멈춰";
                    binding.resetBtn.text = "랩스";
                    state = START_STATE
                    startStopwatch()
                }
            }
        }
        binding.resetBtn.setOnClickListener {
            when (state) {
                INIT_STATE -> {}
                START_STATE -> {recordTime()}
                STOP_STATE -> {
                    binding.resetBtn.visibility = View.GONE
                    binding.startBtn.text = "리셋";
                    binding.startBtn.text = "시작";
                    state = INIT_STATE
                    resetStopwatch()
                }
            }
        }
    }

    private fun startStopwatch() {
        StartTime = SystemClock.uptimeMillis()
        handler.postDelayed(StopwatchRunnable, 0 )
    }

    private fun pausedStopwatch() {
        TimeBuff += MilSecTime
        handler.removeCallbacks(StopwatchRunnable)
    }

    private fun recordTime() {
        repsArray.add("" + MinTime + ":"+ String.format("%02d", SecTime) + ":" + String.format("%03d", MilTime) + "\n")
        binding.reps.append("" + MinTime + ":"+ String.format("%02d", SecTime) + ":" + String.format("%03d", MilTime) + "\n")
    }

    private fun resetStopwatch() {
        MilSecTime = 0
        StartTime = SystemClock.uptimeMillis()
        TimeBuff = 0L
        UpdateTime = 0L
        SecTime = 0
        MinTime = 0
        MilTime = 0
        binding.timeTxt.text = "00:00:00"
        repsArray.clear()
        binding.reps.text = ""
    }

    object StopwatchRunnable : Runnable {
        override fun run() {
            MilSecTime = SystemClock.uptimeMillis() - StartTime
            UpdateTime = TimeBuff + MilSecTime
            SecTime = (UpdateTime / 1000).toInt()
            MinTime = (SecTime / 60).toLong()
            SecTime %= 60
            MilTime = UpdateTime % 1000
            binding.timeTxt.text = "" + MinTime + ":"+ String.format("%02d", SecTime) + ":" + String.format("%03d", MilTime)
            handler.postDelayed(this, 0)
            Log.d("state", state.toString())
        }
    }

    companion object {
        private var state : Int = 0
        val handler : Handler = Handler(Looper.getMainLooper())
        private lateinit var binding:FragmentStopwatchBinding
        var INIT_STATE = 0
        var START_STATE = 1
        var STOP_STATE = 2
        var MilTime : Long = 0
        var MilSecTime : Long = 0
        var SecTime : Int = 0
        var MinTime : Long = 0
        var StartTime : Long = SystemClock.uptimeMillis()
        var UpdateTime : Long = 0
        var TimeBuff : Long = 0
    }
}

