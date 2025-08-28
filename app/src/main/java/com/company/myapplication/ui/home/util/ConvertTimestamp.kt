package com.company.myapplication.ui.home.util

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

fun convertTimestamp(timestampStr: String): String {
    val formatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true) // cho phép 0–6 số thập phân
        .optionalEnd()
        .toFormatter()
    val ts = LocalDateTime.parse(timestampStr, formatter)
    val now = LocalDateTime.now()

    val daysDiff = ChronoUnit.DAYS.between(ts.toLocalDate(), now.toLocalDate())

//    Log.e("TIME", daysDiff.toString())

    return when (daysDiff) {
        0L -> ts.plusHours(7).format(DateTimeFormatter.ofPattern("HH:mm")) // cùng ngày → hiển thị giờ:phút
        1L -> "Hôm qua"
        2L -> "Ngày kia"
        else -> ts.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) // >=3 ngày thì hiện ngày
    }
}