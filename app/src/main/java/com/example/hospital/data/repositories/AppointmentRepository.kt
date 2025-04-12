package com.example.hospital.data.repositories

import com.example.hospital.data.models.Appointment
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentRepository {
    private val db = FirebaseFirestore.getInstance()

    fun bookAppointment(appointment: Appointment, onResult: (Boolean) -> Unit) {
        db.collection("appointments")
            .add(appointment)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getAppointmentsForPatient(patientId: String, callback: (List<Appointment>) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("appointments")
            .whereEqualTo("patient_id", patientId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val list = querySnapshot.documents.mapNotNull { it.toObject(Appointment::class.java) }
                callback(list)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }


}

