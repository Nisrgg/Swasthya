package com.example.hospital.data.repositories

import com.example.hospital.data.models.Doctor
import com.google.firebase.firestore.FirebaseFirestore

class DoctorRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addDoctor(name: String, specialization: String, availableSlots: List<String>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val newDoctorRef = db.collection("doctors").document()

        val doctor = Doctor(
            id = newDoctorRef.id,
            name = name,
            specialization = specialization,
            availableSlots = availableSlots
        )

        newDoctorRef.set(doctor)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getDoctors(onResult: (List<Doctor>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("doctors").get()
            .addOnSuccessListener { snapshot ->
                val doctors = snapshot.toObjects(Doctor::class.java)
                onResult(doctors)
            }
            .addOnFailureListener { onFailure(it) }
    }
}