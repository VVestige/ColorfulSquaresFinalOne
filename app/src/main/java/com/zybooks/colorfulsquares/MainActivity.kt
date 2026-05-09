package com.zybooks.colorfulsquares

import android.R
import android.R.color.white
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    var flashDuration by remember { mutableStateOf(500L)}
    var pauseDuration by remember { mutableStateOf(300L) }
    var activeIndex by remember { mutableStateOf(-1) }
    var score by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var randomMode by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reaction!",
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "Score: $score",
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick ={
                                score = 0
                            }
                        ) {
                            Text("Reset")
                        }
                    } },
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.fillMaxSize().padding(innerPadding).background(color = Color.LightGray)
        ) {
            Column(){
                Row() {
                    ColorBox(colors[0],
                        onCorrectClick = {
                            score++
                        })
                    Spacer(modifier = Modifier.width(10.dp))
                    ColorBox(colors[1],
                        onCorrectClick = {
                            score++
                        })
                }
                Row(){
                    ColorBox(colors[2],
                        onCorrectClick = {
                            score++
                        })
                    Spacer(modifier = Modifier.width(10.dp))
                    ColorBox(colors[3],
                        onCorrectClick = {
                            score++
                        })
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    isRunning = !isRunning
                    if(isRunning){
                        score = 0
                        flashDuration = 500L
                        pauseDuration = 300L

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
                                delay(flashDuration)

                                //Restoring color
                                tempList[index] = baseColors[index]
                                colors = tempList
                                activeIndex = -1
                                delay(pauseDuration)

                                if(!randomMode) {
                                    index++
                                }

                                if(flashDuration > 120L) {
                                    flashDuration -= 10L
                                }

                                if(pauseDuration > 80L) {
                                    pauseDuration -= 5L
                                }
                            }

                        }
                    }
                }
            ) {
                Text(
                    if (isRunning) "Stop Game"
                    else "Start Game"
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
fun ColorBox(clr: Color, onCorrectClick: ()-> Unit) {
    Box(
        modifier = Modifier.size(120.dp).background(clr).clickable{
            if(clr == Color.White) {
                onCorrectClick()
            }
        }
    )
}