package com.googledevteam.bookfinder.camera

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.viewfinder.core.ImplementationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun CameraCapture(
    onCaptureStarted: () -> Unit,
    onTextRecognized: (String) -> Unit,
    onCaptureError: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = remember(context) { ContextCompat.getMainExecutor(context) }
    val preview = remember { Preview.Builder().build() }
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    val providerFuture = remember(context) { ProcessCameraProvider.getInstance(context) }
    var surfaceRequest by remember { mutableStateOf<SurfaceRequest?>(null) }
    var cameraReady by remember { mutableStateOf(false) }
    var isCapturing by remember { mutableStateOf(false) }

    DisposableEffect(providerFuture, lifecycleOwner) {
        var disposed = false
        var boundProvider: ProcessCameraProvider? = null

        preview.setSurfaceProvider(mainExecutor) { request ->
            surfaceRequest = request
        }

        providerFuture.addListener(
            {
                if (!disposed) {
                    try {
                        val provider = providerFuture.get()
                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageCapture,
                        )
                        boundProvider = provider
                        cameraReady = true
                    } catch (exception: Exception) {
                        onCaptureError(exception.localizedMessage ?: "Could not start the camera.")
                    }
                }
            },
            mainExecutor,
        )

        onDispose {
            disposed = true
            boundProvider?.unbind(preview, imageCapture)
            cameraReady = false
            surfaceRequest = null
        }
    }

    Box(
        modifier = modifier
            .height(360.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFF17201A)),
        contentAlignment = Alignment.Center,
    ) {
        val request = surfaceRequest
        if (request == null) {
            CircularProgressIndicator(color = Color.White)
        } else {
            CameraXViewfinder(
                surfaceRequest = request,
                modifier = Modifier.fillMaxSize(),
                implementationMode = ImplementationMode.EMBEDDED,
                isTapToFocusEnabled = true,
            )
        }
    }

    androidx.compose.material3.Button(
        modifier = Modifier,
        enabled = cameraReady && !isCapturing,
        onClick = {
            isCapturing = true
            onCaptureStarted()
            imageCapture.takePicture(
                mainExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(imageProxy: androidx.camera.core.ImageProxy) {
                        val mediaImage = imageProxy.image
                        if (mediaImage == null) {
                            imageProxy.close()
                            isCapturing = false
                            onCaptureError("The camera returned an empty image. Try again.")
                            return
                        }

                        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                        recognizer.process(
                            InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees,
                            ),
                        )
                            .addOnSuccessListener { result -> onTextRecognized(result.text) }
                            .addOnFailureListener { exception ->
                                onCaptureError(exception.localizedMessage ?: "Text recognition failed.")
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                                recognizer.close()
                                isCapturing = false
                            }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        isCapturing = false
                        onCaptureError(exception.localizedMessage ?: "Could not capture the page.")
                    }
                },
            )
        },
    ) {
        androidx.compose.material3.Text(if (isCapturing) "Reading page…" else "Capture page")
    }
}
