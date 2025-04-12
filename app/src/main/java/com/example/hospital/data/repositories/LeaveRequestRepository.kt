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
                "reason" to request.reason,
                "status" to request.status
            )
            db.collection("leave_requests").add(data).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getDoctorLeaveRequests(doctorId: String): List<LeaveRequest> {
        return try {
            val snapshot = db.collection("leave_requests")
                .whereEqualTo("doctor_id", doctorId.trim()) // 🔥 IMPORTANT
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                val data = doc.data
                LeaveRequest(
                    doctorId = data?.get("doctor_id").toString(),
                    startDate = data?.get("start_date").toString(),
                    endDate = data?.get("end_date").toString(),
                    reason = data?.get("reason").toString(),
                    status = data?.get("status").toString()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


}