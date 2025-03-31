package com.example.hospital.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.models.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile = _profile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _success = MutableStateFlow<String?>(null)
    val success = _success.asStateFlow()

    init {
        loadProfile()
    }

    /**
     * Loads the user's profile from Firestore
     */
    private fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
                val document = firestore.collection("users").document(userId).get().await()
                if (document.exists()) {
                    _profile.value = document.toObject<UserProfile>()
                } else {
                    _error.value = "Profile not found"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load profile"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Saves the updated user profile to Firestore
     */
    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
                val documentRef = firestore.collection("users").document(userId)

                // Check if the profile is modified before saving
                val currentProfile = _profile.value
                if (currentProfile != profile) {
                    documentRef.update(
                        mapOf(
                            "fullName" to profile.fullName,
                            "dateOfBirth" to profile.dateOfBirth,
                            "gender" to profile.gender,
                            "phoneNumber" to profile.phoneNumber,
                            "email" to profile.email,
                            "address" to profile.address,
                            "bloodGroup" to profile.bloodGroup,
                            "emergencyContact" to profile.emergencyContact,
                            "medicalConditions" to profile.medicalConditions
                        )
                    ).await()
                    _profile.value = profile
                    _error.value = "Profile updated successfully"
                } else {
                    _error.value = "No changes to update"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Clears the success and error states after displaying
     */
    fun clearStatus() {
        _error.value = null
        _success.value = null
    }
}