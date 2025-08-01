package com.isadev.declarapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalculateActivity : AppCompatActivity(), customToolbar {
    lateinit var btnCalculate: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)
        initToolBar()

        btnCalculate = findViewById(R.id.calculate_activity_btn_calculate)
        btnCalculate.setOnClickListener {
            val intent = Intent(this, PreviousActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_calculate)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "DECLARACIÓN MENSUAL"
    }
}