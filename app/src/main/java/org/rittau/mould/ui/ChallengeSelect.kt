package org.rittau.mould.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.ChallengeRank

@Composable
fun ChallengeSelect(value: ChallengeRank, onValueChange: (ChallengeRank) -> Unit) {
    Column {
        ChallengeRank.values().forEach { rank ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = rank == value,
                        onClick = { onValueChange(rank) },
                        role = Role.RadioButton,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(selected = rank == value, onClick = { onValueChange(rank) })
                Text(rank.name)
                ChallengeIndicator(rank, modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}
