package com.ddevs.bmicalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.pow

class MainViewModel: ViewModel() {
    var weight by mutableStateOf(60)
    var height by mutableStateOf(160)
    var age by mutableStateOf(20)
    var gender by mutableStateOf("Male")

    var state by mutableStateOf(BmiCategories.Normal)
    var bmi by mutableStateOf(0f)

    enum class BmiCategories{
        Underweight,Normal,Overweight
    }

    fun calculateBMI(){
        val heightsq:Float=((height.toFloat()/100))
        bmi= (weight.toFloat()/ heightsq.toDouble().pow(2.0)).toFloat()
        state = when {
            bmi<18.5 -> BmiCategories.Underweight
            bmi in 18.5..25.0 -> BmiCategories.Normal
            else -> BmiCategories.Overweight
        }
    }

}