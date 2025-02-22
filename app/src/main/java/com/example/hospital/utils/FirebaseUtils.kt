package com.example.hospital.utils

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtils {
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
}