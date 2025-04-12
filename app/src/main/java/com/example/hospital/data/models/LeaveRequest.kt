package com.example.hospital.data.models

data class LeaveRequest(
    val id: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val leaveDate: String = "",
    val reason: String = "",
    val status: String = "pending"              // ✅ Status: pending, approved, rejected
)
