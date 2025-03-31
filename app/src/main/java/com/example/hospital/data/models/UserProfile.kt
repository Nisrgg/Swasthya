package com.example.hospital.data.models

data class UserProfile(
    val userId: String = "",
    val fullName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val address: String = "",
    val bloodGroup: String = "",
    val emergencyContact: String = "",
    val medicalConditions: String = "",
    val allergies: String = "",               // ✅ New field for allergies
    val pastSurgeries: String = "",           // ✅ New field for past surgeries
    val medications: String = ""              // ✅ New field for ongoing medications
)