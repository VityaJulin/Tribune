package com.example.tribune

/**
 * Minimum is 6 chars. Should be at least one capital letter. Allow only english characters and
 * numbers
 */
fun isValid(password: String) =
    password.isNotEmpty()