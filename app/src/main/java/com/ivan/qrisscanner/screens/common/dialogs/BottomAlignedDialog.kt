package com.ivan.qrisscanner.screens.common.dialogs

import android.view.Gravity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.ivan.qrisscanner.ui.theme.ButtonBlue
import com.ivan.qrisscanner.ui.theme.ThematicGrey

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
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true
        )
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.BOTTOM)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 24.dp,
                    bottom = 24.dp
                )
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W700
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = content,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onPositiveButtonClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonBlue,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = positiveBtnText)
                }
                OutlinedButton(
                    onClick = onNegativeButtonClicked,
                    border = BorderStroke(
                        width = 1.dp,
                        color = ThematicGrey
                    ),
                    modifier = Modifier.fillMaxWidth()
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
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true
        )
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.BOTTOM)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 24.dp,
                    bottom = 24.dp
                )
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W700
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = content,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onSingleButtonClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonBlue,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = btnText)
                }
            }
        }
    }
}
