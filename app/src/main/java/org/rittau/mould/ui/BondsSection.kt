package org.rittau.mould.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.rittau.mould.R
import org.rittau.mould.model.BondsTrack

@Composable
fun BondsSection(track: BondsTrack, navigation: MouldNavigation) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { navigation.onBondsClicked() },
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Bonds", style = MaterialTheme.typography.labelLarge)
            Icon(painterResource(R.drawable.bond), "Bonds", modifier = Modifier.size(18.dp))
        }
        ProgressBar(filled = track.ticks)
    }
}
