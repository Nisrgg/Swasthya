package com.example.hospital.presentation.doctor

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.data.viewmodels.DoctorLeaveViewModel
import java.util.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import com.example.hospital.data.viewmodels.LeaveRequestViewModel

@Composable
fun LeaveRequestScreen(
    viewModel: LeaveRequestViewModel = viewModel(),
    doctorId: String
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Request Leave", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = viewModel.startDate,
            onValueChange = { viewModel.startDate = it },
            label = { Text("Start Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.endDate,
            onValueChange = { viewModel.endDate = it },
            label = { Text("End Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.reason,
            onValueChange = { viewModel.reason = it },
            label = { Text("Reason") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.submitLeaveRequest(doctorId) },
            enabled = !viewModel.isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (viewModel.isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Submit Leave Request")
            }
        }

        viewModel.submitSuccess?.let { success ->
            val message = if (success) "Leave request submitted!" else "Submission failed."
            LaunchedEffect(success) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}