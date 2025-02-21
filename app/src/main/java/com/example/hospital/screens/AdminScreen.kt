package com.example.hospital.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Text

@Composable
fun AdminScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("add_doctor") }) {
            Text("Add Doctor")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigate("remove_doctor") }) {
            Text("Remove Doctor")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminScreenPreview(){
    val navController = rememberNavController()
    AdminScreen(navController)
}
