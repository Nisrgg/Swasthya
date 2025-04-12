package com.example.hospital.data.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hospital.data.models.UserProfile
import com.example.hospital.data.repositories.UserRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserProfileViewModel : ViewModel() {

    private val repository = UserRepository()

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var profileExists by mutableStateOf(false)

    fun loadUserProfile(uid: String) {
        isLoading = true
        repository.getUserProfile(uid) {
            userProfile = it
            isLoading = false
        }
    }

    fun saveUserProfile(uid: String, profile: UserProfile) {
        isLoading = true
        repository.saveUserProfile(uid, profile) {
            isSuccess = it
            isLoading = false
        }
    }

    fun checkIfProfileExists(uid: String, onResult: (Boolean) -> Unit) {
        repository.doesUserProfileExist(uid) {
            profileExists = it
            onResult(it)
        }
    }

    fun checkIfUserProfileExists(userId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val doc = Firebase.firestore
                    .collection("user_profiles") // 👈 your collection name
                    .document(userId)
                    .get()
                    .await()
                onResult(doc.exists())
            } catch (e: Exception) {
                Log.e("UserProfileVM", "Error checking profile: ", e)
                onResult(false)
            }
        }
    }

}