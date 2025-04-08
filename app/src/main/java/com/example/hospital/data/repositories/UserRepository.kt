package com.example.hospital.data.repositories

import com.example.hospital.data.models.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun saveUserProfile(uid: String, profile: UserProfile, onResult: (Boolean) -> Unit) {
        usersCollection.document(uid).set(profile)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getUserProfile(uid: String, onResult: (UserProfile?) -> Unit) {
        usersCollection.document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profile = document.toObject(UserProfile::class.java)
                    onResult(profile)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun doesUserProfileExist(uid: String, onResult: (Boolean) -> Unit) {
        usersCollection.document(uid).get()
            .addOnSuccessListener { document ->
                onResult(document.exists())
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
}