package com.example.todoapps.ui.shared

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todoapps.R
import com.example.todoapps.data.local.AppDatabase
import com.example.todoapps.data.repository.ToDoRepository
import com.example.todoapps.ui.add.AddViewModel
import com.example.todoapps.ui.add.AddViewModelFactory
import com.example.todoapps.ui.home.HomeViewModel
import com.example.todoapps.ui.home.HomeViewModelFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideActionBar()
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}