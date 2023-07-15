package org.rittau.mould.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun StatSelect(character: Character, stat: StatOrTrack? = null, onChange: (StatOrTrack?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    fun statLabel(s: StatOrTrack?): String {
        if (s == null) return "None (+0)"
        val add = character.statOrTrackValue(s)
        return "${s.name} (+${add})"
    }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            statLabel(stat),
            readOnly = true,
            singleLine = true,
            label = { Text("Stat or Track") },
            leadingIcon = if (stat != null) ({
                val icon = statIcon(stat)
                icon(modifier = Modifier.size(24.dp))
            }) else null,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            onValueChange = {},
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor(),
        )
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
