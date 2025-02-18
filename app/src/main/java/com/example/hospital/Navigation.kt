package com.example.hospital

import android.net.Uri

sealed class Screen(val route: String) {
    object WelcomeScreen : Screen("welcome")
    object SignInScreen : Screen("signin")
    object PatientDashboard : Screen("dashboard")
    object ChatBotScreen : Screen("chatbot")

    // 🔧 Fixed: Removed `{doctorJson}` since the list does not need it
    object DoctorListScreen : Screen("doctor_list")

    // 🔧 Fixed: Ensured proper URL encoding for JSON object
    object BookAppointmentScreen : Screen("bookAppointment/{doctorJson}") {
        fun createRoute(doctorJson: String) = "bookAppointment/${Uri.encode(doctorJson)}"
    }

    object ProfileScreen: Screen("profile")
    //object EditProfileScreen: Screen("edit_profile")

    object AppointmentsScreen : Screen("user_appointments")
    object AddDoctorScreen : Screen("add_doctor")
}

