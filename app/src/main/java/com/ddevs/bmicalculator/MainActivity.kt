package com.ddevs.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ddevs.bmicalculator.ui.theme.BMICalculatorTheme
import com.ddevs.bmicalculator.ui.theme.backgroundPurple
import com.ddevs.bmicalculator.ui.theme.lightpurple
import com.ddevs.bmicalculator.ui.theme.red


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorTheme {
                Surface(color = backgroundPurple) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "main") {
                        composable("main") { MainPage(navController, viewModel) }
                        composable("result") { ResultScreen(viewModel) }
                    }
                }
            }
        }

    }
}

@Composable
fun MainPage(navController: NavController,viewModel:MainViewModel) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            "Enter the details",
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.h5,
            color = Color.White
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
                onChange = { if(it.isDigitsOnly() || it.isBlank()) viewModel.weight = it},
                value = viewModel.weight,
                unit = "kg"
            )
            InputField(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                text = "Height",
                onChange = {  if(it.isDigitsOnly() || it.isBlank()) viewModel.height = it},
                value = viewModel.height,
                unit = "cm"
            )
        }
        InputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            text = "Age",
            onChange = { if(it.isDigitsOnly() || it.isBlank()) viewModel.age = it },
            value = viewModel.age,
            unit = "years"
        )
        Button(
            { calculateBMI(navController, viewModel) },
            Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(percent = 20),
            colors = ButtonDefaults.buttonColors(backgroundColor = red, contentColor = Color.White),
        ) {
            Text("Calculate BMI",Modifier.padding(8.dp),style= MaterialTheme.typography.body1)
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
        if (isEnabled) red else lightpurple,
    )
    val textColor by animateColorAsState(
        if(isEnabled) Color.White else Color.Black
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
                    textAlign = Center,
                    style = MaterialTheme.typography.h6,
                    color = textColor
                )
            }
    }
}

@Composable
fun InputField(
    modifier: Modifier,
    text: String,
    onChange: (String) -> Unit,
    value: String,
    unit: String
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Text(modifier = Modifier.padding(vertical = 4.dp), text = text, color = Color.LightGray)
        TextField(
            value, onChange, label = { Text("$text($unit)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ), modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .fillMaxWidth(),
            textStyle= LocalTextStyle.current.copy(fontSize=24.sp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = lightpurple,
                textColor = red,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = red,
                unfocusedBorderColor = Color.LightGray
            ),
            isError = value.isBlank(),
        )
        if(value.isBlank()){
            Text("Input should not be blank",color=red,style=MaterialTheme.typography.caption)
        }
    }
}

fun calculateBMI(navController: NavController,viewModel: MainViewModel){
    if(viewModel.validate()) {
        navController.navigate("result") {
            popUpTo("main")
        }
    }
}
fun Float.format(digits: Int) = "%.${digits}f".format(this)
@Composable
fun ResultScreen(viewModel: MainViewModel) {
    val colorText by animateColorAsState(
        when (viewModel.state) {
            MainViewModel.BmiCategories.Normal -> Color.Green
            MainViewModel.BmiCategories.Overweight -> Color.Blue
            MainViewModel.BmiCategories.Underweight -> Color.Yellow
        }
    )

    val statement = (when (viewModel.state) {
        MainViewModel.BmiCategories.Normal -> "You have a normal body weight.Good job!"
        MainViewModel.BmiCategories.Overweight -> "You have a overweight body.You can do this,exercise more"
        MainViewModel.BmiCategories.Underweight -> "You need to increase your weight.You are underweight."
    })

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your Result",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start),
            color = Color.White
        )
        Image(
            painterResource(R.drawable.ic_undraw_fitness_stats), "Stats image", modifier = Modifier
                .size(200.dp)
                .padding(4.dp)
        )
        LinearProgressIndicator(progress =
        when(viewModel.state){
            MainViewModel.BmiCategories.Normal->0.5f
            MainViewModel.BmiCategories.Overweight->0.75f
            MainViewModel.BmiCategories.Underweight->0.25f }
            , color = colorText, modifier = Modifier.padding(8.dp).height(40.dp).fillMaxWidth().clip(RoundedCornerShape(10)))
        Surface(
            shape = RoundedCornerShape(10), color = red, modifier = Modifier
                .align(
                    Alignment.CenterHorizontally
                )
                .padding(8.dp)
                .fillMaxWidth(),
            elevation = (8).dp
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    viewModel.state.name,
                    color = colorText,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    viewModel.bmi.format(2),
                    color = Color.White,
                    style = LocalTextStyle.current.copy(fontSize=48.sp),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    "Height:${viewModel.height} cm",
                    color = Color.LightGray,
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    "Weight:${viewModel.weight} kg",
                    color = Color.LightGray,
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    "Age:${viewModel.age} years",
                    color = Color.LightGray,
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    statement,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.h6,
                    textAlign = Center
                )
            }

        }


    }
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