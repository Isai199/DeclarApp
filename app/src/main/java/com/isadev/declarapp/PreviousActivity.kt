package com.isadev.declarapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PreviousActivity : AppCompatActivity(), customToolbar {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous)
        initToolBar()
    }

    override fun initToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_previous)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "DECLARACIÓN MENSUAL"
    }
}