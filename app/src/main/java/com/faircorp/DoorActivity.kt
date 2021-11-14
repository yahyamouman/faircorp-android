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

class DoorActivity : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_door)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getLongExtra(DOOR_NAME_PARAM, 0)
        val switch : Switch = findViewById(R.id.switchButton)


        var doorName : String
        var doorStatus : String
        var roomName : String
        var currentTemperature : String?

        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().doorsApiService.findById(id).execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        val door = it.body()
                        if (door != null) {
                            doorName = door.name
                            roomName = door.roomName
                            doorStatus = door.doorStatus.toString()
                            val idRoom = door.roomId
                            /* Here we will call the RoomApi to get the room of our door */
                            lifecycleScope.launch(context = Dispatchers.IO) { // (1)
                                runCatching { ApiServices().roomsApiService.findById(idRoom).execute() } // (2)
                                    .onSuccess {
                                        withContext(context = Dispatchers.Main) { // (3)
                                            val room = it.body()
                                            if (room != null) {
                                                currentTemperature = room.currentTemperature?.toString()

                                                /* I chose to populate the TextViews here all together so they show at the same time in the app */
                                                findViewById<TextView>(R.id.txt_door_name).text = doorName
                                                findViewById<TextView>(R.id.txt_room_name).text = roomName
                                                findViewById<TextView>(R.id.txt_door_status).text = doorStatus
                                                findViewById<TextView>(R.id.txt_door_current_temperature).text = currentTemperature
                                                /* handle the switch button */
                                                if (doorStatus == "OPEN")
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
                            "Error on door loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }

        /* handle the switch button for the doorStatus */
        switch.setOnCheckedChangeListener { _, _ ->
            lifecycleScope.launch(context = Dispatchers.IO) { // (1)
                runCatching { ApiServices().doorsApiService.switchStatus(id).execute() } // (2)
                        .onSuccess {
                            withContext(context = Dispatchers.Main) { // (3)
                                val door = it.body()
                                if (door != null) {
                                    doorStatus = door.doorStatus.toString()
                                    findViewById<TextView>(R.id.txt_door_status).text = doorStatus
                                }
                            }
                        }
                        .onFailure {
                            withContext(context = Dispatchers.Main) { // (3)
                                Toast.makeText(
                                        applicationContext,
                                        "Error on doorStatus switching $it",
                                        Toast.LENGTH_LONG
                                ).show()
                            }
                        }
            }

        }

    }
}