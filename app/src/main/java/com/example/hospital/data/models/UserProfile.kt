package com.example.hospital.data.models

//data class UserProfile(
//    val userId: String = "",
//    val name: String = "",
//    val age: Int = 0,
//    val height: Float = 0f,
//    val weight: Float = 0f,
//    val bloodGroup: String = "",
//    val hasFamilyMedicalHistory: Boolean = false,
//    val familyMedicalHistoryDetails: String = "",
//    val hasAllergies: Boolean = false,
//    val allergyDetails: String = "",
//    val hasOngoingMedications: Boolean = false,
//    val medicationDetails: String = ""
//)


data class UserProfile(
    val user_id: String = "",
    val name: String = "",
    val age: String = "",
    val height: String = "",
    val weight: String = "",
    val blood_group: String = "",
    val family_medical_history: String = "",
    val allergies: String = "",
    val ongoing_medications: String = ""
)
