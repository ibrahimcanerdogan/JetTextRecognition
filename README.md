# JetTextRecognition

A modern Android application that leverages Google's ML Kit Text Recognition v2 to perform real-time text recognition from images. Built with Jetpack Compose, this application demonstrates the power of machine learning in mobile applications.

## Features

- **Real-time Text Recognition**: Recognize text in real-time from images using ML Kit's Text Recognition v2
- **Multi-language Support**: Supports text recognition in multiple languages including:
  - Chinese
  - Devanagari
  - Japanese
  - Korean
  - Latin scripts
- **Text Structure Analysis**: Analyzes text structure by detecting:
  - Blocks (paragraphs or columns)
  - Lines (contiguous text on the same axis)
  - Elements (words or characters)
  - Symbols (individual characters)
- **Modern UI**: Built with Jetpack Compose for a modern, responsive user interface
- **Image Selection**: Choose images from gallery or capture new ones
- **Confidence Scoring**: Provides confidence scores for recognized text

## Technical Details

### Text Recognition Structure

The application processes text recognition results in a hierarchical structure:

- **Blocks**: Contiguous sets of text lines (e.g., paragraphs or columns)
- **Lines**: Contiguous sets of words on the same axis
- **Elements**: Contiguous sets of alphanumeric characters (words in Latin languages)
- **Symbols**: Individual alphanumeric characters

For each detected element, the API provides:
- Bounding boxes
- Corner points
- Rotation information
- Confidence scores
- Recognized languages
- Recognized text

### Requirements

- Android 5.0 (API level 21) or higher
- Camera permission
- Storage permission (for accessing gallery images)

### Dependencies

```gradle
dependencies {
    // ML Kit Text Recognition
    implementation 'com.google.mlkit:text-recognition:16.0.0'
    
    // Jetpack Compose
    implementation platform('androidx.compose:compose-bom:2023.10.01')
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.compose.ui:ui-tooling-preview'
}
```

## Usage

1. Launch the application
2. Choose between:
   - Taking a new photo
   - Selecting an image from gallery
3. The application will process the image and display:
   - Recognized text
   - Confidence scores
   - Text structure information

## Best Practices

- Ensure good lighting conditions for optimal text recognition
- Keep the text in focus and avoid blurry images
- For best results, ensure text is clearly visible and not obscured
- Consider the supported languages when processing text

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## Acknowledgments

- Google ML Kit for providing the text recognition capabilities
- Jetpack Compose team for the modern UI toolkit 