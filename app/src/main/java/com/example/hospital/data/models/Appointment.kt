package com.example.hospital.data.models

import com.google.firebase.Timestamp

data class Appointment(
    val id: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val userId: String = "",
    val userName: String = "",
    val appointmentTime: Timestamp? = null,
    val symptoms: String = "",              // ✅ New field for symptoms
    val status: String = "pending",         // ✅ Status: pending, confirmed, completed, canceled
    val prescriptionUrl: String? = null     // ✅ Link to prescription if uploaded
)
