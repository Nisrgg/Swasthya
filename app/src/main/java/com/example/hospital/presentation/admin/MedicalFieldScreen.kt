package com.example.hospital.presentation.admin

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalFieldSelectionScreen(navController: NavController) {
    val context = LocalContext.current
    // Load the specialization map from the JSON asset.
    val specializationMap = remember { loadDoctorSpecializationMap(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Medical Field") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // For each specialization, display a card.
            items(specializationMap.entries.toList()) { entry ->
                SpecializationCard(
                    specialization = entry.key,
                    onClick = {
                        // Combine the list of doctor IDs into a comma-separated string.
                        val joinedIds = entry.value.joinToString(",")
                        navController.navigate("doctor_list/${entry.key}/$joinedIds")
                    }
                )
            }
        }
    }
}

@Composable
fun SpecializationCard(specialization: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = specialization,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

fun loadDoctorSpecializationMap(context: Context): Map<String, List<String>> {
    return try {
        // Open and read the JSON file from assets.
        val inputStream = context.assets.open("doctBySpec.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        val jsonString = String(buffer, Charset.forName("UTF-8"))
        val jsonObject = JSONObject(jsonString)
        val result = mutableMapOf<String, List<String>>()

        // Parse each key/value pair. Values can be a single String or a JSONArray.
        jsonObject.keys().forEach { key ->
            val value = jsonObject.get(key)
            val ids = when (value) {
                is String -> listOf(value)
                is JSONArray -> List(value.length()) { index -> value.getString(index) }
                else -> emptyList()
            }
            result[key] = ids
        }
        result
    } catch (e: Exception) {
        e.printStackTrace()
        emptyMap()
    }
}