package com.example.hospital.data.models

data class DoctorUnavailability(
    val doctorId: String = "",
    val unavailableDates: List<String> = emptyList()  // ✅ List of dates doctor is unavailable
)
