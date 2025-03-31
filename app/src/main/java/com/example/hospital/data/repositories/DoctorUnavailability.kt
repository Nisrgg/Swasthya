package com.example.hospital.data.repositories

import com.example.hospital.data.models.DoctorUnavailability
import com.google.firebase.firestore.FirebaseFirestore

class DoctorUnavailability {
    private val db = FirebaseFirestore.getInstance()

    // ✅ Add/Update Unavailable Dates
    fun updateUnavailableDates(
        doctorId: String,
        unavailableDates: List<String>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val ref = db.collection("doctor_unavailability").document(doctorId)
        val unavailability = DoctorUnavailability(doctorId, unavailableDates)

        ref.set(unavailability)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // ✅ Get Doctor Unavailability
    fun getUnavailableDates(
        doctorId: String,
        onSuccess: (List<String>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("doctor_unavailability").document(doctorId)
            .get()
            .addOnSuccessListener { document ->
                val unavailability = document.toObject(DoctorUnavailability::class.java)
                onSuccess(unavailability?.unavailableDates ?: emptyList())
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
