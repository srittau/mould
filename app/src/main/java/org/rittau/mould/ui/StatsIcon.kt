package org.rittau.mould.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import org.rittau.mould.R


@Composable
fun EdgeIcon(modifier: Modifier = Modifier) {
    Icon(painterResource(R.drawable.edge), "Edge", modifier = modifier, tint = Color(0xFF88CFAC))
}

@Composable
fun HeartIcon(modifier: Modifier = Modifier) {
    Icon(painterResource(R.drawable.heart), "Heart", modifier = modifier, tint = Color(0xFFFFA0A0))
}

@Composable
fun IronIcon(modifier: Modifier = Modifier) {
    Icon(painterResource(R.drawable.iron), "Iron", modifier = modifier, tint = Color(0xFF99BBE0))
}

@Composable
fun ShadowIcon(modifier: Modifier = Modifier) {
    Icon(painterResource(R.drawable.shadow), "Shadow", modifier = modifier, tint = Color(0xFFAAAAAA))
}

@Composable
fun WitsIcon(modifier: Modifier = Modifier) {
    Icon(painterResource(R.drawable.wits), "Wits", modifier = modifier, tint = Color(0xFFFFC040))
}
