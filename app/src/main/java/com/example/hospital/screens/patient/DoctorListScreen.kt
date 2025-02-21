package com.example.hospital.screens.patient

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hospital.data.models.Doctor
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DoctorListScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val doctors = remember { mutableStateListOf<Doctor>() }

    LaunchedEffect(Unit) {
        db.collection("doctors").get().addOnSuccessListener { snapshot ->
            doctors.clear()
            for (doc in snapshot) {
                val doctor = doc.toObject(Doctor::class.java).copy(id = doc.id)
                doctors.add(doctor)
            }
        }
    }

    LazyColumn {
        items(doctors.size) { index ->
            val doctor = doctors[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { navController.navigate("bookAppointment/${doctor.id}") },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = doctor.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = doctor.specialization, color = Color.Gray)
                }
            }
        }
    }
}