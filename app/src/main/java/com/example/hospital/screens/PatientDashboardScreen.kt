package com.example.hospital.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospital.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDashboardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.ProfileScreen.route)
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }

                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                DashboardCard(
                    icon = Icons.Default.Phone, // Temporary icon for CalendarToday
                    title = "Book Appointment",
                    onClick = { navController.navigate(Screen.DoctorListScreen.route) }
                )
            }
            item {
                DashboardCard(
                    icon = Icons.Default.Person, // This one is correct
                    title = "Appointments",
                    onClick = { navController.navigate(Screen.AppointmentsScreen.route) }
                )
            }
            item {
                DashboardCard(
                    icon = Icons.Default.Search, // Temporary icon for Medical Records
                    title = "Medical Records",
                    onClick = { /* navController.navigate(Screen.MedicalRecords.route) */ }
                )
            }
            item {
                DashboardCard(
                    icon = Icons.Default.Search, // Temporary icon for Pharmacy
                    title = "DocXpert",
                    onClick = {  navController.navigate(Screen.ChatBotScreen.route)  }
                )
            }
            item {
                DashboardCard(
                    icon = Icons.Default.Search, // Temporary icon for Pharmacy
                    title = "Add Doctor",
                    onClick = {  navController.navigate(Screen.AddDoctorScreen.route)  }
                )
            }
        }
    }
}

@Composable
fun DashboardCard(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientPreview(){
    val navController = rememberNavController()
    PatientDashboardScreen(navController)
}