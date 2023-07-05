package org.rittau.mould.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import org.rittau.mould.R

@Composable
fun HealthIcon(modifier: Modifier = Modifier) {
    Icon(painterResource(R.drawable.health), "Health", modifier = modifier, tint = Color(0xFFFFA0A0))
}

@Composable
fun SpiritIcon(modifier: Modifier = Modifier) {
    Icon(painterResource(R.drawable.spirit), "Spirit", modifier = modifier, tint = Color(0xFF99BBE0))
}

@Composable
fun SupplyIcon(modifier: Modifier = Modifier) {
    Icon(painterResource(R.drawable.supply), "Supply", modifier = modifier, tint = Color(0xFFFFC040))
}
