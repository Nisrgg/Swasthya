package com.example.hospital.presentation.patient

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.json.JSONObject
import java.nio.charset.Charset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalFieldSelectionScreen(navController: NavController) {
    val context = LocalContext.current
    val specializationMap = remember { loadDoctorSpecializationMap(context) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Choose Medical Field") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(specializationMap.entries.toList()) { entry ->
                SpecializationCard(
                    specialization = entry.key,
                    onClick = {
                        val joinedIds = entry.value.joinToString(",")
                        navController.navigate("doctor_list/${entry.key}/$joinedIds")
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun SpecializationCard(specialization: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(text = specialization, style = MaterialTheme.typography.titleMedium)
        }
    }
}

fun loadDoctorSpecializationMap(context: Context): Map<String, List<String>> {
    return try {
        val inputStream = context.assets.open("doctBySpec.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        val json = String(buffer, Charset.forName("UTF-8"))
        val jsonObject = JSONObject(json)

        val result = mutableMapOf<String, List<String>>()

        jsonObject.keys().forEach { key ->
            val value = jsonObject.get(key)

            val ids = when (value) {
                is String -> listOf(value)
                is org.json.JSONArray -> List(value.length()) { index -> value.getString(index) }
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
