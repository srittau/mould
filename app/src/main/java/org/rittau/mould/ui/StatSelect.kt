package org.rittau.mould.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.Character
import org.rittau.mould.model.StatOrTrack

private fun statIcon(stat: StatOrTrack): @Composable (modifier: Modifier) -> Unit =
    when (stat) {
        StatOrTrack.Edge -> { modifier -> EdgeIcon(modifier) }
        StatOrTrack.Heart -> { modifier -> HeartIcon(modifier) }
        StatOrTrack.Iron -> { modifier -> IronIcon(modifier) }
        StatOrTrack.Shadow -> { modifier -> ShadowIcon(modifier) }
        StatOrTrack.Wits -> { modifier -> WitsIcon(modifier) }
        StatOrTrack.Health -> { modifier -> HealthIcon(modifier) }
        StatOrTrack.Spirit -> { modifier -> SpiritIcon(modifier) }
        StatOrTrack.Supply -> { modifier -> SupplyIcon(modifier) }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatSelect(
    character: Character,
    stat: StatOrTrack? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onChange: (StatOrTrack?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    fun statLabel(s: StatOrTrack?): String {
        if (s == null) return "None (+0)"
        val add = character.statOrTrackValue(s)
        return "${s.name} (+${add})"
    }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth().menuAnchor(),
        ) {
            if (stat != null) {
                statIcon(stat)(modifier = Modifier.size(24.dp))
            }
            Text(stat?.name ?: "None", style = MaterialTheme.typography.bodyMedium)
            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
        }
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(statLabel(null)) },
                leadingIcon = {},
                onClick = { onChange(null); expanded = false },
            )
            for (option in StatOrTrack.values()) {
                DropdownMenuItem(
                    text = { Text(statLabel(option)) },
                    leadingIcon = {
                        val icon = statIcon(option)
                        icon(modifier = Modifier.size(24.dp))
                    },
                    onClick = { onChange(option); expanded = false },
                )
            }
        }
    }
}
