package com.ivan.qrisscanner.screens.scanner

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.ivan.domain.repository.TransactionRepository
import com.ivan.qrisscanner.enums.DBInsertState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.Executors
import java.util.regex.Pattern

class QRScannerViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private val _isFlashEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFlashEnabled: StateFlow<Boolean> get() = _isFlashEnabled

    private val _lastTransactionWasSuccessful: MutableStateFlow<DBInsertState> = MutableStateFlow(
        DBInsertState.Idle
    )
    val lastTransactionWasSuccessful: StateFlow<DBInsertState> get() = _lastTransactionWasSuccessful

    private var currentQRDataInView: MutableSet<String> = mutableSetOf()

    private var cameraControl: CameraControl? = null
    private val barcodeScanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
            .build()
    )

    private val invalidRegex =
        Pattern.compile("^.*(?:\\\\.js|\\\"src\\\"|'src'|\\\\.append|http:|https:|<script>|onload=|<|>).*")

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
        }
    }

    suspend fun bindToCamera(
        localContext: Context,
        lifecycleOwner: LifecycleOwner,
        onQROrAztecDetected: (value: String) -> Unit
    ) {
        val processCameraProvider =
            ProcessCameraProvider.awaitInstance(localContext.applicationContext)

        val cameraExecutor = Executors.newSingleThreadExecutor()
        val imageAnalyzer = MlKitAnalyzer(
            listOf(barcodeScanner),
            ImageAnalysis.COORDINATE_SYSTEM_ORIGINAL,
            cameraExecutor
        ) { result: MlKitAnalyzer.Result? ->
            val data = result?.getValue(barcodeScanner)
            data?.forEachIndexed { i, barcode ->
                barcode.rawValue?.let {
                    if (!currentQRDataInView.contains(it)) {
                        currentQRDataInView.add(it)
                        if (i == 0) {
                            Log.d("qrscanner", "adding qr data in view $it")
                            onQROrAztecDetected(it)
                        }
                    }
                }
            }
            val taggedForRemoval = mutableSetOf<String>()
            val dataAsRawValues = data?.map { code -> code.rawValue }
            currentQRDataInView.forEach {
                if (dataAsRawValues?.contains(it) == false) {
                    taggedForRemoval.add(it)
                }
            }
            currentQRDataInView.removeAll(taggedForRemoval)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().apply {
                setAnalyzer(cameraExecutor, imageAnalyzer)
            }

        val camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            cameraPreviewUseCase,
            imageAnalysis
        )
        cameraControl = camera.cameraControl

        // Cancellation signals we're done with the camera
        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
            cameraControl = null
            cameraExecutor.shutdown()
        }
    }

    fun setFlashEnabled(flashEnabled: Boolean) {
        _isFlashEnabled.value = flashEnabled

        cameraControl?.enableTorch(_isFlashEnabled.value)
    }

    fun insertTransactionDataToDB(
        rawValue: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _lastTransactionWasSuccessful.value = DBInsertState.Loading

            val matcher = invalidRegex.matcher(rawValue)
            val hasInvalidThings = matcher.find()

            if (hasInvalidThings) {
                // Display invalid QR Code
                _lastTransactionWasSuccessful.value = DBInsertState.Fail
            } else {
                // Continue to History
                val result = repository.insertTransactionData(rawValue)
                if (result) {
                    _lastTransactionWasSuccessful.value = DBInsertState.Success
                } else {
                    _lastTransactionWasSuccessful.value = DBInsertState.Fail
                }
            }
        }
    }

    fun scanImageUriWithBarcodeScanner(localContext: Context, imageUri: Uri) {
        val image: InputImage
        try {
            image = InputImage.fromFilePath(localContext, imageUri)
            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    val firstRawValue = barcodes.firstOrNull()?.rawValue
                    if (firstRawValue != null) {
                        insertTransactionDataToDB(firstRawValue)
                    } else {
                        _lastTransactionWasSuccessful.value = DBInsertState.Fail
                    }
                }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun reset() {
        _lastTransactionWasSuccessful.value = DBInsertState.Idle
    }
}