package com.isadev.declarapp

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class PreviousActivity : AppCompatActivity(), customToolbar {

    lateinit var scrollPrevious: ScrollView

    lateinit var tvTotal: TextView
    lateinit var tvMonth: TextView
    lateinit var tvStatus: TextView
    lateinit var tvCash: TextView
    lateinit var tvIsr: TextView
    lateinit var tvIntermediary: TextView
    lateinit var tvTotalSat: TextView

    private var isr: Double = 0.0
    private var total: Double = 0.0
    private var intermediary: Double = 0.0
    private var month: String = ""
    private var year: Int = 0

    private var status: String = "pendiente"

    private var totalSat: Double = 0.0

    lateinit var cbPayment: CheckBox
    lateinit var btnSave: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous)
        initToolBar()
        initObjects()
        generateDeclaration()
    }

    private fun initObjects() {
        scrollPrevious = findViewById(R.id.previous_activity_scroll)

        tvTotal = findViewById(R.id.previous_activity_tv_total_value)
        tvMonth = findViewById(R.id.previous_activity_tv_month_value)
        tvStatus = findViewById(R.id.previous_activity_tv_status_value)
        tvCash = findViewById(R.id.previous_activity_tv_cash_value)
        tvIsr = findViewById(R.id.previous_activity_tv_isr_value)
        tvIntermediary = findViewById(R.id.previous_activity_tv_intermediary_value)
        tvTotalSat = findViewById(R.id.previous_activity_total_sat)

        cbPayment = findViewById(R.id.previous_activity_cb_payment)
        btnSave = findViewById(R.id.previous_activity_btn_save)

        isr = 0.0
        total = 0.0
        intermediary = 0.0
        month = ""
        year = 0

        cbPayment.isChecked = false
        status = "pendiente"

        totalSat = 0.0

        btnSave.setOnClickListener {
            saveDeclaration()
        }

        cbPayment.setOnClickListener {
            if (cbPayment.isChecked) {
                status = "pagado"
                tvStatus.text = status
            } else {
                status = "pendiente"
                tvStatus.text = status
            }
        }

    }
    private fun generateDeclaration() {
        val data = intent.extras
        isr = data!!.getDouble("isr")
        total = data?.getDouble("total") ?: 0.0
        intermediary = data!!.getDouble("income_without_vat")
        month =  Utility.getMonth(data!!.getInt("month"))
        year = data!!.getString("year").toString().toInt()

        val totalCash = total - intermediary
        val totalMonth = totalCash + intermediary

        val isrCaused = totalMonth * 0.021

        val isrInCharge = isrCaused - isr
        val ivaInCharge = totalCash * 0.08

        tvTotal.text = "$ $total"
        tvMonth.text = "$month"
        tvCash.text = "$ ${Utility.roundNumber(totalCash)}"
        tvIsr.text = "$ $isr"
        tvIntermediary.text = "$ $intermediary"

        totalSat = Utility.roundNumber(isrInCharge + ivaInCharge)
        tvTotalSat.text = "Pagar al sat(aprox): $ $totalSat"
    }
    private fun saveDeclaration() {
        var dbDeclarations = FirebaseFirestore.getInstance()
        dbDeclarations.collection("declarations/$year/$month").document().set(
            hashMapOf(
                "isr" to isr,
                "total" to total,
                "intermediary" to intermediary,
                "month" to month,
                "year" to year,
                "status" to status,
                "total_sat" to totalSat
            )
        )
        Snackbar.make(scrollPrevious, "Declaración guardada con éxito", Snackbar.LENGTH_LONG).setAction("OK") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }.show()
    }

    override fun initToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_previous)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "DECLARACIÓN MENSUAL"
    }
}