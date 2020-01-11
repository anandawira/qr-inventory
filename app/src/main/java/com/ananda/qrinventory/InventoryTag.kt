package com.ananda.qrinventory

data class InventoryTag(
    val model: String,
    val monogram: String,
    val spec: String,
    val lotNumber: String,
    val quantity: String,
    val remarks: String
)