package com.example.hospital.presentation.patient

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
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
import com.example.hospital.core.Screen
import com.example.hospital.googleSignIn.AuthViewModel
import com.example.hospital.googleSignIn.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDashboardScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val oneTapClient = remember { Identity.getSignInClient(context) }
    val scope = rememberCoroutineScope()

    // Initialize GoogleAuthUiClient
    val googleAuthUiClient = remember { GoogleAuthUiClient(context, oneTapClient, authViewModel) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.ProfileScreen.route)
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    DashboardCard(
                        icon = Icons.Default.Phone,
                        title = "Book Appointment",
                        onClick = { navController.navigate(Screen.MedicalFieldScreen.route) }
                    )
                }
                item {
                    DashboardCard(
                        icon = Icons.Default.List,
                        title = "Appointments",
                        onClick = { navController.navigate(Screen.AppointmentsListScreen.route) }
                    )
                }
                item {
                    DashboardCard(
                        icon = Icons.Default.Chat,
                        title = "DocXpert",
                        onClick = { navController.navigate(Screen.ChatBotScreen.route) }
                    )
                }
                item {
                    DashboardCard(
                        icon = Icons.Default.ExitToApp,
                        title = "Logout",
                        onClick = {
                            scope.launch {
                                try {
                                    googleAuthUiClient.signOut()
                                    Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()
                                    navController.navigate("welcome") {
                                        popUpTo(0) { inclusive = true }
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
}

@Composable
fun DashboardCard(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientPreview() {
    val navController = rememberNavController()
    // Use a dummy AuthViewModel in preview.
    val authViewModel = AuthViewModel()
    PatientDashboardScreen(navController, authViewModel)
}