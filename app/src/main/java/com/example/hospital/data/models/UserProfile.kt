package com.example.hospital.data.models

import java.io.Serializable

data class UserProfile(
    val name: String = "",
    val age: Int = 0,
    val height: Float = 0f,
    val weight: Float = 0f,
    val blood_group: String = "",
    val has_family_history: Boolean = false,
    val family_history_details: String = "",
    val has_allergies: Boolean = false,
    val allergy_details: String = "",
    val has_medications: Boolean = false,
    val medication_details: String = ""
) : Serializable

