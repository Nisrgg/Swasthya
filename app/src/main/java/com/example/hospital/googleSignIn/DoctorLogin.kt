package com.example.hospital.googleSignIn

import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.hospital.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//fun loginDoctor(email: String, password: String, navController: NavController, onComplete: (Boolean) -> Unit) {
//    val auth = FirebaseAuth.getInstance()
//    val db = FirebaseFirestore.getInstance()
//
//    auth.signInWithEmailAndPassword(email, password)
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val user = auth.currentUser
//                val uid = user?.uid
//
//                if (uid != null) {
//                    // ✅ Check if the user is a doctor
//                    db.collection("doctors").document(uid).get()
//                        .addOnSuccessListener { document ->
//                            if (document != null && document.exists()) {
//                                val role = document.getString("role")
//                                if (role == "doctor") {
//                                    navController.navigate("admin_dashboard")
//                                    onComplete(true)
//                                } else {
//                                    Toast.makeText(navController.context, "Unauthorized! Not a Doctor", Toast.LENGTH_SHORT).show()
//                                    FirebaseAuth.getInstance().signOut()
//                                    onComplete(false)
//                                }
//                            } else {
//                                Toast.makeText(navController.context, "No Doctor found!", Toast.LENGTH_SHORT).show()
//                                FirebaseAuth.getInstance().signOut()
//                                onComplete(false)
//                            }
//                        }
//                }
//            } else {
//                onComplete(false)
//            }
//        }
//}


fun loginDoctor(email: String, password: String, navController: NavController, onComplete: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val uid = user?.uid

                if (uid != null) {
                    // ✅ Check if the logged-in user is a doctor in Firestore
                    db.collection("doctors").document(uid).get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val role = document.getString("role")

                                if (role == "doctor") {
                                    // ✅ Refresh token to get updated custom claims
                                    user.getIdToken(true)
                                        .addOnSuccessListener { result ->
                                            val claims = result.claims
                                            Log.d("AuthCheck", "UID: ${user.uid}, Claims: ${claims.toString()}")

                                            if (claims["role"] == "doctor") {
                                                Log.d("DoctorLogin", "Doctor login successful!")
                                                navController.navigate(Screen.AdminScreen.route) // Correct screen for doctor
                                            } else {
                                                Log.e("AuthError", "Unauthorized Access: Not a Doctor")
                                                Toast.makeText(
                                                    navController.context,
                                                    "Unauthorized Access! Not a Doctor.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                auth.signOut()
                                            }
                                        }
                                        .addOnFailureListener {
                                            Log.e("AuthError", "Failed to refresh token: ${it.message}")
                                            Toast.makeText(
                                                navController.context,
                                                "Error fetching account details.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            auth.signOut()
                                        }
                                } else {
                                    Log.e("LoginError", "Unauthorized Access: Not a Doctor in Firestore!")
                                    Toast.makeText(
                                        navController.context,
                                        "Unauthorized Access! Not a Doctor.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    auth.signOut()
                                }
                            } else {
                                Log.e("LoginError", "Doctor profile not found in Firestore!")
                                Toast.makeText(
                                    navController.context,
                                    "No doctor found with this account.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                auth.signOut()
                            }
                            onComplete()
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirestoreError", "Error fetching doctor info", e)
                            Toast.makeText(
                                navController.context,
                                "Error verifying account. Try again!",
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
                    "Login failed! Check email and password.",
                    Toast.LENGTH_SHORT
                ).show()
                onComplete()
            }
        }
}