package com.isadev.declarapp

import android.widget.LinearLayout

object Utility {
    internal fun roundNumber(number: Double): Double {
        var decimals: String = ""
        decimals = number.toString().substring(number.toString().indexOf(".") + 1)
        if (decimals.length > 2) {
            return number.toString().substring(0, number.toString().indexOf(".") + 3).toDouble()
        } else {
            return number
        }
    }

    fun getMonth(month: Int): String {
        val listMonth = arrayOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
            "Octubre", "Noviembre", "Diciembre")

        return listMonth[month - 1]
    }

    fun setHeightLinearLayout(ly: LinearLayout, value: Int) {
        val params: LinearLayout.LayoutParams = ly.layoutParams as LinearLayout.LayoutParams
        params.height = value
        ly.layoutParams = params
    }
}