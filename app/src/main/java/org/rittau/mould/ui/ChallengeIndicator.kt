package org.rittau.mould.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.ChallengeRank

val COLOR_TROUBLESOME = Color(0xFF999999)
val COLOR_DANGEROUS = Color(0xFF00CC00)
val COLOR_FORMIDABLE = Color(0xFF0080FF)
val COLOR_EXTREME = Color(0xFFFF66FF)
val COLOR_EPIC = Color(0xFFFF8000)

@Composable
fun ChallengeIndicator(rank: ChallengeRank, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        when (rank) {
            ChallengeRank.Troublesome -> Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                ProgressBox(4, modifier = Modifier.scale(.5f), color = COLOR_TROUBLESOME)
                ProgressBox(4, modifier = Modifier.scale(.5f), color = COLOR_TROUBLESOME)
                ProgressBox(4, modifier = Modifier.scale(.5f), color = COLOR_TROUBLESOME)
            }
            ChallengeRank.Dangerous -> Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                ProgressBox(4, modifier = Modifier.scale(.5f), color = COLOR_DANGEROUS)
                ProgressBox(4, modifier = Modifier.scale(.5f), color = COLOR_DANGEROUS)
            }
            ChallengeRank.Formidable -> ProgressBox(4, modifier = Modifier.scale(.5f), color = COLOR_FORMIDABLE)
            ChallengeRank.Extreme -> ProgressBox(2, modifier = Modifier.scale(.5f), color = COLOR_EXTREME)
            ChallengeRank.Epic -> ProgressBox(1, modifier = Modifier.scale(.5f), color = COLOR_EPIC)
        }
    }
}
