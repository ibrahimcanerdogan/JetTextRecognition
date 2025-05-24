package com.ibrahimcanerdogan.jettextrecognition.domain.model

data class TextRecognitionResult(
    val text: String,
    val confidence: Float,
    val timestamp: Long = System.currentTimeMillis()
) 