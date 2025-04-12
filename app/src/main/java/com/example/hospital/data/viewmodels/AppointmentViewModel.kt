package com.example.hospital.data.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.hospital.data.models.Appointment
import com.example.hospital.data.repositories.AppointmentRepository


class AppointmentViewModel : ViewModel() {
    private val repo = AppointmentRepository()

    init {
        Log.d("AppointmentsVM", "ViewModel created")
    }


    private val _appointments = mutableStateOf<List<Appointment>>(emptyList())
    val appointments: State<List<Appointment>> = _appointments

    fun fetchAppointments(patientId: String) {
        repo.getAppointmentsForPatient(patientId) { list ->
            Log.d("AppointmentsVM", "Fetched ${list.size} appointments")
            _appointments.value = list
        }
    }
}
