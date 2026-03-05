package com.ivan.qrisscanner.screens.scanner

import android.Manifest
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ivan.qrisscanner.Route
import com.ivan.qrisscanner.enums.DBInsertState
import com.ivan.qrisscanner.screens.common.dialogs.SingleButtonDialog
import com.ivan.qrisscanner.screens.common.dialogs.TwoStackedButtonDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRScannerScreen(
    activityNavController: NavController,
    onSignalOpenAppSettings: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalActivity.current

    /*
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!cameraPermissionState.status.isGranted) {
                    cameraPermissionState.launchPermissionRequest()
                }
                // viewModel.isConnected(ssid, wifiManager)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    */

    LaunchedEffect(true) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.status is PermissionStatus.Granted) {
        QRScannerContent(activityNavController)
    } else {
        TwoStackedButtonDialog(
            title = stringResource(com.ivan.qrisscanner.R.string.permissions_dialog_title),
            content = stringResource(com.ivan.qrisscanner.R.string.permissions_dialog_content),
            positiveBtnText = stringResource(com.ivan.qrisscanner.R.string.permissions_dialog_pos_btn_text),
            negativeBtnText = stringResource(com.ivan.qrisscanner.R.string.permissions_dialog_neg_btn_text),
            onPositiveButtonClicked = {
                onSignalOpenAppSettings()
            },
            onNegativeButtonClicked = {
                activity?.finish()
            },
            onDismissRequest = {}
        )
    }
}

@Composable
fun QRScannerContent(
    activityNavController: NavController,
    viewModel: QRScannerViewModel = koinViewModel()
) {
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
    val isFlashEnabled by viewModel.isFlashEnabled.collectAsStateWithLifecycle()
    val lastTransactionSuccess by viewModel.lastTransactionWasSuccessful.collectAsStateWithLifecycle()
    val displayFailureDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val coordinateTransformer = remember {
        MutableCoordinateTransformer()
    }

    val pickMedia =
        rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.scanImageUriWithBarcodeScanner(context, uri)
            }
        }

    fun onQROrAztecDetected(rawValue: String) {
        viewModel.insertTransactionDataToDB(rawValue)
    }

    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(
            context,
            lifecycleOwner,
            ::onQROrAztecDetected
        )
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            viewModel.reset()
        }
    }

    LaunchedEffect(lastTransactionSuccess) {
        when (lastTransactionSuccess) {
            DBInsertState.Idle, DBInsertState.Loading -> {}
            DBInsertState.Success -> {
                activityNavController.navigate(Route.History.routeName)
                viewModel.reset()
            }

            DBInsertState.Fail -> {
                displayFailureDialog.value = true
                viewModel.reset()
            }
        }
    }

    Box(contentAlignment = Alignment.BottomCenter) {
        surfaceRequest?.let { request ->
            CameraXViewfinder(
                surfaceRequest = request,
                coordinateTransformer = coordinateTransformer,
                modifier = Modifier.fillMaxSize()
            )
        }
        Row(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 56.dp),
        ) {
            IconButton(
                onClick = {
                    viewModel.setFlashEnabled(!isFlashEnabled)
                },
                shape = CircleShape,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(red = 0x0E, green = 0x0E, blue = 0x0E),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    painter = painterResource(
                        if (isFlashEnabled) {
                            com.ivan.qrisscanner.R.drawable.ic_flash_off
                        } else {
                            com.ivan.qrisscanner.R.drawable.ic_flash_on
                        }
                    ),
                    contentDescription = "Flash Button"
                )
            }
            Spacer(Modifier.weight(1.0f))
            IconButton(
                onClick = {
                    pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                },
                shape = CircleShape,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(red = 0x0E, green = 0x0E, blue = 0x0E),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    painter = painterResource(com.ivan.qrisscanner.R.drawable.ic_add_photo),
                    contentDescription = "Picture Picker Button"
                )
            }
        }

        if (displayFailureDialog.value) {
            SingleButtonDialog(
                title = stringResource(com.ivan.qrisscanner.R.string.invalid_qr_dialog_title),
                content = stringResource(com.ivan.qrisscanner.R.string.invalid_qr_dialog_content),
                btnText = stringResource(com.ivan.qrisscanner.R.string.invalid_qr_dialog_btn_text),
                onSingleButtonClicked = { displayFailureDialog.value = false },
                onDismissRequest = { displayFailureDialog.value = false }
            )
        }
    }
}