package com.example.hospital.presentation.doctor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hospital.googleSignIn.loginDoctor
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.autofill.*
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.platform.LocalDensity

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DoctorLoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val autofill = LocalAutofill.current
    val autofillTree = LocalAutofillTree.current
//    val density = LocalDensity.current

    val emailNode = remember {
        AutofillNode(
            onFill = { email = it },
            autofillTypes = listOf(AutofillType.EmailAddress)
        )
    }
    val passwordNode = remember {
        AutofillNode(
            onFill = { password = it },
            autofillTypes = listOf(AutofillType.Password)
        )
    }

    // Registering AutofillNodes
    DisposableEffect(Unit) {
        autofillTree += emailNode
        autofillTree += passwordNode
        onDispose {
            autofillTree += emailNode
            autofillTree += passwordNode
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Doctor Login",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    val bounds = coordinates.boundsInWindow()
                    emailNode.boundingBox = bounds
                    autofill?.requestAutofillForNode(emailNode)
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    val bounds = coordinates.boundsInWindow()
                    passwordNode.boundingBox = bounds
                    autofill?.requestAutofillForNode(passwordNode)
                }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                loading = true
                loginDoctor(email, password, navController) {
                    loading = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        if (loading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}