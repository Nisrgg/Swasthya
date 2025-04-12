package com.example.hospital.data.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.models.LeaveRequest
import com.example.hospital.data.repositories.LeaveRequestRepository
import kotlinx.coroutines.launch
import android.util.Log


class LeaveRequestViewModel(
    private val repository: LeaveRequestRepository = LeaveRequestRepository()
) : ViewModel() {

    var activeLeaveExists by mutableStateOf(false)
    var leaveHistory by mutableStateOf(listOf<LeaveRequest>())
        private set

    var startDate by mutableStateOf("")
    var endDate by mutableStateOf("")
    var reason by mutableStateOf("")

    var isSubmitting by mutableStateOf(false)
    var submitSuccess by mutableStateOf<Boolean?>(null)

    fun submitLeaveRequest(doctorId: String) {
        if (startDate.isBlank() || endDate.isBlank() || reason.isBlank()) return

        isSubmitting = true
        submitSuccess = null

        viewModelScope.launch {
            val request = LeaveRequest(
                doctorId = doctorId,
                startDate = startDate,
                endDate = endDate,
                reason = reason
            )
            val result = repository.submitLeaveRequest(request)
            isSubmitting = false
            submitSuccess = result
        }
    }

    fun loadLeaveHistory(doctorId: String) {
        viewModelScope.launch {
            val allRequests = repository.getDoctorLeaveRequests(doctorId)
            leaveHistory = allRequests
            activeLeaveExists = allRequests.any {
                it.status == "pending" || it.status == "ongoing"
            }
            Log.d("LeaveFetch", "Doctor ID: $doctorId")

            Log.d("LeaveRequestRepo", "Fetched ${leaveHistory.size} leave requests")

        }
    }
}