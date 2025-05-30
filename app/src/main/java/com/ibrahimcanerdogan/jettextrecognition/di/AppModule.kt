package com.ibrahimcanerdogan.jettextrecognition.di

import com.ibrahimcanerdogan.jettextrecognition.data.repository.TextRecognitionRepositoryImpl
import com.ibrahimcanerdogan.jettextrecognition.domain.repository.TextRecognitionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    @Binds
    @Singleton
    abstract fun bindTextRecognitionRepository(
        textRecognitionRepositoryImpl: TextRecognitionRepositoryImpl
    ): TextRecognitionRepository
} 