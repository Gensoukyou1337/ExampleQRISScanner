package com.ivan.qrisscanner.screens.common.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TwoStackedButtonDialog(
    title: String,
    content: String,
    positiveBtnText: String,
    negativeBtnText: String,
    onDismissRequest: () -> Unit,
    onPositiveButtonClicked: () -> Unit,
    onNegativeButtonClicked: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title
                )
                Text(
                    text = content
                )
                Button(
                    onClick = onPositiveButtonClicked
                ) {
                    Text(text = positiveBtnText)
                }
                Button(
                    onClick = onNegativeButtonClicked
                ) {
                    Text(text = negativeBtnText)
                }
            }
        }
    }
}

@Composable
fun SingleButtonDialog(
    title: String,
    content: String,
    btnText: String,
    onDismissRequest: () -> Unit,
    onSingleButtonClicked: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title
                )
                Text(
                    text = content
                )
                Button(
                    onClick = onSingleButtonClicked
                ) {
                    Text(text = btnText)
                }
            }
        }
    }
}
