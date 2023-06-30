package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.rittau.mould.model.Character
import org.rittau.mould.saveCharacter
import org.rittau.mould.ui.theme.MouldTheme

@Composable
fun CharacterName(character: Character) {
    var editing by rememberSaveable {
        mutableStateOf(false)
    }
    var name by rememberSaveable {
        mutableStateOf(character.name)
    }

    if (editing) {
        NameField(
            name,
            modifier = Modifier.fillMaxWidth(),
            onChange = {
                name = it
                character.name = it
            },
            onClose = {
                editing = false
                CoroutineScope(IO).launch {
                    saveCharacter(character)
                }
            },
        )
    } else {
        NameDisplay(
            name, modifier = Modifier.fillMaxWidth()
        ) { editing = true }
    }
}

@Composable
fun NameDisplay(name: String, modifier: Modifier = Modifier, onEdit: () -> Unit) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            if (name != "") name else "Unnamed",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onEdit) {
            Icon(Icons.Filled.Edit, "Edit name")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameField(
    name: String,
    modifier: Modifier = Modifier,
    onChange: (name: String) -> Unit,
    onClose: () -> Unit,
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            name,
            singleLine = true,
            onValueChange = { onChange(it) },
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { onClose() }) {
            Icon(Icons.Filled.Done, "Edit name")
        }
    }

}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NameDisplayPreview() {
    MouldTheme {
        Surface {
            NameDisplay("John") {}
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NameFieldPreview() {
    MouldTheme {
        Surface {
            NameField("John", onChange = {}, onClose = {})
        }
    }
}
