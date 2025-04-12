package com.example.hospital.data.models

data class Doctor(
    var id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val age: Int = 0,
    val gender: String = "",
    val education: String = "",
    val specialization: String = "",
    val experience: Int = 0,
    val available_slots: Map<String, List<String>> = emptyMap()
)
