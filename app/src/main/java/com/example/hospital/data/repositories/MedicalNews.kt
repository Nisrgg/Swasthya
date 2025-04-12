package com.example.hospital.data.repositories

import com.example.hospital.data.models.MedicalNews
import com.google.firebase.firestore.FirebaseFirestore

class MedicalNews {
    private val db = FirebaseFirestore.getInstance()

    // ✅ Add Medical News
    fun addMedicalNews(
        title: String,
        content: String,
        author: String,
        sourceUrl: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val newNewsRef = db.collection("medical_news").document()

        val news = MedicalNews(
            id = newNewsRef.id,
            title = title,
            content = content,
            author = author,
            sourceUrl = sourceUrl
        )

        newNewsRef.set(news)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // ✅ Get Medical News
    fun getMedicalNews(onSuccess: (List<MedicalNews>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("medical_news")
            .get()
            .addOnSuccessListener { documents ->
                val newsList = documents.mapNotNull { it.toObject(MedicalNews::class.java) }
                onSuccess(newsList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
