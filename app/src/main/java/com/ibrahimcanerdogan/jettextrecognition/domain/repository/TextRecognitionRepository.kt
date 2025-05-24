package com.ibrahimcanerdogan.jettextrecognition.domain.repository

import android.graphics.Bitmap
import com.ibrahimcanerdogan.jettextrecognition.domain.model.TextRecognitionResult
import kotlinx.coroutines.flow.Flow

interface TextRecognitionRepository {
    suspend fun recognizeText(bitmap: Bitmap): Flow<TextRecognitionResult>
    suspend fun checkModelAvailability(): Boolean
} 