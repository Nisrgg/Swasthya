package com.example.hospital.data.repositories

import android.util.Log
import com.example.hospital.data.models.Doctor
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
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
            Log.e("DoctorRepository", "Failed to fetch all doctors", e)
            emptyList()
        }
    }

    suspend fun getDoctorsByIds(ids: List<String>): List<Doctor> {
        if (ids.isEmpty()) return emptyList()
        return try {
            val snapshot = db.collection("doctors")
                .whereIn(FieldPath.documentId(), ids)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Doctor::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Failed to fetch doctors by IDs", e)
            emptyList()
        }
    }
    fun checkIfDoctor(uid: String, onResult: (Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection("doctors").document(uid).get()
            .addOnSuccessListener { document ->
                onResult(document.exists())
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

}