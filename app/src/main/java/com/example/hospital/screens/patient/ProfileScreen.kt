package com.example.hospital.screens.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hospital.data.models.UserProfile
import com.example.hospital.data.viewmodels.ProfileViewModel
import java.time.*
import java.time.format.DateTimeFormatter
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var isEditMode by remember { mutableStateOf(false) }

    // Form states
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var emergencyContact by remember { mutableStateOf("") }
    var medicalConditions by remember { mutableStateOf("") }

    // Improved colors for both edit and view modes
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f), // Brighter text in view mode
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f), // Key change: better contrast for disabled fields
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        disabledBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), // Visible border in view mode
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
        disabledLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), // Visible label in view mode
        disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f) // Subtle background for better field visibility
    )

    // Update local state when profile loaded
    LaunchedEffect(profile) {
        profile?.let {
            fullName = it.fullName
            dateOfBirth = it.dateOfBirth
            gender = it.gender
            phoneNumber = it.phoneNumber
            email = it.email
            address = it.address
            bloodGroup = it.bloodGroup
            emergencyContact = it.emergencyContact
            medicalConditions = it.medicalConditions
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isEditMode = !isEditMode }) {
                        Icon(
                            if (isEditMode) Icons.Default.Close else Icons.Default.Edit,
                            contentDescription = if (isEditMode) "Cancel" else "Edit"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        enabled = isEditMode,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                }

                item {
                    OutlinedTextField(
                        value = dateOfBirth,
                        onValueChange = {},
                        label = { Text("Date of Birth") },
                        enabled = isEditMode,
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (isEditMode) {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Default.DateRange, "Select Date")
                                }
                            }
                        },
                        colors = textFieldColors
                    )
                }

                // Gender Dropdown - Fixed Implementation
                item {
                    val genders = listOf("Male", "Female", "Other")
                    var genderMenuExpanded by remember { mutableStateOf(false) }

                    // For view mode: just display the value with proper styling
                    if (!isEditMode) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            label = { Text("Gender") },
                            enabled = false,
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors
                        )
                    } else {
                        // For edit mode: use the dropdown menu
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = gender,
                                onValueChange = {},
                                label = { Text("Gender") },
                                readOnly = true,
                                enabled = true,
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    IconButton(onClick = { genderMenuExpanded = true }) {
                                        Icon(Icons.Default.ArrowDropDown, "Show gender options")
                                    }
                                },
                                colors = textFieldColors
                            )

                            DropdownMenu(
                                expanded = genderMenuExpanded,
                                onDismissRequest = { genderMenuExpanded = false },
                                modifier = Modifier.width(IntrinsicSize.Max)
                            ) {
                                genders.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            gender = option
                                            genderMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Other fields
                item {
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        enabled = isEditMode,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                }

                item {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        enabled = isEditMode,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                }

                item {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Address") },
                        enabled = isEditMode,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                }

                // Blood Group Dropdown - Fixed Implementation
                item {
                    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
                    var bloodGroupMenuExpanded by remember { mutableStateOf(false) }

                    // For view mode: just display the value with proper styling
                    if (!isEditMode) {
                        OutlinedTextField(
                            value = bloodGroup,
                            onValueChange = {},
                            label = { Text("Blood Group") },
                            enabled = false,
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors
                        )
                    } else {
                        // For edit mode: use the dropdown menu
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = bloodGroup,
                                onValueChange = {},
                                label = { Text("Blood Group") },
                                readOnly = true,
                                enabled = true,
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    IconButton(onClick = { bloodGroupMenuExpanded = true }) {
                                        Icon(Icons.Default.ArrowDropDown, "Show blood group options")
                                    }
                                },
                                colors = textFieldColors
                            )

                            DropdownMenu(
                                expanded = bloodGroupMenuExpanded,
                                onDismissRequest = { bloodGroupMenuExpanded = false },
                                modifier = Modifier.width(IntrinsicSize.Max)
                            ) {
                                bloodGroups.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            bloodGroup = option
                                            bloodGroupMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = emergencyContact,
                        onValueChange = { emergencyContact = it },
                        label = { Text("Emergency Contact") },
                        enabled = isEditMode,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                }

                item {
                    OutlinedTextField(
                        value = medicalConditions,
                        onValueChange = { medicalConditions = it },
                        label = { Text("Medical Conditions") },
                        enabled = isEditMode,
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = textFieldColors
                    )
                }

                if (isEditMode) {
                    item {
                        Button(
                            onClick = {
                                val updatedProfile = UserProfile(
                                    userId = profile?.userId ?: "",
                                    fullName = fullName,
                                    dateOfBirth = dateOfBirth,
                                    gender = gender,
                                    phoneNumber = phoneNumber,
                                    email = email,
                                    address = address,
                                    bloodGroup = bloodGroup,
                                    emergencyContact = emergencyContact,
                                    medicalConditions = medicalConditions
                                )
                                viewModel.saveProfile(updatedProfile)
                                isEditMode = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.elevatedButtonElevation(5.dp)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val localDateTime = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(millis),
                            ZoneId.systemDefault()
                        )
                        dateOfBirth =
                            localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}