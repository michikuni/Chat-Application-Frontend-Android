package com.company.myapplication.ui.home.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

fun convertTimestamp(timestampStr: String?): String {
    if (timestampStr.isNullOrBlank() || timestampStr == "null") {
        return "N/A"
    }

    return try {
        // Parse chuỗi Instant (UTC)
        val instant = Instant.parse(timestampStr)

        // ✅ Chuyển sang múi giờ Việt Nam (GMT+7)
        val ts = instant.atZone(ZoneOffset.ofHours(7)).toLocalDateTime()
        val now = LocalDateTime.now(ZoneOffset.ofHours(7))

        val daysDiff = ChronoUnit.DAYS.between(ts.toLocalDate(), now.toLocalDate())

        when (daysDiff) {
            0L -> ts.format(DateTimeFormatter.ofPattern("HH:mm")) // cùng ngày → hiển thị giờ:phút
            1L -> "Hôm qua"
            2L -> "Ngày kia"
            else -> ts.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) // >=3 ngày thì hiện ngày
        }
    } catch (e: Exception) {
        // Nếu không phải dạng Instant (ví dụ: yyyy-MM-dd HH:mm:ss)
        try {
            val formatter = DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                .optionalEnd()
                .toFormatter()

            val ts = LocalDateTime.parse(timestampStr, formatter)
                .plusHours(7) // ✅ Cộng thêm 7 tiếng nếu dữ liệu gốc không có múi giờ
            val now = LocalDateTime.now(ZoneOffset.ofHours(7))

            val daysDiff = ChronoUnit.DAYS.between(ts.toLocalDate(), now.toLocalDate())

            when (daysDiff) {
                0L -> ts.format(DateTimeFormatter.ofPattern("HH:mm"))
                1L -> "Hôm qua"
                2L -> "Ngày kia"
                else -> ts.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
        } catch (_: Exception) {
            "N/A"
        }
    }
}
