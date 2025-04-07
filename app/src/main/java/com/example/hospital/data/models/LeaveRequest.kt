package com.example.hospital.data.models

data class LeaveRequest(
    val doctorId: String,
    val startDate: String,
    val endDate: String,
    val reason: String
)