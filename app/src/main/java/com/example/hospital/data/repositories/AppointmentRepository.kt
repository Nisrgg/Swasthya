package com.example.hospital.data.repositories

import com.example.hospital.data.models.Appointment
import com.example.hospital.utils.FirebaseUtils
import com.google.firebase.Timestamp

class AppointmentRepository {

    fun bookAppointment(
        doctorId: String,
        doctorName: String,
        userId: String,  // ✅ Ensure userId is passed
        userName: String,
        appointmentTime: Timestamp,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val newAppointmentRef = FirebaseUtils.db.collection("appointments").document()

        val appointment = Appointment(
            id = newAppointmentRef.id,
            doctorId = doctorId,
            doctorName = doctorName,
            userId = userId,  // ✅ Store userId
            userName = userName,
            appointmentTime = appointmentTime
        )

        newAppointmentRef.set(appointment)
            .addOnSuccessListener {
                println("✅ Appointment booked successfully for userId: $userId")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                println("❌ Error booking appointment: ${exception.message}")
                onFailure(exception)
            }
    }

    fun getAppointments(
        userId: String,
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseUtils.db.collection("appointments")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    println("⚠️ No appointments found for userId: $userId")
                } else {
                    println("✅ Appointments fetched for userId: $userId")
                    documents.forEach { println(it.data) }
                }

                val appointments = documents.mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception ->
                println("❌ Error fetching appointments: ${exception.message}")
                onFailure(exception)
            }
    }
}