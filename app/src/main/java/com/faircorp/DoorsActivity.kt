package com.faircorp

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import com.faircorp.Service.ApiServices
import com.faircorp.adapter.DoorsAdapter
import androidx.lifecycle.lifecycleScope
import com.faircorp.Service.DoorApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoorsActivity : BasicActivity() , OnDoorSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doors)
        val recyclerView = findViewById<RecyclerView>(R.id.list_doors)
        val adapter = DoorsAdapter(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        lifecycleScope.launch(context = Dispatchers.IO) {
            runCatching { ApiServices().doorsApiService.findAll().execute() }
                .onSuccess {
                    withContext(context = Dispatchers.Main) {
                        adapter.update(it.body() ?: emptyList())
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(
                            applicationContext,
                            "Error on doors loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }


    }

    override fun onDoorSelected(id: Long) {
        val intent = Intent(this, DoorActivity::class.java).putExtra(DOOR_NAME_PARAM, id)
        startActivity(intent)
    }
}