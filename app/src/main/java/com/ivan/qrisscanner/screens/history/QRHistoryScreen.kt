package com.ivan.qrisscanner.screens.history

import android.icu.text.NumberFormat
import android.icu.util.Currency
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ivan.qrisscanner.ui.theme.ThematicGrey
import com.ivan.qrisscanner.ui.theme.ThematicLightGrey
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
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = ::backToScanner
                ) {
                    Icon(
                        painterResource(com.ivan.qrisscanner.R.drawable.ic_arrow_back),
                        contentDescription = "Back to Scanner"
                    )
                }

                Text(
                    text = stringResource(com.ivan.qrisscanner.R.string.qr_history_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W700
                )
            }
        }
    ) { contentPadding ->
        Box(
            Modifier.padding(contentPadding)
        ) {
            LazyColumn {
                items(transactionsList.value.size) { index ->
                    Column(Modifier.padding(horizontal = 24.dp)) {
                        Spacer(Modifier.height(16.dp))
                        Row {
                            Text(
                                text = transactionsList.value[index].merchantName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400,
                                modifier = Modifier.weight(1.0f)
                            )
                            Text(
                                text = idrFormatter.format(transactionsList.value[index].transactionAmount),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W700,
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = transactionsList.value[index].merchantLocation,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            color = ThematicGrey
                        )
                        Spacer(Modifier.height(16.dp))
                        if (index < transactionsList.value.size - 1) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(
                                        ThematicLightGrey
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}