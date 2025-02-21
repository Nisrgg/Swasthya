package com.example.hospital.googleSignIn

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hospital.utils.AppState
import com.example.hospital.utils.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel: ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()
    fun resetState(){
        _state.update { AppState() }
    }

    fun onSignInResult(result: SignInResult) {
        Log.d("AuthViewModel", "Sign in result received: $result")

        _state.update { it.copy(
            isSignedIn = result.data != null,
            signInError = result.errorMessage
        ) }
        Log.d("AuthViewModel", "State updated: ${_state.value}")

    }
}
