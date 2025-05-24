package com.ibrahimcanerdogan.jettextrecognition.presentation.text

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimcanerdogan.jettextrecognition.domain.repository.TextRecognitionRepository
import com.ibrahimcanerdogan.jettextrecognition.presentation.common.TextRecognitionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextRecognitionViewModel @Inject constructor(
    private val repository: TextRecognitionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TextRecognitionUiState>(TextRecognitionUiState.Initial)
    val uiState: StateFlow<TextRecognitionUiState> = _uiState

    fun recognizeText(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = TextRecognitionUiState.Loading
            
            repository.recognizeText(bitmap)
                .catch { e ->
                    _uiState.value = TextRecognitionUiState.Error(e.message ?: "Bilinmeyen bir hata oluştu")
                }
                .collect { result ->
                    _uiState.value = when {
                        result.text.isBlank() -> TextRecognitionUiState.Warning("Görselde herhangi bir metin bulunamadı. Lütfen metin içeren bir görsel seçin.")
                        else -> TextRecognitionUiState.Success(result)
                    }
                }
        }
    }

    fun checkModelAvailability() {
        viewModelScope.launch {
            val isAvailable = repository.checkModelAvailability()
            if (!isAvailable) {
                _uiState.value = TextRecognitionUiState.Error("Metin tanıma modeli kullanılamıyor")
            }
        }
    }
} 