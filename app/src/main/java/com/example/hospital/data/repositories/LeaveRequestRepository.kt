package com.example.hospital.data.repositories

import com.example.hospital.data.models.LeaveRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LeaveRequestRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun submitLeaveRequest(request: LeaveRequest): Boolean {
        return try {
            val data = hashMapOf(
                "doctor_id" to request.doctorId,
                "start_date" to request.startDate,
                "end_date" to request.endDate,
                "reason" to request.reason
            )
            db.collection("leave_requests").add(data).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}