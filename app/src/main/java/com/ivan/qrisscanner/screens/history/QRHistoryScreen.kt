package com.ivan.qrisscanner.screens.history

import android.icu.text.NumberFormat
import android.icu.util.Currency
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

private val idrFormatter = NumberFormat.getCurrencyInstance().apply {
    maximumFractionDigits = 0
    currency = Currency.getInstance("IDR")
}

@Composable
fun QRHistoryScreen(
    activityNavController: NavController,
    viewModel: QRHistoryViewModel = koinViewModel(),
) {
    val transactionsList = viewModel.transactionsList.collectAsStateWithLifecycle()

    fun backToScanner() {
        activityNavController.popBackStack()
    }

    LaunchedEffect(true) {
        viewModel.getAllTransactions()
    }

    Scaffold(
        modifier = Modifier.safeContentPadding(),
        topBar = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = ::backToScanner
                ) {
                    Icon(
                        painterResource(com.ivan.qrisscanner.R.drawable.ic_arrow_back),
                        contentDescription = "Back to Scanner"
                    )
                }

                Text("Transaction History")
            }
        }
    ) { contentPadding ->
        Box(
            Modifier.padding(contentPadding)
        ) {
            LazyColumn {
                items(transactionsList.value.size) { index ->
                    Column {
                        Row {
                            Text(
                                text = transactionsList.value[index].merchantName,
                                modifier = Modifier.weight(1.0f)
                            )
                            Text(text = idrFormatter.format(transactionsList.value[index].transactionAmount))
                        }
                        Text(text = transactionsList.value[index].merchantLocation)
                    }
                }
            }
        }
    }
}