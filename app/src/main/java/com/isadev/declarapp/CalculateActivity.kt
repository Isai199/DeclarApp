package com.isadev.declarapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class CalculateActivity : AppCompatActivity(), customToolbar, TextWatcher {
    lateinit var btnCalculate: AppCompatButton
    lateinit var spnWeekends: Spinner

    lateinit var lyFirstWeek: LinearLayout
    lateinit var lySecondWeek: LinearLayout
    lateinit var lyThirdWeek: LinearLayout
    lateinit var lyFourthWeek: LinearLayout
    lateinit var lyFifthWeek: LinearLayout

    lateinit var etFirstWeek: EditText
    lateinit var etSecondWeek: EditText
    lateinit var etThirdWeek: EditText
    lateinit var etFourthWeek: EditText
    lateinit var etFifthWeek: EditText

    lateinit var rlUploadFile: RelativeLayout
    lateinit var viewTextarea: View
    private var REQUEST_CODE_FILE = 1

    lateinit var tvTotal: TextView
    var total: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)
        initToolBar()

        initObjects()
    }

    override fun initToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_calculate)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "DECLARACIÓN MENSUAL"
    }

    private fun initObjects() {
        lyFirstWeek = findViewById(R.id.calculate_activity_ly_first_week)
        lySecondWeek = findViewById(R.id.calculate_activity_ly_second_week)
        lyThirdWeek = findViewById(R.id.calculate_activity_ly_third_week)
        lyFourthWeek = findViewById(R.id.calculate_activity_ly_fourth_week)
        lyFifthWeek = findViewById(R.id.calculate_activity_ly_fifth_week)

        etFirstWeek = findViewById<EditText>(R.id.calculate_activity_et_first_week)
        etSecondWeek = findViewById(R.id.calculate_activity_et_second_week)
        etThirdWeek = findViewById(R.id.calculate_activity_et_third_week)
        etFourthWeek = findViewById(R.id.calculate_activity_et_fourth_week)
        etFifthWeek = findViewById(R.id.calculate_activity_et_fifth_week)

        etFirstWeek.setText("")
        etSecondWeek.setText("")
        etThirdWeek.setText("")
        etFourthWeek.setText("")
        etFifthWeek.setText("")

        initUploadFile()

        tvTotal = findViewById(R.id.calculate_activity_total_text)

        etFirstWeek.addTextChangedListener(this)
        etSecondWeek.addTextChangedListener(this)
        etThirdWeek.addTextChangedListener(this)
        etFourthWeek.addTextChangedListener(this)
        etFifthWeek.addTextChangedListener(this)

        initSpinner()

        btnCalculate = findViewById(R.id.calculate_activity_btn_calculate)
        btnCalculate.setOnClickListener {
            val intent = Intent(this, PreviousActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initSpinner() {
        spnWeekends = findViewById(R.id.calculate_activity_spinner_weekends)
        var adapter = ArrayAdapter.createFromResource(this, R.array.weekes, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnWeekends.adapter = adapter

        spnWeekends.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        lyFirstWeek.visibility = View.VISIBLE
                        lySecondWeek.visibility = View.GONE
                        lyThirdWeek.visibility = View.GONE
                        lyFourthWeek.visibility = View.GONE
                        lyFifthWeek.visibility = View.GONE
                    }
                    1 -> {
                        lyFirstWeek.visibility = View.VISIBLE
                        lySecondWeek.visibility = View.VISIBLE
                        lyThirdWeek.visibility = View.GONE
                        lyFourthWeek.visibility = View.GONE
                        lyFifthWeek.visibility = View.GONE
                    }
                    2 -> {
                        lyFirstWeek.visibility = View.VISIBLE
                        lySecondWeek.visibility = View.VISIBLE
                        lyThirdWeek.visibility = View.VISIBLE
                        lyFourthWeek.visibility = View.GONE
                        lyFifthWeek.visibility = View.GONE
                    }
                    3 -> {
                        lyFirstWeek.visibility = View.VISIBLE
                        lySecondWeek.visibility = View.VISIBLE
                        lyThirdWeek.visibility = View.VISIBLE
                        lyFourthWeek.visibility = View.VISIBLE
                        lyFifthWeek.visibility = View.GONE
                    }
                    4 -> {
                        lyFirstWeek.visibility = View.VISIBLE
                        lySecondWeek.visibility = View.VISIBLE
                        lyThirdWeek.visibility = View.VISIBLE
                        lyFourthWeek.visibility = View.VISIBLE
                        lyFifthWeek.visibility = View.VISIBLE
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        calculateTotal()
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }
    override fun afterTextChanged(s: Editable?) {  }

    private fun initUploadFile() {
        rlUploadFile = findViewById(R.id.calculate_activity_rl_upload_file)
        viewTextarea = findViewById(R.id.calculate_activity_view_textarea)

        viewTextarea.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "text/xml"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_FILE && resultCode == RESULT_OK) {
            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val uri = data.clipData!!.getItemAt(i).uri
                    readXml(uri)
                }
            } else if (data?.data != null) {
                val uri = data.data
                readXml(uri)
            }

        }
    }

    private fun readXml(uri: Uri?) {
        val inputStream = contentResolver.openInputStream(uri!!)
        if (inputStream != null) {

            val factory = DocumentBuilderFactory.newInstance()
            factory.isNamespaceAware = true

            val docBuilder = factory.newDocumentBuilder()
            val doc = docBuilder.parse(inputStream)
            doc.documentElement.normalize()

            val nodeList = doc.getElementsByTagNameNS("*","ImpRetenidos")

            for (i in 0 until nodeList.length) {
                val element = nodeList.item(i) as? Element ?: continue
                val total = element.getAttribute("TipoPagoRet")
                println("TipoPagoRet: $total")
            }
        } else {
            println("Error al abrir el archivo XML")
        }
    }

    private fun calculateTotal() {
        var auxTotal: Double = 0.0
        if (lyFirstWeek.visibility == View.VISIBLE && etFirstWeek.text.toString() != "") {
            auxTotal += etFirstWeek.text.toString().toDouble()
        }
        if (lySecondWeek.visibility == View.VISIBLE && etSecondWeek.text.toString() != "") {
            auxTotal += etSecondWeek.text.toString().toDouble()
        }
        if (lyThirdWeek.visibility == View.VISIBLE && etThirdWeek.text.toString() != "") {
            auxTotal += etThirdWeek.text.toString().toDouble()
        }
        if (lyFourthWeek.visibility == View.VISIBLE && etFourthWeek.text.toString() != "") {
            auxTotal += etFourthWeek.text.toString().toDouble()
        }
        if (lyFifthWeek.visibility == View.VISIBLE && etFifthWeek.text.toString() != "") {
            auxTotal += etFifthWeek.text.toString().toDouble()
        }
        total = roundNumber(auxTotal)
        tvTotal.text = "Total efectivo: $total"
    }

    private fun roundNumber(number: Double): Double {
        var decimals: String = ""
        decimals = number.toString().substring(number.toString().indexOf(".") + 1)
        if (decimals.length > 2) {
            return number.toString().substring(0, number.toString().indexOf(".") + 3).toDouble()
        } else {
            return number
        }
    }
}
