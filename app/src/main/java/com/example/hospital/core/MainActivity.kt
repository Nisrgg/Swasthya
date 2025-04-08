package com.example.hospital.core

import  android.os.Build
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hospital.chatBot.ChatBotViewModel
import com.example.hospital.googleSignIn.AuthViewModel
import com.example.hospital.googleSignIn.GoogleAuthUiClient
import com.example.hospital.core.theme.HospitalTheme
import com.example.hospital.data.models.UserProfile
import com.example.hospital.data.viewmodels.AppointmentDoctorViewModel
import com.example.hospital.data.viewmodels.AppointmentViewModel
import com.example.hospital.data.viewmodels.UserProfileViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import com.example.hospital.presentation.auth.SignInScreenUI
import com.example.hospital.presentation.doctor.DoctorLoginScreen
import com.example.hospital.presentation.patient.ChatPage
import com.example.hospital.presentation.admin.DoctorListScreen
import com.example.hospital.presentation.patient.PatientDashboardScreen
import com.example.hospital.presentation.patient.DoctorPreviewScreen
import com.example.hospital.presentation.admin.MedicalFieldSelectionScreen
import com.example.hospital.presentation.admin.OnboardingScreen
import com.example.hospital.presentation.admin.RescheduleAppointmentScreen
import com.example.hospital.presentation.appointment.AppointmentsListScreen
import com.example.hospital.presentation.doctor.DoctorHomeScreen
import com.example.hospital.data.viewmodels.DoctorViewModel
import com.example.hospital.data.viewmodels.LeaveRequestViewModel
import com.example.hospital.presentation.doctor.LeaveRequestScreen
import com.example.hospital.presentation.doctor.MyAppointmentsScreen
import com.example.hospital.presentation.patient.UserProfileScreen
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private val chatViewModel: ChatBotViewModel by viewModels()
    private val doctorViewModel: DoctorViewModel by viewModels()
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private val appointmentDoctorViewModel: AppointmentDoctorViewModel by viewModels()
    private val currentUser = FirebaseAuth.getInstance().currentUser


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
                                LaunchedEffect(Unit) {
                                    if (currentUser != null) {
                                        currentUser.getIdToken(true)
                                            .addOnSuccessListener { result ->
                                                val role = result.claims["role"] as? String
                                                when (role) {
                                                    "doctor" -> {
                                                        val doctorId = currentUser.uid
                                                        navController.navigate("doctor_home/$doctorId") {
                                                            popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                                                        }
                                                    }
                                                    null -> {
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
                                                    else -> {
                                                        FirebaseAuth.getInstance().signOut()
                                                        navController.navigate(Screen.SignInScreen.route) {
                                                            popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                                                        }
                                                    }
                                                }
                                            }
                                            .addOnFailureListener {
                                                navController.navigate(Screen.SignInScreen.route) {
                                                    popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                                                }
                                            }
                                    } else {
                                        navController.navigate(Screen.SignInScreen.route) {
                                            popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                                        }
                                    }
                                }
                            }

                            composable(Screen.SignInScreen.route) {
                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = { result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult =
                                                    googleAuthUiClient.signInWithIntent(intent = result.data ?: return@launch)
                                                viewModel.onSignInResult(signInResult)
                                            }
                                        }
                                    })

                                LaunchedEffect(key1 = state.isSignedIn) {
                                    if (state.isSignedIn) {
                                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                                        if (userId != null) {
                                            navController.navigate(Screen.PatientDashboard.route) {
                                                popUpTo(Screen.SignInScreen.route) { inclusive = true }
                                            }
                                        } else {
                                            navController.navigate(Screen.OnboardingScreen.route) {
                                                popUpTo(Screen.SignInScreen.route) { inclusive = true }
                                            }
                                        }
                                    }
                                }

                                SignInScreenUI(navController, onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(signInSender ?: return@launch).build()
                                        )
                                    }
                                })
                            }

                            composable(Screen.PatientDashboard.route) {
                                PatientDashboardScreen(navController, viewModel)
                            }

                            composable(Screen.ChatBotScreen.route) {
                                ChatPage(modifier = Modifier.padding(innerPadding), chatViewModel)
                            }

                            composable(Screen.AppointmentsListScreen.route) { backStackEntry ->
                                val viewModel: AppointmentViewModel = viewModel(backStackEntry)
                                AppointmentsListScreen(viewModel = viewModel)
                            }

                            composable(Screen.ProfileScreen.route) {
                                val viewModel: UserProfileViewModel = viewModel()
                                val userId = FirebaseAuth.getInstance().currentUser?.uid

                                if (userId != null) {
                                    LaunchedEffect(userId) {
                                        if (viewModel.userProfile == null && !viewModel.isLoading) {
                                            viewModel.loadUserProfile(userId)
                                        }
                                    }

                                    when {
                                        viewModel.isLoading -> {
                                            Box(modifier = Modifier.fillMaxSize()) {
                                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                                            }
                                        }
                                        viewModel.userProfile != null -> {
                                            UserProfileScreen(
                                                userId = userId,
                                                navController = navController,
                                                viewModel = viewModel,
                                                onProfileUpdated = {
                                                    navController.navigate(Screen.PatientDashboard.route) {
                                                        popUpTo(Screen.ProfileScreen.route) { inclusive = true }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                } else {
                                    navController.navigate(Screen.SignInScreen.route)
                                }
                            }

                            composable(Screen.OnboardingScreen.route) { backStackEntry ->
                                val viewModel: UserProfileViewModel = viewModel()
                                val userId = FirebaseAuth.getInstance().currentUser?.uid
                                val editedProfile = navController.previousBackStackEntry
                                    ?.savedStateHandle?.get<UserProfile>("editProfile")

                                if (userId != null) {
                                    OnboardingScreen(
                                        userId = userId,
                                        navController = navController,
                                        viewModel = viewModel,
                                        existingProfile = editedProfile
                                    ) {
                                        navController.navigate(Screen.PatientDashboard.route) {
                                            popUpTo(Screen.OnboardingScreen.route) { inclusive = true }
                                        }
                                    }
                                } else {
                                    navController.navigate(Screen.SignInScreen.route)
                                }
                            }

                            composable(Screen.DoctorHomeScreen.route, arguments = listOf(navArgument("doctorId") { type = NavType.StringType })) { backStackEntry ->
                                val doctorId = backStackEntry.arguments?.getString("doctorId")!!
                                DoctorHomeScreen(doctorId, navController)
                            }

                            composable(Screen.DoctorLoginScreen.route) {
                                DoctorLoginScreen(navController)
                            }

                            composable(Screen.MedicalFieldScreen.route) {
                                MedicalFieldSelectionScreen(navController)
                            }

                            composable(
                                route = Screen.DoctorListScreen.route,
                                arguments = listOf(
                                    navArgument("specialization") { type = NavType.StringType },
                                    navArgument("ids") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val spec = backStackEntry.arguments?.getString("specialization") ?: ""
                                val ids = backStackEntry.arguments?.getString("ids")?.split(",") ?: emptyList()

                                DoctorListScreen(
                                    navController = navController,
                                    specialization = spec,
                                    doctorIds = ids
                                )
                            }

                            composable(Screen.DoctorPreviewScreen.route) { backStackEntry ->
                                val doctorId = backStackEntry.arguments?.getString("doctorId")
                                if (!doctorId.isNullOrEmpty()) {
                                    DoctorPreviewScreen(doctorId = doctorId, navController)
                                }
                            }

                            composable(Screen.MyAppointment.route) { backStackEntry ->
                                val doctorId = backStackEntry.arguments?.getString("doctorId")
                                val viewModel: DoctorViewModel by viewModels()
                                if (doctorId != null) {
                                    MyAppointmentsScreen(doctorId = doctorId, viewModel = viewModel)
                                }
                            }

                            composable(Screen.LeaveScreen.route) { backStackEntry ->
                                val doctorId = backStackEntry.arguments?.getString("doctorId")
                                val viewModel: LeaveRequestViewModel by viewModels()
                                if (doctorId != null) {
                                    LeaveRequestScreen(doctorId = doctorId, viewModel = viewModel)
                                }
                            }

                            composable(Screen.RescheduleScreen.route) { backStackEntry ->
                                val doctorId = backStackEntry.arguments?.getString("doctorId")
                                val viewModel: DoctorViewModel by viewModels()
                                if (doctorId != null) {
                                    RescheduleAppointmentScreen(doctorId = doctorId, viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}