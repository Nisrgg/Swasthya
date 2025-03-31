package com.example.hospital.data.repositories

import com.example.hospital.data.models.Appointment
import com.example.hospital.utils.FirebaseUtils
import com.google.firebase.Timestamp

class AppointmentRepository {

    // ✅ Book Appointment
    fun bookAppointment(
        doctorId: String,
        doctorName: String,
        userId: String,
        userName: String,
        appointmentTime: Timestamp,
        symptoms: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val newAppointmentRef = FirebaseUtils.db.collection("appointments").document()

        val appointment = Appointment(
            id = newAppointmentRef.id,
            doctorId = doctorId,
            doctorName = doctorName,
            userId = userId,
            userName = userName,
            appointmentTime = appointmentTime,
            symptoms = symptoms
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

    // ✅ Get User Appointments
    fun getAppointments(
        userId: String,
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseUtils.db.collection("appointments")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val appointments = documents.mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // ✅ Get All Appointments
    fun getAllAppointments(onSuccess: (List<Appointment>) -> Unit, onFailure: (Exception) -> Unit) {
        FirebaseUtils.db.collection("appointments")
            .get()
            .addOnSuccessListener { documents ->
                val appointments = documents.mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // ✅ Update Appointment Status
    fun updateAppointmentStatus(
        appointmentId: String,
        status: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseUtils.db.collection("appointments").document(appointmentId)
            .update("status", status)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}