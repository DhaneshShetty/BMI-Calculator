package com.ddevs.bmicalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.pow

class MainViewModel: ViewModel() {
    var weight by mutableStateOf("60")
    var height by mutableStateOf("160")
    var age by mutableStateOf("20")
    var gender by mutableStateOf("Male")

    var state by mutableStateOf(BmiCategories.Normal)
    var bmi by mutableStateOf(0f)
    var error: String? by mutableStateOf(null)

    enum class BmiCategories{
        Underweight,Normal,Overweight
    }

    fun validate():Boolean{
        val heightCal: Int
        val weightCal: Int
        if(height.isBlank()){
            error="Fill a value for height"
            return false
        }
        else
            heightCal=Integer.parseInt(height)
        if(weight.isBlank()){
            error="Fill a value for weight"
            return false
        }
        else
            weightCal=Integer.parseInt(weight)
        calculateBMI(heightCal, weightCal)
        error=null
        return true
    }

    private fun calculateBMI(heightCal:Int, weightCal:Int){
        val heightsq:Float=((heightCal.toFloat()/100))
        bmi= (weightCal.toFloat()/ heightsq.toDouble().pow(2.0)).toFloat()
        state = when {
            bmi<18.5 -> BmiCategories.Underweight
            bmi in 18.5..25.0 -> BmiCategories.Normal
            else -> BmiCategories.Overweight
        }
    }

}