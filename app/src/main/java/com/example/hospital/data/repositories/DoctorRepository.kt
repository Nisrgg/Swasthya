package com.example.hospital.data.repositories

import com.example.hospital.data.models.Doctor
import com.google.firebase.firestore.FirebaseFirestore

class DoctorRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addDoctor(
        name: String,
        specialization: String,
        availableSlots: Map<String, List<String>>, // ✅ Accepts date-wise slots
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val newDoctorRef = db.collection("doctors").document()

        val doctor = Doctor(
            id = newDoctorRef.id,
            name = name,
            specialization = specialization,
            availableSlots = availableSlots // ✅ Stores in Firestore
        )

        newDoctorRef.set(doctor)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun removeDoctor(doctorId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("doctors").document(doctorId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getDoctors(onSuccess: (List<Doctor>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("doctors")
            .get()
            .addOnSuccessListener { documents ->
                val doctors = documents.mapNotNull { it.toObject(Doctor::class.java) }
                onSuccess(doctors)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}