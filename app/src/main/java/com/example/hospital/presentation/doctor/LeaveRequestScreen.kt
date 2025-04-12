package com.example.hospital.presentation.doctor

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.hospital.core.theme.HospitalTextField
import com.example.hospital.core.theme.HospitalTheme
import com.example.hospital.core.theme.PrimaryButton
import com.example.hospital.data.viewmodels.LeaveRequestViewModel

@Composable
fun LeaveRequestScreen(
    viewModel: LeaveRequestViewModel = viewModel(),
    doctorId: String
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadLeaveHistory(doctorId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Leave Request",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )


        HospitalTextField(
            value = viewModel.startDate,
            onValueChange = { viewModel.startDate = it },
            label = "Start Date (YYYY-MM-DD)"
        )

        HospitalTextField(
            value = viewModel.endDate,
            onValueChange = { viewModel.endDate = it },
            label = "End Date (YYYY-MM-DD)"
        )

        HospitalTextField(
            value = viewModel.reason,
            onValueChange = { viewModel.reason = it },
            label = "Reason"
        )

//        if (viewModel.activeLeaveExists) {
//            Text(
//                text = "You already have a pending or ongoing leave request.",
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }

        PrimaryButton(
            text = if (viewModel.isSubmitting) "Submitting..." else "Submit Leave Request",
            onClick = { viewModel.submitLeaveRequest(doctorId) },
            enabled = !viewModel.isSubmitting && !viewModel.activeLeaveExists
        )

        viewModel.submitSuccess?.let { success ->
            val message = if (success) "Leave request submitted!" else "Submission failed."
            LaunchedEffect(success) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaveRequestScreenPreview() {
    HospitalTheme {
        LeaveRequestScreen(doctorId = "0KUtjnZ93wfpPbynyNrnKiYdbe33")
    }
}
