package com.isadev.declarapp

data class Declaration(
    var isr: Double?=null,
    var total: Double?=null,
    var intermediary: Double?=null,
    var month: Int?=null,
    var year: Int?=null,
    var status: String?=null,
    var total_sat: Double?=null,
    var cash: Double?=null
)
