package com.example.calculator


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.calculator.databinding.ActivityMainBinding
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isDotUsed=false
    private val calc=Calculations()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for(id in binding.numButtons.referencedIds) {
            val button=findViewById<Button>(id)
            button.setOnClickListener { addToScreen(button) }
        }
        for(id in binding.signButtons.referencedIds) {
            val button=findViewById<Button>(id)
            button.setOnClickListener{ addSignToScreen(button)}
        }
        binding.buttonDot.setOnClickListener { addDotToScreen() }
        binding.buttonAC.setOnClickListener { clearScreen() }
        binding.buttonAns.setOnClickListener { calculate() }
        binding.buttonPlMin.setOnClickListener { changeSign() }
        binding.buttonPercent.setOnClickListener { toPercent() }
    }

    private fun addToScreen(button: Button){
        val newText=binding.screen.text.toString()+button.text.toString()
        binding.screen.text = newText
        nonNumCheckable(true)
    }

    private fun addSignToScreen(button: Button){
        val newText=binding.screen.text.toString()+button.text.toString()
        binding.screen.text = newText
        nonNumCheckable(false)
        isDotUsed=false
    }

    private fun addDotToScreen() {
        if (!isDotUsed) {
            val newText = binding.screen.text.toString() + ","
            binding.screen.text = newText
            nonNumCheckable(false)
            isDotUsed = true
        }
    }

    private fun clearScreen(){
        binding.screen.text = ""
        nonNumCheckable(false)
        isDotUsed=false
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

    private fun calculate() {
        nonNumCheckable(true)
        val text=calc.calculate(binding.screen.text.toString())
        Toast.makeText(this, text,Toast.LENGTH_SHORT).show()
        Toast.makeText(this, floor(text.replace(',','.').toDouble()).toString(),Toast.LENGTH_SHORT).show()
        binding.screen.text=text
        isDotUsed=',' in text
    }

    private fun changeSign(){
        val newText=binding.screen.text.toString()+"X(-1)"
        binding.screen.text=newText
        calculate()
    }

    private fun toPercent(){
        calculate()
        val newText=binding.screen.text.toString()+"÷100"
        binding.screen.text=newText
        calculate()
    }


}
