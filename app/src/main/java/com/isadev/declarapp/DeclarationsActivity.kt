package com.isadev.declarapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Query

class DeclarationsActivity : AppCompatActivity(), customToolbar, DeclarationsAdapter.OnItemClickListener {
    lateinit var clMain: ConstraintLayout
    lateinit var spWeekends: Spinner
    lateinit var recyclerViewDeclarations: RecyclerView
    lateinit var myAdapter: DeclarationsAdapter

    lateinit var popup: ConstraintLayout

    lateinit var years: Set<String>

    lateinit var declarationsArray : ArrayList<Declaration>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declarations)
        initToolBar()

        clMain = findViewById(R.id.cl_declarations_activity_main)
        spWeekends = findViewById(R.id.declarations_activity_spinner_months)
        popup = findViewById(R.id.declarations_activity_popup)

        spWeekends.isEnabled = false
        popup.visibility = View.GONE

        recyclerViewDeclarations = findViewById(R.id.rvDeclarations)
        recyclerViewDeclarations.layoutManager = LinearLayoutManager(this)
        recyclerViewDeclarations.setHasFixedSize(true)

        declarationsArray = ArrayList()
        myAdapter = DeclarationsAdapter(declarationsArray, this)
        recyclerViewDeclarations.adapter = myAdapter

        years = emptySet()
        loadRecyclerView("year", Query.Direction.DESCENDING)
    }
    private fun loadRecyclerView(field: String, order: Query.Direction, year: Int? = null) {
        declarationsArray.clear()
        BDdeclarations("declarations").getDeclarations(field, order, year) {
            if (years.isEmpty()) {
                for (declaration in it)
                    if (declaration.year != null) years += setOf(declaration.year.toString())
                initSpinner()
            }
            declarationsArray.addAll(it)
            myAdapter.notifyDataSetChanged()
        }
    }
    private fun initSpinner() {
        spWeekends.isEnabled = true
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spWeekends.adapter = adapter

        spWeekends.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (years.isNotEmpty())
                    loadRecyclerView("year", Query.Direction.DESCENDING, spWeekends.selectedItem.toString().toInt())
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {  }
        }
    }

    override fun initToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_declarations)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "DECLARACIONES"
    }

    override fun showMessage(message: String) {
        Snackbar.make(clMain, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showPopup(callback: () -> Unit) {
        val btnDelete = findViewById<AppCompatButton>(R.id.declarations_activity_popup_btn_delete)
        val btnCancel = findViewById<AppCompatButton>(R.id.declarations_activity_popup_btn_cancel)
        popup.visibility = View.VISIBLE

        btnDelete.setOnClickListener {
            popup.visibility = View.GONE
            callback()
        }

        btnCancel.setOnClickListener {
            popup.visibility = View.GONE
        }
    }
}