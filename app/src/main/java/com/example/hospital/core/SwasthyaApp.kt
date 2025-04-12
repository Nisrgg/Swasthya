package com.example.swasthya.core

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.Navigation
import com.example.hospital.core.theme.HospitalTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwasthyaApp() {
    HospitalTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Navigation
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SwasthyaAppPreview() {
    SwasthyaApp()
}
