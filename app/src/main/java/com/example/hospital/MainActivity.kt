package com.example.hospital

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hospital.chatBot.ChatBotViewModel
import com.example.hospital.googleSignIn.AuthViewModel
import com.example.hospital.googleSignIn.GoogleAuthUiClient
import com.example.hospital.screens.admin.AddDoctorScreen
import com.example.hospital.screens.patient.AppointmentsScreen
import com.example.hospital.screens.patient.BookAppointmentScreen
import com.example.hospital.screens.chatbot.ChatPage
import com.example.hospital.screens.patient.DoctorListScreen
//import com.example.hospital.screens.EditProfileScreen
import com.example.hospital.screens.patient.PatientDashboardScreen
import com.example.hospital.screens.patient.ProfileScreen
import com.example.hospital.screens.auth.SignInScreenUI
import com.example.hospital.ui.theme.HospitalTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.data.viewmodels.ProfileViewModel
import com.example.hospital.screens.admin.AdminScreen
import com.example.hospital.screens.admin.AllAppointmentsScreen
import com.example.hospital.screens.admin.RemoveDoctorScreen


class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private val chatViewModel: ChatBotViewModel by viewModels()

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            viewModel = viewModel,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }


    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HospitalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        val state by viewModel.state.collectAsState()
                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = Screen.WelcomeScreen.route
                        ) {

                            composable(Screen.WelcomeScreen.route) {
                                LaunchedEffect(key1 = Unit) {
                                    val userData = googleAuthUiClient.getSignedInUser()
                                    if (userData != null) {
                                        navController.navigate(Screen.PatientDashboard.route) {
                                            popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(Screen.SignInScreen.route) {
                                            popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                                        }
                                    }
                                }


                            }
                            composable(Screen.PatientDashboard.route) {
                                PatientDashboardScreen(navController, viewModel)
                            }
                            composable(Screen.SignInScreen.route) {
                                val launcher =
                                    rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
                                        onResult = { result ->
                                            Log.d(
                                                "MainActivity",
                                                "Sign-in result received: ${result.resultCode}"
                                            )
                                            if (result.resultCode == RESULT_OK) {
                                                lifecycleScope.launch {
                                                    val signInResult =
                                                        googleAuthUiClient.signInWithIntent(
                                                            intent = result.data ?: return@launch
                                                        )
                                                    Log.d(
                                                        "MainActivity",
                                                        "SignInResult obtained: $signInResult"
                                                    )
                                                    viewModel.onSignInResult(signInResult)
                                                }

                                            }
                                        })
                                LaunchedEffect(key1 = state.isSignedIn) {
                                    Log.d(
                                        "MainActivity", "SignIn state changed: ${state.isSignedIn}"
                                    )
                                    if (state.isSignedIn) {
                                        Log.d(
                                            "MainActivity",
                                            "Attempting navigation to PatientDashboard"
                                        )
                                        navController.navigate(Screen.PatientDashboard.route) {
                                            popUpTo(Screen.SignInScreen.route) { inclusive = true }
                                        }
                                    }
                                }
                                SignInScreenUI(navController, onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInSender ?: return@launch
                                            ).build()
                                        )

                                    }
                                })
                            }
                            composable(Screen.ChatBotScreen.route) {
                                ChatPage(modifier = Modifier.padding(innerPadding), chatViewModel)
                            }

                            composable(Screen.DoctorListScreen.route) {
                                DoctorListScreen(navController)
                            }

                            composable(Screen.BookAppointmentScreen.route) { backStackEntry ->
                                val doctorJson = backStackEntry.arguments?.getString("doctorJson")?.let { Uri.decode(it) } ?: ""
                                BookAppointmentScreen(navController, doctorJson)
                            }

                            composable(Screen.AdminScreen.route){
                                AdminScreen(navController)
                            }

                            composable(Screen.RemoveDoctorScreen.route){
                                RemoveDoctorScreen()
                            }

                            composable(Screen.AddDoctorScreen.route) {
                                AddDoctorScreen(navController)
                            }

                            composable(Screen.AllAppointmentsScreen.route) {
                                AllAppointmentsScreen()
                            }

                            composable(Screen.AppointmentsScreen.route) {
                                AppointmentsScreen()  // Pass if needed
                            }

                            composable(Screen.ProfileScreen.route) {
                                // Correctly instantiate ProfileViewModel using viewModel()
                                val profileViewModel: ProfileViewModel = viewModel()
                                ProfileScreen(
                                    viewModel = profileViewModel,
                                    onNavigateBack = { navController.navigateUp() }
                                )
                            }

                        }
                    }
                }
            }

        }
    }
}