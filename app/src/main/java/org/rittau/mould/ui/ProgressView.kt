package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.Character
import org.rittau.mould.ui.theme.MouldTheme
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressView(character: Character) {
    val name = character.name.ifBlank { "Unnamed Character" }
    Column {
        TopAppBar(
            title = { Text(text = name) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
        ) {
            Text("Bonds", style = MaterialTheme.typography.labelLarge)
            ProgressBar(filled = character.bondCount)
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProgressViewPreview() {
    MouldTheme {
        ProgressView(Character("Joe", bonds = setOf(UUID.randomUUID(), UUID.randomUUID())))
    }
}
