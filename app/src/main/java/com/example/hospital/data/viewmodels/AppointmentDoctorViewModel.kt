package com.example.hospital.data.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.hospital.data.models.Doctor  // Adjust this if your Doctor model is in a different package
import com.example.hospital.data.models.Appointment  // Adjust this if needed

class AppointmentDoctorViewModel : ViewModel() {
    private val _doctorMap = mutableStateOf<Map<String, Doctor>>(emptyMap())
    val doctorMap: State<Map<String, Doctor>> = _doctorMap

    private val doctorCollection = Firebase.firestore.collection("doctors")

    fun fetchDoctorsForAppointments(appointments: List<Appointment>) {
        val doctorIds = appointments.map { it.doctor_id }.distinct()
        val currentMap = _doctorMap.value.toMutableMap()

        doctorIds.forEach { id ->
            if (!currentMap.containsKey(id)) {
                doctorCollection.document(id).get().addOnSuccessListener { doc ->
                    doc?.toObject(Doctor::class.java)?.let { doctor ->
                        currentMap[id] = doctor
                        _doctorMap.value = currentMap
                    }
                }
            }
        }
    }
}
