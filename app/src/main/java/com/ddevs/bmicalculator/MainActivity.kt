package com.ddevs.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ddevs.bmicalculator.ui.theme.BMICalculatorTheme
import com.ddevs.bmicalculator.ui.theme.Purple200
import com.ddevs.bmicalculator.ui.theme.Purple500

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorTheme {
                val navController= rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") { MainPage() }
                    composable("result") { ResultScreen() }
                }
            }
        }
    }
}

@Composable
fun MainPage() {
    var maleFemale by remember {
        mutableStateOf("Male")
    }
    Column(modifier=Modifier.padding(8.dp)) {
        Text(
            "Hello Dear User",
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.h5
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            RadioButton(text = "Male", image = R.drawable.ic_male_avatar, isEnabled = maleFemale == "Male",modifier=Modifier.weight(1f).padding(2.dp),onClick={maleFemale="Male"})
            RadioButton(text = "Female", image = R.drawable.ic_female_avatar, isEnabled = maleFemale != "Male",modifier=Modifier.weight(1f).padding(2.dp),onClick={maleFemale="Female"})
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RadioButton(modifier: Modifier,isEnabled:Boolean=true, text:String, image: Int,onClick:()->Unit){
    val surfaceColor by animateColorAsState(
        if (isEnabled) Purple500 else Purple200,
    )
    val elevation by animateDpAsState(targetValue = if(isEnabled) 2.dp else 1.dp)
        Surface(shape=MaterialTheme.shapes.medium,elevation = elevation, color = surfaceColor, modifier = modifier, onClick = onClick) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painterResource(image), text, modifier = Modifier.size(80.dp).padding(4.dp))
                Text(text, Modifier.padding(4.dp), textAlign = TextAlign.Center,style=MaterialTheme.typography.h6)
            }
        }
}

@Composable
fun ResultScreen(){

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BMICalculatorTheme {
        MainPage()
    }
}