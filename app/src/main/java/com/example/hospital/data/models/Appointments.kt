package com.example.hospital.data.models

import com.google.firebase.Timestamp

data class Appointment(
    val appointment_date: Timestamp = Timestamp.now(),
    val doctor_id: String = "",
    val patient_id: String = "",
    val prescription: String = "",
    val slot: String = "",
    val status: String = "",
    val patient_name: String = "" // Add this!
)

data class AppointmentWithId(
    val id: String,
    val data: Appointment
)