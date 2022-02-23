package com.example.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.calculator.databinding.ActivityMainBinding
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var isDotUsed=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    private fun nonNumCheckable(b:Boolean){
        binding.buttonDot.isClickable=b
        binding.buttonAns.isClickable=b
        binding.buttonPlus.isClickable=b
        binding.buttonMinus.isClickable=b
        binding.buttonMult.isClickable=b
        binding.buttonDivide.isClickable=b
        binding.buttonPercent.isClickable=b
        binding.buttonPlMin.isClickable=b
    }

    fun addToScreen(view: View){
        val newText=binding.screen.text.toString()+(view as Button).text.toString()
        binding.screen.text = newText
        nonNumCheckable(true)
    }

    fun addDotToScreen(view: View) {
        if (!isDotUsed) {
            val newText = binding.screen.text.toString() + ","
            binding.screen.text = newText
            nonNumCheckable(false)
            isDotUsed = true
        }
    }

    fun addSignToScreen(view: View){
        val newText=binding.screen.text.toString()+(view as Button).text.toString()
        binding.screen.text = newText
        nonNumCheckable(false)
        isDotUsed=false
    }


    fun clearScreen(view: View){
        binding.screen.text = ""
        nonNumCheckable(false)
        isDotUsed=false
    }

    fun calculate(view: View){
        val multOrDiv=Regex("""((-?\d+(\.\d+)?)|(\(-?\d+(\.\d+)?\)))[X÷]((-?\d+(\.\d+)?)|(\(-?\d+(\.\d+)?\)))""")
        val plusOrMin=Regex("""((-?\d+(\.\d+)?)|(\(-?\d+(\.\d+)?\)))[+−]((-?\d+(\.\d+)?)|(\(-?\d+(\.\d+)?\)))""")
        var text=binding.screen.text.toString().replace(",",".")
        var performance:String
        var perf:String
        var ans:Double
        while (text.contains(multOrDiv)){
            performance= multOrDiv.find(text,0)!!.value
            perf=performance.replace("(","").replace(")","")
            ans = if(perf.contains('÷')){
                perf.substringBefore('÷').toDouble()/perf.substringAfter('÷').toDouble()
            }else{
                perf.substringBefore('X').toDouble()*perf.substringAfter('X').toDouble()
            }
            text=text.replaceFirst(performance,ans.toBigDecimal().toPlainString())
        }
        while (text.contains(plusOrMin)){
            performance= plusOrMin.find(text,0)!!.value
            perf=performance.replace("(","").replace(")","")
            ans = if(perf.contains('+')){
                perf.substringBefore('+').toDouble()+perf.substringAfter('+').toDouble()
            }else{
                perf.substringBefore('−').toDouble()-perf.substringAfter('−').toDouble()
            }
            text=text.replaceFirst(perf,ans.toBigDecimal().toPlainString())
        }
        nonNumCheckable(true)

        if(text.toDouble()==floor(text.toDouble())){
            isDotUsed=false
            text=text.substringBefore('.')
        }else{
            isDotUsed=true
        }
        binding.screen.text=text.replace('.',',')
    }

    fun changeSign(view: View){
        val newText=binding.screen.text.toString()+"X(-1)"
        binding.screen.text=newText
        binding.buttonAns.callOnClick()
    }

    fun toPercent(view: View){
        binding.buttonAns.callOnClick()
        var text=binding.screen.text.toString().replace(",",".")
        text=(text.toDouble()/100).toBigDecimal().toPlainString()
        if(text.toDouble()==floor(text.toDouble())){
            isDotUsed=false
            text=text.substringBefore('.')
        }else{
            isDotUsed=true
        }
        binding.screen.text=text.replace('.',',')
    }
}