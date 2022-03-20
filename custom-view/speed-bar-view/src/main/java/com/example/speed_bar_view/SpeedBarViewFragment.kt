package com.example.speed_bar_view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_speed_bar_view.*

/**
 * @author 89hnim
 * @since 19/03/2022
 */
internal class SpeedBarViewFragment : Fragment(R.layout.fragment_speed_bar_view),
    SpeedBarView.SpeedBarViewCallback {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val values = mutableListOf<String>()
        values.addAll(generateListX("0"))
        values.addAll(generateListX("1"))
        values.addAll(generateListX("2"))
        values.addAll(generateListX("3"))
        values.addAll(generateListX("4"))
        values.add("5.0x")
        Log.d("dsk", "onViewCreated: $values")
        speed_bar.init(
            values,
            listOf(1, 10, 20, 30, 40, 50),
            10,
            this
        )
    }

    private fun generateListX(number: String): MutableList<String> {
        val result = mutableListOf<String>()
        for (i in 0..9) {
            if (number == "0" && i == 0) continue
            result.add("$number.${i}x")
        }
        return result
    }

    override fun onSpeedChanged(speed: String) {
        Log.d("dsk", "onSpeedChanged: $speed")
    }

}