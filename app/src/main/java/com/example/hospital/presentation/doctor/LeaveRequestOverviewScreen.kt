package com.example.hospital.presentation.doctor

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.core.theme.PrimaryButton
import com.example.hospital.data.viewmodels.LeaveRequestViewModel

@Composable
fun LeaveRequestsOverviewScreen(
    doctorId: String,
    onNewLeaveClick: () -> Unit
) {
    val viewModel: LeaveRequestViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val trimmedId = doctorId.trim()
        viewModel.loadLeaveHistory(trimmedId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Leave Requests", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(12.dp))

        if (viewModel.leaveHistory.isEmpty()) {
            Text("No leave requests found.")
        } else {
            viewModel.leaveHistory.forEach { req ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("From ${req.startDate} to ${req.endDate}")
                        Text("Reason: ${req.reason}")
                        Text("Status: ${req.status}")
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        PrimaryButton(
            text = "Request New Leave",
            onClick = {
                if (viewModel.activeLeaveExists) {
                    Toast.makeText(context, "Pending or ongoing leave exists.", Toast.LENGTH_SHORT).show()
                } else {
                    onNewLeaveClick()
                }
            }
        )
    }
}