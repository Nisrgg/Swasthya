package com.example.hospital.data.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.models.Doctor
import com.example.hospital.data.repositories.DoctorRepository
import com.example.hospital.utils.FirebaseUtils.db
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoctorViewModel(
    private val repository: DoctorRepository = DoctorRepository()
) : ViewModel() {

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors

    val selectedDoctor = mutableStateOf<Doctor?>(null)

    fun fetchDoctorsBySpecialization(ids: List<String>) {
        viewModelScope.launch {
            val fetchedDoctors = repository.getDoctorsByIds(ids) // ✅ call suspend normally
            _doctors.value = fetchedDoctors
        }
    }


    fun fetchDoctorById(id: String) {
        db.collection("doctors").document(id)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.toObject(Doctor::class.java)?.let {
                    selectedDoctor.value = it.copy(id = snapshot.id)
                }
            }
            .addOnFailureListener {
                selectedDoctor.value = null
            }
    }
}