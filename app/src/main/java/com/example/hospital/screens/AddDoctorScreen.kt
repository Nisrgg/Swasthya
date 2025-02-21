package com.example.hospital.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hospital.data.repositories.DoctorRepository

@Composable
fun AddDoctorScreen() {
    val name = remember { mutableStateOf("") }
    val specialization = remember { mutableStateOf("") }
    val slots = remember { mutableStateOf("") }
    val repository = DoctorRepository()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Doctor Name") }
        )

        OutlinedTextField(
            value = specialization.value,
            onValueChange = { specialization.value = it },
            label = { Text("Specialization") }
        )

        OutlinedTextField(
            value = slots.value,
            onValueChange = { slots.value = it },
            label = { Text("Available Slots (comma separated)") }
        )

        Button(onClick = {
            val slotList = slots.value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            if (name.value.isNotBlank() && specialization.value.isNotBlank()) {
                repository.addDoctor(
                    name.value,
                    specialization.value,
                    slotList,
                    onSuccess = { println("Doctor added successfully!") },
                    onFailure = { e -> println("Error: ${e.message}") }
                )
            }
        }) {
            Text("Add Doctor")
        }
    }
} 