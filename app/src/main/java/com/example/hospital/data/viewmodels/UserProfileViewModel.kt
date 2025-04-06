package com.example.hospital.data.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hospital.data.models.UserProfile
import com.example.hospital.data.repositories.UserRepository

class UserProfileViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {
    var userProfile by mutableStateOf(UserProfile())
        private set

    var isProfileLoaded by mutableStateOf(false)
        private set

    fun updateProfileField(update: UserProfile.() -> UserProfile) {
        userProfile = userProfile.update()
    }

    fun saveProfile(onResult: (Boolean) -> Unit) {
        repository.saveUserProfile(userProfile) {
            isProfileLoaded = it
            onResult(it)
        }
    }

    fun loadUserProfile(userId: String) {
        repository.fetchUserProfile(userId) {
            if (it != null) {
                userProfile = it
                isProfileLoaded = true
            } else {
                isProfileLoaded = false
            }
        }
    }
}

