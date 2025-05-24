package com.ibrahimcanerdogan.jettextrecognition.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ibrahimcanerdogan.jettextrecognition.presentation.camera.CameraScreen
import com.ibrahimcanerdogan.jettextrecognition.presentation.text.TextRecognitionScreen
import com.ibrahimcanerdogan.jettextrecognition.presentation.text.TextRecognitionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import android.os.Build
import androidx.compose.foundation.background

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModel: TextRecognitionViewModel = hiltViewModel()

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    var hasGalleryPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            viewModel.recognizeText(bitmap)
            navController.navigate("text_recognition")
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasGalleryPermission = isGranted
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            snackbarMessage = "Galeri izni gerekli. Ayarlardan izin verebilirsiniz"
            showSnackbar = true
        }
    }

    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(
                    onCameraClick = { navController.navigate("camera") },
                    onGalleryClick = {
                        if (hasGalleryPermission) {
                            galleryLauncher.launch("image/*")
                        } else {
                            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                Manifest.permission.READ_MEDIA_IMAGES
                            } else {
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            }
                            permissionLauncher.launch(permission)
                        }
                    }
                )
            }
            composable("camera") {
                CameraScreen(
                    onImageCaptured = { file ->
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        viewModel.recognizeText(bitmap)
                        navController.navigate("text_recognition")
                    },
                    onError = { message ->
                        snackbarMessage = message
                        showSnackbar = true
                    }
                )
            }
            composable("text_recognition") {
                TextRecognitionScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.navigateUp() }
                )
            }
        }

        if (showSnackbar) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(
                        onClick = {
                            showSnackbar = false
                            // Open app settings
                            val intent = android.content.Intent(
                                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                android.net.Uri.fromParts("package", context.packageName, null)
                            )
                            context.startActivity(intent)
                        }
                    ) {
                        Text("Ayarlara Git")
                    }
                },
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ) {
                Text(snackbarMessage)
            }
        }
    }
}

@Composable
fun HomeScreen(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Logo ve Başlık
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TextFields,
                    contentDescription = "App Logo",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Metin Tanıma",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Fotoğraf çekin veya galeriden seçin",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Butonlar
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ElevatedButton(
                        onClick = onCameraClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 8.dp,
                            hoveredElevation = 8.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Kamera",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Fotoğraf Çek",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    ElevatedButton(
                        onClick = onGalleryClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 8.dp,
                            hoveredElevation = 8.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Galeri",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Galeriden Seç",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
} 