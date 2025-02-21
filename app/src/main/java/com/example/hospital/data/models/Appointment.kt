package com.example.hospital.data.models

import com.google.firebase.Timestamp

data class Appointment(
    val id: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val userId: String = "",  // ✅ Add this field
    val userName: String = "",
    val appointmentTime: Timestamp? = null
)