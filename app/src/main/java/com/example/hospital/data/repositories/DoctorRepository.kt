package com.example.hospital.data.repositories

import com.example.hospital.data.models.Doctor
import com.google.firebase.firestore.FirebaseFirestore

class DoctorRepository {
    private val db = FirebaseFirestore.getInstance()

//    // ✅ Add Doctor
//    fun addDoctor(
//        name: String,
//        specialization: String,
//        availableSlots: Map<String, List<String>>,
//        experience: Int,
//        education: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val newDoctorRef = db.collection("doctors").document()
//
//        val doctor = Doctor(
//            id = newDoctorRef.id,
//            name = name,
//            specialization = specialization,
//            availableSlots = availableSlots,
//            experience = experience,
//            education = education
//        )
//
//        newDoctorRef.set(doctor)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { onFailure(it) }
//    }
//
    // ✅ Remove Doctor
    fun removeDoctor(doctorId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("doctors").document(doctorId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // ✅ Get All Doctors
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

//    // ✅ Update Doctor Availability
//    fun updateDoctorAvailability(
//        doctorId: String,
//        availableSlots: Map<String, List<String>>,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        db.collection("doctors").document(doctorId)
//            .update("availableSlots", availableSlots)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { onFailure(it) }
//    }
}