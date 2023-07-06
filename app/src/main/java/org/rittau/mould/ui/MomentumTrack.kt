package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.Character
import org.rittau.mould.model.MIN_MOMENTUM
import org.rittau.mould.saveCharacterSync
import org.rittau.mould.ui.theme.MouldTheme

@Composable
fun MomentumTrack(character: Character) {
    val track = character.momentumTrack

    var value by rememberSaveable {
        mutableStateOf(track.value)
    }

    fun onLose() {
        value = track.lose()
        saveCharacterSync(character)
    }

    fun onGain() {
        value = track.gain()
        saveCharacterSync(character)
    }

    fun onReset() {
        value = track.reset()
        saveCharacterSync(character)
    }

    val numbers = when (value) {
        in MIN_MOMENTUM..MIN_MOMENTUM + 2 -> listOf(
            MIN_MOMENTUM,
            MIN_MOMENTUM + 1,
            MIN_MOMENTUM + 2,
            null,
            track.resetValue,
            null,
            track.max,
        )

        in MIN_MOMENTUM..track.resetValue - 2 -> listOf(
            MIN_MOMENTUM,
            null,
            value,
            null,
            track.resetValue,
            null,
            track.max,
        )

        in MIN_MOMENTUM..track.resetValue + 1 -> listOf(
            MIN_MOMENTUM,
            null,
            track.resetValue - 1,
            track.resetValue,
            track.resetValue + 1,
            null,
            track.max,
        )

        in MIN_MOMENTUM..track.max - 3 -> listOf(
            MIN_MOMENTUM,
            null,
            track.resetValue,
            null,
            value,
            null,
            track.max,
        )

        else -> listOf(
            MIN_MOMENTUM,
            null,
            track.resetValue,
            null,
            track.max - 2,
            track.max - 1,
            track.max,
        )
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(enabled = track.canLose, onClick = { onLose() }) {
            Icon(Icons.Filled.Remove, "Lose", tint = MaterialTheme.colorScheme.secondary)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .border(
                    .5.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(4.dp),
                )
                .clip(RoundedCornerShape(4.dp)),
        ) {
            for (num in numbers) {
                if (num != null) {
                    ConditionItem(value = num, selected = value == num, reset = track.resetValue == num)
                } else {
                    PlaceholderConditionItem()
                }
            }
        }
        IconButton(enabled = track.canGain, onClick = { onGain() }) {
            Icon(Icons.Filled.Add, "Gain", tint = MaterialTheme.colorScheme.secondary)
        }
        IconButton(enabled = track.canReset, onClick = { onReset() }) {
            Icon(Icons.Filled.Refresh, "Reset", tint = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MomentumTrackPreview() {
    val character = Character("", momentum = 3)
    MouldTheme {
        MomentumTrack(character)
    }
}
