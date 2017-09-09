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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.connect.*
import kotlinx.android.synthetic.main.wifi_design.*

class WifiInformation : AppCompatActivity() {

    internal var mainWifi: WifiManager? = null
    var receiverWifi: WifiReceiver? = null
    var wifiList: List<ScanResult>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wifi_design)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        wifiEnable()
    }

    @SuppressLint("WifiManagerLeak")
    fun wifiEnable(){
        // Initiate wifi service manager
        mainWifi = getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (!mainWifi!!.isWifiEnabled) {
            // If wifi disabled then enable it
            Toast.makeText(applicationContext, "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show()

            mainWifi!!.isWifiEnabled = true
        }

        // wifi scaned value broadcast receiver
        receiverWifi = WifiReceiver()

        // Register broadcast receiver
        // Broacast receiver will automatically call when number of wifi connections changed
        registerReceiver(receiverWifi, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        supportActionBar!!.title = "Scanning..."
        mainWifi!!.startScan()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 0x12345)
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        } else {
            mainWifi!!.startScan()
            //do something, permission was previously granted; or legacy device
        }

        list?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // selected item
            val ssid = (view as TextView).text.toString()
            ssid.connectToWifi()
            Toast.makeText(this@WifiInformation, "Wifi SSID : " + ssid, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0x12345) {
            grantResults
                    .filter { it != PackageManager.PERMISSION_GRANTED }
                    .forEach { return }
            mainWifi!!.startScan()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 0, 0, "Refresh")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPause() {
        unregisterReceiver(receiverWifi)
        super.onPause()
    }

    override fun onResume() {
        registerReceiver(receiverWifi, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        super.onResume()
    }

    // Broadcast receiver class called its receive method
    // when number of wifi connections changed
    inner class WifiReceiver : BroadcastReceiver() {

        // This method call when number of wifi connections changed
        override fun onReceive(c: Context, intent: Intent) {

            supportActionBar!!.title = "Scanning..."
            wifiList = mainWifi!!.scanResults


            val filtered = arrayOfNulls<String>(wifiList!!.size)
            supportActionBar!!.title = "Total Connections: ${filtered.size}"

            // Setting wifi details to list view
            var counter = 0
            wifiList!!.forEach { eachWifi ->

                filtered[counter] = eachWifi.SSID
                counter++
            }
            filtered.sort()
            list!!.adapter = ArrayAdapter<String>(applicationContext, R.layout.list_item, R.id.label, filtered)
        }
    }

    private fun String.connectToWifi() {
        val dialog = Dialog(this@WifiInformation)
        dialog.setContentView(R.layout.connect)
        dialog.setTitle("Connect to Network")

        dialog.textSSID1.text = this
        // if button is clicked, connect to the network;
        dialog.okButton.setOnClickListener {
            val checkPassword = dialog.textPassword.text.toString()
            finallyConnect(checkPassword, this)
            dialog.dismiss()
        }
        dialog.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun finallyConnect(networkPass: String, networkSSID: String) {
        val wifiConfig = WifiConfiguration()
        wifiConfig.SSID = String.format("\"%s\"", networkSSID)
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass)

        // remember id
        val netId = mainWifi!!.addNetwork(wifiConfig)
        mainWifi!!.disconnect()
        mainWifi!!.enableNetwork(netId, true)
        mainWifi!!.reconnect()

        val conf = WifiConfiguration()
        conf.SSID = "\"\"" + networkSSID + "\"\""
        conf.preSharedKey = "\"" + networkPass + "\""
        mainWifi!!.addNetwork(conf)
    }
}
