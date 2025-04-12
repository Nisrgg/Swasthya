package com.example.hospital.data.repositories

import com.example.hospital.data.models.LeaveRequest
import com.google.firebase.firestore.FirebaseFirestore

class LeaveRequest {
    private val db = FirebaseFirestore.getInstance()

    // ✅ Submit Leave Request
    fun submitLeaveRequest(
        doctorId: String,
        doctorName: String,
        leaveDate: String,
        reason: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val newLeaveRequestRef = db.collection("leave_requests").document()

        val leaveRequest = LeaveRequest(
            id = newLeaveRequestRef.id,
            doctorId = doctorId,
            doctorName = doctorName,
            leaveDate = leaveDate,
            reason = reason
        )

        newLeaveRequestRef.set(leaveRequest)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // ✅ Get Leave Requests
    fun getLeaveRequests(onSuccess: (List<LeaveRequest>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("leave_requests")
            .get()
            .addOnSuccessListener { documents ->
                val leaveRequests = documents.mapNotNull { it.toObject(LeaveRequest::class.java) }
                onSuccess(leaveRequests)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // ✅ Update Leave Status
    fun updateLeaveStatus(
        leaveRequestId: String,
        status: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("leave_requests").document(leaveRequestId)
            .update("status", status)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
