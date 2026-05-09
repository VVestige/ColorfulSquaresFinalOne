package com.zybooks.colorfulsquares

import android.R
import android.R.color.white
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zybooks.colorfulsquares.ui.theme.ColorfulSquaresTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColorfulSquaresTheme {
                    ColorsScreen()
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorsScreen(modifier: Modifier = Modifier) {
    val baseColors = listOf(
        Color.Red,
        Color.Yellow,
        Color.Blue,
        Color.Green
    )
    var colors by remember { mutableStateOf(baseColors) }
    var isRunning by remember { mutableStateOf(false) }
    var randomMode by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Colors!") },
                actions = {}
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.fillMaxSize().padding(innerPadding).background(color = Color.Black)
        ) {
            Column(){
                Row() {
                    ColorBox(colors[0])
                    Spacer(modifier = Modifier.width(10.dp))
                    ColorBox(colors[1])
                }
                Row(){
                    ColorBox(colors[2])
                    Spacer(modifier = Modifier.width(10.dp))
                    ColorBox(colors[3])
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    isRunning = !isRunning
                    if(isRunning){
                        coroutineScope.launch {
                            var index = 0
                            while(isRunning) {
                                index = if (randomMode) {
                                    Random.nextInt(baseColors.size)
                                } else {
                                    index % baseColors.size
                                }
                                //Turning boxes white
                                val tempList = colors.toMutableList()
                                tempList[index] = Color.White
                                colors = tempList
                                delay(300)

                                //Restoring color
                                tempList[index] = baseColors[index]
                                colors = tempList
                                delay(300)

                                if(!randomMode) {
                                    index++
                                }
                            }

                        }
                    }
                }
            ) {
                Text(
                    if (isRunning) "Stop Sequence"
                    else "Start Sequence"
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox (
                    checked = randomMode,
                    onCheckedChange = {
                        randomMode = it
                    }
                )
                Text("Random Mode")
            }
        }
    }

}

@Composable
fun ColorBox(clr: Color) {
    Box(
        modifier = Modifier.size(120.dp).background(clr)
    )
}