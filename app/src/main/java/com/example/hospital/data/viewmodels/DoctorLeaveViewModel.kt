package com.example.hospital.data.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.hospital.data.models.LeaveRequest
import com.example.hospital.data.repositories.DoctorRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LeaveState {
    object Idle : LeaveState()
    object Loading : LeaveState()
    object Success : LeaveState()
    data class Error(val message: String) : LeaveState()
}

class DoctorLeaveViewModel(private val doctorRepository: DoctorRepository) : ViewModel() {

    private val _leaveState = mutableStateOf<LeaveState>(LeaveState.Idle)
    val leaveState = _leaveState

    private val _startDate = MutableStateFlow("")

    private val _endDate = MutableStateFlow("")

    private val _reason = MutableStateFlow("")


}
