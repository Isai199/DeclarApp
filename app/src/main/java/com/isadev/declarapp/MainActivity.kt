package com.isadev.declarapp

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity(), customToolbar {
    lateinit var rlBtnCalcular: RelativeLayout
    lateinit var rlBtnDeclaraciones: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()

        rlBtnCalcular = findViewById<RelativeLayout>(R.id.main_activity_rl_calcular)
        rlBtnDeclaraciones = findViewById<RelativeLayout>(R.id.main_activity_rl_registers)

        rlBtnCalcular.setOnClickListener {
            val intent = Intent(this, CalculateActivity::class.java)
            startActivity(intent)
        }

        rlBtnDeclaraciones.setOnClickListener {
            val intent = Intent(this, DeclarationsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "DeclarApp"
    }
}

interface customToolbar {
    fun initToolBar()
}