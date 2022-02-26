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
        val regexNum=Regex("""((-?\d+(\.\d+)?)|(\(-?\d+(\.\d+)?\)))""")
        val multOrDiv=Regex("""$regexNum[X÷]$regexNum""")
        val plusOrMin=Regex("""$regexNum[+−]$regexNum""")
        var text=binding.screen.text.toString().replace(",",".").replace("(","").replace(")","")
        var expression:String
        var ans:Double
        while (text.contains(multOrDiv)){
            expression= multOrDiv.find(text,0)!!.value
            ans = if(expression.contains('÷')){
                if(expression.substringAfter('÷').toDouble()==0.0){
                    divisionByZero()
                    return
                }
                expression.substringBefore('÷').toDouble()/expression.substringAfter('÷').toDouble()
            }else{
                expression.substringBefore('X').toDouble()*expression.substringAfter('X').toDouble()
            }
            text=text.replaceFirst(expression,ans.toBigDecimal().toPlainString())
        }
        while (text.contains(plusOrMin)){
            expression= plusOrMin.find(text,0)!!.value
            ans = if(expression.contains('+')){
                expression.substringBefore('+').toDouble()+expression.substringAfter('+').toDouble()
            }else{
                expression.substringBefore('−').toDouble()-expression.substringAfter('−').toDouble()
            }
            text=text.replaceFirst(expression,ans.toBigDecimal().toPlainString())
        }
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
        nonNumCheckable(true)
        text=removeZeroFraction(text)
        binding.screen.text=text.replace('.',',')
    }

    private fun changeSign(){
        val newText=binding.screen.text.toString()+"X(-1)"
        binding.screen.text=newText
        calculate()
    }

    private fun toPercent(){
        binding.buttonAns.callOnClick()
        var text=binding.screen.text.toString().replace(",",".")
        text=(text.toDouble()/100).toBigDecimal().toPlainString()
        text=removeZeroFraction(text)
        binding.screen.text=text.replace('.',',')
    }

    private fun removeZeroFraction(text:String) : String{
        if(text.toDouble()==floor(text.toDouble())) {
            isDotUsed = false
            return text.substringBefore('.')
        }
        isDotUsed=true
        return text
    }
    private fun divisionByZero(){
        binding.screen.text="0"
        isDotUsed=false
        nonNumCheckable(true)
        Toast.makeText(this,"Division by zero is prohibited!",Toast.LENGTH_LONG).show()
    }
}
