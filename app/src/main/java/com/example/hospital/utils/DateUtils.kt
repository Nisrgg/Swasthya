package com.example.hospital.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun stamp(timestamp: Timestamp?): String {
        return timestamp?.toDate()?.let { date ->
            SimpleDateFormat("EEEE, MMM dd 'at' hh:mm a", Locale.getDefault()).format(date)
        } ?: "Invalid Date"
    }

    fun convertSlotToTimestamp(date: Date, time: String): Timestamp {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val dateTimeString = "${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)} $time"
        val parsedDate = sdf.parse(dateTimeString)
        return Timestamp(parsedDate!!)
    }
}
