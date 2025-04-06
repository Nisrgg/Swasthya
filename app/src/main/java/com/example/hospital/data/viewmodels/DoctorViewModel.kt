package com.example.hospital.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.models.Doctor
import com.example.hospital.data.repositories.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoctorViewModel(private val repository: DoctorRepository = DoctorRepository()) : ViewModel() {
    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors

    init {
        fetchDoctors()
    }

    private fun fetchDoctors() {
        viewModelScope.launch {
            _doctors.value = repository.getAllDoctors()
        }
    }
}
