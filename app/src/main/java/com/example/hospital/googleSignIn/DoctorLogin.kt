package com.example.hospital.googleSignIn

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospital.core.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun loginDoctor(
    email: String,
    password: String,
    navController: NavController,
    onComplete: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val uid = user?.uid

                if (uid != null) {
                    // ✅ Force token refresh to get updated custom claims
                    user.getIdToken(true)
                        .addOnSuccessListener { result ->
                            val claims = result.claims
                            val isDoctor = claims["role"] == "doctor"

                            if (isDoctor) {
                                Log.d("DoctorLogin", "Login success for doctor: $email")
                                navController.navigate(Screen.DoctorHomeScreen.route) {
                                    popUpTo(Screen.SignInScreen.route) { inclusive = true }
                                }
                            } else {
                                Log.e("LoginError", "Not a doctor account.")
                                Toast.makeText(
                                    navController.context,
                                    "Unauthorized! You are not a doctor.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                auth.signOut()
                            }
                            onComplete()
                        }
                        .addOnFailureListener {
                            Log.e("TokenError", "Failed to refresh token: ${it.message}")
                            Toast.makeText(
                                navController.context,
                                "Error fetching token. Try again!",
                                Toast.LENGTH_SHORT
                            ).show()
                            auth.signOut()
                            onComplete()
                        }
                } else {
                    onComplete()
                }
            } else {
                Log.e("AuthError", "Login failed: ${task.exception?.message}")
                Toast.makeText(
                    navController.context,
                    "Login failed! Check credentials.",
                    Toast.LENGTH_SHORT
                ).show()
                onComplete()
            }
        }
}