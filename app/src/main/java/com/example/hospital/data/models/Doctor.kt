package com.example.hospital.data.models

data class Doctor(
    val id: String = "",
    val name: String = "",
    val specialization: String = "",
    val availableSlots: Map<String, List<String>> = emptyMap(),
    val dateAdded: Long = System.currentTimeMillis(),
    val experience: Int = 0,                  // ✅ New field for experience in years
    val education: String = "",               // ✅ New field for qualification
    val rating: Float = 0.0f,                  // ✅ Doctor rating
    val isAvailable: Boolean = true            // ✅ Is doctor currently available?
)
