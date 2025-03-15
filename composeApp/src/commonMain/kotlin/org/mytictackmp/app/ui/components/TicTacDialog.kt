package org.mytictackmp.app.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mytictackmp.app.game.GameDialog


@Composable
fun TicTacDialog(gameDialog: GameDialog, onConfirm: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text(text = gameDialog.title) },
        text = { Text(text = gameDialog.message) },
        confirmButton = {
            Button(onClick = {
                onConfirm()
            }) {
                Text(text = gameDialog.confirmButtonText)
            }
        },
        dismissButton = {
            Button(onClick = {
                onCancel()
            }) {
                Text(text = gameDialog.cancelButtonText)
            }
        },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.LightGray
    )
}

@Preview
@Composable
fun PreviewGameDialog() {
    TicTacDialog(
        gameDialog = GameDialog.CancelGame,
        onConfirm = {},
        onCancel = {}
    )
}
