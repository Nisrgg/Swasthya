package com.example.hospital.data.repositories

import android.util.Log
import com.example.hospital.data.models.Appointment
import com.example.hospital.data.models.AppointmentWithId
import com.example.hospital.data.models.Doctor
import com.example.hospital.data.models.LeaveRequest
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.tasks.await

class DoctorRepository {

    private val db = FirebaseFirestore.getInstance()



    suspend fun getDoctorsByIds(ids: List<String>): List<Doctor> {
        if (ids.isEmpty()) return emptyList()
        return try {
            val snapshot = db.collection("doctors")
                .whereIn(FieldPath.documentId(), ids)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Doctor::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Failed to fetch doctors by IDs", e)
            emptyList()
        }
    }

    fun getAppointmentsForDoctor(doctorId: String, onResult: (List<AppointmentWithId>) -> Unit) {
        db.collection("appointments")
            .whereEqualTo("doctor_id", doctorId)
            .get()
            .addOnSuccessListener { snapshot ->
                val appointments = snapshot.documents.mapNotNull {
                    val data = it.toObject(Appointment::class.java)
                    data?.let { appt -> AppointmentWithId(it.id, appt) }
                }
                onResult(appointments)
            }
    }

    fun getUpcomingAppointments(
        doctorId: String,
        onComplete: (List<Pair<String, Appointment>>) -> Unit
    ) {
        val today = Timestamp.now()
        db.collection("appointments")
            .whereEqualTo("doctor_id", doctorId)
            .whereGreaterThanOrEqualTo("appointment_date", today)
            .orderBy("appointment_date")
            .get()
            .addOnSuccessListener { result ->
                val appointments = result.documents.mapNotNull {
                    val appointment = it.toObject(Appointment::class.java)
                    if (appointment != null) it.id to appointment else null
                }
                Log.d("REPO", "Fetched ${appointments.size} upcoming appointments")
                onComplete(appointments)
            }
            .addOnFailureListener {
                Log.e("REPO", "Failed to fetch appointments", it)
            }
    }

    fun rescheduleAppointment(
        appointmentId: String,
        newDate: Timestamp,
        newSlot: String,
        onComplete: (Boolean) -> Unit
    ) {
        db.collection("appointments").document(appointmentId)
            .update(
                mapOf(
                    "appointment_date" to newDate,
                    "slot" to newSlot,
                    "status" to "rescheduled"
                )
            )
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

//    fun requestLeave(doctorId: String, start: String, end: String, onComplete: (Boolean) -> Unit) {
//        val leave = mapOf("doctor_id" to doctorId, "start_date" to start, "end_date" to end)
//        db.collection("leave_requests").add(leave)
//            .addOnSuccessListener { onComplete(true) }
//            .addOnFailureListener { onComplete(false) }
//    }

    fun submitLeaveRequest(request: LeaveRequest, onComplete: (Boolean) -> Unit) {
        val db = com.google.firebase.ktx.Firebase.firestore
        db.collection("leave_requests")
            .add(request)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    //never used
    suspend fun getAllDoctors(): List<Doctor> {
        return try {
            val snapshot = db.collection("doctors").get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Doctor::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Failed to fetch all doctors", e)
            emptyList()
        }
    }
    fun checkIfDoctor(uid: String, onResult: (Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection("doctors").document(uid).get()
            .addOnSuccessListener { document ->
                onResult(document.exists())
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

}