package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.rittau.mould.ui.theme.MouldTheme

@Composable
fun D6(currentRoll: Int, diceSize: Dp = 100.dp, outline: Boolean = false) {
    val diceColor = MaterialTheme.colorScheme.secondary
    val fontSize = with(LocalDensity.current) { diceSize.toSp() } / 2
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(diceSize)
            .drawBehind {
                drawRoundRect(
                    color = diceColor,
                    topLeft = Offset(40f, 40f),
                    size = Size(size.width - 80f, size.height - 80f),
                    cornerRadius = CornerRadius(8f, 8f),
                    style = if (outline) Stroke(10f) else Fill
                )
            },
    ) {
        Text(
            currentRoll.toString(),
            color = if (outline) diceColor else MaterialTheme.colorScheme.onSecondary,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun d10Path(size: Size): Path {
    val path = Path().apply {
        moveTo(size.width / 2, 10f)
        lineTo(size.width - 10, size.height / 3 * 2)
        lineTo(size.width / 2, size.height - 10)
        lineTo(10f, size.height / 3 * 2)
        close()
    }
    return path
}

@Composable
private fun D10Base(text: String, diceSize: Dp, outline: Boolean) {
    val diceColor = MaterialTheme.colorScheme.secondary
    val fontSize = with(LocalDensity.current) { diceSize.toSp() } / 2
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(diceSize)
            .drawBehind {
                drawPath(
                    d10Path(size),
                    color = diceColor,
                    style = if (outline) Stroke(10f) else Fill
                )
            },
    ) {
        Text(
            text,
            color = if (outline) diceColor else MaterialTheme.colorScheme.onSecondary,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 5.dp)
        )
    }
}

@Composable
fun D10(
    currentRoll: Int,
    diceSize: Dp = 100.dp,
    outline: Boolean = false,
    zeroAsTen: Boolean = false,
) {
    val number = if (zeroAsTen && currentRoll == 0) 10 else currentRoll
    D10Base(number.toString(), diceSize = diceSize, outline = outline)
}

@Composable
fun D100(
    currentRoll: Int,
    diceSize: Dp = 100.dp,
    outline: Boolean = false,
) {
    D10Base("${currentRoll}0", diceSize = diceSize, outline = outline)
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DicePreview() {
    MouldTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                D6(3)
                D10(5)
                D100(3)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                D6(3, outline = true)
                D10(5, outline = true)
                D100(3, outline = true)
            }
        }
    }
}