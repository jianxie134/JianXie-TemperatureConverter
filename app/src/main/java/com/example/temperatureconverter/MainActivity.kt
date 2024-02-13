package com.example.temperatureconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var seekBarCelsius: SeekBar
    private lateinit var seekBarFahrenheit: SeekBar

    private lateinit var textViewCelsius: TextView
    private lateinit var textViewFahrenheit: TextView

    private var wasBelow20: Boolean? = null
    private val converter = Converter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarCelsius = findViewById(R.id.seekBar_C)
        seekBarFahrenheit = findViewById(R.id.seekBar_F)

        textViewCelsius = findViewById(R.id.TV_C_show)
        textViewFahrenheit = findViewById(R.id.TV_F_show)

        textViewFahrenheit.text = String.format("%.2f°F", 32.00)
        textViewCelsius.text = String.format("%.2f°C", 0.00)

        initializeSeekBarListeners()
    }

    private fun initializeSeekBarListeners() {
        seekBarCelsius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    updateCelsius(progress.toDouble())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBarFahrenheit.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    updateFahrenheit(progress.toDouble())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if ((seekBar?.progress ?: 0) < 32) {
                    seekBar?.progress = 32
                    textViewFahrenheit.text = String.format("%.2f°F", 32.00)
                    textViewCelsius.text = String.format("%.2f°C", 0.00)
                }
            }
        })
    }

    private fun updateCelsius(progress: Double) {
        val fahrenheitValue = converter.celsiusToFahrenheit(progress)
        seekBarFahrenheit.progress = fahrenheitValue.toInt()
        textViewCelsius.text = String.format("%.2f°C", progress)
        textViewFahrenheit.text = String.format("%.2f°F", fahrenheitValue)
        checkTemperature(progress)
    }

    private fun updateFahrenheit(progress: Double) {
        val celsiusValue = converter.fahrenheitToCelsius(progress)
        seekBarCelsius.progress = celsiusValue.toInt()
        textViewFahrenheit.text = String.format("%.2f°F", progress)
        textViewCelsius.text = String.format("%.2f°C", celsiusValue)
        checkTemperature(celsiusValue)
    }

    private fun checkTemperature(value: Double) {
        val message = if (value <= 20) {
            if (wasBelow20 == null || !wasBelow20!!)
                "I wish it were warmer."
            else
                return
        } else {
            if (wasBelow20 == null || wasBelow20!!)
                "I wish it were colder."
            else
                return
        }
        showCenteredSnackBar(message)
        wasBelow20 = value <= 20
    }

    private fun showCenteredSnackBar(message: String) {
        val snackbar = Snackbar.make(seekBarCelsius, message, Snackbar.LENGTH_SHORT)
        val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
        snackbar.show()
    }
}
