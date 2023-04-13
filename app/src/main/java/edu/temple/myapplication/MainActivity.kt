package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button

class MainActivity : AppCompatActivity() {

    // reference to TimeBinder received onBind()
    lateinit var timerBinder: TimerService.TimerBinder

    // flag to check if activity is connected to a service
    var isConnected = false


    // define callbacks for when service is connected
    val serviceConnection = object:ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder =  service as TimerService.TimerBinder
            isConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // bind to Service
        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )


        // use IBinder to start the Service
        findViewById<Button>(R.id.playButton).setOnClickListener {
            if(isConnected) timerBinder.start(100)


        }

        // use IBinder to stop the Service
        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            if(isConnected) timerBinder.pause()

        }

        // use IBinder to stop the Service
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if(isConnected) timerBinder.stop()

        }
    }

    // when activity is destroyed, disconnect from service
    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}