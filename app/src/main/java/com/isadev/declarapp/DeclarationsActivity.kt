package com.isadev.declarapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DeclarationsActivity : AppCompatActivity(), customToolbar {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declarations)
        initToolBar()
    }

    override fun initToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_declarations)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "DECLARACIONES"
    }
}