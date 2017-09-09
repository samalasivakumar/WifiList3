package com.example.sivakumar.wifilist3

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.connect.*
import android.media.AudioManager
import java.lang.Compiler.disable
import android.bluetooth.BluetoothAdapter
import android.graphics.drawable.Drawable
import android.net.Uri
import java.io.File
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    var silentStatus: Boolean = false
    var vibrateStatus: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wifi.setOnClickListener {
            val intent = Intent(this, WifiInformation::class.java)
            startActivity(intent)
        }

        silent.setOnClickListener {
            val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.ringerMode = if (!silentStatus) AudioManager.RINGER_MODE_SILENT else AudioManager.RINGER_MODE_NORMAL
            if (!silentStatus)
                silent.setImageResource(R.drawable.silent_on);
            else
                silent.setImageResource(R.drawable.silent);
            silentStatus = !silentStatus

        }

        vibrate.setOnClickListener {
            val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.ringerMode = if (!vibrateStatus) AudioManager.RINGER_MODE_VIBRATE else AudioManager.RINGER_MODE_NORMAL
            if (!vibrateStatus)
                vibrate.setImageResource(R.drawable.vibrate_on);
            else
                vibrate.setImageResource(R.drawable.vibrate);
            vibrateStatus = !vibrateStatus
        }

        bluetooth.setOnClickListener {
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter.isEnabled) {
                mBluetoothAdapter.disable()
                bluetooth.setImageResource(R.drawable.bluetooth);
            }
            else {
                mBluetoothAdapter.enable()
                bluetooth.setImageResource(R.drawable.bluetooth_on);
            }
        }
    }

}
