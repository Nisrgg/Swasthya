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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp) // Add padding to the list
    ) {
        items(doctors.size) { index ->
            val doctor = doctors[index]
            DoctorCard(doctor = doctor, navController = navController)
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Vertical padding for spacing between cards
            .clickable { navController.navigate("bookAppointment/${doctor.id}") },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = doctor.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = doctor.specialization,
                color = Color.Gray,
                fontSize = 16.sp // Slightly smaller font size for specialization
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorListPreview() {
    // Create a mock list of doctors for the preview
    val mockDoctors = listOf(
        Doctor(id = "1", name = "Dr. John Doe", specialization = "Cardiologist"),
        Doctor(id = "2", name = "Dr. Jane Smith", specialization = "Dermatologist"),
        Doctor(id = "3", name = "Dr. Emily Johnson", specialization = "Pediatrician")
    )

    // Use MaterialTheme to apply the theme
    MaterialTheme {
        // Simulate the DoctorListScreen with mock data
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(mockDoctors.size) { index ->
                val doctor = mockDoctors[index]
                DoctorCard(doctor = doctor, navController = rememberNavController()) // Use a mock NavController
            }
        }
    }
}