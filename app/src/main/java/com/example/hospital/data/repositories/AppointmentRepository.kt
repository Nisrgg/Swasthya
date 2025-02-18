package com.example.hospital.data.repositories

import com.example.hospital.data.models.Appointment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentRepository {
    private val db = FirebaseFirestore.getInstance()

    fun bookAppointment(
        doctorId: String,
        doctorName: String,
        userName: String,
        appointmentTime: Timestamp,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val newAppointmentRef = db.collection("appointments").document()

        val appointment = Appointment(
            id = newAppointmentRef.id,
            doctorId = doctorId,
            doctorName = doctorName,
            userName = userName,
            appointmentTime = appointmentTime
        )

        newAppointmentRef.set(appointment)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // ✅ Fetch all appointments instead of filtering by userId
    fun getAppointments(onSuccess: (List<Appointment>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("appointments")
            .get()
            .addOnSuccessListener { documents ->
                val appointments = documents.mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}

