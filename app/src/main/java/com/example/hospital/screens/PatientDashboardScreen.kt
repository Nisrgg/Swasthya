package com.example.hospital.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospital.Screen
import com.example.hospital.googleSignIn.AuthViewModel
import com.example.hospital.googleSignIn.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDashboardScreen(navController: NavController, authViewModel: AuthViewModel) {

    val context = LocalContext.current
    val oneTapClient = remember { Identity.getSignInClient(context) }
    val scope = rememberCoroutineScope() // Add this line for coroutine scope

    // Initialize GoogleAuthUiClient
    val googleAuthUiClient = remember { GoogleAuthUiClient(context, oneTapClient, authViewModel) }
    val auth = FirebaseAuth.getInstance()

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
                    icon = Icons.Default.Close,
                    title = "Logout",
                    onClick = {
                        // Launch coroutine for suspension function
                        scope.launch {
                            try {
                                // Call the suspend function from within a coroutine
                                googleAuthUiClient.signOut()
                                Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()

                                navController.navigate("welcome"){
                                    popUpTo(0){ inclusive = true }
                                    launchSingleTop = true
                                }
                            } catch (e: Exception) {
                                Log.e("Logout", "Error signing out: ${e.message}")
                                Toast.makeText(context, "Sign out failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
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

suspend fun signOut(
    context: Context,
    googleAuthUiClient: GoogleAuthUiClient,
    navController: NavController
) {
    // Get Firebase Auth instance
    val auth = FirebaseAuth.getInstance()

    try {
        // Sign out from Firebase Auth
        auth.signOut()

        // Call the GoogleAuthUiClient signOut to handle Google Sign-In state
        googleAuthUiClient.signOut()

        // Show success message
        Toast.makeText(context, "Logged Out Successfully", Toast.LENGTH_SHORT).show()

        // Navigate to sign-in screen and clear back stack
        navController.navigate("signin") {
            popUpTo("patientDashboard") { inclusive = true }
            launchSingleTop = true
        }
    } catch (e: Exception) {
        // Handle any potential errors
        Log.e("SignOut", "Error signing out: ${e.message}")
        Toast.makeText(context, "Sign out failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun PatientPreview(){
    val navController = rememberNavController()
    val authview = AuthViewModel()
    PatientDashboardScreen(navController, authview)
}

