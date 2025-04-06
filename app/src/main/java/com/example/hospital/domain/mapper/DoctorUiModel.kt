package com.example.hospital.domain.mapper

import com.example.hospital.data.models.Doctor

data class DoctorUiModel(
    val name: String,
    val specialization: String,
    val experience: Int
)

fun Doctor.toUiModel(): DoctorUiModel {
    return DoctorUiModel(
        name = this.name,
        specialization = this.specialization,
        experience = this.experience
    )
}
