package com.example.hospital.core

sealed class Screen(val route: String) {

    object WelcomeScreen : Screen("welcome")
    object SignInScreen : Screen("signin")
    object DoctorLoginScreen : Screen("doctor_login")

    object PatientDashboard : Screen("dashboard")
    object ChatBotScreen : Screen("chatbot")
    object AppointmentsListScreen: Screen("my_appointments")

    object ProfileScreen: Screen("profile/{userId}")
    object OnboardingScreen: Screen("post_login/{userId}")

    object AdminScreen : Screen("admin_screen")     // miscellaneous

    // booking appointments
    object DoctorPreviewScreen: Screen("doctor_preview/{doctorId}")
    object DoctorListScreen : Screen("doctor_list/{specialization}/{ids}")
    object MedicalFieldScreen: Screen("field_selection")
}

