package com.example.hospital.presentation.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.data.models.UserProfile
import com.example.hospital.data.viewmodels.UserProfileViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavController


@Composable
fun OnboardingScreen(
    userId: String,
    navController: NavController,
    viewModel: UserProfileViewModel = viewModel(),
    onComplete: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }

    var hasFamilyHistory by remember { mutableStateOf(false) }
    var familyHistoryDetails by remember { mutableStateOf("") }

    var hasAllergies by remember { mutableStateOf(false) }
    var allergyDetails by remember { mutableStateOf("") }

    var hasMedications by remember { mutableStateOf(false) }
    var medicationDetails by remember { mutableStateOf("") }

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    Column(Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Create Profile", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
        OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Height (cm)") })
        OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") })

        Spacer(Modifier.height(12.dp))
        DropdownMenuBox(bloodGroups, selected = bloodGroup, label = "Blood Group") {
            bloodGroup = it
        }

        Spacer(Modifier.height(12.dp))
        YesNoSection("Family Medical History?", hasFamilyHistory, onToggle = { hasFamilyHistory = it }) {
            OutlinedTextField(value = familyHistoryDetails, onValueChange = { familyHistoryDetails = it }, label = { Text("Details") })
        }

        YesNoSection("Allergies?", hasAllergies, onToggle = { hasAllergies = it }) {
            OutlinedTextField(value = allergyDetails, onValueChange = { allergyDetails = it }, label = { Text("Allergy Details") })
        }

        YesNoSection("Ongoing Medications?", hasMedications, onToggle = { hasMedications = it }) {
            OutlinedTextField(value = medicationDetails, onValueChange = { medicationDetails = it }, label = { Text("Medication Details") })
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val profile = UserProfile(
                user_id = userId,
                name = name,
                age = age,
                height = height,
                weight = weight,
                blood_group = bloodGroup,
                family_medical_history = if (hasFamilyHistory) "Yes: $familyHistoryDetails" else "No",
                allergies = if (hasAllergies) "Yes: $allergyDetails" else "No",
                ongoing_medications = if (hasMedications) "Yes: $medicationDetails" else "No"
            )
            viewModel.updateProfileField { profile }
            viewModel.saveProfile {
                if (it) onComplete()
            }
        }) {
            Text("Save Profile")
        }
    }
}

@Composable
fun YesNoSection(
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    ifYesContent: @Composable () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onToggle)
    }
    if (checked) {
        Spacer(Modifier.height(8.dp))
        ifYesContent()
    }
}

@Composable
fun DropdownMenuBox(options: List<String>, selected: String, label: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    expanded = false
                    onSelected(option)
                })
            }
        }
    }
}
