package com.example.tribune

import android.content.Intent
import android.graphics.Bitmap

const val AUTHENTICATED_SHARED_KEY = "authenticated_shared_key"
const val API_SHARED_FILE = "API_shared_file"
const val BASE_URL = "https://android-krud-api.herokuapp.com/"
const val USER_ID = "userId"
const val REQUEST_IMAGE_CAPTURE = 1

private const val BITMAP_KEY = "data"
val Intent.bitmap: Bitmap?
    get() = extras?.get(BITMAP_KEY) as? Bitmap?