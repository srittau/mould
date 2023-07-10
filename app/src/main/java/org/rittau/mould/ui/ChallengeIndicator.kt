package org.rittau.mould.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.ChallengeRank

@Composable
fun ChallengeIndicator(rank: ChallengeRank, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        when (rank) {
            ChallengeRank.Troublesome -> ProgressBox(1, modifier = Modifier.scale(.5f), color = Color(0xFF00CC00))
            ChallengeRank.Dangerous -> ProgressBox(2, modifier = Modifier.scale(.5f), color = Color(0xFF88A800))
            ChallengeRank.Formidable -> ProgressBox(4, modifier = Modifier.scale(.5f), color = Color(0xFFFF8000))
            ChallengeRank.Extreme -> Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                ProgressBox(4, modifier = Modifier.scale(.5f), color = Color(0xFFFF7A33))
                ProgressBox(4, modifier = Modifier.scale(.5f), color = Color(0xFFFF7A33))
            }

            ChallengeRank.Epic -> Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                ProgressBox(4, modifier = Modifier.scale(.5f), color = Color(0xFFFF6666))
                ProgressBox(4, modifier = Modifier.scale(.5f), color = Color(0xFFFF6666))
                ProgressBox(4, modifier = Modifier.scale(.5f), color = Color(0xFFFF6666))
            }
        }
    }
}
