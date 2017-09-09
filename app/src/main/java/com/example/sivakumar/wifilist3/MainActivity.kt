package com.example.sivakumar.wifilist3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.media.AudioManager
import android.bluetooth.BluetoothAdapter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTiles()
    }

    private fun initTiles() {

        val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        tile_wifi.setOnClickListener {
            val intent = Intent(this, WifiInformation::class.java)
            startActivity(intent)
        }

        tile_silent.setOnClickListener {

            if (audioManager.ringerMode == AudioManager.RINGER_MODE_SILENT) {
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                tile_silent.setImageResource(R.drawable.silent)
            } else {
                audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
                tile_silent.setImageResource(R.drawable.silent_on)
            }
        }

        tile_vibrate.setOnClickListener {

            if (audioManager.ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                tile_vibrate.setImageResource(R.drawable.vibrate)
            } else {
                audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                tile_vibrate.setImageResource(R.drawable.vibrate_on)
            }
        }

        tile_bluetooth.setOnClickListener {

            if (bluetoothAdapter.isEnabled) {
                bluetoothAdapter.disable()
                tile_bluetooth.setImageResource(R.drawable.bluetooth)
            } else {
                bluetoothAdapter.enable()
                tile_bluetooth.setImageResource(R.drawable.bluetooth_on)
            }
        }
    }
}
