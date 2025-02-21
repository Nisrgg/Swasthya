package com.example.hospital.data.models

data class Doctor(
    val id: String = "",
    val name: String = "",
    val specialization: String = "",
    val availableSlots: Map<String, List<String>> = emptyMap(), // ✅ Stores date-wise slots
    val dateAdded: Long = System.currentTimeMillis()
)