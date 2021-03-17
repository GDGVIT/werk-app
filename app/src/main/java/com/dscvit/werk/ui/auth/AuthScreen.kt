package com.dscvit.werk.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen() {
    Column {
        Box(
            Modifier
                .weight(1f)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Auth Screen", modifier = Modifier.align(alignment = Alignment.Center))
        }
        Box(
            modifier = Modifier
                .weight(2f)
                .padding(24.dp, 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {}
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.Black,
                        )
                    ) {}
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
private fun AuthPreview() {
    AuthScreen()
}
