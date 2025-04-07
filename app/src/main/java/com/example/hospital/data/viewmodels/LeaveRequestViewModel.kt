package com.example.hospital.data.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.models.LeaveRequest
import com.example.hospital.data.repositories.LeaveRequestRepository
import kotlinx.coroutines.launch

class LeaveRequestViewModel(
    private val repository: LeaveRequestRepository = LeaveRequestRepository()
) : ViewModel() {

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
}