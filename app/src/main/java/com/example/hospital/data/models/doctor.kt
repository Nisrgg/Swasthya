package com.example.hospital.data.models

data class Doctor(
    val id: String = "",
    val name: String = "",
    val specialization: String = "",
    val availableSlots: List<String> = emptyList(),
    val dateAdded: Long = System.currentTimeMillis()  // Store as timestamp
)