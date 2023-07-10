package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.rittau.mould.ui.theme.MouldTheme

const val PROGRESS_BAR_SIZE = 10

@Composable
fun ProgressBar(filled: Int, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        for (i in 0 until PROGRESS_BAR_SIZE) {
            val thisFilled = (filled - i * 4).coerceIn(0..4)
            ProgressBox(thisFilled)
        }
    }
}

@Composable
fun ProgressBox(
    filled: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Box(modifier = modifier
        .size(20.dp)
        .border(.5.dp, color)
        .drawBehind {
            if (filled >= 1) {
                drawLine(
                    color,
                    Offset(8f, size.height - 8f),
                    Offset(size.width - 8f, 8f),
                    strokeWidth = 4f,
                )
            }
            if (filled >= 2) {
                drawLine(
                    color,
                    Offset(8f, 8f),
                    Offset(size.width - 8f, size.height - 8f),
                    strokeWidth = 4f,
                )
            }
            if (filled >= 3) {
                drawLine(
                    color,
                    Offset(size.width / 2, 4f),
                    Offset(size.width / 2, size.height - 4f),
                    strokeWidth = 4f,
                )
            }
            if (filled >= 4) {
                drawLine(
                    color,
                    Offset(4f, size.height / 2),
                    Offset(size.width - 4f, size.height / 2),
                    strokeWidth = 4f,
                )
            }
        })
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProgressBarPreview() {
    MouldTheme {
        ProgressBar(10)
    }
}
