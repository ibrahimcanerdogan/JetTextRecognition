package com.ibrahimcanerdogan.jettextrecognition.data.repository

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.ibrahimcanerdogan.jettextrecognition.domain.model.TextRecognitionResult
import com.ibrahimcanerdogan.jettextrecognition.domain.repository.TextRecognitionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class TextRecognitionRepositoryImpl @Inject constructor() : TextRecognitionRepository {
    
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override suspend fun recognizeText(bitmap: Bitmap): Flow<TextRecognitionResult> = callbackFlow {
        val image = InputImage.fromBitmap(bitmap, 0)
        
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val result = TextRecognitionResult(
                    text = visionText.text,
                    confidence = calculateConfidence(visionText)
                )
                trySend(result)
            }
            .addOnFailureListener { e ->
                close(e)
            }
        
        awaitClose()
    }

    override suspend fun checkModelAvailability(): Boolean {
        return try {
            // ML Kit doesn't provide a direct way to check model availability
            // We'll assume it's available and handle any errors during processing
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun calculateConfidence(visionText: com.google.mlkit.vision.text.Text): Float {
        // Since ML Kit doesn't provide direct confidence scores,
        // we'll calculate a simple confidence based on text block count and text length
        val textBlocks = visionText.textBlocks
        if (textBlocks.isEmpty()) return 0f
        
        // Calculate confidence based on text block count and text length
        val blockCount = textBlocks.size
        val totalLength = visionText.text.length
        
        // Simple confidence calculation (can be adjusted based on your needs)
        return (blockCount * 0.3f + (totalLength / 100f) * 0.7f).coerceIn(0f, 1f)
    }
} 