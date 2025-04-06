package com.example.hospital.data.repositories

import com.example.hospital.data.models.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun saveUserProfile(profile: UserProfile, onResult: (Boolean) -> Unit) {
        db.collection("users").document(profile.user_id)
            .set(profile)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun fetchUserProfile(userId: String, onResult: (UserProfile?) -> Unit) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    onResult(doc.toObject(UserProfile::class.java))
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}
