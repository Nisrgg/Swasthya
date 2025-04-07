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

    // booking appointments
    object DoctorPreviewScreen: Screen("doctor_preview/{doctorId}")
    object DoctorListScreen : Screen("doctor_list/{specialization}/{ids}")
    object MedicalFieldScreen: Screen("field_selection")

    // Doctor
    object DoctorHomeScreen: Screen("doctor_home/{doctorId}")


    object RescheduleScreen : Screen("reschedule_appointment/{doctorId}")
    object LeaveScreen: Screen("leave_request/{doctorId}")
    object MyAppointment: Screen("all_appointments/{doctorId}")
}

