package com.example.hospital.core

sealed class Screen(val route: String) {

    object WelcomeScreen : Screen("welcome")
    object SignInScreen : Screen("signin")

    object PatientDashboard : Screen("dashboard")
    object ChatBotScreen : Screen("chatbot")

    object AdminScreen : Screen("admin_screen")

    object DoctorListScreen : Screen("doctor_list")

    object DoctorLoginScreen : Screen("doctor_login")

    object ProfileScreen: Screen("profile")

    object AppointmentsScreen : Screen("user_appointments")
}

