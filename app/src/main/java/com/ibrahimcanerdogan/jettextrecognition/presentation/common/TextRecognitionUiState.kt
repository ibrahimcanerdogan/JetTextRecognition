package com.ibrahimcanerdogan.jettextrecognition.presentation.common

import com.ibrahimcanerdogan.jettextrecognition.domain.model.TextRecognitionResult

sealed class TextRecognitionUiState {
    data object Initial : TextRecognitionUiState()
    data object Loading : TextRecognitionUiState()
    data class Success(val result: TextRecognitionResult) : TextRecognitionUiState()
    data class Warning(val message: String) : TextRecognitionUiState()
    data class Error(val message: String) : TextRecognitionUiState()
} 