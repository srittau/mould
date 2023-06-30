package org.rittau.mould.ui

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun D6(currentRoll: Int) {
    Text(currentRoll.toString(), fontSize = 80.sp, textAlign = TextAlign.Center, modifier = Modifier.width(100.dp))
}

@Composable
fun D10(currentRoll: Int) {
    Text(currentRoll.toString(), fontSize = 80.sp, textAlign = TextAlign.Center, modifier = Modifier.width(100.dp))
}

@Composable
fun D100(currentRoll: Int) {
    Text("${currentRoll}0", fontSize = 80.sp, textAlign = TextAlign.Center, modifier = Modifier.width(100.dp))
}
