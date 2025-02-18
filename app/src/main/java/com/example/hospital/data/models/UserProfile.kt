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
    val medicalConditions: String = ""
)
