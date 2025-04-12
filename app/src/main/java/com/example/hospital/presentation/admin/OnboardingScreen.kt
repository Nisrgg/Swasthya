package com.example.hospital.presentation.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hospital.data.models.UserProfile
import com.example.hospital.data.viewmodels.UserProfileViewModel

@Composable
fun OnboardingScreen(
    userId: String,
    navController: NavController,
    viewModel: UserProfileViewModel = viewModel(),
    existingProfile: UserProfile? = null,
    onProfileSaved: () -> Unit
) {
    var name by remember { mutableStateOf(existingProfile?.name ?: "") }
    var age by remember { mutableStateOf(existingProfile?.age?.toString() ?: "") }
    var height by remember { mutableStateOf(existingProfile?.height?.toString() ?: "") }
    var weight by remember { mutableStateOf(existingProfile?.weight?.toString() ?: "") }
    var bloodGroup by remember { mutableStateOf(existingProfile?.blood_group ?: "") }
    var hasFamilyHistory by remember { mutableStateOf(existingProfile?.has_family_history ?: false) }
    var familyDetails by remember { mutableStateOf(existingProfile?.family_history_details ?: "") }
    var hasAllergies by remember { mutableStateOf(existingProfile?.has_allergies ?: false) }
    var allergyDetails by remember { mutableStateOf(existingProfile?.allergy_details ?: "") }
    var hasMedications by remember { mutableStateOf(existingProfile?.has_medications ?: false) }
    var medicationDetails by remember { mutableStateOf(existingProfile?.medication_details ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("${if (existingProfile == null) "Welcome! Let's build" else "Edit"} your profile", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
        OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Height (cm)") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
        OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
        OutlinedTextField(value = bloodGroup, onValueChange = { bloodGroup = it }, label = { Text("Blood Group") })

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = hasFamilyHistory, onCheckedChange = { hasFamilyHistory = it })
            Text("Family Medical History?")
        }
        if (hasFamilyHistory) {
            OutlinedTextField(value = familyDetails, onValueChange = { familyDetails = it }, label = { Text("Details") })
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = hasAllergies, onCheckedChange = { hasAllergies = it })
            Text("Allergies?")
        }
        if (hasAllergies) {
            OutlinedTextField(value = allergyDetails, onValueChange = { allergyDetails = it }, label = { Text("Details") })
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = hasMedications, onCheckedChange = { hasMedications = it })
            Text("Ongoing Medications?")
        }
        if (hasMedications) {
            OutlinedTextField(value = medicationDetails, onValueChange = { medicationDetails = it }, label = { Text("Details") })
        }

        Button(
            onClick = {
                val profile = UserProfile(
                    name = name,
                    age = age.toIntOrNull() ?: 0,
                    height = height.toFloatOrNull() ?: 0f,
                    weight = weight.toFloatOrNull() ?: 0f,
                    blood_group = bloodGroup,
                    has_family_history = hasFamilyHistory,
                    family_history_details = familyDetails,
                    has_allergies = hasAllergies,
                    allergy_details = allergyDetails,
                    has_medications = hasMedications,
                    medication_details = medicationDetails
                )
                viewModel.saveUserProfile(userId, profile)
                onProfileSaved()
            },
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        ) {
            Text("${if (existingProfile == null) "Submit" else "Update"} Profile")
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
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(label, modifier = Modifier.weight(1f))
            Switch(checked = checked, onCheckedChange = onToggle)
        }
        if (checked) {
            Spacer(modifier = Modifier.height(8.dp))
            ifYesContent()
        }
    }
}

@Composable
fun DropdownMenuBox(
    options: List<String>,
    selected: String,
    label: String,
    onSelected: (String) -> Unit
) {
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
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        onSelected(option)
                    }
                )
            }
        }
    }
}

