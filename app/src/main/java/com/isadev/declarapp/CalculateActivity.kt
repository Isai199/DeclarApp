package com.isadev.declarapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class CalculateActivity : AppCompatActivity(), customToolbar, TextWatcher {
    lateinit var tvTitle: TextView
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
    lateinit var lyShowFile: LinearLayout
    lateinit var uploadIcon: ImageView
    private var REQUEST_CODE_FILE = 1

    private var month = mutableListOf<Int>()
    private var year = mutableListOf<String>()
    private var isr: Double = 0.0
    private var income_without_vat: Double = 0.0

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
        tvTitle = findViewById(R.id.calculate_activity_title)
        tvTitle.text = "GANANCIAS DEL MES"

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

        uploadIcon = findViewById(R.id.calculate_activity_iv_upload_file)
        uploadIcon.setImageResource(R.drawable.plus_circle_outline)

        initUploadFile()

        tvTotal = findViewById(R.id.calculate_activity_total_text)

        etFirstWeek.addTextChangedListener(this)
        etSecondWeek.addTextChangedListener(this)
        etThirdWeek.addTextChangedListener(this)
        etFourthWeek.addTextChangedListener(this)
        etFifthWeek.addTextChangedListener(this)

        initSpinner()

        month.clear()
        year.clear()

        btnCalculate = findViewById(R.id.calculate_activity_btn_calculate)
        btnCalculate.isEnabled = false

        btnCalculate.setOnClickListener {
            generateData()
        }
    }
    private fun generateData() {
        println("DataObtained isr: $isr, total: $total, income_without_vat: $income_without_vat, month: $month, year: $year")
        val intent = Intent(this, PreviousActivity::class.java)
        intent.putExtra("isr", isr)
        intent.putExtra("total", total)
        intent.putExtra("income_without_vat", income_without_vat)
        intent.putExtra("month", month[0])
        intent.putExtra("year", year[0])
        startActivity(intent)
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
        rlUploadFile.tag = "upload"

        rlUploadFile.setOnClickListener { it ->
            if (it.tag == "upload") {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "text/xml"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                startActivityForResult(intent, 1)
            } else {
                deleteFiles()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        income_without_vat = 0.0
        isr = 0.0
        month.clear()
        year.clear()

        if (requestCode == REQUEST_CODE_FILE && resultCode == RESULT_OK) {
            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val uri = data.clipData!!.getItemAt(i).uri
                    readXml(uri)
                    showFile()
                }
                if (areXmlsValid()){
                    updateTitleDate()
                    rlUploadFile.tag = "delete"
                    uploadIcon.setImageResource(R.drawable.trash_can)
                    btnCalculate.isEnabled = true
                } else {
                    deleteFiles()
                    Toast.makeText(this, "Los archivos XML no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else if (data?.data != null) {
                val uri = data.data
                readXml(uri)
                updateTitleDate()
                showFile()
                rlUploadFile.tag = "delete"
                uploadIcon.setImageResource(R.drawable.trash_can)
                btnCalculate.isEnabled = true
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

            val nodeTotalList = doc.getElementsByTagNameNS("*","Totales")
            for (i in 0 until nodeTotalList.length) {
                val element = nodeTotalList.item(i) as? Element ?: continue
                val total = element.getAttribute("MontoTotOperacion")
                income_without_vat += total.toDouble()
            }

            val nodeISRList = doc.getElementsByTagNameNS("*","ServiciosPlataformasTecnologicas")
            for (i in 0 until nodeISRList.length) {
                val element = nodeISRList.item(i) as? Element ?: continue
                val monto = element.getAttribute("TotalISRRetenido")
                isr += monto.toDouble()
                println("ISR: $isr")
            }

            val nodeMonthList = doc.getElementsByTagNameNS("*","Periodo")
            for (i in 0 until nodeMonthList.length) {
                val element = nodeMonthList.item(i) as? Element ?: continue
                month.add(element.getAttribute("MesFin").toInt())
                year.add(element.getAttribute("Ejercicio"))
            }
            println("Month: $month")
        } else {
            println("Error al abrir el archivo XML")
        }
    }
    private fun areXmlsValid(): Boolean {
        val allMonthSame = month.isNotEmpty() && month.all { it == month[0] }
        val allYearSame = year.isNotEmpty() && year.all { it == year[0] }
        return allMonthSame && allYearSame
    }
    private fun showFile() {
        lyShowFile = findViewById(R.id.calculate_activity_ly_upload_file)
        val newImageView = ImageView(this)
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64f, resources.displayMetrics).toInt()
        val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64f, resources.displayMetrics).toInt()
        newImageView.layoutParams = LinearLayout.LayoutParams(width, height)
        newImageView.setImageResource(R.drawable.file_document_outline)
        lyShowFile.addView(newImageView)
    }
    private fun deleteFiles() {
        month.clear()
        year.clear()
        income_without_vat = 0.0
        isr = 0.0
        tvTitle.text = "GANANCIAS DEL MES"

        lyShowFile.removeAllViews()
        uploadIcon.setImageResource(R.drawable.plus_circle_outline)

        rlUploadFile.tag = "upload"
        btnCalculate.isEnabled = false
    }

    private fun updateTitleDate() {
        tvTitle.text = "GANANCIAS DEL MES DE ${Utility.getMonth(month[0])} DEL AÑO ${year[0]}"
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

        total = Utility.roundNumber(auxTotal)
        tvTotal.text = "Total efectivo: $total"
    }
}
