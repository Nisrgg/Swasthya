package com.example.hospital.data.models

import com.google.firebase.Timestamp

data class Appointment(
    val id: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val userName: String = "",
    val appointmentTime: Timestamp = Timestamp.now()
) {
    constructor() : this("", "", "", "", Timestamp.now()) // No-arg constructor for Firestore
}
