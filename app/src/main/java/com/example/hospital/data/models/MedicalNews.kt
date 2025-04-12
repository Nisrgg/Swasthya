package com.example.hospital.data.models

data class MedicalNews(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val author: String = "",
    val datePublished: Long = System.currentTimeMillis(),
    val sourceUrl: String = ""                  // ✅ URL to the original article
)
