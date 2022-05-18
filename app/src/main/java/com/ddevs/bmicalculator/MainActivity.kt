package com.ddevs.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ddevs.bmicalculator.ui.theme.BMICalculatorTheme
import com.ddevs.bmicalculator.ui.theme.Purple200
import com.ddevs.bmicalculator.ui.theme.Purple500

class MainActivity : ComponentActivity() {
    private val viewModel:MainViewModel by lazy{
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") { MainPage(navController,viewModel) }
                    composable("result") { ResultScreen(viewModel) }
                }
            }
        }
    }
}

@Composable
fun MainPage(navController: NavController,viewModel:MainViewModel) {
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()) {
        Text(
            "Hello Dear User",
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.h5
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RadioButton(text = "Male",
                image = R.drawable.ic_male_avatar,
                isEnabled = viewModel.gender == "Male",
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp),
                onClick = { viewModel.gender = "Male" })
            RadioButton(text = "Female",
                image = R.drawable.ic_female_avatar,
                isEnabled = viewModel.gender != "Male",
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp),
                onClick = { viewModel.gender = "Female" })
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InputField(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                text = "Weight",
                onChange = { if(it.isNotEmpty()){viewModel.weight = Integer.parseInt(it)} },
                value = viewModel.weight,
                unit = "kg"
            )
            InputField(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                text = "Height",
                onChange = { if(it.isNotEmpty()){viewModel.height = Integer.parseInt(it) }},
                value = viewModel.height,
                unit = "cm"
            )
        }
        InputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            text = "Age",
            onChange = { if(it.isNotEmpty()){viewModel.age = Integer.parseInt(it)}},
            value = viewModel.age,
            unit = "years"
        )
        Button(
            { calculateBMI(navController,viewModel)},
            Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(backgroundColor = Purple500),
        ) {
            Text("Calculate BMI",Modifier.padding(8.dp))
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RadioButton(
    modifier: Modifier,
    isEnabled: Boolean = true,
    text: String,
    image: Int,
    onClick: () -> Unit
) {
    val surfaceColor by animateColorAsState(
        if (isEnabled) Purple500 else Purple200,
    )
    val elevation by animateDpAsState(targetValue = if (isEnabled) 2.dp else 1.dp)
    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = elevation,
        color = surfaceColor,
        modifier = modifier,
        onClick = onClick
    ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(image), text, modifier = Modifier
                        .size(80.dp)
                        .padding(4.dp)
                )
                Text(
                    text,
                    Modifier.padding(4.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
            }
    }
}

@Composable
fun InputField(
    modifier: Modifier,
    text: String,
    onChange: (String) -> Unit,
    value: Int,
    unit: String
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Text(modifier = Modifier.padding(vertical = 4.dp), text = text)
        OutlinedTextField(
            value.toString(), onChange, label = { Text("$text($unit)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
        )
    }
}

fun calculateBMI(navController: NavController,viewModel: MainViewModel){
    viewModel.calculateBMI()
    navController.navigate("result"){
        popUpTo("main")
    }
}

@Composable
fun ResultScreen(viewModel: MainViewModel) {
    Text("BMI:"+viewModel.state.name+"Value:"+viewModel.bmi)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BMICalculatorTheme {
        val navController = rememberNavController()
        val viewModel=MainViewModel()
        NavHost(navController, startDestination = "main") {
            composable("main") { MainPage(navController, viewModel) }
            composable("result") { ResultScreen(viewModel) }
        }
    }
}