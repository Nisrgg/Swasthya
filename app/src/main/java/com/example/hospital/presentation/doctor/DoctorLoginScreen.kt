package com.example.hospital.presentation.doctor

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospital.googleSignIn.loginDoctor
import com.example.hospital.core.theme.HospitalTextField
import com.example.hospital.core.theme.PrimaryButton
import com.example.hospital.core.theme.SectionTitle

@Composable
fun DoctorLoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(title = "Doctor Login")

        Spacer(modifier = Modifier.height(24.dp))

        HospitalTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        HospitalTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (loading) "Logging in..." else "Login",
            onClick = {
                loading = true
                loginDoctor(email, password, navController) {
                    loading = false
                }
            },
            enabled = !loading
        )

        if (loading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorLoginPreview(){
    DoctorLoginScreen(rememberNavController())
}