package com.faircorp

import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.faircorp.Service.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WindowActivity : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getLongExtra(WINDOW_NAME_PARAM, 0)
        val switch : Switch = findViewById(R.id.switchButton)


        var windowName : String
        var windowStatus : String
        var roomName : String
        var currentTemperature : String?

        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().windowsApiService.findById(id).execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        val window = it.body()
                        if (window != null) {
                            windowName = window.name
                            roomName = window.roomName
                            windowStatus = window.windowStatus.toString()
                            val idRoom = window.roomId
                            /* Here we will call the RoomApi to get the room of our window */
                            lifecycleScope.launch(context = Dispatchers.IO) { // (1)
                                runCatching { ApiServices().roomsApiService.findById(idRoom).execute() } // (2)
                                    .onSuccess {
                                        withContext(context = Dispatchers.Main) { // (3)
                                            val room = it.body()
                                            if (room != null) {
                                                currentTemperature = room.currentTemperature?.toString()

                                                /* I chose to populate the TextViews here all together so they show at the same time in the app */
                                                findViewById<TextView>(R.id.txt_window_name).text = windowName
                                                findViewById<TextView>(R.id.txt_room_name).text = roomName
                                                findViewById<TextView>(R.id.txt_window_status).text = windowStatus
                                                findViewById<TextView>(R.id.txt_window_current_temperature).text = currentTemperature
                                                /* handle the switch button */
                                                if (windowStatus == "OPEN")
                                                    switch.isChecked()
                                                else
                                                    !switch.isChecked()
                                            }
                                        }
                                    }
                                    .onFailure {
                                        withContext(context = Dispatchers.Main) { // (3)
                                            Toast.makeText(
                                                applicationContext,
                                                "Error on rooms loading $it",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                            }
                            /* end RoomApi*/
                        }

                    }

                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Error on window loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }

        /* handle the switch button for the windowStatus */
        switch.setOnCheckedChangeListener { _, _ ->
            lifecycleScope.launch(context = Dispatchers.IO) { // (1)
                runCatching { ApiServices().windowsApiService.switchStatus(id).execute() } // (2)
                        .onSuccess {
                            withContext(context = Dispatchers.Main) { // (3)
                                val window = it.body()
                                if (window != null) {
                                    windowStatus = window.windowStatus.toString()
                                    findViewById<TextView>(R.id.txt_window_status).text = windowStatus
                                }
                            }
                        }
                        .onFailure {
                            withContext(context = Dispatchers.Main) { // (3)
                                Toast.makeText(
                                        applicationContext,
                                        "Error on windowStatus switching $it",
                                        Toast.LENGTH_LONG
                                ).show()
                            }
                        }
            }

        }

    }
}