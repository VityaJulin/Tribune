package com.example.tribune

/**
 * Minimum is 6 chars. Should be at least one capital letter. Allow only english characters and
 * numbers
 */
fun isValid(password: String) =
    password.isNotEmpty()

fun getTimeAgo(create: Int): String {
    val current = (System.currentTimeMillis() / 1000).toInt()
    val create = create
    val periodMinutes: Int = (current - create) / 60
    var hour = 0
    var minutes = 0

    return when {
        periodMinutes < 60 -> {
            minutes = periodMinutes
            "posted $minutes minutes ago"
        }
        else -> {
            hour = (periodMinutes / 60).toInt()
            minutes = periodMinutes - (hour * 60)
            "posted $hour hour $minutes minutes ago"
        }
    }
}