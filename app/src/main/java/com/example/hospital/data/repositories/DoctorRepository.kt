package com.example.hospital.data.repositories

import com.example.hospital.data.models.Doctor
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DoctorRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllDoctors(): List<Doctor> {
        return try {
            val snapshot = db.collection("doctors").get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Doctor::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

