package com.ivan.qrisscanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ivan.qrisscanner.screens.history.QRHistoryScreen
import com.ivan.qrisscanner.screens.scanner.QRScannerScreen
import com.ivan.qrisscanner.ui.theme.QRISScannerTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRISScannerTheme {
                MainNavHost(::openAppSettings)
            }
        }
    }

    private fun openAppSettings() {
        val context = this
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        context.startActivity(intent)
    }
}

@Serializable
sealed class Route(val routeName: String) {
    object Scanner : Route("Scanner")
    object History : Route("History")
}

@Composable
fun MainNavHost(
    openAppSettingsClosure: () -> Unit
) {
    val activityNavController = rememberNavController()

    NavHost(
        navController = activityNavController,
        startDestination = Route.Scanner.routeName
    ) {
        composable(
            route = Route.Scanner.routeName
        ) {
            QRScannerScreen(
                activityNavController = activityNavController,
                onSignalOpenAppSettings = openAppSettingsClosure
            )
        }
        composable(
            route = Route.History.routeName
        ) {
            QRHistoryScreen(activityNavController = activityNavController)
        }
    }
}